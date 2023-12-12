const fs = require("fs");

Object.prototype.tap = function (callback) {
  callback(this);
  return this;
};

console.log("Calories MAX");
console.log(
  fs
    .readFileSync("input.txt", { encoding: "utf-8" })
    .split("\n")
    .reduce(
      (acc, line) => {
        if (!line.length) {
          acc.push([]);
        } else {
          acc[acc.length - 1].push(parseInt(line));
        }
        return acc;
      },
      [[]]
    )
    .map(
      (bucket) =>
        bucket.length ? bucket.reduce((sum, entry) => sum + entry) : 0,
      0
    )
    .sort((a, b) => b - a)
    .filter((_, i) => i < 3)
    .tap((maxes) =>
      console.log(
        "SUM",
        maxes.reduce((sum, value) => sum + value, 0)
      )
    )
    .reduce((text, entry, i) => text + `${i + 1}: ${entry}\n`, "")
);
