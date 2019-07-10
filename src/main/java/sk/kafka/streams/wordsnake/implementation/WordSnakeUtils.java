package sk.kafka.streams.wordsnake.implementation;

import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class WordSnakeUtils {

  static final int UP = 0;
  static final int RIGHT = 1;
  static final int DOWN = 2;

  static int findMaxPossibleWidth(String[] words) {
    return IntStream.range(0, words.length)
        .filter(index -> index % 2 == 1)
        .mapToObj(odd -> words[odd].length())
        .reduce(1, Integer::sum);
  }

  static int findMaxPossibleHeight(String[] words) {
    return IntStream.range(0, words.length)
        .filter(index -> index % 2 == 0)
        .mapToObj(even -> words[even].length())
        .reduce(1, Integer::sum);
  }

  static char[][] makeCanvasWithSpaces(int height, int width) {
    char[][] canvas = new char[height][width];
    IntStream.range(0, height)
        .forEach(x -> IntStream.range(0, width)
            .forEach(y -> canvas[x][y] = ' '));
    return canvas;
  }

  static String asString(char[][] canvas, int height, int width) {

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

  private WordSnakeUtils() {
  }

  static int updateActualWidth(int currentActualWidth, int currentHorizontalPosition) {
    return currentActualWidth < currentHorizontalPosition ?
        currentHorizontalPosition : currentActualWidth;
  }

  static int updateActualHeight(int currentActualHeight, int currentVerticalPosition) {
    return currentActualHeight < currentVerticalPosition ?
        currentVerticalPosition : currentActualHeight;
  }
}
