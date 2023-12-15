#include <iostream>
#include <fstream>
#include <regex>
#include <optional>

#include <cassert>

#include "classifiers.h"

using namespace std;

int main(int argc, char *argv[])
{
    // a couple of tests, just to be sure
    assert(handClassifier("AAAAA") == HandType::FIVE_KIND);
    assert(handClassifier("AAAAT") == HandType::FOUR_KIND);
    assert(handClassifier("AATAA") == HandType::FOUR_KIND);
    assert(handClassifier("A99AA") == HandType::FULL_HOUSE);
    assert(handClassifier("55512") == HandType::THREE_KIND);
    assert(handClassifier("12162") == HandType::TWO_PAIR);
    assert(handClassifier("71237") == HandType::PAIR);
    assert(handClassifier("A187T") == HandType::HIGH_CARD);

    vector<string> hands = {
        "12345",
        "AAAAA",
        "TTTTT",
        "55999",
        "44442",
        "33878",
        "71477",
        "55599"};

    sort(hands.begin(), hands.end(), [](string a, string b)
         { return handComparator(a, b) < 0; });

    vector<string> expected = {
        "AAAAA",
        "TTTTT",
        "44442",
        "55999",
        "55599",
        "71477",
        "33878",
        "12345"};

    assert(hands == expected);

    // Jolly tests
    assert(jollyHandClassifier("AAAAA") == HandType::FIVE_KIND);
    assert(jollyHandClassifier("JAAAA") == HandType::FIVE_KIND);
    assert(jollyHandClassifier("JJAAA") == HandType::FIVE_KIND);
    assert(jollyHandClassifier("JJJAA") == HandType::FIVE_KIND);
    assert(jollyHandClassifier("JJJJA") == HandType::FIVE_KIND);
    assert(jollyHandClassifier("JJJJJ") == HandType::FIVE_KIND);

    assert(jollyHandClassifier("AAAAT") == HandType::FOUR_KIND);
    assert(jollyHandClassifier("JAAAT") == HandType::FOUR_KIND);
    assert(jollyHandClassifier("JJAAT") == HandType::FOUR_KIND);
    assert(jollyHandClassifier("JJJAT") == HandType::FOUR_KIND);

    assert(jollyHandClassifier("A99AA") == HandType::FULL_HOUSE);
    assert(jollyHandClassifier("AJ99A") == HandType::FULL_HOUSE);
    assert(jollyHandClassifier("AJ9J9") == HandType::FOUR_KIND);

    assert(jollyHandClassifier("55J12") == HandType::THREE_KIND);
    assert(jollyHandClassifier("5JJ12") == HandType::THREE_KIND);
    assert(jollyHandClassifier("JJJ12") != HandType::THREE_KIND);

    assert(jollyHandClassifier("12162") == HandType::TWO_PAIR);
    assert(jollyHandClassifier("1236JJ") == HandType::THREE_KIND);
    assert(jollyHandClassifier("12361J") == HandType::THREE_KIND);

    assert(jollyHandClassifier("71237") == HandType::PAIR);
    assert(jollyHandClassifier("7123J") == HandType::PAIR);

    assert(jollyHandClassifier("A187T") == HandType::HIGH_CARD);

    return 0;
}