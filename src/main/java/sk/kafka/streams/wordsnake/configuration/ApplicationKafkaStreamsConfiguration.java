package sk.kafka.streams.wordsnake.configuration;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("application.streams-config")
@Data //getters, setters
@NoArgsConstructor // Spring needs universal constructor to init properties
public class ApplicationKafkaStreamsConfiguration {

  private String applicationId;
  private String wordsToEliminate;
  private String schemaRegistryUrl = "http://schema-registry:8081";
  private String inputTopic = "input";
  private String outputRawTopic = "output_raw";
  private String outputProcessedTopic = "output_processed";
  private String invalidTopic = "invalid_topic";

}
