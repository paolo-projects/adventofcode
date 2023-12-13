<?php

require_once "./game.php";
require_once "./coords.php";

function is_symbol($char)
{
    return preg_match('/[\*\@\=\%\+\$\&\/\-\#]/', $char);
}

$input = file_get_contents("input.txt");


$lines = preg_split("/\r\n|\n|\r/", $input);
$number_entries = new Set();
$symbols = [];

function extract_number($lines, $i, $j): NumberEntry | null
{
    if (is_numeric($lines[$i][$j])) {
        $start = $j;
        while ($start > 0 && is_numeric($lines[$i][$start - 1])) {
            $start--;
        }
        $end = $j;
        while ($end < strlen($lines[$i]) - 1 && is_numeric($lines[$i][$end + 1])) {
            $end++;
        }

        return new NumberEntry(
            intval(substr($lines[$i], $start, $end - $start + 1)),
            new Span(
                new Point($i, $start),
                new Point($i, $end)
            )
        );
    }

    return null;
}

for ($i = 0; $i < sizeof($lines); $i++) {
    $line = $lines[$i];

    for ($j = 0; $j < strlen($line); $j++) {
        if (is_symbol($line[$j])) {
            $symbol = new SymbolEntry($line[$j], new Point($i, $j));

            for ($r = max(0, $i - 1); $r <= min(sizeof($lines) - 1, $i + 1); $r++) {
                for ($s = max(0, $j - 1); $s <= min(strlen($lines[$i]) - 1, $j + 1); $s++) {
                    if ($r != $i || $s != $j) {
                        $number = extract_number($lines, $r, $s);
                        if ($number != null) {
                            $number_entries->add($number);
                            $symbol->numbers->add($number);
                        }
                    }
                }
            }

            array_push($symbols, $symbol);
        }
    }
}

echo "The result is: " . array_sum(
    array_map(function (NumberEntry $num) {
        return $num->number;
    }, $number_entries->elements)
) . "\n";

$ratios = 0;

foreach ($symbols as $symbol) {
    if ($symbol->symbol == "*" && sizeof($symbol->numbers->elements) == 2) {
        $ratio = $symbol->numbers->elements[0]->number * $symbol->numbers->elements[1]->number;
        $ratios += $ratio;
    }
}

echo "Ratios sum: " . $ratios . "\n";
