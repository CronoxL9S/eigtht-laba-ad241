package rebrin.ad241.lab8;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.Callable;

/**
 * Клас для аналізу одного файлу у окремому потоці
 */
public class FileAnalyzer implements Callable<FileResult> {
    private final String filePath;

    public FileAnalyzer(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public FileResult call() throws Exception {
        System.out.println("Потік " + Thread.currentThread().getName() +
                " обробляє файл: " + filePath);

        // Читаємо файл
        List<String> lines = readFile(filePath);

        // Рахуємо частоту слів
        Map<String, Integer> wordCount = countWords(lines);

        // Знаходимо найпоширеніше слово
        String mostFrequentWord = findMostFrequentWord(wordCount);
        int frequency = wordCount.getOrDefault(mostFrequentWord, 0);

        System.out.println("Потік " + Thread.currentThread().getName() +
                " завершив обробку файлу: " + filePath);

        return new FileResult(filePath, mostFrequentWord, frequency);
    }

    /**
     * Читає всі рядки з файлу
     */
    private List<String> readFile(String filePath) throws IOException {
        try {
            return Files.readAllLines(Path.of(filePath));
        } catch (IOException e) {
            System.err.println("Помилка читання файлу: " + filePath);
            throw e;
        }
    }

    /**
     * Рахує частоту кожного слова
     */
    private Map<String, Integer> countWords(List<String> lines) {
        Map<String, Integer> wordCount = new HashMap<>();

        for (String line : lines) {
            // Розбиваємо рядок на слова (тільки букви і цифри)
            String[] words = line.toLowerCase()
                    .replaceAll("[^a-zA-Zа-яА-ЯіІїЇєЄґҐ0-9\\s]", "")
                    .split("\\s+");

            for (String word : words) {
                word = word.trim();
                if (!word.isEmpty()) {
                    wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
                }
            }
        }

        return wordCount;
    }

    /**
     * Знаходить найпоширеніше слово
     */
    private String findMostFrequentWord(Map<String, Integer> wordCount) {
        if (wordCount.isEmpty()) {
            return "";
        }

        String mostFrequent = "";
        int maxFrequency = 0;

        for (Map.Entry<String, Integer> entry : wordCount.entrySet()) {
            if (entry.getValue() > maxFrequency) {
                maxFrequency = entry.getValue();
                mostFrequent = entry.getKey();
            }
        }

        return mostFrequent;
    }
}
