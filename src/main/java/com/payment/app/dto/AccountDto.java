package com.payment.app.dto;

import java.math.BigDecimal;

public class AccountDto {

    private String accountNo;
    private BigDecimal balance;

    public AccountDto() {
    }

    public AccountDto(String accountNo, BigDecimal balance) {
        this.accountNo = accountNo;
        this.balance = balance;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
