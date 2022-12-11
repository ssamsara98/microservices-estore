package com.ssamsara98.estore.core.query;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class FetchUserPaymentDetailsQuery {
	private UUID userId;
}
