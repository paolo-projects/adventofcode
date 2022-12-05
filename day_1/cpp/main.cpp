#include <iostream>
#include <fstream>

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
            max_calories = max(latest_max_calories, max_calories);
            latest_max_calories = 0;
            continue;
        }

        latest_max_calories += stol(line);
    }

    max_calories = max(latest_max_calories, max_calories);

    cout << "Max calories: " << max_calories << endl;
}