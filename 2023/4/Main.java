import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws IOException {
        var input = Files.readAllLines(Paths.get("input.txt"));
        var cards = new ArrayList<Card>();

        var cardPattern = Pattern.compile("^Card\\s+(\\d+):.+$");
        var numbersPattern = Pattern.compile("(\\d+)");

        for (var line : input) {
            var cardIdM = cardPattern.matcher(line);
            if (cardIdM.matches()) {
                var cardId = Integer.valueOf(cardIdM.group(1));
                var sections = line.substring(line.indexOf(":") + 1).split("\\|");

                var winningNumbers = new HashSet<Integer>();
                var ownNumbers = new HashSet<Integer>();

                var winningNumsMatcher = numbersPattern.matcher(sections[0]);
                while (winningNumsMatcher.find()) {
                    winningNumbers.add(Integer.valueOf(winningNumsMatcher.group(1)));
                }

                var ownNumsMatcher = numbersPattern.matcher(sections[1]);
                while (ownNumsMatcher.find()) {
                    ownNumbers.add(Integer.valueOf(ownNumsMatcher.group(1)));
                }

                var myWinningNumbers = new HashSet<Integer>(ownNumbers);
                myWinningNumbers.retainAll(winningNumbers);

                var score = 0;
                if (myWinningNumbers.size() > 0) {
                    score = 1 << (myWinningNumbers.size() - 1);
                }

                cards.add(new Card(cardId, score, myWinningNumbers));
            }
        }

        var totalPoints = cards.stream().map((card) -> card.points).reduce(0, Integer::sum);

        System.out.printf("Total points: %d\n", totalPoints);

        var copies = new HashMap<Integer, Integer>();
        for (int i = 1; i <= cards.size(); i++) {
            copies.put(i, 1);
        }

        for (Card card : cards) {
            var cardId = card.id;
            var cardsCount = copies.get(cardId);

            for (int n = cardId + 1; n < cardId + 1 + card.winningNumbers.size(); n++) {
                copies.put(n, copies.get(n) + cardsCount);
            }
        }

        int totalCards = copies.values().stream().reduce(0, Integer::sum);

        System.out.printf("Total cards: %d\n", totalCards);
    }
}