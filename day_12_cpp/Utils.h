#pragma once

#include <string>
#include <list>
#include "GraphNode.h"

void trim(std::string &input)
{
    while (input.size() > 0 && input[0] == ' ')
    {
        input.erase(input.begin());
    }

    while (input.size() > 0 && input[input.size() - 1] == ' ')
    {
        input.erase(input.end() - 1);
    }
}