package com.ssamsara98.estore.users_service.query.rest;

import com.ssamsara98.estore.core.model.User;
import com.ssamsara98.estore.core.query.FetchUserPaymentDetailsQuery;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UsersQueryController {
	QueryGateway queryGateway;

	@Autowired
	public UsersQueryController(QueryGateway queryGateway) {
		this.queryGateway = queryGateway;
	}

	@GetMapping("/{userId}/payment-details")
	public User getUserPaymentDetails(@PathVariable String userId) {
		FetchUserPaymentDetailsQuery query = new FetchUserPaymentDetailsQuery(userId);
		return queryGateway.query(query, ResponseTypes.instanceOf(User.class)).join();
	}

}
