const { readFileSync } = require("fs");

const { Mapping, MappingRange } = require("./mapping");

const inputFile = readFileSync("input.txt", { encoding: "utf-8" });

let inputSeeds = [];
let inputSeedRanges = [];
const mappings = {};

const numberPattern = /\d+/g;
const numberRangesPattern = /(\d+) (\d+)/g;
const mappingPattern = /^([a-z]+)-to-([a-z]+) map:/;
const rangePattern = /^(\d+) (\d+) (\d+)/;

let mappingName = null;

for (const line of inputFile.split("\n")) {
  if (line.startsWith("seeds")) {
    inputSeeds = [...line.replace("seeds:", "").matchAll(numberPattern)].map(
      (v) => Number(v[0])
    );
    inputSeedRanges = [
      ...line.replace("seeds:", "").matchAll(numberRangesPattern),
    ].map((v) => [Number(v[1]), Number(v[2])]);
  } else if (mappingPattern.exec(line)) {
    const match = mappingPattern.exec(line);
    mappingName = match[1];
    mappings[mappingName] = new Mapping(match[1], match[2]);
  } else if (line.length && rangePattern.exec(line)) {
    const match = rangePattern.exec(line);

    mappings[mappingName].ranges.push(
      new MappingRange(Number(match[2]), Number(match[1]), Number(match[3]))
    );
  }
}

console.log(inputSeedRanges);

let outputLocations = [];

for (const seed of inputSeeds) {
  let destMapping = "seed";
  let mappingValue = seed;

  while (destMapping != "location") {
    const m = mappings[destMapping];
    mappingValue = m.map(mappingValue);
    destMapping = m.destinationName;
  }

  outputLocations.push(mappingValue);
}

console.log(`Min location is: ${Math.min(...outputLocations)}`);

let minLocation = Number.MAX_SAFE_INTEGER;
let rI = 1;

for (const range of inputSeedRanges) {
  console.log(`Progress: ${rI} / ${inputSeedRanges.length}`);
  for (let v = range[0]; v < range[0] + range[1]; v++) {
    let destMapping = "seed";
    let mappingValue = v;

    while (destMapping != "location") {
      const m = mappings[destMapping];
      mappingValue = m.map(mappingValue);
      destMapping = m.destinationName;
    }

    minLocation = Math.min(minLocation, v);
  }

  rI++;
}

console.log(`New min location with ranges is: ${minLocation}`);
