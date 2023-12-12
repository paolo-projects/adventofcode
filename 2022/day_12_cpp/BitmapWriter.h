#pragma once
#include <string>
#include <fstream>
#include <algorithm>
#include "Matrix.h"
#include "GraphNode.h"
#include "EasyBMP.h"

class BitmapWriter
{
public:
    BitmapWriter() = delete;
    BitmapWriter(const BitmapWriter &) = delete;
    BitmapWriter(Matrix<GraphNode> &m) : matrix(m) {}
    void write(const std::string &outFile)
    {
        int minPoint = 0;
        int maxPoint = std::max_element(matrix.getData().begin(), matrix.getData().end(), [](const GraphNode &a, const GraphNode &b)
                                        { return a.z < b.z; })
                           ->z;
        double delta = maxPoint - minPoint;

        BMP outImage;
        outImage.SetSize(matrix.width, matrix.height);
        outImage.SetBitDepth(8);
        CreateGrayscaleColorTable(outImage);

        for (auto point : matrix)
        {
            unsigned char val = std::round((point.value.z - minPoint) / delta * 255);
            outImage(point.x, point.y)->Red = outImage(point.x, point.y)->Green = outImage(point.x, point.y)->Blue = val;
            outImage(point.x, point.y)->Alpha = 255;
        }

        outImage.WriteToFile(outFile.c_str());
    }

private:
    Matrix<GraphNode> &matrix;
};