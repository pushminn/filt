// package pushminn8;
//
// import java.nio.file.*;
// import picocli.CommandLine;
// import picocli.CommandLine.Command;
// import picocli.CommandLine.Option;
// import picocli.CommandLine.Parameters;
// // some exports omitted for the sake of brevity
//
// @Command(name = "filter-util", version = "filter-util 1.0", mixinStandardHelpOptions = true) 
// public class ASCIIArt implements Runnable { 
//
//     @Option(names = { "-s", "--font-size" }, description = "Font size") 
//     int fontSize = 19;
//
//     @Parameters();
//     private String[] words = { "Hello,", "picocli" }; 
//
//     @Override
//     public void run() { 
//         // The business logic of the command goes here...
//         // In this case, code for generation of ASCII art graphics
//         // (omitted for the sake of brevity).
//     }
//
//     public static void main(String[] args) {
//         int exitCode = new CommandLine(new ASCIIArt()).execute(args); 
//         System.exit(exitCode); 
//     }
// }
//
// public class ProgramArguments {
//   // private static final Path INT_FILE;
//   // private static final Path FLOAT_FILE;
//   // private static final Path STRING_FILE;
//   // private static final Path OUTPUT_PATH;
//   // private static final Path OUTPUT_NAME_PREFIX;
//   // private static final Path[] INPUT_FILES;
//   // private static final boolean APPEND_OPT;
//   //
//   // // Опции для статистики.
//   // private static final boolean SHOW_STATS_SIMPLE_OPT;
//   // private static final boolean SHOW_STATS_FULL_OPT;
//
//   public static void initialize(String[] args) {
//     if (args.length < 1) {
//       showHelp();
//     }
//   }
//
//   public static void showHelp() {
//     String usageMsg = " Usage: <main-class> [-a] [-s|-f] [-o <path-to-result>] [-p <name-prefix>] FILES... ";
//     System.out.println(usageMsg);
//   }
//
// }
