package com.ssamsara98.estore.products_service;

//import com.ssamsara98.estore.core.config.XStreamConfig;
import com.ssamsara98.estore.products_service.command.interceptors.CreateProductCommandInterceptor;
import com.ssamsara98.estore.products_service.core.errorhandling.ProductsServiceEventsErrorHandler;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.config.EventProcessingConfigurer;
import org.axonframework.eventsourcing.EventCountSnapshotTriggerDefinition;
import org.axonframework.eventsourcing.SnapshotTriggerDefinition;
import org.axonframework.eventsourcing.Snapshotter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Import;

@EnableDiscoveryClient
@SpringBootApplication
//@Import({XStreamConfig.class})
public class ProductsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductsServiceApplication.class, args);
	}

	@Autowired
	public void registerCreateProductCommandInterceptor(ApplicationContext context,
			CommandBus commandBus) {
		commandBus.registerDispatchInterceptor(context.getBean(CreateProductCommandInterceptor.class));

	}

	@Autowired
	public void configure(EventProcessingConfigurer config) {
		config.registerListenerInvocationErrorHandler("product-group",
				conf -> new ProductsServiceEventsErrorHandler());

//		config.registerListenerInvocationErrorHandler("product-group",
//				conf -> PropagatingErrorHandler.instance());
	}

	@Bean(name="productSnapshotTriggerDefinition")
	public SnapshotTriggerDefinition productSnapshotTriggerDefinition(Snapshotter snapshotter) {
		return new EventCountSnapshotTriggerDefinition(snapshotter, 3);
	}

}
