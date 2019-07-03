package sk.kafka.streams.wordsnake.transform;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KeyValueMapper;
import sk.kafka.streams.wordsnake.implementation.WordSnakeService;
import sk.kafka.streams.wordsnake.model.Sentence;

@Slf4j
@AllArgsConstructor
public class SentenceTransformer implements
    KeyValueMapper<GenericRecord, GenericRecord, KeyValue<GenericRecord, GenericRecord>> {

  private WordSnakeService snakeService;
  private SentenceProcessor sentenceProcessor;

  @Override
  public KeyValue<GenericRecord, GenericRecord> apply(GenericRecord key, GenericRecord value) {
    String sentenceContent = value.get(Sentence.CONTENT_FIELD).toString();
    log.info("Value content -> {}", sentenceContent);

    sentenceContent = sentenceProcessor.processSentence(sentenceContent);

    String wordSnake = snakeService.makeSnake(sentenceContent);

    value.put(Sentence.CONTENT_FIELD, wordSnake);
    return new KeyValue<>(key, value);
  }
}
