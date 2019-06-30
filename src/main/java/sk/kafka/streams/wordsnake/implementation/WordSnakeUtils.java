package sk.kafka.streams.wordsnake.implementation;

import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class WordSnakeUtils {

  static final int UP = 0;
  static final int RIGHT = 1;
  static final int DOWN = 2;

  static char[][] makeCanvasWithSpaces(int height, int width) {
    char[][] canvas = new char[height][width];
    IntStream.range(0, height)
        .forEach(x -> IntStream.range(0, width)
            .forEach(y -> canvas[x][y] = ' '));
    return canvas;
  }

  static String asString(char[][] canvas, int height, int width) {
    log.debug("AsString height: {} width: {}", height, width);

    StringBuilder result = new StringBuilder();
    IntStream.range(0, height + 1)
        .forEach(x -> IntStream.range(0, width + 1)
            .forEach(y -> {
              if (y == width)
                result.append('\n');
              else
                result.append(canvas[x][y]);
            }));
    return result.toString();
  }

}
