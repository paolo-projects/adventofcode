class MappingRange {
  sourceStart;
  destinationStart;
  rangeLen;

  constructor(sourceStart, destinationStart, length) {
    this.sourceStart = sourceStart;
    this.destinationStart = destinationStart;
    this.rangeLen = length;
  }

  isContained(source) {
    return (
      source >= this.sourceStart && source < this.sourceStart + this.rangeLen
    );
  }

  map(source) {
    return this.destinationStart + (source - this.sourceStart);
  }
}

class Mapping {
  sourceName;
  destinationName;
  ranges;

  constructor(sourceName, destinationName) {
    this.sourceName = sourceName;
    this.destinationName = destinationName;
    this.ranges = [];
  }

  map(source) {
    for (const range of this.ranges) {
      if (range.isContained(source)) {
        return range.map(source);
      }
    }

    return source;
  }
}

module.exports = {
  Mapping,
  MappingRange,
};
