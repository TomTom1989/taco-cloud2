package tacos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
//@EntityScan(basePackages = "tacos")
public class TacoCloudApplication {

	public static void main(String[] args) {
		SpringApplication.run(TacoCloudApplication.class, args);
	}

}
