package com.ssamsara98.estore.orders_service.core.events;

import com.ssamsara98.estore.orders_service.core.model.OrderStatus;

import lombok.Value;

@Value
public class OrderApprovedEvent {

	private final String orderId;
	private final OrderStatus orderStatus = OrderStatus.APPROVED;

}
