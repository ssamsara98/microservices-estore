/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ssamsara98.estore.orders_service.query;

import com.ssamsara98.estore.orders_service.core.data.OrderEntity;
import com.ssamsara98.estore.orders_service.core.data.OrdersRepository;
import com.ssamsara98.estore.orders_service.core.events.OrderApprovedEvent;
import com.ssamsara98.estore.orders_service.core.events.OrderCreatedEvent;
import com.ssamsara98.estore.orders_service.core.events.OrderRejectedEvent;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup("order-group")
public class OrderEventsHandler {

	private final OrdersRepository ordersRepository;

	public OrderEventsHandler(OrdersRepository ordersRepository) {
		this.ordersRepository = ordersRepository;
	}

	@EventHandler
	public void on(OrderCreatedEvent event) throws Exception {
		OrderEntity orderEntity = new OrderEntity();
		BeanUtils.copyProperties(event, orderEntity);

		ordersRepository.save(orderEntity);
	}


	@EventHandler
	public void on(OrderApprovedEvent orderApprovedEvent) {
		OrderEntity orderEntity = ordersRepository.findByOrderId(orderApprovedEvent.getOrderId());

		if (orderEntity == null) {
			// TODO: Do something about it
			return;
		}

		orderEntity.setOrderStatus(orderApprovedEvent.getOrderStatus());

		ordersRepository.save(orderEntity);

	}

	@EventHandler
	public void on(OrderRejectedEvent orderRejectedEvent) {
		OrderEntity orderEntity = ordersRepository.findByOrderId(orderRejectedEvent.getOrderId());
		orderEntity.setOrderStatus(orderRejectedEvent.getOrderStatus());
		ordersRepository.save(orderEntity);
	}

}
