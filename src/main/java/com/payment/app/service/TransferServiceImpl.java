package com.payment.app.service;

import com.payment.app.dto.TransferDto;
import com.payment.app.exception.NotFoundException;
import com.payment.app.exception.TransferException;
import com.payment.app.helper.ExceptionHelper;
import com.payment.app.model.Account;
import com.payment.app.model.Transfer;
import com.payment.app.model.embedded.TransferId;
import com.payment.app.repository.TransferRepositoryImpl;
import io.quarkus.arc.Lock;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;

@ApplicationScoped
public class TransferServiceImpl implements TransferService {

    @Inject
    TransferRepositoryImpl repository;

    @Inject
    AccountService accountService;

    @Override
    @Transactional
    @Lock(value = Lock.Type.WRITE)
    public String post(TransferDto transferDto) {
        TransferId transferId = new TransferId();
        String creditAccount = transferDto.getCreditAccount();
        String debitAccount = transferDto.getDebitAccount();
        String transactionCode = transferDto.getTransactionCode();

        // 1. create transfer transaction
        BigDecimal amount = transferDto.getAmount();
        transferId.setDebitAccount(debitAccount);
        transferId.setCreditAccount(creditAccount);
        transferId.setTransactionCode(transactionCode);
        Transfer transfer = new Transfer();
        transfer.setTransferId(transferId);
        transfer.setAmount(amount);

        try {
            repository.persistAndFlush(transfer);
        } catch (Exception e) {
            if (e.getMessage().contains("Unique index or primary key violation")) {
                throw new TransferException(ExceptionHelper.TRANSFER_DUPLICATE);
            }
        }

        // 2. update debit balance
        Account dbAccount = accountService.findById(debitAccount);
        if (dbAccount == null) throw new NotFoundException(ExceptionHelper.ACCOUNT_NOT_FOUND);
        BigDecimal dbBalance = dbAccount.getBalance();
        if (dbBalance.subtract(amount).compareTo(BigDecimal.ZERO) < 0)
            throw new TransferException(ExceptionHelper.TRANSFER_MINUS);
        dbAccount.setBalance(dbBalance.subtract(amount));
        accountService.save(dbAccount);

        // 3. update credit balance
        Account crAccount = accountService.findById(creditAccount);
        if (crAccount == null) throw new NotFoundException(ExceptionHelper.ACCOUNT_NOT_FOUND);
        BigDecimal crBalance = crAccount.getBalance();
        crAccount.setBalance(crBalance.add(amount));
        accountService.save(crAccount);

        return "OK";
    }

    @Override
    @Transactional
    public void deleteAll() {
        repository.deleteAll();
    }
}
