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
  private Statistics stats;
  private LineHandler lineHandler;

  public static void main(String[] args) {
    int exitCode = new CommandLine(new FilterUtility()).execute(args);
    System.exit(exitCode);
  }

  // Главная логика.
  @Override
  public void run() {
    initialize();
    processFiles();
    stats.printToStdOut(shortStatsOption, fullStatsOption);
    cleanup();
  }

  // ArrayList<Path> acceptedFiles = new ArrayList<>();
  public void initialize() {
    readers = new ArrayList<BufferedReader>();
    inputFiles.forEach(file -> {
      try {
        String type = Files.probeContentType(file);
        if (!Files.isRegularFile(file)) {
          System.err.println("Внимание: '"+file+"' не является доступным для чтения файлом, игнорируем");
        } else if (type != null && type.contains("text")) {
          readers.add(Files.newBufferedReader(file));
          // acceptedFiles.add(file);
        } else {
          System.err.println("Внимание: не похоже, что '"+file+"' является текстовым файлом, игнорируем");
        }
      // } catch (NoSuchFileException e) {
      //   System.err.println("Внимание: файла '"+file+"' не существует, игнорируем");
      // } catch (AccessDeniedException e) {
      //   System.err.println("Внимание: отказано в доступе к файлу '"+file+"', игнорируем");
      } catch (IOException e) {
        System.err.println("Внимание: не удалось открыть файл '"+file+"'для чтения, продолжаем без него");
      }
    });

    if (readers.isEmpty()) {
      System.err.println("Ошибка: не удалось открыть ни один файл для обработки, останавливаем программу");
      int failureExitCode = 1;
      System.exit(failureExitCode);
    }

    stats = new Statistics();
    lineHandler = new LineHandler(outputDir, namePrefix, stats, appendOption);
  }

  public void processFiles() {
    // int i = 0;
    while(!readers.isEmpty()) {
      ArrayList<BufferedReader> readersToRemove = new ArrayList<>();
      for(BufferedReader reader : readers) {
        // i++;
        try {
          String line = reader.readLine();
          if(line == null) {
            readersToRemove.add(reader);
          } else {
            line = line.trim();
            if (!line.isEmpty()) {
              lineHandler.handleLine(line);
            }
          }
        } catch (IOException e) {
          System.err.println("Внимание: произошла ошибка при попытке чтения файла: "+e.getMessage());
        // System.out.println(i + " file: " + acceptedFiles.get(i));
          // e.printStackTrace();
          readersToRemove.add(reader);
        }
      }
      readersToRemove.forEach(reader -> {
        try {
          if (reader != null) {
            reader.close();
            readers.remove(reader);
          }
        } catch (IOException e) {
          System.err.println("Внимание: не удалось закрыть файл для чтения "+e.getMessage());
        }
      });
    }
  }

  public void cleanup() {
    lineHandler.close();
  }
}
