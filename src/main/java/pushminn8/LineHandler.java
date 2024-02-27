package pushminn8;

import java.nio.file.*;
import java.io.*;

public class LineHandler {
  private BufferedWriter intWriter;
  private BufferedWriter floatWriter;
  private BufferedWriter stringWriter;

  private Statistics stats;

  private Path outputDir;
  private String namePrefix;
  private boolean appendOption;

  private boolean integersFileCreated;
  private boolean floatsFileCreated;
  private boolean stringsFileCreated;

  public LineHandler(Path outputDir, String namePrefix, Statistics stats, boolean appendOption) {
    try {
      Files.createDirectories(outputDir);
    } catch (IOException e) {
      outputDir = Path.of("");
      System.err.println("Внимание: не удалось создать папку "+outputDir+" : "
                         + e.getMessage()
                         + ": используем текущую папку");
    }
    this.stats = stats;
    this.outputDir = outputDir;
    this.namePrefix = namePrefix;
    this.appendOption = appendOption; 
  }

  public void close() {
    try {
      if (intWriter != null) {
        intWriter.close();
      }
      if (floatWriter != null) {
        floatWriter.close();
      }
      if (stringWriter != null) {
        stringWriter.close();
      }
    } catch (IOException e) {
      System.err.println("Не удалось закрыть файлы для записи: "
                         + e.getMessage());
    }
  }

  public void handleLine(String line) {
    try {
      if (StringTypeChecker.isInteger(line)) {
        writeInteger(line);
      } else if (StringTypeChecker.isFloat(line)) {
        writeFloat(line);
      } else {
        writeString(line);
      }
    } catch (IOException e) {
      System.err.println("Не удалось сделать запись в файл: "+e.getMessage());
    }
  }

  private void writeInteger(String line) throws IOException {
    try {
      stats.datum(Long.parseLong(line));
    } catch (NumberFormatException e) {
      writeString(line);
    }
    if (!integersFileCreated) {
      initIntWriter();
    }
    intWriter.write(line);
    intWriter.newLine();
  }

  private void writeFloat(String line) throws IOException {
    try {
      stats.datum(Double.parseDouble(line));
    } catch (NumberFormatException e) {
      writeString(line);
    }
    if (!floatsFileCreated) {
      initFloatWriter();
    }
    floatWriter.write(line);
    floatWriter.newLine();
  }
  private void writeString(String line) throws IOException {
    try {
        stats.datum(line);
        if (!stringsFileCreated) {
          initStringWriter();
        }
        stringWriter.write(line);
        stringWriter.newLine();
    } catch (IOException e) {
      System.err.println("Не удалось сделать запись в файл: "+e.getMessage());
    }
  }
  private void initIntWriter() {
    try {
      Path intFile = outputDir.resolve(namePrefix + "integers.txt");
      if (appendOption) {
        intWriter = Files.newBufferedWriter(intFile,
                                            StandardOpenOption.CREATE,
                                            StandardOpenOption.WRITE,
                                            StandardOpenOption.APPEND);
      } else {
        intWriter = Files.newBufferedWriter(intFile);
      }
    } catch (IOException e) {
      System.err.println("Ошибка: не удалось открыть файл для записи целых чисел: " + e.getMessage());
      int failureExitCode = 1;
      System.exit(failureExitCode);
    }
    integersFileCreated = true;
  }

  private void initFloatWriter() {
    try {
      Path floatFile = outputDir.resolve(namePrefix + "floats.txt");
      if (appendOption) {
        floatWriter = Files.newBufferedWriter(floatFile,
                                              StandardOpenOption.CREATE,
                                              StandardOpenOption.WRITE,
                                              StandardOpenOption.APPEND);
      } else {
        floatWriter = Files.newBufferedWriter(floatFile);
      }
    } catch (IOException e) {
      System.err.println("Ошибка: не удалось открыть файл для записи вещественных чисел: " + e.getMessage());
      int failureExitCode = 1;
      System.exit(failureExitCode);
    }
    floatsFileCreated = true;
  }

  private void initStringWriter() {
    try {
      Path stringFile = outputDir.resolve(namePrefix + "strings.txt");
      if (appendOption) {
        stringWriter = Files.newBufferedWriter(stringFile,
                                               StandardOpenOption.CREATE,
                                               StandardOpenOption.WRITE,
                                               StandardOpenOption.APPEND);
      } else {
        stringWriter = Files.newBufferedWriter(stringFile);
      }
    } catch (IOException e) {
      System.err.println("Ошибка: не удалось открыть файл для записи строк: " + e.getMessage());
      int failureExitCode = 1;
      System.exit(failureExitCode);
    }
    stringsFileCreated = true;
  }
}
