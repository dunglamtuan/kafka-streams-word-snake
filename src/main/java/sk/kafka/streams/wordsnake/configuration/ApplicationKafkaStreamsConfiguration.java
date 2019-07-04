package sk.kafka.streams.wordsnake.configuration;

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
  private String inputPathFile;
  private String schemaRegistryUrl = "http://localhost:8081";
  private String inputTopic = "input";
  private String outputProcessedTopic = "output_processed";
  private String outputFilePath = "C:\\Users\\lamtuad\\Documents\\kafka-streams-word-snake\\output.txt";

}
