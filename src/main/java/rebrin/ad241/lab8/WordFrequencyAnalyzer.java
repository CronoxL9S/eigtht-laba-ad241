package rebrin.ad241.lab8;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Головний клас для багатопотокового аналізу файлів
 */
public class WordFrequencyAnalyzer {
    private final ExecutorService executor;
    private final List<FileResult> results;

    public WordFrequencyAnalyzer(int threadCount) {
        this.executor = Executors.newFixedThreadPool(threadCount);
        this.results = new ArrayList<>();
    }

    /**
     * Аналізує кілька файлів паралельно
     */
    public void analyzeFiles(List<String> filePaths) throws InterruptedException, ExecutionException {
        List<Future<FileResult>> futures = new ArrayList<>();

        // Запускаємо потік для кожного файлу
        for (String filePath : filePaths) {
            Future<FileResult> future = executor.submit(new FileAnalyzer(filePath));
            futures.add(future);
        }

        // Збираємо результати
        for (Future<FileResult> future : futures) {
            FileResult result = future.get();  // Чекаємо завершення
            results.add(result);
        }

        // Закриваємо пул потоків
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
    }

    /**
     * Зберігає результати у текстовий файл
     */
    public void saveResults(String outputPath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            writer.write("--- РЕЗУЛЬТАТИ АНАЛІЗУ ЧАСТОТИ СЛІВ ---\n");
            writer.write("Дата: " + java.time.LocalDateTime.now() + "\n\n");

            for (FileResult result : results) {
                writer.write(result.toString());
                writer.newLine();
            }

            writer.write("\n=== КІНЕЦЬ ЗВІТУ ===");
        }
        System.out.println("Результати збережено у файл: " + outputPath);
    }

    /**
     * Виводить результати у консоль
     */
    public void printResults() {
        System.out.println("\n=== РЕЗУЛЬТАТИ АНАЛІЗУ ===");
        for (FileResult result : results) {
            System.out.println(result);
        }
    }

    /**
     * Повертає результати для тестування
     */
    public List<FileResult> getResults() {
        return results;
    }
}
