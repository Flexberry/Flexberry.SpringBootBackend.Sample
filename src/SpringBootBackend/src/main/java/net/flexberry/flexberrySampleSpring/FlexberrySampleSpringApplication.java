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

		logger.trace(String.format("A TRACE Message: {0}", startMessage));
		logger.debug(String.format("A DEBUG Message: {0}", startMessage));
		logger.info(String.format("A INFO Message: {0}", startMessage));
		logger.warn(String.format("A WARN Message: {0}", startMessage));
		logger.error(String.format("A ERROR Message: {0}", startMessage));
	}
	@Bean
	CommandLineRunner init(StorageService storageService) {
		return (args) -> {
			storageService.init();
		};
	}
}
