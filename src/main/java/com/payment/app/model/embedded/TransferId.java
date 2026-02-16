package com.payment.app.model.embedded;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class TransferId implements Serializable {

    private String debitAccount;
    private String creditAccount;
    private String transactionCode;

    public String getDebitAccount() {
        return debitAccount;
    }

    public void setDebitAccount(String debitAccount) {
        this.debitAccount = debitAccount;
    }

    public String getCreditAccount() {
        return creditAccount;
    }

    public void setCreditAccount(String creditAccount) {
        this.creditAccount = creditAccount;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }
}
