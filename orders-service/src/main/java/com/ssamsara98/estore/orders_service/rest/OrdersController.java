/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ssamsara98.estore.orders_service.rest;

import com.ssamsara98.estore.orders_service.core.model.OrderStatus;
import com.ssamsara98.estore.orders_service.command.commands.CreateOrderCommand;

import java.util.UUID;
import javax.validation.Valid;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("orders")
public class OrdersController {

	private final CommandGateway commandGateway;

	@Autowired
	public OrdersController(CommandGateway commandGateway) {
		this.commandGateway = commandGateway;
	}

	@PostMapping
	public String createOrder(@Valid @RequestBody OrderCreateRest order) {

		String userId = "27b95829-4f3f-4ddf-8983-151ba010e35b";

		CreateOrderCommand createOrderCommand = CreateOrderCommand.builder()
			.addressId(order.getAddressId())
			.productId(order.getProductId())
			.userId(userId)
			.quantity(order.getQuantity())
			.orderId(UUID.randomUUID().toString())
			.orderStatus(OrderStatus.CREATED)
			.build();

		return commandGateway.sendAndWait(createOrderCommand);

	}

}
