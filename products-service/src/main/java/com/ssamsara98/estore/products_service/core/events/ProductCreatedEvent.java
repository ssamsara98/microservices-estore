package com.ssamsara98.estore.products_service.core.events;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class ProductCreatedEvent {
	private UUID productId;
	private String title;
	private BigDecimal price;
	private Integer quantity;
}
