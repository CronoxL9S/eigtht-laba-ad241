package rebrin.ad241.lab8;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тести для WordFrequencyAnalyzer
 */
public class WordFrequencyAnalyzerTest {

    @TempDir
    Path tempDir;

    private Path testFile1;
    private Path testFile2;
    private Path testFile3;

    @BeforeEach
    public void setup() throws IOException {
        // Створюємо тестові файли
        testFile1 = tempDir.resolve("test1.txt");
        Files.writeString(testFile1,
                "java java java programming language\n" +
                        "java is great");

        testFile2 = tempDir.resolve("test2.txt");
        Files.writeString(testFile2,
                "python python python python\n" +
                        "is simple");

        testFile3 = tempDir.resolve("test3.txt");
        Files.writeString(testFile3,
                "test test test test test\n" +
                        "word frequency");
    }

    /**
     * Тест 1: Перевірка визначення найпоширенішого слова
     */
    @Test
    public void testMostFrequentWord() throws Exception {
        WordFrequencyAnalyzer analyzer = new WordFrequencyAnalyzer(3);

        List<String> files = Arrays.asList(
                testFile1.toString(),
                testFile2.toString(),
                testFile3.toString()
        );

        analyzer.analyzeFiles(files);
        List<FileResult> results = analyzer.getResults();

        // Перевіряємо кількість результатів
        assertEquals(3, results.size());

        // Перевіряємо file1: java має бути найпоширенішим (4 рази)
        FileResult result1 = results.get(0);
        assertEquals("java", result1.getMostFrequentWord());
        assertEquals(4, result1.getFrequency());

        // Перевіряємо file2: python має бути найпоширенішим (4 рази)
        FileResult result2 = results.get(1);
        assertEquals("python", result2.getMostFrequentWord());
        assertEquals(4, result2.getFrequency());

        // Перевіряємо file3: test має бути найпоширенішим (5 разів)
        FileResult result3 = results.get(2);
        assertEquals("test", result3.getMostFrequentWord());
        assertEquals(5, result3.getFrequency());
    }

    /**
     * Тест 2: Перевірка роботи з порожнім файлом
     */
    @Test
    public void testEmptyFile() throws Exception {
        Path emptyFile = tempDir.resolve("empty.txt");
        Files.writeString(emptyFile, "");

        WordFrequencyAnalyzer analyzer = new WordFrequencyAnalyzer(1);
        analyzer.analyzeFiles(Arrays.asList(emptyFile.toString()));

        List<FileResult> results = analyzer.getResults();
        assertEquals(1, results.size());
        assertEquals("", results.get(0).getMostFrequentWord());
        assertEquals(0, results.get(0).getFrequency());
    }

    /**
     * Тест 3: Перевірка багатопотоковості (3 файли одночасно)
     */
    @Test
    public void testConcurrentProcessing() throws Exception {
        WordFrequencyAnalyzer analyzer = new WordFrequencyAnalyzer(3);

        List<String> files = Arrays.asList(
                testFile1.toString(),
                testFile2.toString(),
                testFile3.toString()
        );

        long startTime = System.currentTimeMillis();
        analyzer.analyzeFiles(files);
        long endTime = System.currentTimeMillis();

        // Перевіряємо, що всі файли оброблені
        assertEquals(3, analyzer.getResults().size());

        // Виводимо час для інформації
        System.out.println("Час обробки 3 файлів: " + (endTime - startTime) + " мс");
    }

    /**
     * Тест 4: Перевірка збереження результатів у файл
     */
    @Test
    public void testSaveResults() throws Exception {
        WordFrequencyAnalyzer analyzer = new WordFrequencyAnalyzer(1);
        analyzer.analyzeFiles(Arrays.asList(testFile1.toString()));

        Path outputFile = tempDir.resolve("results.txt");
        analyzer.saveResults(outputFile.toString());

        // Перевіряємо, що файл створено
        assertTrue(Files.exists(outputFile));

        // Перевіряємо вміст файлу
        String content = Files.readString(outputFile);
        assertTrue(content.contains("java"));
        assertTrue(content.contains("Частота: 4"));
    }
}
