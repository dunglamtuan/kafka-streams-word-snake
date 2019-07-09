package sk.kafka.streams.wordsnake;

import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.streams.serdes.avro.GenericAvroSerializer;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.TopicPartition;

class KafkaSupport {

  static KafkaProducer<GenericRecord, GenericRecord> getProducer(String bootstrapServer, String schemaRegistry) {
    Map<String, Object> configs = new HashMap<>();
    configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
    configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, GenericAvroSerializer.class);
    configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, GenericAvroSerializer.class);
    configs.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistry);

    return new KafkaProducer<>(configs);
  }

  static Consumer<GenericRecord, GenericRecord> getConsumer(String bootstrapServer,  String schemaRegistry, String topic) {
    Map<String, Object> configs = new HashMap<>();
    configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
    configs.put(ConsumerConfig.GROUP_ID_CONFIG, "group-test");
    configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
    configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
    configs.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistry);

    Consumer<GenericRecord, GenericRecord> consumer = new KafkaConsumer(configs);

    List<TopicPartition> topicPartitions = Collections.singletonList(new TopicPartition(topic, 0));
    consumer.assign(topicPartitions);
    consumer.seekToBeginning(topicPartitions);
    //consumer.subscribe(Collections.singletonList(topic));
    return consumer;
  }

}
