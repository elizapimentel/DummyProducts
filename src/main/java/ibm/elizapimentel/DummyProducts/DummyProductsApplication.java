package ibm.elizapimentel.DummyProducts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class DummyProductsApplication {

	public static void main(String[] args) {

		SpringApplication.run(DummyProductsApplication.class, args);
	}

}
