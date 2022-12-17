Array.prototype.zipWithNext = function <T>(this: T[]): Array<[T, T]> {
  const result: Array<[T, T]> = [];
  for (let i = 0; i < this.length - 1; i++) {
    result.push([this[i], this[i + 1]]);
  }
  return result;
};
