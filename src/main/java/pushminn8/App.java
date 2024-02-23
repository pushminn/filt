package pushminn8;

import java.nio.file.*;
import java.io.*;
import java.util.*;
import java.util.stream.*;

public class App 
{
  private Path intFile;
  private Path floatFile;
  private Path stringFile;
  private BufferedWriter intOut;
  private BufferedWriter floatOut;
  private BufferedWriter stringOut;

  public App() {
    intFile = Paths.get("integers.txt");
    floatFile = Paths.get("floats.txt");
    stringFile = Paths.get("strings.txt");

    try {
      intOut = Files.newBufferedWriter(intFile);
      floatOut = Files.newBufferedWriter(floatFile);
      stringOut = Files.newBufferedWriter(stringFile);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    App util = new App();
    util.process(Paths.get("in2.txt"));
    util.cleanup();
  }

  public void process(Path inputFile) {
    try {
      Files.lines(inputFile)
           .map(line -> line.trim())
           .filter(line -> !line.isEmpty())
           .forEach(line -> writeToFile(line));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void writeToFile(String str) {
    try {
      if (StringTypeChecker.isInteger(str)) {
        intOut.write(str);
        intOut.newLine();
      } else if (StringTypeChecker.isFloat(str)) {
        floatOut.write(str);
        floatOut.newLine();
      } else {
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
