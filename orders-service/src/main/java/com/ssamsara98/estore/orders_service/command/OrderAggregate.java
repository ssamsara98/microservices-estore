/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ssamsara98.estore.orders_service.command;

import com.ssamsara98.estore.orders_service.command.commands.ApprovedOrderCommand;
import com.ssamsara98.estore.orders_service.core.events.OrderApprovedEvent;
import com.ssamsara98.estore.orders_service.core.events.OrderCreatedEvent;
import com.ssamsara98.estore.orders_service.core.model.OrderStatus;
import com.ssamsara98.estore.orders_service.command.commands.CreateOrderCommand;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

@Aggregate
public class OrderAggregate {

	@AggregateIdentifier
	private String orderId;
	private String productId;
	private String userId;
	private int quantity;
	private String addressId;
	private OrderStatus orderStatus;

	public OrderAggregate() {
	}

	@CommandHandler
	public OrderAggregate(CreateOrderCommand createOrderCommand) {
		OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent();
		BeanUtils.copyProperties(createOrderCommand, orderCreatedEvent);

		AggregateLifecycle.apply(orderCreatedEvent);
	}

	@EventSourcingHandler
	public void on(OrderCreatedEvent orderCreatedEvent) throws Exception {
		this.orderId = orderCreatedEvent.getOrderId();
		this.productId = orderCreatedEvent.getProductId();
		this.userId = orderCreatedEvent.getUserId();
		this.addressId = orderCreatedEvent.getAddressId();
		this.quantity = orderCreatedEvent.getQuantity();
		this.orderStatus = orderCreatedEvent.getOrderStatus();
	}

	@CommandHandler
	public void handle(ApprovedOrderCommand approvedOrderCommand) {
//		create and publish the OrderApprovedEvent
		OrderApprovedEvent orderApprovedEvent = new OrderApprovedEvent(approvedOrderCommand.getOrderId());

		AggregateLifecycle.apply(orderApprovedEvent);
	}

	@EventSourcingHandler
	protected void on(OrderApprovedEvent orderApprovedEvent) {
		this.orderStatus = orderApprovedEvent.getOrderStatus();
	}
}
