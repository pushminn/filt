package pushminn8;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Отвечает за обработку каждой строки, то есть определение её типа
 * (целое число или вещественное, или строка) и запись в файл,
 * содержащий все значения данного типа.
 */
public class LineHandler {
  /** Определяет тип данных, которые будут записываться в файл.*/
  private enum Type { INTEGER, FLOAT, STRING }
  /** Содержит открытые для записи файлы.*/
  private Map<Type, BufferedWriter> writers;

  // Информация, необходимая для открытия файлов для записи.
  private Path outputDir;
  private String namePrefix;
  private boolean appendOption;

  /** 
   * При инициализации пробует создать указанную директорию.
   * <p>
   * Если это не получается, то программа завершается с ошибкой. */
  public LineHandler(Path outputDir, String namePrefix, boolean appendOption) {
    try {
      Files.createDirectories(outputDir);
    } catch (IOException e) {
      System.err.println("Ошибка: не удалось создать папку "+outputDir+" "
                         +e.getMessage()+". Останавливаем программу");
      int failureExitCode = 1;
      System.exit(failureExitCode);
    }
    writers = new HashMap<Type, BufferedWriter>();
    this.outputDir = outputDir;
    this.namePrefix = namePrefix;
    this.appendOption = appendOption; 
  }

  /** Закрывает все открытые для записи файлы.*/
  public void close() {
    try {
      if (writers.get(Type.INTEGER) != null) {
        writers.get(Type.INTEGER).close();
        writers.remove(Type.INTEGER);
      }
      if (writers.get(Type.FLOAT) != null) {
        writers.get(Type.FLOAT).close();
        writers.remove(Type.FLOAT);
      }
      if (writers.get(Type.STRING) != null) {
        writers.get(Type.STRING).close();
        writers.remove(Type.STRING);
      }
    } catch (IOException e) {
      System.err.println("Не удалось закрыть файлы для записи: "+e.getMessage());
      int failureExitCode = 1;
      System.exit(failureExitCode);
    }
  }

  /**
   * Записывает line в файл с соответствующим типом данных и
   * передаёт информацию в stats для сбора статистики по
   * сделанным записям.
   * <p>
   * В статистике участвуют только те числа, размер которых
   * позволяет хранить их в Long и Double, остальные числа
   * записываются как строки.
   *
   * @param line   строка, которая будет записана в файл.
   *               Если представляет собой целое или
   *               вещественное число, то записывается в
   *               отдельный файл. По разным типам собирается
   *               отдельная статистика.
   * @param stats  объект для сбора статистики
   */
  public void handleLine(String line, Statistics stats) {
    if (StringTypeChecker.isInteger(line)) {
      try {
        long integerLine = Long.parseLong(line);
        stats.datum(integerLine);
        write(integerLine);
      } catch (NumberFormatException e) {
        // Если цифра получается слишком большая, чтобы поместиться в Long,
        // то она не участвует в статистике для чисел и просто записывается
        // как строка.
        write(line);
      }
    } else if (StringTypeChecker.isFloat(line)) {
      try {
        double floatPointLine = Double.parseDouble(line);
        stats.datum(floatPointLine);
        write(floatPointLine);
      } catch (NumberFormatException e) {
        // Если цифра получается слишком большая, чтобы поместиться в Double,
        // то она не участвует в статистике для чисел и просто записывается
        // как строка.
        write(line);
      }
    } else {
      stats.datum(line);
      write(line);
    }
  }

  /** Сделать запись в файл целых чисел.*/
  private void write(Long integerLine) {
    try {
      if (!writers.containsKey(Type.INTEGER)) {
        initWriter(Type.INTEGER);
      }
      BufferedWriter integerWriter = writers.get(Type.INTEGER);
      integerWriter.write(integerLine.toString());
      integerWriter.newLine();
    } catch (IOException e) {
      System.err.println("Не удалось сделать запись в файл: "+e.getMessage());
      int failureExitCode = 1;
      System.exit(failureExitCode);
    }
  }
  /** Сделать запись в файл вещественных чисел.*/
  private void write(Double floatPointLine) {
    try {
      if (!writers.containsKey(Type.FLOAT)) {
        initWriter(Type.FLOAT);
      }
      BufferedWriter floatPointWriter = writers.get(Type.FLOAT);
      floatPointWriter.write(floatPointLine.toString());
      floatPointWriter.newLine();
    } catch (IOException e) {
      System.err.println("Не удалось сделать запись в файл: "+e.getMessage());
      int failureExitCode = 1;
      System.exit(failureExitCode);
    }
  }
  /** Сделать запись в файл строк.*/
  private void write(String line) {
    try {
      if (!writers.containsKey(Type.STRING)) {
        initWriter(Type.STRING);
      }
      BufferedWriter stringWriter = writers.get(Type.STRING);
      stringWriter.write(line);
      stringWriter.newLine();
    } catch (IOException e) {
      System.err.println("Не удалось сделать запись в файл: "+e.getMessage());
      int failureExitCode = 1;
      System.exit(failureExitCode);
    }
  }

  /** 
   * Открыть файл для записи указанныго типа данных.
   *
   * @param lineType  тип данных, которые должны записываться
   * */
  private void initWriter(Type lineType) {
    String namePostfix = "";
    if (lineType == Type.INTEGER) {
      namePostfix = "integers.txt";
    } else if (lineType == Type.FLOAT) {
      namePostfix = "floats.txt";
    } else if (lineType == Type.STRING) {
      namePostfix = "strings.txt";
    }
    try {
      Path filename = outputDir.resolve(namePrefix + namePostfix);
      BufferedWriter writer;
      if (appendOption) {
        writer = Files.newBufferedWriter(filename,
                                            StandardOpenOption.CREATE,
                                            StandardOpenOption.WRITE,
                                            StandardOpenOption.APPEND);
      } else {
        writer = Files.newBufferedWriter(filename);
      }
      writers.put(lineType, writer);
    } catch (IOException e) {
      System.err.println("Ошибка: не удалось открыть файл для записи: "+e.getMessage());
      int failureExitCode = 1;
      System.exit(failureExitCode);
    }
  }
}
