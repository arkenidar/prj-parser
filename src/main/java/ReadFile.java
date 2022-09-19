import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class ReadFile {

    private final Map<String, Integer> map = new HashMap<>();

    public static void main(String[] args) {
        new ReadFile().scan();
    }

    public void scan() {
        Set<String> stopWords = new TreeSet<>();
        try (Scanner scanner = new Scanner(new File("english_stopwords.txt"))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                stopWords.add(line);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        try (Scanner scanner = new Scanner(new File("realdonaldtrump.csv"))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                parseLine(line, stopWords);
            }
            mapPrint();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void parseLine(String line, Set<String> stopWords) {
        String[] parts = line.split(","); // comma-separated values in .csv
        String tweet = parts[2]; // tweet "content" in CSV

        for (String word : tweet.split("[^a-zA-Z]+")) {

            if (word.equals("")) continue;
            word = word.toLowerCase();

            if (stopWords.contains(word)) continue;

            Integer count = map.get(word);
            if (count == null) count = 0;

            map.put(word, count + 1);
        }
    }

    private void mapPrint() {
        Comparator<Map.Entry<String, Integer>> comparator = Comparator.comparing(Map.Entry<String, Integer>::getValue).reversed();

        for (Map.Entry<String, Integer> entry :
                map.entrySet().stream().sorted(comparator).limit(30).toList()
        ) {
            System.out.println(entry.getValue() + ":" + entry.getKey());
        }
    }
}