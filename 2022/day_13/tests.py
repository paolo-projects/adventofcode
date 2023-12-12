from main import check_elements, ComparisonResult


def test_check():
  assert check_elements(1, 2) == ComparisonResult.RIGHT
  assert check_elements(2, 2) == ComparisonResult.SKIP
  assert check_elements(3, 2) == ComparisonResult.WRONG
  assert check_elements([[1], [2, 3, 4]], [[1], 4]) == ComparisonResult.RIGHT
  assert check_elements([[[]]], [[]]) == ComparisonResult.WRONG
  assert check_elements([[]], [[[]]]) == ComparisonResult.RIGHT
  assert check_elements([[4, 4], 4, 4], [[4, 4], 4, 4, 4]) == ComparisonResult.RIGHT
  assert check_elements([7, 7, 7, 7], [7, 7, 7]) == ComparisonResult.WRONG
  assert check_elements([1, [2, [3, [4, [5, 6, 7]]]], 8, 9],
                        [1, [2, [3, [4, [5, 6, 0]]]], 8, 9]) == ComparisonResult.WRONG
  assert check_elements([9], [[8, 7, 6]]) == ComparisonResult.WRONG
  assert check_elements([1, 1, 3, 1, 1], [1, 1, 5, 1, 1]) == ComparisonResult.RIGHT
  assert check_elements([], [3]) == ComparisonResult.RIGHT
