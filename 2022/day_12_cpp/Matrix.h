#pragma once

#include <vector>

template <typename T>
class Matrix
{
public:
    struct MatrixPoint
    {
        int x;
        int y;
        T &value;
    };
    struct Iterator
    {
        using iterator_category = std::forward_iterator_tag;
        using difference_type = std::ptrdiff_t;
        using value_type = MatrixPoint;

        Iterator(T *ptr = nullptr, int width = 0) : _ptr(ptr), width(width) {}

        MatrixPoint operator*()
        {
            return MatrixPoint{x, y, *_ptr};
        }
        T *operator->()
        {
            return _ptr;
        }
        Iterator &operator++()
        {
            if (++x >= width)
            {
                x = 0;
                y++;
            }
            _ptr++;
            return *this;
        }
        Iterator operator++(int)
        {
            Iterator tmp = *this;
            ++(*this);
            return tmp;
        }
        friend bool operator==(const Iterator &a, const Iterator &b)
        {
            return a._ptr == b._ptr;
        }
        friend bool operator!=(const Iterator &a, const Iterator &b)
        {
            return a._ptr != b._ptr;
        }

    private:
        int x = 0, y = 0, width;
        T *_ptr;
    };

public:
    Matrix(int width, int height, const T &defaultValue) : width(width), height(height)
    {
        data.resize(width * height, defaultValue);
    }

    T &set(int x, int y, const T &value)
    {
        data[y * width + x] = value;
        return data[y * width + x];
    }

    T &set(int x, int y, T &&value)
    {
        data[y * width + x] = value;
        return data[y * width + x];
    }

    T &get(int x, int y)
    {
        return data[y * width + x];
    }

    std::vector<T> &getData()
    {
        return data;
    }

    const std::vector<T> &getData() const
    {
        return data;
    }

    Iterator begin()
    {
        return Iterator(&data[0], width);
    }
    Iterator end()
    {
        return Iterator(&data[0] + data.size(), width);
    }

    int width, height;

private:
    std::vector<T> data;
};