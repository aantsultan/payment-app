package com.payment.app.repository;

import com.payment.app.model.Transfer;
import com.payment.app.model.embedded.TransferId;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TransferRepositoryImpl implements PanacheRepositoryBase<Transfer, TransferId> {
}
