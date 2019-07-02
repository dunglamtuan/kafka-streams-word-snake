package sk.kafka.streams.wordsnake.transform;

import lombok.AllArgsConstructor;
import sk.kafka.streams.wordsnake.configuration.ApplicationKafkaStreamsConfiguration;
import sk.kafka.streams.wordsnake.model.Sentence;

@AllArgsConstructor
public class SentenceProcessor {

  private String rawSentence;
  private final ApplicationKafkaStreamsConfiguration applicationConfig;

  public Sentence processSentence() {
    String partialProcessed = processToUpperCase(this.rawSentence);
    partialProcessed = processWordsElimination(partialProcessed);
    return new Sentence(partialProcessed);
  }

  private String processToUpperCase(String processingSentence) {
    return processingSentence.toUpperCase();
  }

  private String processWordsElimination(String processingSentence) {
    String result = processingSentence;
    for (char eliminate : applicationConfig.getWordsToEliminate().toCharArray()) {
      result = result.replaceAll(String.valueOf(eliminate), "");
    }

    return result;
  }
}