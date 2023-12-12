export type MatrixContructionCallback = (row: number, column: number) => number;

export class Matrix {
  values: number[];
  constructor(
    private rows: number,
    private columns: number,
    def: number | MatrixContructionCallback
  ) {
    if (typeof def === "function") {
      this.values = Array.from({ length: rows * columns }, (_, i) =>
        (def as MatrixContructionCallback)(Math.floor(i / columns), i % columns)
      );
    } else {
      this.values = Array.from({ length: rows * columns }, () => def);
    }
  }

  copy(): Matrix {
    return new Matrix(this.rows, this.columns, (row, column) =>
      this.get(row, column)
    );
  }

  set(row: number, column: number, value: number) {
    this.values[row * this.columns + column] = value;
  }

  get(row: number, column: number): number {
    return this.values[row * this.columns + column];
  }

  getRows(): number {
    return this.rows;
  }

  getColumns(): number {
    return this.columns;
  }

  addConstant(constant: number): Matrix {
    const result = this.copy();
    result.values = result.values.map((v) => v + constant);
    return result;
  }

  add(other: Matrix): Matrix {
    if (other.rows != this.rows || other.columns != this.columns) {
      throw new Error("Can't add matrices of different size");
    }

    const result = this.copy();
    result.values = result.values.map((v, i) => v + other.values[i]);
    return result;
  }

  multiply(other: Matrix): Matrix {
    if (this.columns != other.rows) {
      throw new Error(
        "Can't multiply matrices where M1 columns don't match M2 rows"
      );
    }
    return new Matrix(this.rows, other.columns, (row, column) => {
      let result = 0;
      for (let k = 0; k < this.columns; k++) {
        result += this.get(row, k) * other.get(k, column);
      }
      return result;
    });
  }

  determinant(): number {
    if (this.rows != this.columns) {
      throw Error("Can't get the determinant of a NON-square matrix");
    }

    if (this.rows == 2) {
      return this.get(0, 0) * this.get(1, 1) - this.get(0, 1) * this.get(1, 0);
    } else {
      let result = 0;
      for (let c = 0; c < this.columns; c++) {
        result += this.get(0, c) * this.subMatrix(0, c).determinant();
      }
      return result;
    }
  }

  subMatrix(excludeRow: number, excludeColumn: number): Matrix {
    const result = new Matrix(
      this.rows - 1,
      this.columns - 1,
      (row, column) => 0
    );
    for (let r = 0; r < this.rows; r++) {
      for (let c = 0; c < this.columns; c++) {
        const row = r < excludeRow ? r : r - 1;
        const column = c < excludeColumn ? c : c - 1;
        if (r != excludeRow && c != excludeColumn) {
          result.set(row, column, this.get(r, c));
        }
      }
    }
    return result;
  }

  static vectorMatrix(values: number[]): Matrix {
    return new Matrix(values.length, 1, (row, column) => {
      return values[row];
    });
  }

  static diagonal(size: number, value: number): Matrix {
    return new Matrix(size, size, (row, column) =>
      row === column ? value : 0
    );
  }

  static unitary(size: number): Matrix {
    return Matrix.diagonal(size, 1);
  }

  static translation(coords: [number, number]): Matrix {
    return new Matrix(3, 3, (row, column) => {
      if (row == column) {
        return 1;
      } else if (column == 2) {
        return coords[row];
      } else return 0;
    });
  }

  static scaling(amount: [number, number]): Matrix {
    return new Matrix(3, 3, (row, column) => {
      if (row == column) {
        if (row < 2) {
          return amount[row];
        } else {
          return 1;
        }
      }
      return 0;
    });
  }
}
