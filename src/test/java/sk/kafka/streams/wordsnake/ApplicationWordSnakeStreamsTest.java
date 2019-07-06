package sk.kafka.streams.wordsnake;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Properties;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.TopologyTestDriver;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import sk.kafka.streams.wordsnake.configuration.ApplicationKafkaStreamsConfiguration;

public class ApplicationWordSnakeStreamsTest {

  private static ApplicationKafkaStreamsConfiguration appConfig;
  private static KafkaStreamsConfiguration stemsConfig;

  //@Test
  void basicTest() {
    ApplicationKafkaStreamsConfiguration appConfig = mock(ApplicationKafkaStreamsConfiguration.class);
    when(appConfig.getInputTopic()).thenReturn("input");
    when(appConfig.getOutputProcessedTopic()).thenReturn("output");

    KafkaStreamsConfiguration stemsConfig = mock(KafkaStreamsConfiguration.class);

  }

  private static Properties driverConfig() {
    Properties config = new Properties();
    config.put(StreamsConfig.APPLICATION_ID_CONFIG, "test");
    config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "test:1234");
    config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.ByteArray().getClass().getName());

    return config;
  }
}
