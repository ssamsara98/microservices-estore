package com.ssamsara98.estore.products_service.core.errorhandling;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorMessage {

	@Builder.Default
	private LocalDateTime timestamp = LocalDateTime.now();

	private String message;

}
