package sk.kafka.streams.wordsnake.transform;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KeyValueMapper;

@Slf4j
@NoArgsConstructor
public class SentenceTransformer implements
    KeyValueMapper<GenericRecord, GenericRecord, KeyValue<GenericRecord, GenericRecord>> {

  @Override
  public KeyValue<GenericRecord, GenericRecord> apply(GenericRecord key, GenericRecord value) {
    log.info("Value content{}", value.get("content"));
    return new KeyValue<>(key, value);
  }
}
