package com.ssamsara98.estore.products_service.command;

import com.ssamsara98.estore.products_service.core.data.ProductLookupEntity;
import com.ssamsara98.estore.products_service.core.data.ProductLookupRepository;
import com.ssamsara98.estore.products_service.core.events.ProductCreatedEvent;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup("product-group")
public class ProductLookupEventsHandler {
	private final ProductLookupRepository productLookupRepository;

	public ProductLookupEventsHandler(ProductLookupRepository productLookupRepository) {
		this.productLookupRepository = productLookupRepository;
	}

	@EventHandler
	public void on(ProductCreatedEvent event) {
		ProductLookupEntity productLookupEntity = new ProductLookupEntity(event.getProductId(),
			event.getTitle());

		productLookupRepository.save(productLookupEntity);
	}
}
