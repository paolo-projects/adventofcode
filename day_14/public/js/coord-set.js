class CoordSetIterator {
    subject;
    i;
    constructor(subject) {
      this.subject = Array.from(subject.values);
      this.i = 0;
    }
  
    next() {
      if (this.i < this.subject.length) {
        const key = this.subject[this.i++]
          .split(",")
          .map((v) => Number(v.trim()));
        return {
          done: false,
          value: key,
        };
      } else {
        return {
          done: true,
          value: undefined,
        };
      }
    }
  }

class CoordSet {
    values;
    constructor() {
      this.values = new Set();
    }
  
    set(key) {
      const index = key[0].toString() + "," + key[1].toString();
      this.values.add(index);
    }
  
    has(key) {
      const index = key[0].toString() + "," + key[1].toString();
      return this.values.has(index);
    }
  
    delete(key) {
      const index = key[0].toString() + "," + key[1].toString();
      this.values.delete(index);
    }
  
    [Symbol.iterator] = function () {
      return new CoordSetIterator(this);
    };
  }
