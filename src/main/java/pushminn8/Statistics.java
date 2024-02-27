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
  private long integersMin;
  private long integersMax;
  private long integersSum;

  private double floatsMin;
  private double floatsMax;
  private double floatsSum;
  
  private String stringShortest;
  private String stringLongest;

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
  public long minInteger() {
    return integersMin;
  }
  public long maxInteger() {
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

  public void printToStdOut(boolean shortStatsOption, boolean fullStatsOption) {

    String intsMsg = "";
    String floatsMsg = "";
    String numbersMsg = "";
    String stringsMsg = "";
    if (numOfIntegers > 0) {
      intsMsg = "\n"
                     + "\n  Целые числа:"
                     + String.format("%-24s", "\n    Минимальное: ")
                     + minInteger()
                     + String.format("%-24s", "\n    Максимальное: ")
                     + maxInteger()
                     + String.format("%-24s", "\n    Сумма: ")
                     + sumOfIntegers()
                     + String.format("%-24s", "\n    Среднее: ")
                     + String.format("%.2f", meanOfIntegers());
    } 
    if (numOfFloats > 0) {
      floatsMsg = "\n"
                       + "\n  Вещественные числа:"
                       + String.format("%-24s", "\n    Минимальное: ")
                       + String.format("%.2f", minFloat())
                       + String.format("%-24s", "\n    Максимальное: ")
                       + String.format("%.2f", maxFloat())
                       + String.format("%-24s", "\n    Сумма: ")
                       + String.format("%.2f", sumOfFloats())
                       + String.format("%-24s", "\n    Среднее: ")
                       + String.format("%.2f", meanOfFloats());
    }
    if (numOfIntegers > 0 && numOfFloats > 0) {
      numbersMsg = "\n"
                        + "\n  Числа в общем:"
                        + String.format("%-24s", "\n    Минимальное: ")
                        + String.format("%.2f", minNumber())
                        + String.format("%-24s", "\n    Максимальное: ")
                        + String.format("%.2f", maxNumber())
                        + String.format("%-24s", "\n    Сумма: ")
                        + String.format("%.2f", sumOfNumbers())
                        + String.format("%-24s", "\n    Среднее: ")
                        + String.format("%.2f", meanOfNumbers());
    }
    if (numOfStrings > 0) {
      stringsMsg = "\n"
                        + "\n  Строки:"
                        + String.format("%-24s", "\n    Самая короткая: ")
                        + shortestString()
                        + String.format("%-24s", "\n    Самая длинная: ")
                        + longestString();
    }

    String shortStats = "# Количество записанных элементов:"
      + "\n"
      + String.format("%-24s", "\n  Целые числа: ")
      + countIntegers()
      + String.format("%-24s", "\n  Вещественные числа: ")
      + countFloats()
      + String.format("%-24s", "\n  Строки: ")
      + countStrings();

    String fullStats = "# Подробная статистика по каждому типу:"
      + intsMsg
      + floatsMsg
      + numbersMsg
      + stringsMsg;

    if (fullStatsOption) {
      System.out.println(shortStats + "\n\n" + fullStats);
    } else if (shortStatsOption) {
      System.out.println(shortStats);
    }
  }
}
