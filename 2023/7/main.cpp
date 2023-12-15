#include <iostream>
#include <fstream>
#include <regex>
#include <optional>

#include <cassert>

#include "classifiers.h"

using namespace std;

int main(int argc, char *argv[])
{
      ifstream input("input.txt");
      std::string input_str((std::istreambuf_iterator<char>(input)),
                            std::istreambuf_iterator<char>());

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

      sort(games.begin(), games.end(), [](const Game &g1, const Game &g2)
           { return handComparator(g1.figures, g2.figures, true) < 0; });

      scores = 0;

      for (int i = 0; i < games.size(); i++)
      {
            long score = games[i].points * (games.size() - i);
            scores += score;
      }

      cout << "P2: Final score is " << scores << endl;

      return 0;
}