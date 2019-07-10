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

  private Class<? extends WordSnakeService> wordSnakeClass;
  private SentenceProcessor sentenceProcessor;

  @Override
  public KeyValue<GenericRecord, GenericRecord> apply(GenericRecord key, GenericRecord value) {
    WordSnakeService snakeService;
    log.debug("KeyValueMapper.apply with class {}", wordSnakeClass.getName());
    try {
      snakeService = wordSnakeClass.getDeclaredConstructor().newInstance();
    } catch (Exception e) {
      log.error("Cannot construct word snake service from class {}", wordSnakeClass, e);
      return null;
    }
    String sentenceContent = value.get(Sentence.CONTENT_FIELD_NAME).toString();
    sentenceContent = sentenceProcessor.processSentence(sentenceContent);

    String wordSnake = snakeService.makeSnake(sentenceContent);

    value.put(Sentence.CONTENT_FIELD_NAME, wordSnake);
    return new KeyValue<>(key, value);
  }
}
