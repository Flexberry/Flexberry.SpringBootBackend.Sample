package net.flexberry.flexberrySampleSpring;

import net.flexberry.flexberrySampleSpring.service.KafkaProducerService;
import net.flexberry.flexberrySampleSpring.service.StorageService;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;


@SpringBootApplication
@EnableJpaRepositories(repositoryFactoryBeanClass = EnversRevisionRepositoryFactoryBean.class)
@EnableKafka
public class FlexberrySampleSpringApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(FlexberrySampleSpringApplication.class, args);
		sendExampleLogMessage();
//		sendExampleKafkaMessage();
		KafkaProducerService producerService = new KafkaProducerService();
		producerService.sendMessageToKafka();
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
	@Autowired
	private static KafkaSenderExample kafkaSenderExample;
	@Value("${spring.kafka.template.default-topic}")
	private static String TOPIC;
	private static void sendExampleKafkaMessage() {
		kafkaSenderExample.sendMessage("~Hello, world of Kafka!~", TOPIC);
	}

//	@Bean
//	public NewTopic topic() {
//		return TopicBuilder.name("topic1")
//				.partitions(10)
//				.replicas(1)
//				.build();
//	}
//
//	@Bean
//	public ApplicationRunner runner(KafkaTemplate<String, String> template) {
//		return args -> {
//			template.send("topic1", "test");
//		};
//	}
}
