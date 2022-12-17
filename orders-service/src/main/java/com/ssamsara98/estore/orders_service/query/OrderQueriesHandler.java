package com.ssamsara98.estore.orders_service.query;

import com.ssamsara98.estore.orders_service.core.data.OrderEntity;
import com.ssamsara98.estore.orders_service.core.data.OrdersRepository;
import com.ssamsara98.estore.orders_service.core.model.OrderSummary;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

@Component
public class OrderQueriesHandler {

	OrdersRepository ordersRepository;

	public OrderQueriesHandler(OrdersRepository ordersRepository) {
		this.ordersRepository = ordersRepository;
	}

	@QueryHandler
	public OrderSummary findOrder(FindOrderQuery findOrderQuery) {
		OrderEntity orderEntity = ordersRepository.findByOrderId(findOrderQuery.getOrderId());
		return new OrderSummary(orderEntity.getOrderId(),
			orderEntity.getOrderStatus(), "");
	}

}
