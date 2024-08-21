package ns.sos;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;


@EnableMethodSecurity
@EnableJpaAuditing
@SpringBootApplication
@EnableScheduling
@EnableEncryptableProperties
public class SosApplication {

	public static void main(String[] args) {
		SpringApplication.run(SosApplication.class, args);
	}
}
