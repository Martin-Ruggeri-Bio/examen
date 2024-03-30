package ar.com.plug.examen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import ar.com.plug.examen.domain.model.Product;
import ar.com.plug.examen.domain.service.ProductService;

@SpringBootApplication
@Configuration
@ComponentScan("ar.com.plug")
public class Application implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Autowired
	private ProductService productService;

	@Override
	public void run(String... args) throws Exception {
		Product product = new Product(
			1L,
			"Super Pancho",
			"Pancho con jamon, queso, palta y lluvia de papas",
			750.00F,
			"https://cdn.cienradios.com/wp-content/uploads/sites/13/2020/04/Panchos-argentinos-nota.jpg",
			true,
			"2024-03-29T12:00:00Z",
			"2024-03-29T12:00:00Z"
		);
		System.out.println("este es el product por default");
		System.out.println(product);
		product = productService.add(product);
	}
}