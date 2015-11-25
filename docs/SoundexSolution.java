package util;

import java.util.*;

public class SoundexSolution {
   private static final int MAX_CODE_LENGTH = 4;
   private Map<Character, Character> digits = new HashMap<Character, Character>();
   {
      putAll("bfpv", '1');
      putAll("cgjkqsxz", '2');
      putAll("dt", '3');
      putAll("l", '4');
      putAll("mn", '5');
      putAll("r", '6');
   }

   public String encode(String text) {
      return pad(head(text) + tail(toDigits(text)));
   }

   public String pad(String code) {
      int zeros = MAX_CODE_LENGTH - code.length();
      return code + (zeros > 0 ? StringFactory.create('0', zeros) : "");
   }

   private String head(String text) {
      return text.substring(0, 1).toUpperCase();
   }

   private String toDigits(String text) {
      StringBuilder code = new StringBuilder();
      encodeFirstLetter(code, text);
      encodeTail(code, text);
      return code.toString();
   }

   private void encodeFirstLetter(StringBuilder code, String word) {
      code.append(toDigit(word.charAt(0)));
   }

   private void encodeTail(StringBuilder code, String text) {
      for (int i = 1; i < text.length() && isNotComplete(code); i++) 
         encode(code, text.charAt(i), text.charAt(i - 1));
   }

   private void encode(StringBuilder code, char letter, char lastLetter) {
      if (isVowelLike(letter)) return;
      char digit = toDigit(letter);
      if (isDuplicate(digit, code) && !isVowel(lastLetter)) return;
      code.append(digit);
   }

   private boolean isDuplicate(char digit, StringBuilder code) {
      if (code.length() == 0) return false;
      return digit == code.charAt(code.length() - 1);
   }

   private boolean isVowel(char letter) {
      return "aeiouy".indexOf(Character.toLowerCase(letter)) != -1;
   }
   
   private boolean isVowelLike(char letter) {
      return "aeiouhyw".indexOf(Character.toLowerCase(letter)) != -1;
   }

   private boolean isNotComplete(StringBuilder code) {
      return code.length() < MAX_CODE_LENGTH;
   }

   private void putAll(String letters, char digit) {
      for (int i = 0; i < letters.length(); i++)
         digits.put(letters.charAt(i), digit);
   }

   char toDigit(char ch) {
      char key = Character.toLowerCase(ch);
      if (!digits.containsKey(key)) return '?';
      return digits.get(key);
   }

   private String tail(String text) {
      return text.substring(1);
   }
}
