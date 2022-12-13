#pragma once

#include <map>

enum ConnectionDirection
{
    TOP,
    RIGHT,
    BOTTOM,
    LEFT
};

struct GraphNode
{
    GraphNode(int z, int distance) : z(z), distance(distance) {}
    int z, distance;
    GraphNode *previous = nullptr;
    std::map<ConnectionDirection, GraphNode *> connections;
    bool visited = false;

    bool operator==(const GraphNode &rhs) const
    {
        return z == rhs.z && distance == rhs.distance;
    }

    void connect(ConnectionDirection direction, GraphNode *target)
    {
        connections[direction] = target;
    }
};