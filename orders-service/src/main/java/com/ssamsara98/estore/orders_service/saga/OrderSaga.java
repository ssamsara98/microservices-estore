package com.ssamsara98.estore.orders_service.saga;

import com.ssamsara98.estore.core.commands.ProcessPaymentCommand;
import com.ssamsara98.estore.core.commands.ReserveProductCommand;
import com.ssamsara98.estore.core.events.PaymentProcessedEvent;
import com.ssamsara98.estore.core.events.ProductReservedEvent;
import com.ssamsara98.estore.core.model.User;
import com.ssamsara98.estore.core.query.FetchUserPaymentDetailsQuery;
import com.ssamsara98.estore.orders_service.command.commands.ApprovedOrderCommand;
import com.ssamsara98.estore.orders_service.core.events.OrderApprovedEvent;
import com.ssamsara98.estore.orders_service.core.events.OrderCreatedEvent;
import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.CommandResultMessage;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.spring.stereotype.Saga;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Saga
public class OrderSaga {
	private final Logger LOGGER = LoggerFactory.getLogger(OrderSaga.class);

	@Autowired
	private transient CommandGateway commandGateway;

	@Autowired
	private transient QueryGateway queryGateway;

	@StartSaga
	@SagaEventHandler(associationProperty = "orderId")
	public void handle(OrderCreatedEvent orderCreatedEvent) {
		ReserveProductCommand reserveProductCommand = ReserveProductCommand.builder()
			.orderId(orderCreatedEvent.getOrderId())
			.productId(orderCreatedEvent.getProductId())
			.quantity(orderCreatedEvent.getQuantity())
			.userId(orderCreatedEvent.getUserId())
			.build();

		LOGGER.info("OrderCreatedEvent handled for orderId: " + reserveProductCommand.getOrderId() + " and productId" + reserveProductCommand.getProductId());

		commandGateway.send(reserveProductCommand, new CommandCallback<ReserveProductCommand, Object>() {
			@Override
			public void onResult(CommandMessage<? extends ReserveProductCommand> commandMessage, CommandResultMessage<?> commandResultMessage) {
				if (commandResultMessage.isExceptional()) {
//					Start a compensating transaction

				}
			}
		});
	}

	@SagaEventHandler(associationProperty = "orderId")
	public void handle(ProductReservedEvent productReservedEvent) {
//		process user payment
		LOGGER.info("ProductReservedEvent handled for orderId: " + productReservedEvent.getOrderId() + " and productId" + productReservedEvent.getProductId());

		FetchUserPaymentDetailsQuery fetchUserPaymentDetailsQuery = new FetchUserPaymentDetailsQuery(productReservedEvent.getUserId());

		User userPaymentDetails = null;

		try {
			userPaymentDetails = queryGateway.query(fetchUserPaymentDetailsQuery, ResponseTypes.instanceOf(User.class)).join();
		} catch (Exception ex) {
			LOGGER.error(ex.getMessage());

//			start compensating transaction
			return;
		}

		if (userPaymentDetails == null) {
//			start compensating transaction
			return;
		}

		LOGGER.info("Successfully fetched user payment details for user: " + userPaymentDetails.getFirstName());

		ProcessPaymentCommand processPaymentCommand = ProcessPaymentCommand.builder()
			.orderId(productReservedEvent.getOrderId())
			.paymentDetails(userPaymentDetails.getPaymentDetails())
			.paymentId(UUID.randomUUID().toString())
			.build();

		String result = null;
		try {
			result = commandGateway.sendAndWait(processPaymentCommand, 10, TimeUnit.SECONDS);
		} catch (Exception ex) {
			LOGGER.error(ex.getMessage());

//			start compensating transaction
			return;
		}

		if (result == null) {
			LOGGER.info("The ProcessPaymentCommand resulted in NULL. Initiating a compensating transaction");
//			start compensating transaction
		}

	}

	@SagaEventHandler(associationProperty = "orderId")
	public void handle(PaymentProcessedEvent paymentProcessedEvent) {
//		send an ApprovedOrderCommand
		ApprovedOrderCommand approvedOrderCommand = new ApprovedOrderCommand(paymentProcessedEvent.getOrderId());
		commandGateway.send(approvedOrderCommand);
	}

	@EndSaga
	@SagaEventHandler(associationProperty = "orderId")
	public void handle(OrderApprovedEvent orderApprovedEvent) {
		LOGGER.info("Order is approved. Order Saga is complete for orderId: " + orderApprovedEvent.getOrderId());
//		SagaLifecycle.end();
	}
}
