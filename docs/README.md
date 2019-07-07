Program description
======

Program consists of 3 steps:
1.  Using *EventListener* and *KafkaProducer* to sending test data from file to kafka cluster (started by docker)
2.  Streams app starts and transforms test data to word snake.
3.  Using *KafkaConsumer* to read data from topic consisted of transformed data and writes to file

**Test data file and output data file can be configured by application.properties**