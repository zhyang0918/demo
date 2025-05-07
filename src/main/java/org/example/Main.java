package org.example;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {
    public static final int REPEAT_COUNT = 3;

    public static void main(String[] args) {
        step1();
        step2();
    }

    private static void step1() {
        String currentStr = "aabcccbbad";
        System.out.println("step1 input: " + currentStr);

        while(true) {
            String next = process(currentStr);

            if (next.equals(currentStr)) {
                break;
            }

            System.out.println(next);
            currentStr = next;
        }
    }

    private static void step2() {
        String currentStr = "abcccbad";
        System.out.println("step2 input: " + currentStr);

        while(true) {
            String next = process2(currentStr);

            if (next.equals(currentStr)) {
                break;
            }

            currentStr = next;
        }
    }

    /**
     * 处理分组后的字符串，如3个一组则转成"",并join
     * @param s
     * @return
     */
    private static String process(String s) {
        List<String> groups = splitIntoGroups(s);
        return groups.stream()
                .map(group -> {
                    int len = group.length();
                    if (len >= REPEAT_COUNT) {
                        int remainder = len % REPEAT_COUNT;
                        return remainder > 0 ? group.substring(0, remainder) : "";
                    } else {
                        return group;
                    }
                })
                // 过滤空字符串
                .filter(str -> !str.isEmpty())
                .collect(Collectors.joining());
    }

    /**
     * 维护一个字符串列表，将连续相同的字符串合并到这个list中
     * @param s
     * @return
     */
    private static List<String> splitIntoGroups(String s) {
        if (s.isEmpty()) {
            return Collections.emptyList();
        }
        return s.chars()
                .mapToObj(c -> (char) c)
                .collect(ArrayList::new, (list, c) -> {
                    if (!list.isEmpty()) {
                        String last = list.get(list.size() - 1);
                        if (last.charAt(0) == c) {
                            list.set(list.size() - 1, last + c);
                            return;
                        }
                    }
                    list.add(String.valueOf(c));
                    }, ArrayList::addAll);
    }

    /**
     * 处理字符串：找到第一个连续三个相同字符，替换为左侧字符
     * @param s
     * @return
     */
    private static String process2(String s) {
        String rst = "";
        // 查找第一个连续三个相同字符的位置, abcde->abc, bcd, cde
        OptionalInt firstRepeatIndex = IntStream.range(0, s.length() - 2)
                .filter(i -> s.charAt(i) == s.charAt(i + 1) && s.charAt(i) == s.charAt(i + 2))
                .findFirst();

        if (firstRepeatIndex.isPresent()) {
            int index = firstRepeatIndex.getAsInt();
            // 左侧的字符
            char leftChar = (index > 0) ? s.charAt(index - 1) : 0;
            // 转换为左侧字符
            String replacement = (leftChar != 0) ? String.valueOf(leftChar) : "";
            rst = s.substring(0, index).concat(replacement).concat(s.substring(index + REPEAT_COUNT));

            if (rst.length() == 1) {
                System.out.println(rst);
            } else {
                System.out.println(rst + "，" + s.substring(index, index + REPEAT_COUNT) + " is replaced by " + replacement);
            }
        }

        return rst;
    }
}