package sk.kafka.streams.wordsnake;

import io.confluent.kafka.schemaregistry.client.MockSchemaRegistryClient;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;

public class KafkaSupport {

  static KafkaProducer<GenericRecord, GenericRecord> getProducer(String bootstrapServer, MockSchemaRegistryClient schemaRegistry) {
    Map<String, Object> configs = new HashMap<>();
    configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
    KafkaAvroSerializer kafkaAvroSerializer = new KafkaAvroSerializer(schemaRegistry);

    return new KafkaProducer(configs, kafkaAvroSerializer, kafkaAvroSerializer);
  }

  static Consumer<GenericRecord, GenericRecord> getConsumer(String bootstrapServer,  MockSchemaRegistryClient schemaRegistry, String topic) {
    Map<String, Object> configs = new HashMap<>();
    configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
    configs.put(ConsumerConfig.GROUP_ID_CONFIG, "group-test");
    configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
    configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);

    KafkaAvroDeserializer deserializer = new KafkaAvroDeserializer(schemaRegistry);

    Consumer<GenericRecord, GenericRecord> consumer = new KafkaConsumer(configs, deserializer, deserializer);

    consumer.subscribe(Collections.singletonList(topic));
    return consumer;
  }

}
