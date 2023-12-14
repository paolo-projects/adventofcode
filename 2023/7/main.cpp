#include <iostream>
#include <fstream>
#include <algorithm>
#include <numeric>
#include <map>
#include <regex>
#include <optional>

#include <cassert>

using namespace std;

string FIGURES = "AKQJT98765432";

enum HandType
{
      FIVE_KIND = 0,
      FOUR_KIND = 1,
      FULL_HOUSE = 2,
      THREE_KIND = 3,
      TWO_PAIR = 4,
      PAIR = 5,
      HIGH_CARD = 6
};

struct Game
{
      string figures;
      long points;
};

map<char, int>
getCardsScore(string hand)
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

HandType handClassifier(string hand)
{
      auto scores = getCardsScore(hand);
      if (any_of(
              scores.begin(), scores.end(), [](const pair<char, int> &entry)
              { return entry.second == 5; }))
      {
            return HandType::FIVE_KIND;
      }
      else if (any_of(
                   scores.begin(), scores.end(), [](const pair<char, int> &entry)
                   { return entry.second == 4; }))
      {
            return HandType::FOUR_KIND;
      }
      else if (any_of(
                   scores.begin(), scores.end(), [](const pair<char, int> &entry)
                   { return entry.second == 3; }) &&
               any_of(
                   scores.begin(), scores.end(), [](const pair<char, int> &entry)
                   { return entry.second == 2; }))
      {
            return HandType::FULL_HOUSE;
      }
      else if (any_of(
                   scores.begin(), scores.end(), [](const pair<char, int> &entry)
                   { return entry.second == 3; }))
      {
            return HandType::THREE_KIND;
      }
      else if (accumulate(scores.begin(), scores.end(), 0, [](int acc, const pair<char, int> &entry)
                          { return acc + (entry.second == 2 ? 1 : 0); }) == 2)
      {
            return HandType::TWO_PAIR;
      }
      else if (any_of(
                   scores.begin(), scores.end(), [](const pair<char, int> &entry)
                   { return entry.second == 2; }))
      {
            return HandType::PAIR;
      }
      else
      {
            return HandType::HIGH_CARD;
      }
}

int handComparator(string hand1, string hand2)
{
      if (hand1.length() != 5 || hand2.length() != 5)
      {
            throw runtime_error("invalid hands provided");
      }

      auto rank1 = handClassifier(hand1);
      auto rank2 = handClassifier(hand2);

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
                        return FIGURES.find(fig1) - FIGURES.find(fig2);
                  }
            }
      }

      return 0;
}

int main(int argc, char *argv[])
{
      ifstream input("input.txt");
      std::string input_str((std::istreambuf_iterator<char>(input)),
                            std::istreambuf_iterator<char>());

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

      regex pattern("^(.{5}) (\\d+)$", regex_constants::multiline);

      vector<Game> games;

      for (auto match = sregex_iterator(input_str.begin(), input_str.end(), pattern); match != sregex_iterator(); match++)
      {
            smatch m = *match;
            string figures = m[1].str();
            long points = stol(m[2].str());

            games.emplace_back(Game{
                figures,
                points});
      }

      sort(games.begin(), games.end(), [](const Game &g1, const Game &g2)
           { return handComparator(g1.figures, g2.figures) < 0; });

      long scores = 0;

      for (int i = 0; i < games.size(); i++)
      {
            long score = games[i].points * (games.size() - i);
            scores += score;
      }

      cout << "Final score is " << scores << endl;

      return 0;
}