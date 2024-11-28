package com.payment.populator;

import com.payment.dto.PaymentDto;
import com.payment.entity.Payment;

public class PaymentPopulator extends AbstractPopulator<Payment, PaymentDto> {
    @Override
    public PaymentDto populate(Payment payment, PaymentDto paymentDto) {
        return null;
    }

    @Override
    public PaymentDto getTarget() {
        return new PaymentDto();
    }
}
