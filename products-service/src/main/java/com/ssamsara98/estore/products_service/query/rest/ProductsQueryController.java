package com.ssamsara98.estore.products_service.query.rest;

import lombok.RequiredArgsConstructor;
import com.ssamsara98.estore.products_service.query.FindProductByIdQuery;
import com.ssamsara98.estore.products_service.query.FindProductsQuery;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductsQueryController {

	private final QueryGateway queryGateway;

	@GetMapping
	public List<ProductRestModel> getProducts() {

		FindProductsQuery findProductsQuery = new FindProductsQuery();

		CompletableFuture<List<ProductRestModel>> query = queryGateway.query(findProductsQuery, ResponseTypes.multipleInstancesOf(ProductRestModel.class));
		return query.join();
	}

	@GetMapping("{id}")
	public ProductRestModel getProductById(@PathVariable UUID id) {
		FindProductByIdQuery findProductByIdQuery = new FindProductByIdQuery(id);
		CompletableFuture<ProductRestModel> query = queryGateway.query(findProductByIdQuery, ProductRestModel.class);
		return query.join();
	}

}
