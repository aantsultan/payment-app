package com.payment.app.service;

import com.payment.app.dto.TransferDto;

public interface TransferService {

    String post (TransferDto transferDto);

    void deleteAll();

}
