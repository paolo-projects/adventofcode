#include <iostream>
#include <fstream>
#include <vector>
#include <algorithm>

std::vector<int> maxes;

int main(int argc, char *argv[])
{
    using namespace std;

    int latest_max_calories = 0;
    int max_calories = 0;

    ifstream input_file("input.txt");
    string line;
    while (!getline(input_file, line).eof())
    {
        if (line.empty())
        {
            maxes.emplace_back(latest_max_calories);
            latest_max_calories = 0;
            continue;
        }

        latest_max_calories += stol(line);
    }

    maxes.emplace_back(latest_max_calories);

    sort(maxes.begin(), maxes.end(), greater<int>());

    int sum = 0;

    cout << "Max calories" << endl;
    for (int i = 0; i < 3; i++)
    {
        sum += maxes[i];
        cout << i + 1 << ": " << maxes[i] << endl;
    }

    cout << "Sum: " << sum << endl;
}