package rebrin.ad241.lab8;

import java.util.Arrays;
import java.util.List;

/**
 * Головний клас програми
 */
public class Main {
    public static void main(String[] args) {
        try {
            // Список файлів для аналізу
            List<String> files = Arrays.asList(
                    "file1.txt",
                    "file2.txt",
                    "file3.txt"
            );

            System.out.println("Початок багатопотокового аналізу...\n");

            // Створюємо аналізатор з 3 потоками
            WordFrequencyAnalyzer analyzer = new WordFrequencyAnalyzer(3);

            // Вимірюємо час виконання
            long startTime = System.currentTimeMillis();

            // Аналізуємо файли
            analyzer.analyzeFiles(files);

            long endTime = System.currentTimeMillis();

            // Виводимо результати
            analyzer.printResults();

            // Зберігаємо у файл
            analyzer.saveResults("results.txt");

            System.out.println("\nЧас виконання: " + (endTime - startTime) + " мс");

        } catch (Exception e) {
            System.err.println("Помилка: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
