package com.ssamsara98.estore.products_service.rest;

import com.ssamsara98.estore.products_service.command.CreateProductCommand;
import com.ssamsara98.estore.products_service.query.FindProductsQuery;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("products")
public class ProductsController {
	private final Environment env;
	private final CommandGateway commandGateway;
	private final QueryGateway queryGateway;

	@Autowired
	public ProductsController(Environment env, CommandGateway commandGateway, QueryGateway queryGateway) {
		this.env = env;
		this.commandGateway = commandGateway;
		this.queryGateway = queryGateway;
	}


	//	Command
	@PostMapping
	public String createProduct(@Valid @RequestBody CreateProductRestModel createProductRestModel) {
		System.out.println("ProductsController->createProduct " + createProductRestModel.toString());
		CreateProductCommand createProductCommand = CreateProductCommand.builder()
			.price(createProductRestModel.getPrice())
			.quantity(createProductRestModel.getQuantity())
			.title(createProductRestModel.getTitle())
			.productId(UUID.randomUUID().toString())
			.build();

		String returnValue;
		try {
			returnValue = commandGateway.sendAndWait(createProductCommand);
		} catch (Exception e) {
			returnValue = e.getLocalizedMessage();
		}

		return returnValue;
	}

	@PutMapping
	public String updateProduct() {
		return "UpdateProduct";
	}

	@DeleteMapping
	public String deleteProduct() {
		return "DeleteProduct";
	}

	//	Query
	@GetMapping
	public List<ProductRestModel> getProducts() {
		FindProductsQuery findProductQuery = new FindProductsQuery();
		List<ProductRestModel> products = queryGateway.query(findProductQuery, ResponseTypes.multipleInstancesOf(ProductRestModel.class)).join();

		return products;
	}
}
