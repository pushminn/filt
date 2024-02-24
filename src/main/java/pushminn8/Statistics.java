package pushminn8;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

public class Statistics {

  // Для короткой статистики.
  private int numOfIntegers;
  private int numOfFloats;
  private int numOfStrings;

  // Для полной статистики по каждому типу.
  private int integersMin;
  private int integersMax;
  private int integersSum;

  private double floatsMin;
  private double floatsMax;
  private double floatsSum;
  
  private String stringShortest;
  private String stringLongest;

  public void datum(int i) {
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

  public int countIntegers() {
    return numOfIntegers;
  }
  public int countFloats() {
    return numOfFloats;
  }
  public int countStrings() {
    return numOfStrings;
  }
  public int minInteger() {
    return integersMin;
  }
  public int maxInteger() {
    return integersMax;
  }
  public double minFloat() {
    return floatsMin;
  }
  public double maxFloat() {
    return floatsMax;
  }
  public double minNumber() {
    return Math.min(integersMin, floatsMin);
  }
  public double maxNumber() {
    return Math.max(integersMax, floatsMax);
  }
  public int sumOfIntegers() {
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

  public void print(boolean shortStatsOption, boolean fullStatsOption) {
    String shortStats = "Количество записанных элементов."
      + "\n  Целые числа: " + countIntegers()
      + "\n  Вещественные числа: " + countFloats()
      + "\n  Строки: " + countStrings();
    String fullStats = "Подробная статистика по каждому типу."
      + "\n  Целые числа:"
      + "\n    Минимальное: " + minInteger()
      + "\n    Максимальное: " + maxInteger()
      + "\n    Сумма: " + sumOfIntegers()
      + "\n    Среднее: " + meanOfIntegers()
      + "\n"
      + "\n  Вещественные числа:"
      + "\n    Минимальное: " + minFloat()
      + "\n    Максимальное: " + maxFloat()
      + "\n    Сумма: " + sumOfFloats()
      + "\n    Среднее: " + meanOfFloats()
      + "\n"
      + "\n  Числа в общем:"
      + "\n    Минимальное: " + minNumber()
      + "\n    Максимальное: " + maxNumber()
      + "\n    Сумма: " + sumOfNumbers()
      + "\n    Среднее: " + meanOfNumbers()
      + "\n"
      + "\n  Строки:"
      + "\n    Самая короткая: " + shortestString()
      + "\n    Самая длинная: " + longestString();

    if (fullStatsOption) {
      System.out.println(shortStats + "\n\n" + fullStats);
    } else if (shortStatsOption) {
      System.out.println(shortStats);
    }
  }
}
