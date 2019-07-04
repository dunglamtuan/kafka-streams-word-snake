package sk.kafka.streams.wordsnake.transform;

import java.util.stream.IntStream;
import lombok.AllArgsConstructor;
import sk.kafka.streams.wordsnake.configuration.ApplicationKafkaStreamsConfiguration;

@AllArgsConstructor
public class SentenceProcessor {

  private final ApplicationKafkaStreamsConfiguration applicationConfig;

  String processSentence(String rawSentence) {
    String partialProcessed = processStringTrim(rawSentence);
    partialProcessed = processToUpperCase(partialProcessed);
    partialProcessed = processWordsElimination(partialProcessed);
    return makeSentenceValidForSnake(partialProcessed);
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

  private String makeSentenceValidForSnake(String processingSentence) {
    String[] words = processingSentence.split(" ");

    String[] results = new String[words.length];
    results[0] = words[0];
    IntStream.range(1, words.length).forEach(index -> {
      String previousWord = words[index - 1];
      results[index] = previousWord.charAt(previousWord.length() - 1) + words[index].substring(1);
    });

    return String.join(" ", results);
  }
}