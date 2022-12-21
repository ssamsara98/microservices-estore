package com.ssamsara98.estore.products_service.query;

import com.ssamsara98.estore.core.events.ProductReservedEvent;
import com.ssamsara98.estore.products_service.core.data.ProductEntity;
import com.ssamsara98.estore.products_service.core.data.ProductsRepository;
import com.ssamsara98.estore.products_service.core.events.ProductCreatedEvent;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup("product-group")
public class ProductEventsHandler {
	private final Logger LOGGER = LoggerFactory.getLogger(ProductEventsHandler.class);
	private final ProductsRepository productsRepository;

	@Autowired
	public ProductEventsHandler(ProductsRepository productsRepository) {
		this.productsRepository = productsRepository;
	}

	@ExceptionHandler(resultType = Exception.class)
	public void handle(Exception exception) throws Exception {
		throw exception;
	}

	@ExceptionHandler(resultType = IllegalArgumentException.class)
	public void handle(IllegalArgumentException exception) {

	}

	@EventHandler
	public void on(ProductCreatedEvent event) {
		System.out.println("ProductEventsHandler->on(ProductCreatedEvent event) " + event.toString());

		ProductEntity productEntity = new ProductEntity();
		BeanUtils.copyProperties(event, productEntity);

		try {
			productsRepository.save(productEntity);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@EventHandler
	public void on(ProductReservedEvent productReservedEvent) {
		ProductEntity productEntity = productsRepository.findByProductId(productReservedEvent.getProductId());
		productEntity.setQuantity(productEntity.getQuantity() - productReservedEvent.getQuantity());
		productsRepository.save(productEntity);

		LOGGER.info("ProductReservedEvent is called for productId: " + productReservedEvent.getProductId() + " and orderId" + productReservedEvent.getOrderId());
	}
}
