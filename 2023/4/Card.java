import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Card {
    public int id;
    public int points;
    public Set<Integer> winningNumbers;

    public Card(int id, int points, Collection<Integer> winningNumbers) {
        this.id = id;
        this.points = points;
        this.winningNumbers = new HashSet<Integer>(winningNumbers);
    }
}
