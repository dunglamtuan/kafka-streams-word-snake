package sk.kafka.streams.wordsnake.transform;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KeyValueMapper;
import sk.kafka.streams.wordsnake.implementation.WordSnakeService;

@Slf4j
@AllArgsConstructor
public class SentenceTransformer implements
    KeyValueMapper<GenericRecord, GenericRecord, KeyValue<GenericRecord, GenericRecord>> {

  private WordSnakeService snakeService;

  @Override
  public KeyValue<GenericRecord, GenericRecord> apply(GenericRecord key, GenericRecord value) {
    String sentenceContent = value.get("content").toString();
    log.info("Value content{}", sentenceContent);

    String wordSnake = snakeService.makeSnake(sentenceContent);
    value.put("content", wordSnake);
    return new KeyValue<>(key, value);
  }
}
