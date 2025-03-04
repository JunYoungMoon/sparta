package com.ana29.deliverymanagement.order.exception;

import com.ana29.deliverymanagement.order.dto.PaymentResultDto;
import lombok.Getter;

@Getter
public class PaymentFailException extends RuntimeException {
	private final PaymentResultDto paymentResult;

	public PaymentFailException(PaymentResultDto paymentResult) {
		super(paymentResult.errorMessage());
		this.paymentResult = paymentResult;
	}
}