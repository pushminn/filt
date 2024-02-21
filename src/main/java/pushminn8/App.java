package pushminn8;

import java.nio.file.*;
import java.io.*;
import java.util.*;

public class App 
{
  public static void main(String[] args) {
    App util = new App();
    util.process(Paths.get("in1.txt"));
  }

  public void process(Path path) {
    try {
      Files.lines(path)
           .forEach(line -> System.out.println(line));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
