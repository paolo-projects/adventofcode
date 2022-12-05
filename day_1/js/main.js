const fs = require("fs");

console.log(
  "Calories MAX",
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
    .reduce((max, entry) => Math.max(max, entry), 0)
);
