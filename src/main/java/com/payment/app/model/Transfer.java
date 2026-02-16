package com.payment.app.model;

import com.payment.app.model.embedded.TransferId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "trx_transfer")
public class Transfer {

    @EmbeddedId
    private TransferId transferId;
    private BigDecimal amount;

    public TransferId getTransferId() {
        return transferId;
    }

    public void setTransferId(TransferId transferId) {
        this.transferId = transferId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
