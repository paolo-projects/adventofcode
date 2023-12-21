export default class Vector {
    constructor(public i: number, public j: number) {
    }

    equals(other: Vector | null | undefined) {
        if(other == null) {
            return false;
        }
        return this.i === other.i && this.j === other.j;
    }

    difference(p: Vector) {
        return new Vector(this.i - p.i, this.j - p.j);
    }

    sum(p: Vector) {
        return new Vector(this.i + p.i, this.j + p.j);
    }
}