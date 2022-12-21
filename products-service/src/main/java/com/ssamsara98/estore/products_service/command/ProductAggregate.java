package com.ssamsara98.estore.products_service.command;

import com.ssamsara98.estore.core.commands.ReserveProductCommand;
import com.ssamsara98.estore.core.events.ProductReservedEvent;
import com.ssamsara98.estore.products_service.core.events.ProductCreatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

@Aggregate
public class ProductAggregate {
	@AggregateIdentifier
	private String productId;
	private String title;
	private BigDecimal price;
	private Integer quantity;

	public ProductAggregate() {
	}

	@CommandHandler
	public ProductAggregate(CreateProductCommand createProductCommand) {
//		 Validate Create Product Command

		if (createProductCommand.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException("Price cannot be less or equal than zero");
		}

		if (createProductCommand.getTitle() == null
			|| createProductCommand.getTitle().isBlank()) {
			throw new IllegalArgumentException("Title cannot be empty");
		}

		ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent();

		BeanUtils.copyProperties(createProductCommand, productCreatedEvent);

		AggregateLifecycle.apply(productCreatedEvent);
	}

	@CommandHandler
	public void handle(ReserveProductCommand reserveProductCommand) {
		if (quantity < reserveProductCommand.getQuantity()) {
			throw new IllegalArgumentException("Insufficient number of items in stock");
		}

		ProductReservedEvent productReservedEvent = ProductReservedEvent.builder()
			.orderId(reserveProductCommand.getOrderId())
			.productId(reserveProductCommand.getProductId())
			.quantity(reserveProductCommand.getQuantity())
			.userId(reserveProductCommand.getUserId())
			.build();

		AggregateLifecycle.apply(productReservedEvent);
	}

	@EventSourcingHandler
	public void on(ProductCreatedEvent event) {
		System.out.println("ProductAggregate->on(ProductCreatedEvent productCreatedEvent) " + event.toString());
		this.productId = event.getProductId();
		this.price = event.getPrice();
		this.title = event.getTitle();
		this.quantity = event.getQuantity();
	}

	@EventSourcingHandler
	public void on(ProductReservedEvent productReservedEvent) {
		this.quantity -= productReservedEvent.getQuantity();
	}
}
