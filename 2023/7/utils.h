#pragma once

#include <map>
#include <algorithm>
#include <optional>

template <typename T, typename U>
inline bool doesMapContain(const std::map<T, U> &container, const U &value)
{
    return std::any_of(
        container.begin(), container.end(), [&value](const std::pair<T, U> &entry)
        { return entry.second == value; });
}

template <typename T, typename U>
inline std::_Rb_tree_const_iterator<std::pair<const T, U>> findInMap(const std::map<T, U> &container, const U &value)
{
    return std::find_if(
        container.begin(), container.end(), [&value](const std::pair<T, U> &entry)
        { return entry.second == value; });
}

template <typename T, typename U>
inline size_t countMapValueOccurrences(const std::map<T, U> &container, const U &value)
{
    return std::count_if(container.begin(), container.end(), [&value](const std::pair<T, U> &entry)
                         { return entry.second == value; });
}