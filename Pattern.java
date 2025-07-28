import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * Pattern class contains methods to test binary strings for pattern instances,
 * morphism operations, and pattern matching using Abelian equality logic.
 */
public class Pattern {

    public static void main(String[] args) {
        // Example usage:
        ArrayList<String> matchedStrings = method("AAAAA", "0", "0001", "1110");

        if (matchedStrings.isEmpty()) {
            System.out.println("No strings have an instance of the given pattern.");
        }
    }

    /**
     * Checks if the string 'str' contains an instance of 'subStr' pattern 
     * considering Abelian equality of segments.
     */
    public static boolean test(String str, String subStr) {
        if (!checkTwoChar(subStr) || str.length() < subStr.length()) {
            return false;
        }

        String subA = "";
        String subB = "";
        int countSubA = 0;
        int countSubB = 0;

        // Identify two unique characters in subStr
        for (int i = 0; i < subStr.length(); i++) {
            String charac = String.valueOf(subStr.charAt(i));
            if (subA.isEmpty()) {
                subA = charac;
                countSubA++;
            } else if (subB.isEmpty() && (!charac.equals(subA))) {
                subB = charac;
                countSubB++;
            } else if (subA.equals(charac))
                countSubA++;
            else if (subB.equals(charac))
                countSubB++;
        }

        if (countSubA == 0 && countSubB == 0) return false;

        for (int i = subStr.length(); i <= str.length(); i++) {
            for (int j = 0; j <= str.length() - i; j++) {
                String candidate = str.substring(j, j + i);

                for (int lenA = 1; lenA <= candidate.length(); lenA++) {
                    for (int lenB = 1; lenB <= candidate.length(); lenB++) {
                        if (countSubA * lenA + countSubB * lenB != candidate.length())
                            continue;

                        int index = 0;
                        String valA = null;
                        String valB = null;
                        boolean valid = true;

                        for (int k = 0; k < subStr.length(); k++) {
                            String ch = String.valueOf(subStr.charAt(k));
                            int len = ch.equals(subA) ? lenA : lenB;

                            if (index + len > candidate.length()) {
                                valid = false;
                                break;
                            }

                            String part = candidate.substring(index, index + len);

                            if (ch.equals(subA)) {
                                if (valA == null) valA = part;
                                else if (!((countChars(valA, '0') == countChars(part, '0'))&& (countChars(valA, '1') == countChars(part, '1'))))  {
                                    valid = false;
                                    break;
                                }
                            } else {
                                if (valB == null) valB = part;
                                else if (!((countChars(valB, '0') == countChars(part, '0'))&& (countChars(valB, '1') == countChars(part, '1'))))  {
                                    valid = false;
                                    break;
                                }
                            }

                            index += len;
                        }

                        if (valid && ((valA != null && countSubB == 0) || (valB != null && countSubA == 0) || (valA != null && valB != null))) {
                            System.out.println("Matched candidate: " + candidate);
                            System.out.println("from " + j + " to " + (j+i));
                            System.out.println("ValA: " + valA);
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    private static int countChars(String s, char c) {
        int count = 0;
        for (char ch : s.toCharArray()) {
            if (ch == c) count++;
        }
        return count;
    }

    public static boolean checkTwoChar(String a) {
        Map<Character, Integer> charMap = new HashMap<>();
        for (char c : a.toCharArray()) {
            charMap.put(c, charMap.getOrDefault(c, 0) + 1);
            if (charMap.size() > 2) {
                System.out.println("Rejected: more than two unique characters.");
                return false;
            }
        }
        return true;
    }

    /**
     * Generates strings using morphism rules and collects those
     * matching the given pattern.
     */
    public static ArrayList<String> method(String pattern, String w, String A, String B) {
        ArrayList<String> list = new ArrayList<>();
        String previousNew = w;

        while (previousNew.length() <= 5000) {
            String newS = morphism(A, B, previousNew);
            previousNew = newS;

            if (test(previousNew, pattern)) {
                System.out.println("Matched String: " + previousNew);
                System.out.println();
                list.add(previousNew);  
            }
        }
        return list;
    }

    /**
     * Applies morphism rules replacing '0' with A and '1' with B.
     */
    public static String morphism(String A, String B, String old) {
        StringBuilder newS = new StringBuilder();
        for (int i = 0; i < old.length(); i++) {
            if (old.charAt(i) == '0') {
                newS.append(A);
            } else if (old.charAt(i) == '1') {
                newS.append(B);
            }
        }
        return newS.toString();
    }
}
