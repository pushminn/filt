package pushminn8;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Проверяет тип строки, является ли она целым
 * числом или вещественным.
 */
public class StringTypeChecker {
  private static final String INT_REGEX = "^[+-]?[0-9]+$";
  private static final String DIGITS     = "(\\d+)";
  // Экспоненциальная часть.
  private static final String EXP        = "[eE][+-]?"+DIGITS;
  // Десятичное вещественное число содержит самое большее следующие
  // части:
  // Знак Цифры . Цифры Экспоненциальная_часть
  private static final String FLOAT_REGEX =
            ("^[+-]?(" +         // Опциональный знак.
             // Цифры  опц_точка  опц_цифры  опц_экспонента.
             "("+DIGITS+"(\\.)?("+DIGITS+"?)("+EXP+")?)|"+
             // Или если число начинается с точки.
             "(\\.("+DIGITS+")("+EXP+")?))");

  private static final Pattern INT_PATTERN = Pattern.compile(INT_REGEX);
  private static final Pattern FLOAT_PATTERN = Pattern.compile(FLOAT_REGEX);
  private static Matcher matcher;

  /** 
   * Проверяет, является ли строка целым числом.
   * 
   * @param str  строка, которая подергается проверке
   * @return     <code>true</code> если str соответствует
   *             регулярному выражению, представляющему
   *             целое число
   *             <code>false</code> в противном случае
   */
  public static boolean isInteger(String str) {
    matcher = INT_PATTERN.matcher(str);
    if (matcher.matches()) {
      return true;
    } else {
      return false;
    }
  }
  /** 
   * Проверяет, является ли строка вещественным числом.
   * 
   * @param str  строка, которая подергается проверке
   * @return     <code>true</code> если str соответствует
   *             регулярному выражению, представляющему
   *             вещественное число
   *             <code>false</code> в противном случае
   */
  public static boolean isFloat(String str) {
    matcher = FLOAT_PATTERN.matcher(str);
    if (matcher.matches()) {
      return true;
    } else {
      return false;
    }
  }
}
