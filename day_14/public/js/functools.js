Array.prototype.zipWithNext = function() {
    const result = [];
    for (let i = 0; i < this.length - 1; i++) {
      result.push([this[i], this[i + 1]]);
    }
    return result;
  };