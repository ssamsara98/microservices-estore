package com.ssamsara98.estore.products_service.command.rest;

import lombok.RequiredArgsConstructor;
import com.ssamsara98.estore.core.model.ProductIdDto;
import com.ssamsara98.estore.products_service.command.CreateProductCommand;
import com.ssamsara98.estore.products_service.mapper.ProductMapper;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductsCommandController {

	private final Environment environment;
	private final ProductMapper mapper;
	private final CommandGateway commandGateway;

	@PostMapping
	public ResponseEntity<ProductIdDto> createProduct(@Valid @RequestBody CreateProductRestModel createProductRestModel) {
		CreateProductCommand createProductCommand = mapper.toCreateCommand(createProductRestModel);

		UUID productId = commandGateway.sendAndWait(createProductCommand);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
			.pathSegment("{id}")
			.buildAndExpand(productId)
			.toUri();

		return ResponseEntity
			.created(location)
			.body(new ProductIdDto(productId));
	}

	@PutMapping
	public String updateProduct() {
		return "Http PUT is handled";
	}

	@DeleteMapping
	public String deleteProduct() {
		return "Http DELETE is handled";
	}

}
