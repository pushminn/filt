package pushminn8;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class StringTypeChecker {
  private static final String INT_REGEX = "^[+-]?[0-9]+$";
  private static final String Digits     = "(\\d+)";
    // an exponent is 'e' or 'E' followed by an optionally
    // signed decimal integer.
  private static final String Exp        = "[eE][+-]?"+Digits;
  private static final String FLOAT_REGEX =
            ("^[+-]?(" +         // Optional sign character
      
             // A decimal floating-point string representing a finite positive
             // number without a leading sign has at most five basic pieces:
             // Digits . Digits ExponentPart FloatTypeSuffix
      
             // Digits ._opt Digits_opt ExponentPart_opt FloatTypeSuffix_opt
             "("+Digits+"(\\.)?("+Digits+"?)("+Exp+")?)|"+
      
             // . Digits ExponentPart_opt FloatTypeSuffix_opt
             "(\\.("+Digits+")("+Exp+")?))");

  // private static final String DOUBLE_REGEX = "^[-+]?[0-9]*(\\.)?[0-9]+([eE][-+]?[0-9]+)?$";
  private static final Pattern intPattern = Pattern.compile(INT_REGEX);
  private static final Pattern floatPattern = Pattern.compile(FLOAT_REGEX);
  private static Matcher matcher;

  public static boolean isInteger(String str) {
    matcher = intPattern.matcher(str);
    if (matcher.matches()) {
      // System.out.println(matcher.group());
      return true;
    } else {
      return false;
    }
  }
  public static boolean isFloat(String str) {
    matcher = floatPattern.matcher(str);
    if (matcher.matches()) {
      // System.out.println(matcher.group());
      return true;
    } else {
      // System.out.println("================Not found in line");
      return false;
    }
  }
}
