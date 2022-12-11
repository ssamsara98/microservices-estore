package com.ssamsara98.estore.products_service.query;

import lombok.Value;

import java.util.UUID;

@Value
public class FindProductByIdQuery {

	UUID productId;

}
