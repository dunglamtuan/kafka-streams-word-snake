package sk.kafka.streams.wordsnake;

import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.GenericAvroSerde;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import sk.kafka.streams.wordsnake.configuration.ApplicationKafkaStreamsConfiguration;
import sk.kafka.streams.wordsnake.transform.SentenceProcessor;

@Configuration
@EnableKafka
@AllArgsConstructor
@Slf4j
public class KafkaWordSnakeInitializer {

  private final KafkaProperties kafkaProperties;
  private final ApplicationKafkaStreamsConfiguration applicationConfiguration;

  @Bean
  public KafkaStreamsConfiguration kStreamsConfigs() {
    new HashMap<String, Object>();
    Map<String, Object> properties = new HashMap<>();
    properties.put(StreamsConfig.CLIENT_ID_CONFIG, kafkaProperties.getClientId());
    properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
    properties.put(StreamsConfig.APPLICATION_ID_CONFIG, applicationConfiguration.getApplicationId());
    properties.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG,
        applicationConfiguration.getSchemaRegistryUrl());
    properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, GenericAvroSerde.class.getName());
    properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, GenericAvroSerde.class.getName());

    return new KafkaStreamsConfiguration(properties);
  }

  @Bean
  public ApplicationWordSnakeStreams startStreams() {
    ApplicationWordSnakeStreams streams = new ApplicationWordSnakeStreams(
        applicationConfiguration, kStreamsConfigs(), initProcessor());

    streams.setupTopology();
    return streams;
  }

  @Bean
  public SentenceProcessor initProcessor() {
    return new SentenceProcessor(applicationConfiguration);
  }

}
