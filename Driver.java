import java.util.Queue;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Driver class to build and analyze a filtered binary tree of strings
 * formed by concatenating two characters (like "A" and "B").
 * Filters patterns based on specific allowed/restricted rules.
 */
public class Driver {

    // Stores strings that are filtered out due to pattern restrictions
    static ArrayList<String> restrictedPatternsList = new ArrayList<>();

    public static void main(String[] args) {
        String firstChar = "A";
        String secondChar = "B";

        // Build filtered binary tree based on firstChar and secondChar
        LinkedBinaryTree<String> tree = buildFilteredBinaryTree(firstChar, secondChar);

        // Print the tree structure starting from the root
        printTree(tree, tree.root(), 0, "Root");

        System.out.println("Tree size: " + tree.size());

        // Separate strings in tree by their length for analysis
        ArrayList<String> length15 = new ArrayList<>();
        ArrayList<String> length14 = new ArrayList<>();
        ArrayList<String> length13 = new ArrayList<>();
        ArrayList<String> length12 = new ArrayList<>();

        for (Position<String> pos : tree.positions()) {
            int len = pos.getElement().length();
            switch (len) {
                case 15 -> length15.add(pos.getElement());
                case 14 -> length14.add(pos.getElement());
                case 13 -> length13.add(pos.getElement());
                case 12 -> length12.add(pos.getElement());
            }
        }

        // Print categorized strings by length
        printListWithSize("Length 15", length15);
        printListWithSize("Length 14", length14);
        printListWithSize("Length 13", length13);
        printListWithSize("Length 12", length12);
    }

    /**
     * Prints list elements with a label and their count.
     */
    public static void printListWithSize(String label, ArrayList<String> list) {
        System.out.println("\n" + label + " strings (" + list.size() + "):");
        for (String s : list) {
            System.out.println(s);
        }
    }

    /**
     * Builds a filtered binary tree by appending `first` and `second` strings,
     * ensuring that the generated strings do not match restricted patterns.
     */
    public static LinkedBinaryTree<String> buildFilteredBinaryTree(String first, String second) {
        LinkedBinaryTree<String> tree = new LinkedBinaryTree<>();

        if (!isTwoCharacterAlphabet(first, second) || first.isEmpty() || second.isEmpty()) {
            throw new IllegalArgumentException("Invalid input strings.");
        }

        Position<String> root = tree.addRoot("");
        Queue<Position<String>> queue = new LinkedList<>();
        queue.add(root);

        // Build tree with BFS approach
        while (!queue.isEmpty()) {
            Position<String> current = queue.poll();
            String currentStr = current.getElement();

            if (currentStr.length() < 15) {
                // Try to append first string (e.g., "A")
                if (currentStr.length() + first.length() <= 15) {
                    String leftStr = currentStr + first;
                    if (isPatternAllowed(leftStr)) {
                        Position<String> leftChild = tree.addLeft(current, leftStr);
                        queue.add(leftChild);
                    } else {
                        restrictedPatternsList.add(leftStr);
                    }
                }

                // Try to append second string (e.g., "B")
                if (currentStr.length() + second.length() <= 15) {
                    String rightStr = currentStr + second;
                    if (isPatternAllowed(rightStr)) {
                        Position<String> rightChild = tree.addRight(current, rightStr);
                        queue.add(rightChild);
                    } else {
                        restrictedPatternsList.add(rightStr);
                    }
                }
            }
        }

        // Remove subtrees matching restricted patterns or substrings
        removeRestrictedSubtrees(tree);

        return tree;
    }

