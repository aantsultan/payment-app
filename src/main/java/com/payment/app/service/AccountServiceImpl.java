package com.payment.app.service;

import com.payment.app.dto.AccountDto;
import com.payment.app.exception.NotFoundException;
import com.payment.app.helper.ExceptionHelper;
import com.payment.app.model.Account;
import com.payment.app.repository.AccountRepositoryImpl;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class AccountServiceImpl implements AccountService {

    private final AccountRepositoryImpl repository;

    public AccountServiceImpl(AccountRepositoryImpl repository) {
        this.repository = repository;
    }

    @Override
    public Account findById(String accountNo) {
        return repository.findById(accountNo);
    }

    @Override
    @Transactional
    public void save(Account account) {
        repository.persistAndFlush(account);
    }

    @Override
    public AccountDto get(String accountNo) {
        Account account = repository.findById(accountNo);
        if (account == null) throw new NotFoundException(ExceptionHelper.ACCOUNT_NOT_FOUND);
        AccountDto accountDto = new AccountDto();
        accountDto.setAccountNo(account.getAccountNo());
        accountDto.setBalance(account.getBalance());
        return accountDto;
    }

    @Override
    @Transactional
    public void deleteAll() {
        repository.deleteAll();
    }
}
