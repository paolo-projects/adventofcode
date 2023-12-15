#include "classifiers.h"

using namespace std;

static string FIGURES = "AKQJT98765432";
static string FIGURES_P2 = "AKQT98765432J";

map<char, int> getCardsScore(string hand)
{
    map<char, int> scores;
    for (char c : hand)
    {
        auto el = scores.find(c);
        if (el == scores.end())
        {
            scores[c] = 1;
        }
        else
        {
            scores[c] += 1;
        }
    }

    return scores;
}

HandType jollyHandClassifier(string hand)
{
    auto scores = getCardsScore(hand);
    map<char, int> jollyModifiedScores(scores);
    int jollies = scores['J'];
    for (auto entry : scores)
    {
        if (entry.first != 'J')
        {
            jollyModifiedScores[entry.first] += jollies;
        }
    }
    jollyModifiedScores.erase('J');

    bool isFullHouse = countMapValueOccurrences(jollyModifiedScores, 3) == 2;

    if (doesMapContain(scores, 5) || doesMapContain(jollyModifiedScores, 5))
    {
        return HandType::FIVE_KIND;
    }
    else if (doesMapContain(scores, 4) || doesMapContain(jollyModifiedScores, 4))
    {
        return HandType::FOUR_KIND;
    }
    else if ((doesMapContain(scores, 3) &&
              doesMapContain(scores, 2)) ||
             isFullHouse)
    {
        return HandType::FULL_HOUSE;
    }
    else if (doesMapContain(scores, 3) || doesMapContain(jollyModifiedScores, 3))
    {
        return HandType::THREE_KIND;
    }
    else if (countMapValueOccurrences(scores, 2) == 2 || countMapValueOccurrences(jollyModifiedScores, 2) == 2)
    {
        return HandType::TWO_PAIR;
    }
    else if (doesMapContain(scores, 2) || doesMapContain(jollyModifiedScores, 2))
    {
        return HandType::PAIR;
    }
    else
    {
        return HandType::HIGH_CARD;
    }
}

HandType handClassifier(string hand)
{
    auto scores = getCardsScore(hand);

    if (doesMapContain(scores, 5))
    {
        return HandType::FIVE_KIND;
    }
    else if (doesMapContain(scores, 4))
    {
        return HandType::FOUR_KIND;
    }
    else if (doesMapContain(scores, 3) &&
             doesMapContain(scores, 2))
    {
        return HandType::FULL_HOUSE;
    }
    else if (doesMapContain(scores, 3))
    {
        return HandType::THREE_KIND;
    }
    else if (countMapValueOccurrences(scores, 2) == 2)
    {
        return HandType::TWO_PAIR;
    }
    else if (doesMapContain(scores, 2))
    {
        return HandType::PAIR;
    }
    else
    {
        return HandType::HIGH_CARD;
    }
}

int handComparator(string hand1, string hand2, bool jolly)
{
    if (hand1.length() != 5 || hand2.length() != 5)
    {
        throw runtime_error("invalid hands provided");
    }

    auto classifier = jolly ? jollyHandClassifier : handClassifier;

    auto rank1 = classifier(hand1);
    auto rank2 = classifier(hand2);

    if (rank1 != rank2)
    {
        return (int)rank1 - (int)rank2;
    }
    else
    {
        for (int i = 0; i < hand1.length(); i++)
        {
            char fig1 = hand1[i];
            char fig2 = hand2[i];

            if (fig1 != fig2)
            {
                return jolly ? (FIGURES_P2.find(fig1) - FIGURES_P2.find(fig2)) : (FIGURES.find(fig1) - FIGURES.find(fig2));
            }
        }
    }

    return 0;
}