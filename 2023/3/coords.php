<?php


class Point
{
    public int $i;
    public int $j;

    public function __construct(int $i, int $j)
    {
        $this->i = $i;
        $this->j = $j;
    }
}

class Span
{
    public Point $start;
    public Point $end;

    public function __construct(Point $start, Point $end)
    {
        $this->start = $start;
        $this->end = $end;
    }
}
