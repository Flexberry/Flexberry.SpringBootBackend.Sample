package net.flexberry.flexberrySampleSpring.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

@Service
@ComponentScan
public class KafkaProducerService {
    @Value("${spring.kafka.template.default-topic}")
    private String TOPIC;
    @Value("${spring.kafka.bootstrap-servers}")
    private String BOOTSTRAP_SERVERS;
    @Value("${spring.kafka.client-id}")
    private String CLIENT_ID;

    private Producer<Long, String> createProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, CLIENT_ID);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return new KafkaProducer<>(props);
    }

    void runProducer(String... args) throws Exception {
        final Producer<Long, String> producer = createProducer();
        long time = System.currentTimeMillis();

        try {
            for (int index = 0; index < args.length; index++) {
                final ProducerRecord<Long, String> record = new ProducerRecord<>(TOPIC, time + index, args[index]);

                RecordMetadata metadata = producer.send(record).get();

                long elapsedTime = System.currentTimeMillis() - time;
                System.out.printf("sent record(\n" +
                                "\tkey=%s \n" +
                                "\tvalue=%s \n)\n" +
                                "meta(partition=%d, offset=%d) time=%d\n",
                        record.key(), record.value(), metadata.partition(), metadata.offset(), elapsedTime);
            }
        } finally {
            producer.flush();
            producer.close();
        }
    }

    public void sendMessageToKafka(String... args) {
        try {
            runProducer(args);
        } catch (Exception e) {
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
        }
    }

}
