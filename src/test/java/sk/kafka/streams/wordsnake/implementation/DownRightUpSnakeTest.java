package sk.kafka.streams.wordsnake.implementation;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import org.junit.jupiter.api.Test;

class DownRightUpSnakeTest {

  @Test
  void makeSnakeTest() {
    //setup
    String sentence = "LOREM IPSUM DOLOR SIT AMET";
    WordSnakeService snakeService = new DownRightUpSnake();

    String expected =
        "L      \n" +
        "O      \n" +
        "R      \n" +
        "E      \n" +
        "IPSUD  \n" +
        "    O T\n" +
        "    L E\n" +
        "    O M\n" +
        "    SIA\n";

    //run
    String snake = snakeService.makeSnake(sentence);

    //verify
    assertThat(snake).isEqualTo(expected);
  }

}
