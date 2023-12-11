package ru.netology;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    private static final AtomicInteger countLength3 = new AtomicInteger(0);
    private static final AtomicInteger countLength4 = new AtomicInteger(0);
    private static final AtomicInteger countLength5 = new AtomicInteger(0);


    public static void main(String[] args) {
        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        Thread threadLength3 = new Thread(() -> countBeautifulWords(3, texts));
        Thread threadLength4 = new Thread(() -> countBeautifulWords(4, texts));
        Thread threadLength5 = new Thread(() -> countBeautifulWords(5, texts));

        threadLength3.start();
        threadLength4.start();
        threadLength5.start();

        try {
            threadLength3.join();
            threadLength4.join();
            threadLength5.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Красивых слов с длиной 3: " + countLength3 + " шт");
        System.out.println("Красивых слов с длиной 4: " + countLength4 + " шт");
        System.out.println("Красивых слов с длиной 5: " + countLength5 + " шт");
    }

    private static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    private static void countBeautifulWords(int length, String[] texts) {
        for (String text : texts) {
            if (isBeautifulWord(text, length)) {
                switch (length) {
                    case 3:
                        countLength3.incrementAndGet();
                        break;
                    case 4:
                        countLength4.incrementAndGet();
                        break;
                    case 5:
                        countLength5.incrementAndGet();
                        break;
                }
            }
        }
    }

    private static boolean isBeautifulWord(String text, int length) {
        if (text.length() != length) {
            return false;
        }

        // Проверка на палиндром
        for (int i = 0; i < length / 2; i++) {
            if (text.charAt(i) != text.charAt(length - i - 1)) {
                return false;
            }
        }

        // Проверка на возрастающий порядок букв
        for (char c = 'a'; c <= 'z'; c++) {
            int count = 0;
            for (int i = 0; i < length; i++) {
                if (text.charAt(i) == c) {
                    count++;
                }
            }
            if (count > 0 && text.indexOf(c) != -1 && text.indexOf(c) + count != text.lastIndexOf(c)) {
                return false;
            }
        }

        return true;
    }
}
