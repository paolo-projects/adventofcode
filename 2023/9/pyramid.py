from typing import List

class Pyramid:
    _sequence: List[int]
    _tree: List[List[int]]

    def __init__(self, sequence: List[int]):
        self._sequence = sequence.copy()
        self._tree = [self._sequence]

        while not all(n == 0 for n in self._tree[-1]):
            tree_row = []
            for i in range(1, len(self._tree[-1])):
                tree_row.append(self._tree[-1][i] - self._tree[-1][i-1])
            self._tree.append(tree_row)

    def extrapolate(self) -> int:
        n = 0
        for i in reversed(range(0, len(self._tree)-1)):
            n += self._tree[i][-1]

        return n

    def extrapolate_left(self) -> int:
        n = 0
        for i in reversed(range(0, len(self._tree)-1)):
            n = self._tree[i][0] - n

        return n

    