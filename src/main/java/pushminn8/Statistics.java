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

  public void print(boolean shortStatsOption, boolean fullStatsOption) {

    String intsMsg = "";
    String floatsMsg = "";
    String numbersMsg = "";
    String stringsMsg = "";
    if (numOfIntegers > 0) {
      intsMsg = "\n"
                     + "\n  Целые числа:"
                     + "\n    Минимальное: " + minInteger()
                     + "\n    Максимальное: " + maxInteger()
                     + "\n    Сумма: " + sumOfIntegers()
                     + "\n    Среднее: " + meanOfIntegers();
    } 
    if (numOfFloats > 0) {
      floatsMsg = "\n"
                       + "\n  Вещественные числа:"
                       + "\n    Минимальное: " + minFloat()
                       + "\n    Максимальное: " + maxFloat()
                       + "\n    Сумма: " + sumOfFloats()
                       + "\n    Среднее: " + meanOfFloats();
    }
    if (numOfIntegers > 0 && numOfFloats > 0) {
      numbersMsg = "\n"
                        + "\n  Числа в общем:"
                        + "\n    Минимальное: " + minNumber()
                        + "\n    Максимальное: " + maxNumber()
                        + "\n    Сумма: " + sumOfNumbers()
                        + "\n    Среднее: " + meanOfNumbers();
    }
    if (numOfStrings > 0) {
      stringsMsg = "\n"
                        + "\n  Строки:"
                        + "\n    Самая короткая: " + shortestString()
                        + "\n    Самая длинная: " + longestString();
    }

    String shortStats = "# Количество записанных элементов:"
      + "\n"
      + "\n  Целые числа: " + countIntegers()
      + "\n  Вещественные числа: " + countFloats()
      + "\n  Строки: " + countStrings();

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
