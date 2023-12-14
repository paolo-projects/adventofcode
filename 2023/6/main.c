#define _GNU_SOURCE
#include <stdio.h>
#include <stdlib.h>
#include <errno.h>
#include <string.h>
#include <limits.h>
#include <math.h>

int read_numbers_sequence(char *input, long *out_array, size_t out_array_len)
{
    char *start = input;
    char *eon;
    long value;
    errno = 0;
    size_t out_index = 0;

    while ((value = strtol(start, &eon, 0)),
           out_index < out_array_len && eon != start &&
               !((errno == EINVAL && value == 0) ||
                 (errno == ERANGE && (value == LONG_MIN || value == LONG_MAX))))
    {
        out_array[out_index++] = value;
        start = eon;
        errno = 0;
    }

    return out_index;
}

long long get_chances(long T, long d)
{
    /*
        d = Ti * (T - Ti)

        - d = distance traveled
        - Ti = charge time
        - T = total time

        0 = - Ti^2 + T*Ti - d

        Ti = T +- sqrt( T^2 - 4 * d )
             ------------------------
                        2

        Ti0 < t < Ti1               t C- N
    */

    double ti0 = (T + sqrt(T * T - 4 * d)) / 2.0;
    double ti1 = (T - sqrt(T * T - 4 * d)) / 2.0;

    long long count = floor(ti0) - ceil(ti1) + 1;
    long long to_subtract = 0;

    if (ti0 == (long long)ti0)
    {
        to_subtract++;
    }

    if (ti1 == (long long)ti1)
    {
        to_subtract++;
    }

    count -= to_subtract;

    return count;
}

int main(void)
{
    FILE *input = fopen("input.txt", "r");

    if (input == NULL)
    {
        fprintf(stderr, "Error opening input file\n");
        return 1;
    }

    char *line = NULL;
    size_t line_l;

    if (getline(&line, &line_l, input) == -1)
    {
        fprintf(stderr, "Error reading first line: %s\n", strerror(errno));
        fclose(input);
        return 1;
    }

    if (strncmp(line, "Time:", 5) != 0)
    {
        fprintf(stderr, "First line is not a time");
        fclose(input);
        return 1;
    }

    long time_sequence[1024];
    size_t time_sequence_len = read_numbers_sequence(&line[5], time_sequence, 1024);

    free(line);
    line = NULL;

    if (getline(&line, &line_l, input) == -1)
    {
        fprintf(stderr, "Error reading second line: %s\n", strerror(errno));
        fclose(input);
        return 1;
    }

    if (strncmp(line, "Distance:", 9) != 0)
    {
        fprintf(stderr, "Second line is not a distance");
        fclose(input);
        return 1;
    }

    fclose(input);

    long distance_sequence[1024];
    size_t distance_sequence_len = read_numbers_sequence(&line[9], distance_sequence, 1024);

    if (time_sequence_len == 0 || distance_sequence_len == 0)
    {
        fprintf(stderr, "One of the sequences is empty!\n");
        return 1;
    }

    if (time_sequence_len != distance_sequence_len)
    {
        fprintf(stderr, "The sequences have different length!\n");
        return 1;
    }

    printf("Data:\n\n");
    for (int i = 0; i < distance_sequence_len; i++)
    {
        printf("Time %d, distance %d\n", time_sequence[i], distance_sequence[i]);
    }
    printf("\n");

    long *result = NULL;

    for (int i = 0; i < distance_sequence_len; i++)
    {
        int T = time_sequence[i];
        int d = distance_sequence[i];

        long count = get_chances(T, d);

        if (result == NULL)
        {
            result = malloc(sizeof(long));
            *result = count;
        }
        else
        {
            *result *= count;
        }
    }

    printf("Result is %ld\n", *result);
    free(result);

    char *one_time[1024];
    memset(one_time, 0, 1024);

    char *one_distance[1024];
    memset(one_distance, 0, 1024);

    for (int i = 0; i < distance_sequence_len; i++)
    {
        sprintf(one_time, "%s%d", one_time, time_sequence[i]);
        sprintf(one_distance, "%s%d", one_distance, distance_sequence[i]);
    }

    long one_time_l = strtol(one_time, NULL, 10);
    long one_distance_l = strtol(one_distance, NULL, 10);

    long long chances = get_chances(one_time_l, one_distance_l);

    printf("Part 2 chances are: %lld\n", chances);

    return 0;
}