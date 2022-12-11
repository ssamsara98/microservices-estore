package com.ssamsara98.estore.products_service.mapper;

import com.ssamsara98.estore.core.commands.CancelProductReservationCommand;
import com.ssamsara98.estore.core.commands.ReserveProductCommand;
import com.ssamsara98.estore.core.events.ProductReservationCancelledEvent;
import com.ssamsara98.estore.core.events.ProductReservedEvent;
import com.ssamsara98.estore.products_service.command.CreateProductCommand;
import com.ssamsara98.estore.products_service.command.rest.CreateProductRestModel;
import com.ssamsara98.estore.products_service.core.data.ProductEntity;
import com.ssamsara98.estore.products_service.core.data.ProductLookupEntity;
import com.ssamsara98.estore.products_service.core.events.ProductCreatedEvent;
import com.ssamsara98.estore.products_service.query.rest.ProductRestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

@Mapper(imports = UUID.class)
public interface ProductMapper {

	ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

	@Mapping(target = "productId", expression = "java(UUID.randomUUID())")
	CreateProductCommand toCreateCommand(CreateProductRestModel model);

	ProductCreatedEvent toCreatedEvent(CreateProductCommand createProductCommand);

	ProductEntity toProductEntity(ProductCreatedEvent event);

	ProductRestModel toProductRestModel(ProductEntity entity);

	ProductLookupEntity toProductLookupEntity(ProductCreatedEvent event);

	ProductReservedEvent toEvent(ReserveProductCommand command);

	ProductReservationCancelledEvent toEvent(CancelProductReservationCommand command);

}
