#include <iostream>
#include <fstream>
#include <map>
#include <vector>
#include <numeric>

std::map<char, int> choicePoints{
    {'X', 1},
    {'Y', 2},
    {'Z', 3}};

std::map<char, int> abcPoints{
    {'A', 1},
    {'B', 2},
    {'C', 3}};

std::map<char, int> points{
    {'X', 0},
    {'Y', 3},
    {'Z', 6}};

std::map<std::pair<char, char>, int> scorePoints{
    {{'A', 'X'}, 3},
    {{'A', 'Y'}, 6},
    {{'A', 'Z'}, 0},
    {{'B', 'X'}, 0},
    {{'B', 'Y'}, 3},
    {{'B', 'Z'}, 6},
    {{'C', 'X'}, 6},
    {{'C', 'Y'}, 0},
    {{'C', 'Z'}, 3},
};

std::map<std::pair<char, char>, char> reverseMoves{
    {{'A', 'X'}, 'C'},
    {{'A', 'Y'}, 'A'},
    {{'A', 'Z'}, 'B'},
    {{'B', 'X'}, 'A'},
    {{'B', 'Y'}, 'B'},
    {{'B', 'Z'}, 'C'},
    {{'C', 'X'}, 'B'},
    {{'C', 'Y'}, 'C'},
    {{'C', 'Z'}, 'A'},
};

struct Move
{
    char opponentMove;
    char expectedResult;

    bool operator==(const Move &rhs)
    {
        return opponentMove == rhs.opponentMove && expectedResult == rhs.expectedResult;
    }

    int getScoreA() const
    {
        return choicePoints[expectedResult] + scorePoints[{opponentMove, expectedResult}];
    }

    int getScoreB() const
    {
        char move = reverseMoves[{opponentMove, expectedResult}];
        return points[expectedResult] + abcPoints[move];
    }
};

std::vector<Move>
    moves;

int main(int argc, char **argv)
{
    using namespace std;

    ifstream input("input.txt");
    int score = 0;

    string line;
    while (!getline(input, line).eof())
    {
        char opponentMove = line[0];
        char myMove = line[2];

        Move move{opponentMove, myMove};
        score += move.getScoreA();
        moves.push_back(move);
    }

    cout << "Part 1 Score: " << score << endl;

    int newScore = 0;
    for (const Move &m : moves)
    {
        newScore += m.getScoreB();
    }

    cout << "Part 2 Score: " << newScore << endl;

    return 0;
}