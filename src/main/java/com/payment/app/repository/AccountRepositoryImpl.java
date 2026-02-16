package com.payment.app.repository;

import com.payment.app.model.Account;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AccountRepositoryImpl implements PanacheRepositoryBase<Account, String> {

    public Account findById(String accountNo) {
        return find("accountNo", accountNo).firstResult();
    }

}
