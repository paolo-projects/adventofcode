import { dir } from "console";
import Vector from "./point";

enum CellType {
    V,
    H,
    NE,
    NW,
    SW,
    SE,
    G,
    START
};

enum CheckResult {
    WALL,
    LOOP
}

export default class GameMap {
    private matrix: CellType[][];
    private startPoint: Vector;

    constructor(input: string) {
        this.matrix = [];
        this.startPoint = new Vector(0, 0);

        let i = 0;
        for(const line of input.split(/\r?\n/)) {
            const cells = [];

            let j = 0;
            for(const char of line) {
                let v: CellType | null = null;

                switch(char) {
                    case '|':
                        v = CellType.V;
                        break;
                    case '-':
                        v = CellType.H;
                        break;
                    case 'L':
                        v = CellType.NE;
                        break;
                    case 'J':
                        v = CellType.NW;
                        break;
                    case '7':
                        v = CellType.SW;
                        break;
                    case 'F':
                        v = CellType.SE;
                        break;
                    case '.':
                        v = CellType.G;
                        break;
                    case 'S':
                        v = CellType.START;
                        this.startPoint = new Vector(i, j);
                        break;
                    default:
                        console.log(`Unrecognized character: ${char}`);
                        break;
                }

                if(v != null) {
                    cells.push(v);
                }

                j++;
            }

            this.matrix.push(cells);
            i++;
        }
    }

    getMatrix() {
        return this.matrix;
    }

    getStartPoint() {
        return this.startPoint;
    }

    canProceed(start: Vector, end: Vector) {
        const startV = this.matrix[start.i][start.j];
        const endV = this.matrix[end.i][end.j];

        if(endV == CellType.G) {
            return false;
        }

        const direction = end.difference(start);
        if(direction.i === -1) {
            // nord
            return [CellType.V, CellType.NE, CellType.NW, CellType.START].includes(startV) && [CellType.V, CellType.SE, CellType.SW, CellType.START].includes(endV);
        } else if (direction.j === 1) {
            // east
            return [CellType.H, CellType.NE, CellType.SE, CellType.START].includes(startV) && [CellType.H, CellType.NW, CellType.SW, CellType.START].includes(endV);
        } else if (direction.i === 1) {
            // south
            return [CellType.V, CellType.SE, CellType.SW, CellType.START].includes(startV) && [CellType.V, CellType.NE, CellType.NW, CellType.START].includes(endV);
        } else if (direction.j === -1) {
            // west
            return [CellType.H, CellType.NW, CellType.SW, CellType.START].includes(startV) && [CellType.H, CellType.NE, CellType.SE, CellType.START].includes(endV);
        }

        return false;
    }

    getFarthest() {
        let previousPosition: Vector | null = null;
        let currentPosition = this.startPoint;
        let started = false;

        let iterations = 0;
        let loop: Vector[] = [];

        while(!started || !currentPosition.equals(this.startPoint)) {
            loop.push(currentPosition);

            const directions = [
                currentPosition.sum(new Vector(-1, 0)), // top
                currentPosition.sum(new Vector(0, 1)), // right
                currentPosition.sum(new Vector(1, 0)), // bottom
                currentPosition.sum(new Vector(0, -1))  // left
            ].filter(p => p.i >= 0 && p.i < this.matrix.length && p.j >= 0 && p.j < this.matrix[0].length) // check map bounds
            .filter(p => !p.equals(previousPosition)) // don't go backwards
            .filter(p => this.canProceed(currentPosition, p)); // check pipe linkage

            previousPosition = currentPosition;
            currentPosition = directions[0];

            started = true;
            iterations++;
        }

        console.log(`Farthest: ${iterations/2}`);
        
        console.log(`Inner cells: ${this.findInsideLoop(loop).length}`);
    }

    findInsideLoop(loop: Vector[]) {
        let cellsToCheck = Array.from({
            length: this.matrix.length * this.matrix[0].length
        }, (_, i) => new Vector(Math.floor(i / this.matrix[0].length), i % this.matrix[0].length))
            .filter(v => loop.some(l => l.equals(v))); // Remove loop cells

        const insideCells = [];

        while(cellsToCheck.length) {
            console.log(`missing ${cellsToCheck.length}`);
            let toRemove = false;
            const runCells: Vector[] = [];

            const cellsBag = cellsToCheck.map((c, i) => ({
                cell: c,
                distance: i === 0 ? 0 : Number.POSITIVE_INFINITY
            }));

            while(cellsBag.length) {
                const [cell, index] = cellsBag.reduce((acc, v, i) => v.distance < acc[0].distance ? [v, i] : acc, [cellsBag[0], 0]);

                if(cell.distance === Number.POSITIVE_INFINITY) {
                    break;
                }

                cellsBag.splice(index);
                runCells.push(cell.cell);

                const directions = [
                    cell.cell.sum(new Vector(-1, 0)), // top
                    cell.cell.sum(new Vector(0, 1)), // right
                    cell.cell.sum(new Vector(1, 0)), // bottom
                    cell.cell.sum(new Vector(0, -1))  // left
                ];

                for(const v of directions) {
                    if(v.i < 0 || v.i >= this.matrix.length || v.j < 0 || v.j >= this.matrix[0].length) {
                        toRemove = true;
                    } else {
                        const sibling = cellsBag.find(c => c.cell.equals(v));
                        if(sibling) {
                            sibling.distance = cell.distance + 1;
                        }
                    }
                }
            }


            if(!toRemove) {
                insideCells.push(...runCells);
            }
            cellsToCheck = cellsToCheck.filter(c => !runCells.some(r => r.equals(c)));
        }

        return insideCells;
    }

    rayTrace(j: number, loop: Vector[]) {
        let containedCellsCount = 0;

        let inTheLoop = false;
        for(let i = 0; i < this.matrix.length; i++) {
            const p = new Vector(i, j);
            const pVal = this.matrix[i][j];

            const loopElement = loop.find(el => el.equals(p));

            if(loopElement) {
                if(inTheLoop && pVal == CellType.V) {
                    continue;
                } else {
                    inTheLoop = !inTheLoop;
                }
            } else {
                if(inTheLoop) {
                    containedCellsCount++;
                }
            }
        }

        return containedCellsCount;
    }
}