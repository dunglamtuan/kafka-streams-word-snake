## Example usage of kafka streams

![alt text](https://emojis.slackmojis.com/emojis/images/1489319472/1861/apache_kafka.png?1489319472)  ![alt text](https://chocolatey.org/content/packageimages/SpringToolSuite.3.9.6.png)  ![alt text](https://advance.careers/media/CACHE/images/companies/logos/043ab2d2-d23f-46b4-bb3e-df473d03fc76/9276bc7fb9e00db368290df1ab59c239.jpg)

Application loads sentences from file (each line is one sentence), then each sentence is send to Kafka by kafka producer.

By using Kafka Streams then sentences are modified to another topic. Modification consists of string trim, character elimination (characters can be parametrized).

The goal is make a snake from words in each sentence, for that, the modification process contains of step, which make a sentence valid for a snake, i.e the first character of each word (except) is the same as the last word of the previous word in a sentence.

## Environment
Kafka cluster is created by docker-compose.yml.

`Make sure to have running docker containers before start the application`

## Development
Integration tests depend on external kafka cluster (from docker). Make sure to start containers before running integration tests