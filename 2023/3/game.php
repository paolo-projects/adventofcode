<?php

require_once "./coords.php";

/**
 * Very inefficient
 * @template T
 */
class Set
{
    public $elements = [];

    /**
     * @param T $element
     */
    public function add($element)
    {
        foreach ($this->elements as $el) {
            if ($el == $element) {
                return;
            }
        }

        array_push($this->elements, $element);
    }

    public function remove($element)
    {
        for ($i = 0; $i < sizeof($this->elements); $i++) {
            if ($this->elements[$i] == $element) {
                array_splice($this->elements, $i, 1);
                break;
            }
        }
    }

    public function contains($element): bool
    {
        for ($i = 0; $i < sizeof($this->elements); $i++) {
            if ($this->elements[$i] == $element) {
                return true;
            }
        }
        return false;
    }
}

class NumberEntry
{
    public int $number;
    public Span $span;

    public function __construct(int $number, Span $span)
    {
        $this->number = $number;
        $this->span = $span;
    }
}

class SymbolEntry
{
    public string $symbol;
    public Point $coords;
    public Set $numbers;

    public function __construct(string $symbol, Point $coords)
    {
        $this->symbol = $symbol;
        $this->coords = $coords;
        $this->numbers = new Set();
    }
}
