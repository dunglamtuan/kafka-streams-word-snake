package sk.kafka.streams.wordsnake.implementation;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.Test;

class DownRightSnakeTest {

  @Test
  void makeSnakeTest() {
    // setup
    String sentence = "LOREM IPSUM DOLOR SIT AMET";

    String expected =
          "L      \n"
        + "O      \n"
        + "R      \n"
        + "E      \n"
        + "IPSUD  \n"
        + "    O  \n"
        + "    L  \n"
        + "    O  \n"
        + "    SIA\n"
        + "      M\n"
        + "      E\n"
        + "      T\n";

    // run
    WordSnakeService snakeService = new DownRightSnake();
    String snake = snakeService.makeSnake(sentence);

    // verify
    assertThat(snake).isEqualTo(expected);
  }

}
