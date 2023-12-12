class CoordMapIterator<E, T> implements Iterator<[E, E]> {
  subject: CoordMap<E, T>;
  i: number;
  constructor(subject: CoordMap<E, T>) {
    this.subject = subject;
    this.i = 0;
  }

  next(): IteratorResult<[E, E], any> {
    if (this.i < this.subject.values.length) {
      const key = Object.keys(this.subject.values)
        [this.i].split(",")
        .map((v) => Number(v)) as [E, E];
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

export default class CoordMap<E, T> implements Iterable<[E, E]> {
  values: { [key: string]: T | undefined };
  constructor() {
    this.values = {};
  }

  set(key: [E, E], value: T) {
    const index = key[0].toString() + "," + key[1].toString();
    this.values[index] = value;
  }

  get(key: [E, E]): T | undefined {
    const index = key[0].toString() + "," + key[1].toString();
    return this.values[index];
  }

  has(key: [E, E]): boolean {
    const index = key[0].toString() + "," + key[1].toString();
    return index in this.values;
  }

  delete(key: [E, E]) {
    const index = key[0].toString() + "," + key[1].toString();
    delete this.values[index];
  }

  [Symbol.iterator] = function (): Iterator<[E, E]> {
    return new CoordMapIterator<E, T>(this);
  };
}
