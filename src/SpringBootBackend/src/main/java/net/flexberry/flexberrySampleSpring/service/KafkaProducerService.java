package net.flexberry.flexberrySampleSpring.service;  //NOSONAR

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {
    @Value("${spring.kafka.template.default-topic}")
    private String topic;
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;
    @Value("${spring.kafka.client-id}")
    private String clientId;

    private Producer<Long, String> createProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, clientId);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        return new KafkaProducer<>(props);
    }

    void runProducer(String... args) throws Exception { //NOSONAR
        org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(KafkaProducerService.class);

        final Producer<Long, String> producer = createProducer();
        long time = System.currentTimeMillis();

        try {
            for (int index = 0; index < args.length; index++) {
                final ProducerRecord<Long, String> producerRecord = new ProducerRecord<>(topic, time + index, args[index]);

                RecordMetadata metadata = producer.send(producerRecord).get();

                long elapsedTime = System.currentTimeMillis() - time;
                logger.info("""
                                sent record(
                                    key=%s
                                    value=%s
                                )    
                                meta(partition=%d, offset=%d) time=%d""",
                        producerRecord.key(), producerRecord.value(), //NOSONAR
                        metadata.partition(), metadata.offset(), elapsedTime);
            }
        }
        finally {
            producer.flush();
            producer.close();
        }
    }

    public void sendMessageToKafka(String... args) {
        try {
            runProducer(args);
        } catch (Exception e) {
            throw new RuntimeException(e); //NOSONAR
        }
    }

    public void sendObjectOperationToKafka(String operation, Object object) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonString = mapper.writeValueAsString(object);
            sendMessageToKafka("Entity: " + object.getClass().getName()+ "\n" +
                    "Operation: " + operation +"\n" +
                    "Object: " + jsonString);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e); //NOSONAR
        }
    }

}
