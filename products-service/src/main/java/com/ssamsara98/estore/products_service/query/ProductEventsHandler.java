package com.ssamsara98.estore.products_service.query;

import com.ssamsara98.estore.products_service.core.data.ProductEntity;
import com.ssamsara98.estore.products_service.core.data.ProductsRepository;
import com.ssamsara98.estore.products_service.core.events.ProductCreatedEvent;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup("product-group")
public class ProductEventsHandler {
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
}
