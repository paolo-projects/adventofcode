class CoordSetIterator<T> implements Iterator<[T, T]> {
  subject: string[];
  i: number;
  constructor(subject: CoordSet<T>) {
    this.subject = Array.from(subject.values);
    this.i = 0;
  }

  next(): IteratorResult<[T, T], any> {
    if (this.i < this.subject.length) {
      const key = this.subject[this.i++]
        .split(",")
        .map((v) => Number(v.trim())) as [T, T];
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

export default class CoordSet<T> implements Iterable<[T, T]> {
  values: Set<string>;
  constructor() {
    this.values = new Set<string>();
  }

  set(key: [T, T]) {
    const index = key[0].toString() + "," + key[1].toString();
    this.values.add(index);
  }

  has(key: [T, T]): boolean {
    const index = key[0].toString() + "," + key[1].toString();
    return this.values.has(index);
  }

  delete(key: [T, T]) {
    const index = key[0].toString() + "," + key[1].toString();
    this.values.delete(index);
  }

  [Symbol.iterator] = function (): Iterator<[T, T]> {
    return new CoordSetIterator<T>(this);
  };
}
