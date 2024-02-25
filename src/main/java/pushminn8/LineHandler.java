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
      System.err.println("Не удалось создать папку "+outputDir+" : "
                         + e.getMessage());
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
      System.err.println("Не удалось закрыть BufferedWriter: "
                         + e.getMessage());
    }
  }

  public void handleLine(String line) {
    try {
      if (StringTypeChecker.isInteger(line)) {
        stats.datum(Long.parseLong(line));
        if (!integersFileCreated) {
          initIntWriter();
        }
        intWriter.write(line);
        intWriter.newLine();
      } else if (StringTypeChecker.isFloat(line)) {
        stats.datum(Double.parseDouble(line));
        if (!floatsFileCreated) {
          initFloatWriter();
        }
        floatWriter.write(line);
        floatWriter.newLine();
      } else {
        stats.datum(line);
        if (!stringsFileCreated) {
          initStringWriter();
        }
        stringWriter.write(line);
        stringWriter.newLine();
      }
    } catch (IOException e) {
      e.printStackTrace();
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
      e.printStackTrace();
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
      e.printStackTrace();
    }
    floatsFileCreated = true;
  }

  private void initStringWriter() {
    try {
      Path stringFile = outputDir.resolve(namePrefix + "string.txt");
      if (appendOption) {
        stringWriter = Files.newBufferedWriter(stringFile,
                                               StandardOpenOption.CREATE,
                                               StandardOpenOption.WRITE,
                                               StandardOpenOption.APPEND);
      } else {
        stringWriter = Files.newBufferedWriter(stringFile);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    stringsFileCreated = true;
  }
}
