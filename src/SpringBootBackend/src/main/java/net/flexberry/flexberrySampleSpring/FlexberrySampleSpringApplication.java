package net.flexberry.flexberrySampleSpring;

import net.flexberry.flexberrySampleSpring.service.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(repositoryFactoryBeanClass = EnversRevisionRepositoryFactoryBean.class)
public class FlexberrySampleSpringApplication {
	public static void main(String[] args) {
		SpringApplication.run(FlexberrySampleSpringApplication.class, args);
		sendExampleLogMessage();
	}
	private static void sendExampleLogMessage() {
		org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(FlexberrySampleSpringApplication.class);
		String startMessage = "Hello my dear friend! Flexberry Sample Spring Application is running!";

		logger.trace("A TRACE Message: {}", startMessage);
		logger.debug("A DEBUG Message: {}", startMessage);
		logger.info("A INFO Message: {}", startMessage);
		logger.warn("A WARN Message: {}", startMessage);
		logger.error("A ERROR Message: {}", startMessage);
	}
}
