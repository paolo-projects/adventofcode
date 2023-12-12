<?php

function is_symbol($char)
{
    return preg_match('/[\'^£$%&*()}{@#~?><>,|=_+¬-]/', $char);
}

$input = file_get_contents("input_test.txt");
$lines = preg_split("/\r\n|\n|\r/", $input);
$touched = array_map(function ($el) {
    return array_map(function ($_) {
        return false;
    }, str_split($el));
}, $lines);

function extract_number($lines, $i, $j, $touched): int | null
{
    if (is_numeric($lines[$i][$j]) && !$touched[$i][$j]) {
        $start = $j;
        while ($start > 0 && is_numeric($lines[$i][$start - 1])) {
            $start--;
        }
        $end = $j;
        while ($end < strlen($lines[$i]) - 1 && is_numeric($lines[$i][$end + 1])) {
            $end++;
        }

        for ($n = $start; $n <= $end; $n++) {
            $touched[$i][$n] = true;
        }

        return intval(substr($lines[$i], $start, $end - $start + 1));
    }

    return null;
}

$numbers = [];

for ($i = 0; $i < sizeof($lines); $i++) {
    $line = $lines[$i];

    for ($j = 0; $j < strlen($line); $j++) {
        if (is_symbol($line[$j])) {
            for ($r = max(0, $i - 1); $r <= min(sizeof($lines) - 1, $i + 1); $r++) {
                for ($s = max(0, $j - 1); $s <= min(strlen($lines[$i]) - 1, $j + 1); $s++) {
                    if ($r != $i || $s != $j) {
                        $number = extract_number($lines, $r, $s, $touched);
                        if ($number != null) {
                            array_push($numbers, $number);
                        }
                    }
                }
            }
        }
    }
}

print_r($numbers);

echo "The result is: " . array_sum($numbers) . "\n";
