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


  @Parameters(paramLabel = "FILE", description = "One or more files to filter in integers, floats and strings")
  private Path inputFile;
  
  private Path intFile;
  private Path floatFile;
  private Path stringFile;
  private BufferedWriter intOut;
  private BufferedWriter floatOut;
  private BufferedWriter stringOut;
  private Statistics stats;

  public App() {
  }

  @Override
  public void run() {
    process(inputFile);
    cleanup();
  }

  public static void main(String[] args) {
    int exitCode = new CommandLine(new App()).execute(args);
    System.exit(exitCode);
  }

  public void process(Path inputFile) {
    intFile = Path.of(outputDir.toString(), namePrefix + "integers.txt");
    floatFile = Path.of(outputDir.toString(), namePrefix + "floats.txt");
    stringFile = Path.of(outputDir.toString(), namePrefix + "strings.txt");

    stats = new Statistics();
    try {
      intOut = Files.newBufferedWriter(intFile);
      floatOut = Files.newBufferedWriter(floatFile);
      stringOut = Files.newBufferedWriter(stringFile);
    } catch (IOException e) {
      e.printStackTrace();
    }
    
    try {
      Files.lines(inputFile)
           .map(line -> line.trim())
           .filter(line -> !line.isEmpty())
           .forEach(line -> writeToFile(line));
    } catch (IOException e) {
      e.printStackTrace();
    }
    stats.print(shortStatsOption, fullStatsOption);
  }

  public void writeToFile(String str) {
    try {
      if (StringTypeChecker.isInteger(str)) {
        stats.datum(Integer.parseInt(str));
        intOut.write(str);
        intOut.newLine();
      } else if (StringTypeChecker.isFloat(str)) {
        stats.datum(Double.parseDouble(str));
        floatOut.write(str);
        floatOut.newLine();
      } else {
        stats.datum(str);
        stringOut.write(str);
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
      System.err.println("Не получить закрыть BufferedWriter: "
                         + e.getMessage());
    }
  }
}
