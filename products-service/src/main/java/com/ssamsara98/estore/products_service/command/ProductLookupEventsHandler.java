package com.ssamsara98.estore.products_service.command;

import lombok.RequiredArgsConstructor;
import com.ssamsara98.estore.products_service.core.data.ProductLookupEntity;
import com.ssamsara98.estore.products_service.core.data.ProductLookupRepository;
import com.ssamsara98.estore.products_service.core.events.ProductCreatedEvent;
import com.ssamsara98.estore.products_service.mapper.ProductMapper;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.ResetHandler;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup("product-group")
@RequiredArgsConstructor
public class ProductLookupEventsHandler {

	private final ProductLookupRepository repository;
	private final ProductMapper mapper;

	@EventHandler
	public void on(ProductCreatedEvent event) {
		ProductLookupEntity productLookupEntity = mapper.toProductLookupEntity(event);
		repository.save(productLookupEntity);
	}

	@ResetHandler
	public void reset(){
		repository.deleteAllInBatch();
	}
}