    /**
     * Checks if the input strings consist of only two unique characters combined.
     */
    public static boolean isTwoCharacterAlphabet(String first, String second) {
        String combined = first + second;
        Map<Character, Integer> charCount = new HashMap<>();

        for (char c : combined.toCharArray()) {
            charCount.put(c, charCount.getOrDefault(c, 0) + 1);
            if (charCount.size() > 2) {
                System.out.println("Rejected: more than two unique characters.");
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if a string pattern is allowed by filtering out restricted patterns,
     * their reversals, and flipped character variants.
     */
    public static boolean isPatternAllowed(String input) {
        ArrayList<String> restrictedPatterns = getRestrictedPatterns();

        // Direct match check
        if (restrictedPatterns.contains(input)) return false;

        // Substring check
        for (String restricted : restrictedPatterns) {
            if (input.contains(restricted)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns a comprehensive list of restricted patterns including their reversals and flipped variants.
     */
    private static ArrayList<String> getRestrictedPatterns() {
        ArrayList<String> basePatterns = new ArrayList<>();
        Collections.addAll(basePatterns,
            "AABBBAAAB", "ABAAABBBA", "AAABABABBB", "AAABABBABB", "AAABABBBAB", 
            "AABBBABAAB", "AABBBABABA", "ABAABABBBA", "ABAABBBABA", "ABABAABBBA",
            "ABBBABAAAB", "AABAABBBAB", "AABBBAABAB", "AABBBAABAAB", "AAABABBAAAB",
            "AABBBABBBAA", "ABABABBBABA", "ABABBABBABA", "AAABAAABBAB", "AAABBABAAAB", 
            "AAABAABAABAB", "AAABABAAABAB", "AABAAABABAAB", "AAABAAABABBA", "AAABAABABAAB", 
            "AAABABAABAAB", "ABBABAAABAAB", "ABABBBABBBABA", "ABAABBBAAB", "AAABBABABB", 
            "AAABBABBAB", "AABAABBABB", "AABABABBBA", "AABABBABBA", "AABABBBAAB", "AABABBBABA", 
            "AABBAABBBA", "AABBABABBA", "AABBABBAAB", "AABBABBABA", "AABBBAABBA", "ABAABBABBA", 
            "AABBABABBBA", "AABABBBABBBA", "AAAA", "AAABAABBB", "AAABBBABB", "AABBABBBA", 
            "AABBBABBA", "AAABBAAABB", "AABABAAABB", "ABBBAABBBA", "AAABAABBAB", "AAABAABAABB", 
            "AAABBAABAAB", "AABAABAABBA", "AABAABBAAAB", "AABABABAAAB", "AAABBAAABAB", "AABAAABABAB", 
            "AABAAABBAAB", "AAABAABAAABAB", "AAABABBBAA", "AAABBAABBB", "AAABBABBAA", "ABABAAABBB", 
            "ABABBBAABBA", "AABABBAAABA", "AAABBABAAABA", "AABBABBABBA", "AABABBABBBA", "AABBBABBBABA", 
            "ABABBABBABBA", "ABABBABBBABA", "ABABBBABABBA", "ABBABABBABBA", "ABAABBBAAA", "AABABBBAAA", 
            "AABAAABAAABAB", "ABBBABBBABBBA", "AAABAAABAAABAAA"
        );

        ArrayList<String> allPatterns = new ArrayList<>(basePatterns);

        // Add reversed patterns
        for (String p : basePatterns) {
            allPatterns.add(new StringBuilder(p).reverse().toString());
        }

        // Add flipped patterns (A <-> B)
        for (String p : new ArrayList<>(allPatterns)) {
            StringBuilder flipped = new StringBuilder();
            for (char ch : p.toCharArray()) {
                flipped.append(ch == 'A' ? 'B' : 'A');
            }
            allPatterns.add(flipped.toString());
        }

        return allPatterns;
    }

    // Stores candidate strings matching restricted substring patterns
    static ArrayList<String> matchedPatterns = new ArrayList<>();

    /**
     * Checks if candidate string matches the given pattern by Abelian equality of segments.
     * Returns true if candidate matches the pattern with equal counts of 'A's and 'B's in corresponding parts.
     */
    public static boolean matchesPattern(String candidate, String pattern) {
        if (pattern.length() > candidate.length()) return false;

        int countA = 0, countB = 0;
        for (char c : pattern.toCharArray()) {
            if (c == 'A') countA++;
            else if (c == 'B') countB++;
        }

        // Try all possible substring lengths for A and B segments
        for (int lenA = 1; lenA <= candidate.length(); lenA++) {
            for (int lenB = 1; lenB <= candidate.length(); lenB++) {
                if (countA * lenA + countB * lenB != candidate.length()) continue;

                String subA = null, subB = null;
                int index = 0;
                boolean valid = true;

                for (char pChar : pattern.toCharArray()) {
                    int segmentLength = (pChar == 'A') ? lenA : lenB;
                    String segment = candidate.substring(index, index + segmentLength);

                    if (pChar == 'A') {
                        if (subA == null) subA = segment;
                        else if (!(countChars(subA, 'A') == countChars(segment, 'A') &&
                                   countChars(subA, 'B') == countChars(segment, 'B'))) {
                            valid = false;
                            break;
                        }
                    } else {
                        if (subB == null) subB = segment;
                        else if (!(countChars(subB, 'A') == countChars(segment, 'A') &&
                                   countChars(subB, 'B') == countChars(segment, 'B'))) {
                            valid = false;
                            break;
                        }
                    }
                    index += segmentLength;
                }

                if (valid) {
                    matchedPatterns.add(candidate);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Helper method to count occurrences of a character in a string.
     */
    private static int countChars(String s, char c) {
        int count = 0;
        for (char ch : s.toCharArray()) {
            if (ch == c) count++;
        }
        return count;
    }

    /**
     * Prints the binary tree in a structured format.
     */
    public static void printTree(LinkedBinaryTree<String> tree, Position<String> node, int depth, String label) {
        if (node == null) return;

        System.out.print("    ".repeat(depth));
        System.out.println(label + ": " + node.getElement());

        printTree(tree, tree.left(node), depth + 1, "L");
        printTree(tree, tree.right(node), depth + 1, "R");
    }

    /**
     * Removes subtrees in the tree that match restricted patterns or contain restricted substrings.
     */
    private static void removeRestrictedSubtrees(LinkedBinaryTree<String> tree) {
        ArrayList<Position<String>> nodesToRemove = new ArrayList<>();

        // Find nodes matching patterns in restrictedPatternsList using matchesPattern
        for (Position<String> pos : tree.positions()) {
            String element = pos.getElement();
            for (String restricted : restrictedPatternsList) {
                if (matchesPattern(element, restricted)) {
                    nodesToRemove.add(pos);
                    break;
                }
            }
        }

        // Remove the matched subtrees
        for (Position<String> node : nodesToRemove) {
            for (Position<String> candidate : tree.positions()) {
                if (candidate.getElement().equals(node.getElement())) {
                    tree.removeSubtree(candidate);
                    break;
                }
            }
        }

        // Additional removals for substrings contained in mpSubstring
        ArrayList<Position<String>> substringRemovals = new ArrayList<>();
        for (Position<String> node : tree.positions()) {
            String element = node.getElement();
            for (String restrictedSubstr : matchedPatterns) {
                if (element.contains(restrictedSubstr)) {
                    substringRemovals.add(node);
                    break;
                }
            }
        }

        for (Position<String> node : substringRemovals) {
            for (Position<String> candidate : tree.positions()) {
                if (candidate.getElement().equals(node.getElement())) {
                    tree.removeSubtree(candidate);
                    break;
                }
            }
        }
    }
}
