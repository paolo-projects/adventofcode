import json
from typing import Union
from enum import Enum, auto


class ComparisonResult(Enum):
  RIGHT = auto()
  WRONG = auto()
  SKIP = auto()


def main():
  with open("input.txt", "r") as input:
    lines = [l.strip() for l in input.readlines() if len(l.strip())]

  pairs = []
  for i in range(0, len(lines), 2):
    pairs.append([json.loads(lines[i]), json.loads(lines[i + 1])])

  indices = []
  for i in range(0, len(pairs)):
    p1, p2 = pairs[i]
    if check_elements(p1, p2) == ComparisonResult.RIGHT:
      indices.append(i + 1)

  sum = 0
  for i in indices:
    sum += i

  print(f"Indices: {indices}")
  print(f"Sum: {sum}")


def part2():
  with open("input.txt", "r") as input:
    packets = [json.loads(l.strip()) for l in input.readlines() if len(l.strip())]
    packets.append([[2]])
    packets.append([[6]])
    my_sort(packets)

    indices = []

    for i in range(len(packets)):
      if packets[i] == [[2]] or packets[i] == [[6]]:
        indices.append(i+1)

    print(indices)
    print(indices[0] * indices[1])


def my_sort(entries: list):
  # a simple bubble sort
  has_reordered = True
  while has_reordered:
    has_reordered = False
    for i in range(len(entries) - 1):
      if check_elements(entries[i], entries[i + 1]) == ComparisonResult.WRONG:
        tmp = entries[i]
        entries[i] = entries[i + 1]
        entries[i + 1] = tmp
        has_reordered = True


def check_elements(elem1: Union[int, list], elem2: Union[int, list]) -> ComparisonResult:
  if type(elem1) is int and type(elem2) is int:
    if elem1 < elem2:
      return ComparisonResult.RIGHT
    elif elem1 > elem2:
      return ComparisonResult.WRONG
    else:
      return ComparisonResult.SKIP
  else:
    elem1 = [elem1] if type(elem1) is int else elem1
    elem2 = [elem2] if type(elem2) is int else elem2

    for i in range(min(len(elem1), len(elem2))):
      res = check_elements(elem1[i], elem2[i])
      if res != ComparisonResult.SKIP:
        return res

    if len(elem1) < len(elem2):
      return ComparisonResult.RIGHT
    elif len(elem1) > len(elem2):
      return ComparisonResult.WRONG

    return ComparisonResult.SKIP


if __name__ == "__main__":
  main()
  part2()
