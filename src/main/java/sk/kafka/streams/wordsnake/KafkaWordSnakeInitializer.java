package sk.kafka.streams.wordsnake;

import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes.StringSerde;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import sk.kafka.streams.wordsnake.configuration.ApplicationKafkaStreamsConfiguration;

@EnableKafka
@EnableKafkaStreams
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
    properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, StringSerde.class.getName());
    properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, StringSerde.class.getName());

    return new KafkaStreamsConfiguration(properties);
  }

  @Bean
  public ApplicationWordSnakeStreams startStreams() {
    ApplicationWordSnakeStreams streams = new ApplicationWordSnakeStreams(applicationConfiguration,
        kStreamsConfigs());

    streams.setupTopology();
    return streams;
  }

}
