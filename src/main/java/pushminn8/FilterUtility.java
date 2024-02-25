package pushminn8;

import java.nio.file.*;
import java.io.*;
import java.util.*;
import java.util.stream.*;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "filt", version = "filt 1.0", mixinStandardHelpOptions = true)
public class FilterUtility implements Runnable {
  @Option(names = { "-s", "--short-stats" }, description = "Show short statistics") 
  private boolean shortStatsOption;
  @Option(names = { "-f", "--full-stats" }, description = "Show full statistics") 
  private boolean fullStatsOption;
  @Option(names = { "-a", "--append" }, description = "Append to existing files") 
  private boolean appendOption;
  @Option(names = { "-o", "--output-dir" }, description = "Directory to store result files")
  private Path outputDir = Path.of("");
  @Option(names = { "-p", "--name-prefix" }, description = "Prefix for names of output files") 
  private String namePrefix = "";
  @Parameters(arity="1..*", paramLabel = "FILE", description = "One or more files to filter to integers, floats and strings")
  private List<Path> inputFiles;
  
  private ArrayList<BufferedReader> readers;
  private BufferedWriter intWriter;
  private BufferedWriter floatWriter;
  private BufferedWriter stringWriter;
  private Statistics stats;

  public static void main(String[] args) {
    int exitCode = new CommandLine(new FilterUtility()).execute(args);
    System.exit(exitCode);
  }

  // Главная логика.
  @Override
  public void run() {
    initialize();
    processFiles();
    stats.print(shortStatsOption, fullStatsOption);
    cleanup();
  }

  public void initialize() {
    readers = new ArrayList<BufferedReader>();
    inputFiles.forEach(file -> {
      try {
        readers.add(Files.newBufferedReader(file));
      } catch (IOException e) {
        e.printStackTrace();
      }
    });

    try {
      Path intFile = Path.of(outputDir.toString(), namePrefix + "integers.txt");
      Path floatFile = Path.of(outputDir.toString(), namePrefix + "floats.txt");
      Path stringFile = Path.of(outputDir.toString(), namePrefix + "strings.txt");
      if (appendOption) {
        intWriter = Files.newBufferedWriter(intFile,
                                            StandardOpenOption.CREATE,
                                            StandardOpenOption.WRITE,
                                            StandardOpenOption.APPEND);
        floatWriter = Files.newBufferedWriter(floatFile,
                                              StandardOpenOption.CREATE,
                                              StandardOpenOption.WRITE,
                                              StandardOpenOption.APPEND);
        stringWriter = Files.newBufferedWriter(stringFile,
                                               StandardOpenOption.CREATE,
                                               StandardOpenOption.WRITE,
                                               StandardOpenOption.APPEND);
      } else {
        intWriter = Files.newBufferedWriter(intFile);
        floatWriter = Files.newBufferedWriter(floatFile);
        stringWriter = Files.newBufferedWriter(stringFile);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    stats = new Statistics();
  }

  public void processFiles() {
    while(!readers.isEmpty()) {
      ArrayList<BufferedReader> readersToRemove = new ArrayList<>();
      for(BufferedReader reader : readers) {
        try {
          String line = reader.readLine();
          if(line == null) {
            readersToRemove.add(reader);
          } else {
            line = line.trim();
            if (!line.isEmpty()) {
              handleLine(line);
            }
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      readersToRemove.forEach(reader -> readers.remove(reader));
    }
  }

  public void handleLine(String line) {
    try {
      if (StringTypeChecker.isInteger(line)) {
        stats.datum(Long.parseLong(line));
        intWriter.write(line);
        intWriter.newLine();
      } else if (StringTypeChecker.isFloat(line)) {
        stats.datum(Double.parseDouble(line));
        floatWriter.write(line);
        floatWriter.newLine();
      } else {
        stats.datum(line);
        stringWriter.write(line);
        stringWriter.newLine();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void cleanup() {
    readers.forEach(reader -> {
      try {
        reader.close();
      } catch (IOException e) {
        System.err.println("Не удалось закрыть BufferedReader: "
                           + e.getMessage());
      }
    });
    try {
      intWriter.close();
      floatWriter.close();
      stringWriter.close();
    } catch (IOException e) {
      System.err.println("Не удалось закрыть BufferedWriter: "
                         + e.getMessage());
    }
  }
}
