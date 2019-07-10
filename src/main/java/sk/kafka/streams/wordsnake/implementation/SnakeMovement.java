package sk.kafka.streams.wordsnake.implementation;

import static sk.kafka.streams.wordsnake.implementation.WordSnakeUtils.updateActualHeight;
import static sk.kafka.streams.wordsnake.implementation.WordSnakeUtils.updateActualWidth;

import java.util.concurrent.atomic.AtomicInteger;

abstract class SnakeMovement {

  int actualHeight = Integer.MIN_VALUE;
  int actualWidth = Integer.MIN_VALUE;

  void moveUp(char[][] canvas, String word, AtomicInteger currentHorizontalPosition,
      AtomicInteger currentVerticalPosition) {
    for (char c : word.toCharArray()) {
      canvas[currentVerticalPosition.getAndDecrement()][currentHorizontalPosition.get()] = c;
    }
    currentVerticalPosition.incrementAndGet();
  }

  void moveDown(char[][] canvas, String word, AtomicInteger currentHorizontalPosition,
      AtomicInteger currentVerticalPosition) {
    for (char c : word.toCharArray()) {
      canvas[currentVerticalPosition.getAndIncrement()][currentHorizontalPosition.get()] = c;
    }
    currentVerticalPosition.decrementAndGet();
    actualHeight = updateActualHeight(actualHeight, currentVerticalPosition.get());
  }

  void moveRight(char[][] canvas, String word, AtomicInteger currentHorizontalPosition,
      AtomicInteger currentVerticalPosition) {
    for (char c : word.toCharArray()) {
      canvas[currentVerticalPosition.get()][currentHorizontalPosition.getAndIncrement()] = c;
    }
    actualWidth = updateActualWidth(actualWidth, currentHorizontalPosition.get());
    currentHorizontalPosition.decrementAndGet();
  }

  void moveLeft(char[][] canvas, String word, AtomicInteger currentHorizontalPosition,
      AtomicInteger currentVerticalPosition) {
    for (char c : word.toCharArray()) {
      canvas[currentVerticalPosition.get()][currentHorizontalPosition.getAndDecrement()] = c;
    }
    currentHorizontalPosition.incrementAndGet();
  }

}
