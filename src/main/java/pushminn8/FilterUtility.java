package pushminn8;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

/**
 * Программа командной строки для фильтрации содержимого файлов
 * на целые числа, вещественные и строки.
 * <p>
 * В качестве разделителя используется перевод строки. Данные из файлов
 * читаются по очереди в соответствии с их перечислением в командной строке.
 * <p>
 * Объект класса содержит данные о необходимых параметрах (которые
 * создаются как аргументы командной строки) и другие объекты, необходимые
 * для выполнения функций. И отвечает за общую логику программы.
 *
 * @author   Николай Пушмин
 */
@Command(name = "filt", version = "filt 1.0", mixinStandardHelpOptions = true)
public class FilterUtility implements Runnable {

  @Option(names = { "-a", "--append" }, description = "Добавить к существующим файлам") 
  private boolean appendOption;
  @Option(names = { "-s", "--short-stats" }, description = "Показать короткую статистику") 
  private boolean shortStatsOption;
  @Option(names = { "-f", "--full-stats" }, description = "Показать полную статистику") 
  private boolean fullStatsOption;
  @Option(names = { "-o", "--output-dir" }, description = "Задать путь для результирующих файлов")
  private Path outputDir = Path.of("");
  @Option(names = { "-p", "--name-prefix" }, description = "Задать префикс для имен результирующих файлов") 
  private String namePrefix = "";
  @Parameters(arity="1..*", paramLabel = "FILES",
              description = "Один или несколько файлов, содержимое которых отсортировать на целые числа, вещественные и строки")
  private List<Path> inputFiles;

  /** Для чтения файлов, которые нужно обработать.*/ 
  private ArrayList<BufferedReader> readers;
  /** Хранит и выводит статистику по обработанным данным.*/
  private Statistics stats;
  /** Делает записи в нужные файлы.*/
  private LineHandler lineHandler;

  public static void main(String[] args) {
    // picocli читает аргументы командной строки и инициализирует
    // необходимые переменные, после чего запускает метод run(),
    // который выполняет основную логику программы.
    int exitCode = new CommandLine(new FilterUtility()).execute(args);
    System.exit(exitCode);
  }

  /** Главная логика программы.*/
  @Override
  public void run() {
    initialize();
    processFiles();
    stats.printToStdOut(shortStatsOption, fullStatsOption);
    cleanup();
  }

  /** 
   * Инициализирует чтение файлов для обработки и другие необходимые
   * для выполнения программы объекты.
   * <p>
   * Если указанные в командной строке аргументы не являются файлами или
   * или они недоступны, или не удаётся определить, что их содержимое
   * является текстом, то эти файлы просто игнорируются. Если таким
   * образом не удаётся открыть ни один файл для обработки, то программа
   * останавливается с кодом 1.
   * <p>
   * Для определения содержимого файлов используется Files.probeContentType() 
   * метод. Он может плохо определять некоторые файлы или полагаться на
   * расширение файла. Поэтому файлы надёжно определяются как текстовые только
   * если у них есть окончание '.txt'.
   * */
  public void initialize() {
    readers = new ArrayList<BufferedReader>();
    inputFiles.forEach(file -> {
      try {
        // probeContentType() может быть ненадёжен для файлов, не
        // оканчивающихся на .txt.
        String type = Files.probeContentType(file);
        if (!Files.isRegularFile(file)) {
          System.err.println("Внимание: '"+file+"' не является доступным "
                             +"для чтения файлом, игнорируем");
        } else if (type != null && type.contains("text")) {
          readers.add(Files.newBufferedReader(file));
        } else {
          System.err.println("Внимание: не похоже, что '"+file+"' является "
                             +"текстовым файлом, игнорируем");
        }
      } catch (IOException e) {
        System.err.println("Внимание: не удалось открыть файл '"+file+"'для чтения,"
                           +" продолжаем без него");
      }
    });

    if (readers.isEmpty()) {
      System.err.println("Ошибка: не удалось открыть ни один файл для обработки,"
                         +" останавливаем программу");
      int failureExitCode = 1;
      System.exit(failureExitCode);
    }

    stats = new Statistics();
    lineHandler = new LineHandler(outputDir, namePrefix, appendOption);
  }

  /**
   * Определяет тип каждой прочитанной строки из файлов, которые получилось
   * открыть для чтения и записывает результат в соответсвующий типу файл: 
   * '[name-prefix](integers|floats|strings).txt'.
   * <p>
   * Обработка файлов происходит поочередно в порядке перечисления их в
   * командной строке.
   * <p>
   * Все файлы для чтения закрываются тут же после того, как больше
   * нечего читать или произошла ошибка при попытке чтения файла.
   * <p>
   * Пустые пространства в строке (пробелы и прочее) не влияют на
   * определение типа прочитанного значения.
   */
  public void processFiles() {
    while(!readers.isEmpty()) {
      ArrayList<BufferedReader> readersToRemove = new ArrayList<>();

      // Поочередно читает и обрабатывает строку из каждого указанного файла
      // до тех пор, пока в них есть что читать или не произошла ошибка при чтении
      // файла.
      for(BufferedReader reader : readers) {
        try {
          String line = reader.readLine();
          if(line == null) {
            readersToRemove.add(reader);
          } else {
            line = line.trim();
            if (!line.isEmpty()) {
              lineHandler.handleLine(line, stats);
            }
          }
        } catch (IOException e) {
          System.err.println("Внимание: произошла ошибка при попытке чтения файла: "+e.getMessage());
          readersToRemove.add(reader);
        }
      }

      // Закрывает все файлы, которые закончились или при чтении которых
      // произошла ошибка.
      readersToRemove.forEach(reader -> {
        try {
          if (reader != null) {
            reader.close();
            readers.remove(reader);
          }
        } catch (IOException e) {
          System.err.println("Внимание: не удалось закрыть файл для чтения: "+e.getMessage());
        }
      });
    }
  }

  /** Закрывает файлы, в которые был записан результат.*/
  public void cleanup() {
    lineHandler.close();
  }
}
