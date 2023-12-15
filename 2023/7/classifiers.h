#pragma once

#include <map>
#include <string>
#include <algorithm>
#include <numeric>
#include <stdexcept>

#include "utils.h"

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

static std::map<HandType, std::string> HandTypeNames = {
    {FIVE_KIND, "FIVE_KIND"},
    {FOUR_KIND, "FOUR_KIND"},
    {FULL_HOUSE, "FULL_HOUSE"},
    {THREE_KIND, "THREE_KIND"},
    {TWO_PAIR, "TWO_PAIR"},
    {PAIR, "PAIR"},
    {HIGH_CARD, "HIGH_CARD"},
};

struct Game
{
    std::string figures;
    long points;
};

std::map<char, int> getCardsScore(std::string hand);
HandType handClassifier(std::string hand);
HandType jollyHandClassifier(std::string hand);
int handComparator(std::string hand1, std::string hand2, bool jolly = false);