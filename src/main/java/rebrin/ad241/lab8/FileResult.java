package rebrin.ad241.lab8;

/**
 * Клас для зберігання результату аналізу файлу
 */
public class FileResult {
    private final String fileName;
    private final String mostFrequentWord;
    private final int frequency;

    public FileResult(String fileName, String mostFrequentWord, int frequency) {
        this.fileName = fileName;
        this.mostFrequentWord = mostFrequentWord;
        this.frequency = frequency;
    }

    public String getFileName() {
        return fileName;
    }

    public String getMostFrequentWord() {
        return mostFrequentWord;
    }

    public int getFrequency() {
        return frequency;
    }

    @Override
    public String toString() {
        return String.format("Файл: %s | Найпоширеніше слово: '%s' | Частота: %d",
                fileName, mostFrequentWord, frequency);
    }
}
