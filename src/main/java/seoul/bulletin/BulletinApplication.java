package seoul.bulletin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class BulletinApplication {

	public static void main(String[] args) {
		SpringApplication.run(BulletinApplication.class, args);
	}

}
