importScripts("/js/coord-set.js", "/js/functools.js");

class Space {
    sandCells;
    settledSandCells;
    wallCells;
    maxY = 0;
    callback;
    emitPosition;
  
    constructor(
      emitPosition,
      callback
    ) {
      this.emitPosition = emitPosition;
      this.sandCells = new CoordSet();
      this.settledSandCells = new CoordSet();
      this.wallCells = new CoordSet();
      this.callback = callback;
    }
  
    static parseInput(
      input,
      emitPosition,
      callback
    ) {
      const walls = input.split("\n").map((line) =>
        line
          .trim()
          .split("->")
          .map(
            (p) =>
              p
                .trim()
                .split(",")
                .map((c) => parseInt(c.trim()))
          )
      );
  
      const space = new Space(emitPosition, callback);
  
      walls.forEach((line) => {
        line.zipWithNext().forEach(([p1, p2]) => {
          const deltaX = p2[0] - p1[0];
          const deltaY = p2[1] - p1[1];
  
          if (deltaX != 0) {
            const minP = p1[0] < p2[0] ? p1 : p2;
            const maxP = p1[0] < p2[0] ? p2 : p1;
            for (let x = minP[0]; x <= maxP[0]; x++) {
              const y = p1[1] + (deltaY * (x - p1[0])) / deltaX;
              space.wallCells.set([x, y]);
              space.maxY = Math.max(y, space.maxY);
            }
          } else {
            const minP = p1[1] < p2[1] ? p1 : p2;
            const maxP = p1[1] < p2[1] ? p2 : p1;
            for (let y = minP[1]; y <= maxP[1]; y++) {
              const x = p1[0] + (deltaX * (y - p1[1])) / deltaY;
              space.wallCells.set([x, y]);
              space.maxY = Math.max(y, space.maxY);
            }
          }
        });
      });
  
      return space;
    }
  
    moveSand([x0, y0], [x1, y1]) {
      this.sandCells.delete([x0, y0]);
      this.sandCells.set([x1, y1]);
    }
  
    isCellFree([x, y]) {
      const isWallCell = this.wallCells.has([x, y]);
      const isSettledCell = this.settledSandCells.has([x, y]);
      return !isWallCell && !isSettledCell && y < this.maxY + 2;
    }
  
    checkBottom([x, y]) {
      for (const [cx, cy] of this.wallCells) {
        if (cx == x && cy > y) {
          return true;
        }
      }
      return false;
    }
  
    pushSandDown([x, y], checkFreeFall) {
      let newPosition;
      let hasTerminated = false;
  
      if (this.isCellFree([x, y + 1])) {
        newPosition = [x, y + 1];
      } else if (this.isCellFree([x - 1, y + 1])) {
        newPosition = [x - 1, y + 1];
      } else if (this.isCellFree([x + 1, y + 1])) {
        newPosition = [x + 1, y + 1];
      } else {
        this.sandCells.delete([x, y]);
        this.settledSandCells.set([x, y]);
        this.callback([x, y]);
        return;
      }
  
      if (checkFreeFall && !this.checkBottom(newPosition)) {
        hasTerminated = true;
      }
  
      this.moveSand([x, y], newPosition);
      return hasTerminated;
    }
  
    advance(checkFreeFall) {
      let hasTerminated = false;
      for (const [cx, cy] of this.sandCells) {
        hasTerminated =
          hasTerminated || this.pushSandDown([cx, cy], checkFreeFall);
      }
      return hasTerminated;
    }
  
    emitSand(checkFreeFall) {
      if (this.advance(checkFreeFall)) {
        return true;
      }
      this.sandCells.set(this.emitPosition);
      return false;
    }
  }