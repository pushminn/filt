package pushminn8;

import java.nio.file.*;
import java.io.*;
import java.util.*;
import java.util.stream.*;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "filter-util", version = "filter-util 1.0", mixinStandardHelpOptions = true) 
public class App implements Runnable {
  @Option(names = { "-s", "--short-stats" }, description = "Show short statistics") 
  private boolean shortStatsOption;

  @Option(names = { "-f", "--full-stats" }, description = "Show full statistics") 
  private boolean fullStatsOption;

  @Option(names = { "-o", "--output-dir" }, description = "Directory to store result files") 
  private Path outputDir = Path.of("");
  
  @Option(names = { "-p", "--name-prefix" }, description = "Prefix for names of output files") 
  private String namePrefix = "";

  @Option(names = { "-a", "--append" }, description = "Append to existing files") 
  private boolean append;


  @Parameters(arity="1..*", paramLabel = "FILE", description = "One or more files to filter in integers, floats and strings")
  private List<Path> inputFiles;
  
  private Path intFile;
  private Path floatFile;
  private Path stringFile;
  private BufferedWriter intOut;
  private BufferedWriter floatOut;
  private BufferedWriter stringOut;
  private ArrayList<BufferedReader> readers;
  private Statistics stats;

  @Override
  public void run() {
    initialize();
    process(inputFiles);
    stats.print(shortStatsOption, fullStatsOption);
    cleanup();
  }

  public static void main(String[] args) {
    int exitCode = new CommandLine(new App()).execute(args);
    System.exit(exitCode);
  }

  public void initialize() {
    intFile = Path.of(outputDir.toString(), namePrefix + "integers.txt");
    floatFile = Path.of(outputDir.toString(), namePrefix + "floats.txt");
    stringFile = Path.of(outputDir.toString(), namePrefix + "strings.txt");
    stats = new Statistics();

    readers = new ArrayList<BufferedReader>();
    inputFiles.forEach(file -> {
      try {
        readers.add(Files.newBufferedReader(file));
      } catch (IOException e) {
        e.printStackTrace();
      }
    });

    try {
      intOut = Files.newBufferedWriter(intFile);
      floatOut = Files.newBufferedWriter(floatFile);
      stringOut = Files.newBufferedWriter(stringFile);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void process(List<Path> files) {
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
        intOut.write(line);
        intOut.newLine();
      } else if (StringTypeChecker.isFloat(line)) {
        stats.datum(Double.parseDouble(line));
        floatOut.write(line);
        floatOut.newLine();
      } else {
        stats.datum(line);
        stringOut.write(line);
        stringOut.newLine();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void cleanup() {
    try {
      intOut.close();
      floatOut.close();
      stringOut.close();
    } catch (IOException e) {
      System.err.println("Не удалось закрыть BufferedWriter: "
                         + e.getMessage());
    }
  }
}
