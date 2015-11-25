package util;

import java.util.*;

public class Soundex {

   static final int MAX_CODE_LENGTH = 4;

   private char code(char c) {
      Character code = digits.get(Character.toLowerCase(c));
      if (code == null)
         return '\0';
      return code.charValue();
   }

   private String zeros(int count) {
      StringBuilder zs = new StringBuilder();
      for (int i = 0; i < count; i++)
         zs.append('0');
      return zs.toString();
   }

   public String encode(String string) {
      StringBuilder s = new StringBuilder();
      s.append(Character.toUpperCase(string.charAt(0)));

      char previousCode = code(string.charAt(0));
      for (int i = 1; i < string.length() && s.length() < MAX_CODE_LENGTH; i++) {
         char c = string.charAt(i);
         if (!isVowelLike(c)) {
            char code = code(c);
            if (code != previousCode) {
               s.append(code);
               previousCode = code;
            }
         }
         else
            previousCode = '\0';
      }

      s.append(zeros(MAX_CODE_LENGTH - s.length()));

      return s.toString();
   }

   Map<Character, Character> digits = new HashMap<Character, Character>();
   {
      putAll("bfpv", '1');
      putAll("cgjkqsxz", '2');
      putAll("dt", '3');
      putAll("l", '4');
      putAll("mn", '5');
      putAll("r", '6');
   }

   private void putAll(String letters, char digit) {
      for (int i = 0; i < letters.length(); i++)
         digits.put(letters.charAt(i), digit);
   }

   boolean isVowel(char letter) {
      return "aeiouy".indexOf(letter) != -1;
   }

   boolean isVowelLike(char letter) {
      return "aeiouywh".indexOf(Character.toLowerCase(letter)) != -1;
   }
}
