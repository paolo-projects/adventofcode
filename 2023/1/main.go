package main

import (
	"fmt"
	"os"
	"strconv"
	"strings"
	"unicode"
)

func comparator(keys map[string]int, to_check string, from_end bool) (int, bool) {
	for k, v := range keys {
		if len(to_check) < len(k) {
			continue
		}

		are_equal := true
		for i := 0; i < len(k); i++ {
			c := to_check[i]
			if from_end {
				c = to_check[len(to_check)-len(k)+i]
			}
			if c != k[i] {
				are_equal = false
				break
			}
		}

		if are_equal {
			return v, true
		}
	}

	return 0, false
}

func parse_nums(lines []string, letter_nums *map[string]int) []string {
	numbers := make([]string, 0)

	for _, line := range lines {
		line_number := ""
		temp := ""

		for _, c := range line {
			if unicode.IsNumber(c) {
				line_number += string(c)
				break
			} else {
				temp += string(c)
			}

			if letter_nums != nil {
				textual_num, ok := comparator(*letter_nums, temp, true)

				if ok {
					line_number += strconv.Itoa(textual_num)
					break
				}
			}
		}

		temp = ""

		for i := range line {
			c := rune(line[len(line)-1-i])
			if unicode.IsNumber(c) {
				line_number += string(c)
				break
			} else {
				temp = string(c) + temp
			}

			if letter_nums != nil {
				textual_num, ok := comparator(*letter_nums, temp, false)

				if ok {
					line_number += strconv.Itoa(textual_num)
					break
				}
			}
		}

		numbers = append(numbers, line_number)
	}

	return numbers
}

func get_sum(numbers []string) int {
	sum := 0

	for _, num := range numbers {
		n, err := strconv.Atoi(num)
		if err == nil {
			sum += n
		}
	}

	return sum
}

func main() {
	NUMS := map[string]int{
		"one":   1,
		"two":   2,
		"three": 3,
		"four":  4,
		"five":  5,
		"six":   6,
		"seven": 7,
		"eight": 8,
		"nine":  9,
	}

	input_b, err := os.ReadFile("input.txt")
	if err != nil {
		panic("Error reading input.txt")
	}
	input := string(input_b)

	lines := strings.Split(input, "\n")
	numbers_no_text := get_sum(parse_nums(lines, nil))
	numbers_with_text := get_sum(parse_nums(lines, &NUMS))

	fmt.Printf("Only digits: the number is %d\n", numbers_no_text)
	fmt.Printf("With letters: the number is %d\n", numbers_with_text)
}
