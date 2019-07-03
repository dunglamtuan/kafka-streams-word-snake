package sk.kafka.streams.wordsnake.transform;

import lombok.AllArgsConstructor;
import sk.kafka.streams.wordsnake.configuration.ApplicationKafkaStreamsConfiguration;

@AllArgsConstructor
public class SentenceProcessor {

  private final ApplicationKafkaStreamsConfiguration applicationConfig;

  public String processSentence(String rawSentence) {
    String partialProcessed = processStringTrim(rawSentence);
    partialProcessed = processToUpperCase(partialProcessed);
    return processWordsElimination(partialProcessed).trim();
  }

  private String processStringTrim(String processingSentence) {
    return processingSentence.trim();
  }

  private String processToUpperCase(String processingSentence) {
    return processingSentence.toUpperCase();
  }

  private String processWordsElimination(String processingSentence) {
    String result = processingSentence;
    for (char eliminate : applicationConfig.getWordsToEliminate().toCharArray()) {
      result = result.replace(String.valueOf(eliminate), "");
    }
    return result;
  }
}