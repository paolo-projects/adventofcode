#include <iostream>
#include <fstream>
#include <vector>
#include <string>
#include <list>
#include <limits>
#include <algorithm>
#include <thread>
#include <cstdio>

#include "GraphNode.h"
#include "Utils.h"
#include "Matrix.h"
#include "BitmapWriter.h"
#include "SDLWindow.h"

int runSimulation(Matrix<GraphNode> &space, SDLWindow &window, GraphNode *endNode);
int runSimulation2(Matrix<GraphNode> &space, SDLWindow &window);

bool canMove(const GraphNode &start, const GraphNode &end)
{
    auto verticalDistance = end.z - start.z;
    return verticalDistance <= 1;
}

bool canMove2(const GraphNode &start, const GraphNode &end)
{
    auto verticalDistance = end.z - start.z;
    return verticalDistance >= -1;
}

int main(int argc, char *argv[])
{
    std::ifstream input("input.txt");

    if (!input.good())
    {
        throw std::runtime_error("input.txt not found");
    }

    std::vector<std::string> lines;
    GraphNode *startNode = nullptr, *endNode = nullptr;
    int maxElevation = 0;

    std::cout << "Parsing input" << std::endl;

    std::string line;
    while (std::getline(input, line))
    {
        trim(line);
        if (line.length() > 0)
        {
            lines.push_back(line);
        }
    }

    std::cout << "Building graph" << std::endl;

    int width = lines[0].length();
    int height = lines.size();
    Matrix<GraphNode> space(width, height, GraphNode(0, 0));
    SDLWindow window(space);

    for (int y = 0; y < lines.size(); y++)
    {
        const auto &line = lines[y];
        for (int x = 0; x < line.size(); x++)
        {
            char c = line[x];
            switch (c)
            {
            case 'S':
                startNode = &space.set(x, y, GraphNode(0, std::numeric_limits<int>::max()));
                break;
            case 'E':
                endNode = &space.set(x, y, GraphNode(-1, std::numeric_limits<int>::max()));
                break;
            default:
                space.set(x, y, GraphNode(c - 'a', std::numeric_limits<int>::max()));
                maxElevation = std::max(maxElevation, c - 'a');
                break;
            }
        }
    }

    if (endNode == nullptr)
    {
        throw std::runtime_error("End node not found");
    }
    if (startNode == nullptr)
    {
        throw std::runtime_error("Start node not found");
    }

    endNode->z = maxElevation;

    BitmapWriter(space).write("field.bmp");

    std::cout << "Building connections" << std::endl;

    for (auto point : space)
    {
        if (point.y + 1 < height && canMove(point.value, space.get(point.x, point.y + 1)))
        {
            point.value.connect(ConnectionDirection::TOP, &space.get(point.x, point.y + 1));
        }
        if (point.y - 1 >= 0 && canMove(point.value, space.get(point.x, point.y - 1)))
        {
            point.value.connect(ConnectionDirection::BOTTOM, &space.get(point.x, point.y - 1));
        }
        if (point.x + 1 < width && canMove(point.value, space.get(point.x + 1, point.y)))
        {
            point.value.connect(ConnectionDirection::RIGHT, &space.get(point.x + 1, point.y));
        }
        if (point.x - 1 >= 0 && canMove(point.value, space.get(point.x - 1, point.y)))
        {
            point.value.connect(ConnectionDirection::LEFT, &space.get(point.x - 1, point.y));
        }
    }

    std::cout << "Press any character to start... " << std::endl;
    getchar();
    std::cout << std::endl;

    for (auto point : space)
    {
        point.value.distance = std::numeric_limits<int>::max();
        point.value.visited = false;
    }
    startNode->distance = 0;
    int endDistance = runSimulation(space, window, endNode);

    GraphNode *closestNode = nullptr;
    int closestDistance = std::numeric_limits<int>::max();
    for (auto point : space)
    {
        if (endDistance - point.value.distance < closestDistance)
        {
            closestDistance = endDistance - point.value.distance;
            closestNode = &point.value;
        }
    }

    if (closestNode == nullptr)
    {
        throw std::runtime_error("Couldn't find closest node");
    }

    for (auto point : space)
    {
        point.value.distance = std::numeric_limits<int>::max();
        point.value.visited = false;
        point.value.connections.clear();

        if (point.y + 1 < height && canMove2(point.value, space.get(point.x, point.y + 1)))
        {
            point.value.connect(ConnectionDirection::TOP, &space.get(point.x, point.y + 1));
        }
        if (point.y - 1 >= 0 && canMove2(point.value, space.get(point.x, point.y - 1)))
        {
            point.value.connect(ConnectionDirection::BOTTOM, &space.get(point.x, point.y - 1));
        }
        if (point.x + 1 < width && canMove2(point.value, space.get(point.x + 1, point.y)))
        {
            point.value.connect(ConnectionDirection::RIGHT, &space.get(point.x + 1, point.y));
        }
        if (point.x - 1 >= 0 && canMove2(point.value, space.get(point.x - 1, point.y)))
        {
            point.value.connect(ConnectionDirection::LEFT, &space.get(point.x - 1, point.y));
        }
    }
    endNode->distance = 0;
    runSimulation2(space, window);

    std::cout << "Press any character to close... " << std::endl;
    getchar();
    std::cout << std::endl;
}

int runSimulation(Matrix<GraphNode> &space, SDLWindow &window, GraphNode *endNode)
{
    std::list<GraphNode *> nodesBucket;
    std::transform(space.getData().begin(), space.getData().end(), std::back_inserter(nodesBucket), [](GraphNode &node)
                   { return &node; });

    std::cout << "Optimizing distance" << std::endl;

    while (!nodesBucket.empty())
    {
        GraphNode *currentNode = *std::min_element(nodesBucket.begin(), nodesBucket.end(), [](GraphNode *a, GraphNode *b)
                                                   { return a->distance < b->distance; });

        if (currentNode == endNode)
        {
            std::cout << "Found: " << currentNode->distance << std::endl;
            return currentNode->distance;
        }

        nodesBucket.remove(currentNode);

        int newDistance = currentNode->distance + 1;
        for (auto connection : currentNode->connections)
        {
            if (newDistance < connection.second->distance)
            {
                connection.second->distance = newDistance;
                connection.second->previous = currentNode;
            }
        }
        currentNode->visited = true;

        window.redraw();
    }
}

int runSimulation2(Matrix<GraphNode> &space, SDLWindow &window)
{
    std::list<GraphNode *> nodesBucket;
    std::transform(space.getData().begin(), space.getData().end(), std::back_inserter(nodesBucket), [](GraphNode &node)
                   { return &node; });

    std::cout << "Optimizing distance" << std::endl;
    GraphNode *currentNode = nullptr;

    while (!nodesBucket.empty())
    {
        currentNode = *std::min_element(nodesBucket.begin(), nodesBucket.end(), [](GraphNode *a, GraphNode *b)
                                        { return a->distance < b->distance; });

        if (currentNode->z == 0)
        {
            std::cout << "Found: " << currentNode->distance << std::endl;
            return currentNode->distance;
        }

        nodesBucket.remove(currentNode);

        int newDistance = currentNode->distance + 1;
        for (auto connection : currentNode->connections)
        {
            if (newDistance < connection.second->distance)
            {
                connection.second->distance = newDistance;
                connection.second->previous = currentNode;
            }
        }
        currentNode->visited = true;

        window.redraw();
    }
}