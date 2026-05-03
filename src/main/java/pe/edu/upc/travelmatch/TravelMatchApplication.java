package pe.edu.upc.travelmatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class TravelMatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(TravelMatchApplication.class, args);
	}

}
