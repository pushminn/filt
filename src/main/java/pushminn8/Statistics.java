package pushminn8;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

/**
 * Собирает статистику по данным, которые передаются в объект
 * этого класса и при необхомисти выводит собранную информацию
 * в standart output stream (stdout).
 * <p>
 * Содержит короткую информацию по количеству записанных
 * данных каждого типа. И детальную по каждому типу.
 * Детальная статистика по каждому типу выводится только при
 * наличии записей этого типа. Эти типы включают в себя:
 * <ul>
 *   <li>Целые числа
 *   <li>Вещественные числа
 *   <li>Числа в общем, когда есть и вещественные, и целые числа
 *   <li>Строки
 * </ul>
 * <p>
 * Детальная статистика включает в себя минимальное и максимальное
 * значение каждого типа числе, их общую сумму и среднее. По строкам
 * отслеживается самая короткая и самая длинные строки.
 * <p>
 * Ситуация, когда сумма чисел становится слишком большой, чтобы
 * помещаться в Long и/или Double, никак не обрабатывается и, вероятно,
 * вызовет неверное поведение или сбой программы.
 */
public class Statistics {

  private int numOfIntegers;
  private int numOfFloats;
  private int numOfStrings;

  private long integersMin;
  private long integersMax;
  private long integersSum;

  private double floatsMin;
  private double floatsMax;
  private double floatsSum;

  private String stringShortest;
  private String stringLongest;

  /** Добавляет значение для статистики по целым числам.*/
  public void datum(long i) {
    if (numOfIntegers == 0) {
      integersMin = i;
      integersMax = i;
    }
    if (i < integersMin) {
      integersMin = i;
    } else if (i > integersMax) {
      integersMax = i;
    }
    integersSum += i;
    numOfIntegers++;
  }
  /** Добавляет значение для статистики по вещественным числам.*/
  public void datum(double d) {
    if (numOfFloats == 0) {
      floatsMin = d;
      floatsMax = d;
    }
    if (d < floatsMin) {
      floatsMin = d;
    } else if (d > floatsMax) {
      floatsMax = d;
    }
    floatsSum += d;
    numOfFloats++;
  }
  /** Добавляет значение для статистики по строкам.*/
  public void datum(String str) {
    if (numOfStrings == 0) {
      stringShortest = str;
      stringLongest = str;
    }
    if (str.length() < stringShortest.length()) {
      stringShortest = str;
    } else if (str.length() > stringLongest.length()) {
      stringLongest = str;
    }
    numOfStrings++;
  }

  /**
   * Выводит на экран (stdout) информацию по собранным данным, причём
   * детализация зависит от переданных аргументов.
   * <p>
   * Если указана fullStatsOption, то наличие shortStatsOption не имеет
   * значение. Если не указана ни одна опция, то статистика не выводится
   * и таким образом этот метод не несёт никакого эффекта.
   * 
   * @param shortStatsOption  При отсутствии fullStatsOption метод выводит на экран
   *                          короткую статистику.
   * @param fullStatsOption   При наличии этой опции метод выводит на экран
   *                          полную статистику по всем собранным типам.
   */
  public void printToStdOut(boolean shortStatsOption, boolean fullStatsOption) {
    String intsMsg = "";
    String floatsMsg = "";
    String numbersMsg = "";
    String stringsMsg = "";
    if (numOfIntegers > 0) {
      intsMsg = "\n"
              + "\n  Целые числа:"
              + String.format("%-24s %d", "\n    Минимальное: ", minInteger())
              + String.format("%-24s %d", "\n    Максимальное: ", maxInteger())
              + String.format("%-24s %d", "\n    Сумма: ", sumOfIntegers())
              + String.format("%-24s %.2f", "\n    Среднее: ", meanOfIntegers());
    } 
    if (numOfFloats > 0) {
      floatsMsg = "\n"
                       + "\n  Вещественные числа:"
                       + String.format("%-24s %.2f", "\n    Минимальное: ", minFloat())
                       + String.format("%-24s %.2f", "\n    Максимальное: ", maxFloat())
                       + String.format("%-24s %.2f", "\n    Сумма: ", sumOfFloats())
                       + String.format("%-24s %.2f", "\n    Среднее: ", meanOfFloats());
    }
    if (numOfIntegers > 0 && numOfFloats > 0) {
      numbersMsg = "\n"
                        + "\n  Числа в общем:"
                        + String.format("%-24s %.2f", "\n    Минимальное: ", minNumber())
                        + String.format("%-24s %.2f", "\n    Максимальное: ", maxNumber())
                        + String.format("%-24s %.2f", "\n    Сумма: ", sumOfNumbers())
                        + String.format("%-24s %.2f", "\n    Среднее: ", meanOfNumbers());
    }
    if (numOfStrings > 0) {
      stringsMsg = "\n"
                        + "\n  Строки:"
                        + String.format("%-24s %s", "\n    Самая короткая: ", shortestString())
                        + String.format("%-24s %s", "\n    Самая длинная: ", longestString());
    }

    String shortStats = "# Количество записанных элементов:"
      + "\n"
      + String.format("%-24s %d", "\n  Целые числа: ", countIntegers())
      + String.format("%-24s %d", "\n  Вещественные числа: ", countFloats())
      + String.format("%-24s %d", "\n  Строки: ", countStrings());

    String fullStats = "# Подробная статистика по каждому типу:"
      + intsMsg
      + floatsMsg
      + numbersMsg
      + stringsMsg;

    // Печать произойдёт только если указан соответсвующий оргумент.
    if (fullStatsOption) {
      System.out.println(shortStats + "\n\n" + fullStats);
    } else if (shortStatsOption) {
      System.out.println(shortStats);
    }
  }

  public int countIntegers() {
    return numOfIntegers;
  }
  public int countFloats() {
    return numOfFloats;
  }
  public int countStrings() {
    return numOfStrings;
  }

  public long minInteger() {
    return integersMin;
  }
  public double minFloat() {
    return floatsMin;
  }
  public double minNumber() {
    return Math.min(integersMin, floatsMin);
  }

  public long maxInteger() {
    return integersMax;
  }
  public double maxFloat() {
    return floatsMax;
  }
  public double maxNumber() {
    return Math.max(integersMax, floatsMax);
  }

  public long sumOfIntegers() {
    return integersSum;
  }
  public double sumOfFloats() {
    return floatsSum;
  }
  public double sumOfNumbers() {
    return floatsSum + integersSum;
  }

  public double meanOfIntegers() {
    return integersSum / numOfIntegers;
  }
  public double meanOfFloats() {
    return floatsSum / numOfFloats;
  }
  public double meanOfNumbers() {
    return (integersSum + floatsSum) / (numOfIntegers + numOfFloats);
  }

  public String shortestString() {
    return stringShortest;
  }
  public String longestString() {
    return stringLongest;
  }
}
