/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ssamsara98.estore.users_service.query;

import com.ssamsara98.estore.core.model.PaymentDetails;
import com.ssamsara98.estore.core.model.User;
import com.ssamsara98.estore.core.query.FetchUserPaymentDetailsQuery;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

@Component
public class UserEventsHandler {

	@QueryHandler
	public User findUserPaymentDetails(FetchUserPaymentDetailsQuery query) {

		PaymentDetails paymentDetails = PaymentDetails.builder()
			.cardNumber("123Card")
			.cvv("123")
			.name("SULTHON ABDULMALIK")
			.validUntilMonth(12)
			.validUntilYear(2030)
			.build();

		User user = User.builder()
			.firstName("Sulthon")
			.lastName("Abdulmalik")
			.userId(query.getUserId())
			.paymentDetails(paymentDetails)
			.build();

		return user;
	}
}
