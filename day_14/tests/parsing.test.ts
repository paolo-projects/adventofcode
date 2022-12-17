import { Space } from "../src/math/space";

describe("test parsing", () => {
  test("sample line", () => {
    const line = "0,0 -> 50,0 -> 50,50";
    const s = Space.parseInput(line, [0, 0], () => {});
    expect(s.wallCells.has([0, 0])).toBe(true);
    expect(s.wallCells.has([20, 0])).toBe(true);
    expect(s.wallCells.has([45, 0])).toBe(true);
    expect(s.wallCells.has([50, 0])).toBe(true);
    expect(s.wallCells.has([50, 20])).toBe(true);
    expect(s.wallCells.has([50, 25])).toBe(true);
    expect(s.wallCells.has([50, 40])).toBe(true);
    expect(s.wallCells.has([50, 50])).toBe(true);
    expect(s.wallCells.has([50, 60])).toBe(false);
    expect(s.wallCells.has([-10, 0])).toBe(false);
  });
  test("test input", () => {
    const input = `498,4 -> 498,6 -> 496,6
503,4 -> 502,4 -> 502,9 -> 494,9`;
    const s = Space.parseInput(input, [0, 0], () => {});
    expect(s.wallCells.has([494, 9])).toBe(true);
    expect(s.wallCells.has([495, 9])).toBe(true);
    expect(s.wallCells.has([496, 9])).toBe(true);
    expect(s.wallCells.has([502, 8])).toBe(true);
    expect(s.wallCells.has([502, 7])).toBe(true);
    expect(s.wallCells.has([502, 6])).toBe(true);
    expect(s.wallCells.has([502, 4])).toBe(true);
    expect(s.wallCells.has([503, 4])).toBe(true);
    expect(s.wallCells.has([496, 6])).toBe(true);
    expect(s.wallCells.has([497, 6])).toBe(true);
    expect(s.wallCells.has([498, 6])).toBe(true);
    expect(s.wallCells.has([498, 5])).toBe(true);
    expect(s.wallCells.has([498, 4])).toBe(true);

    expect(s.wallCells.has([494, 8])).toBe(false);
    expect(s.wallCells.has([495, 8])).toBe(false);
    expect(s.wallCells.has([496, 5])).toBe(false);
    expect(s.wallCells.has([497, 5])).toBe(false);
    expect(s.wallCells.has([501, 7])).toBe(false);
    expect(s.wallCells.has([501, 6])).toBe(false);
    expect(s.wallCells.has([501, 5])).toBe(false);
  });
});
