package dev.aloysius.ShippingLabelGenerator;

import dev.aloysius.ShippingLabelGenerator.Controller.ShipmentController;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

@SpringBootApplication
public class ShippingLabelGeneratorApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShippingLabelGeneratorApplication.class, args);
	}

}