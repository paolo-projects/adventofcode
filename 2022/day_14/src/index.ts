import { applyGestures } from "./gestures";
import { Matrix } from "./math/matrix";
import { CellPosition, Space } from "./math/space";
import { WorkerEvent } from "./utils/worker-event";

const bgColor = "#000000";
const sandColor = "#ebba34";
const depositedSandColor = "eb8934";
const wallColor = "#c4c4c4";

const canvasElement = document.getElementById("animation") as HTMLCanvasElement;
canvasElement.width = 1000;
canvasElement.height = 800;
const canvas = canvasElement.getContext("2d");
const worker = new Worker(new URL("js/worker.js", "http://localhost:3000"));

let wallCells: CellPosition[] = [];
let sandCells: CellPosition[] = [];
let settledSandCells: CellPosition[] = [];

// Build transformation matrix for controlling pan/zoom of camera
let panMatrix = Matrix.translation([-350, 10]);
let scaleMatrix = Matrix.diagonal(3, 3);

worker.onmessage = (event) => {
  const payload = event.data as WorkerEvent;
  switch (payload.type) {
    case "drawing":
      wallCells = payload.wallCells.map(
        (v) => v.split(",").map((v) => parseInt(v)) as CellPosition
      );
      sandCells = payload.sandCells.map(
        (v) => v.split(",").map((v) => parseInt(v)) as CellPosition
      );
      settledSandCells = payload.settledSandCells.map(
        (v) => v.split(",").map((v) => parseInt(v)) as CellPosition
      );
      break;
    case "free-fall-reached":
      console.log("Reached free fall: " + payload.settled);
      break;
    case "reached-emit":
      console.log("Reached EMIT position: " + payload.settled);
      break;
    case "finished":
      worker.terminate();
      break;
  }
};

worker.postMessage({ type: "start" } as WorkerEvent);

function renderFunction() {
  canvas.fillStyle = bgColor;
  canvas.fillRect(0, 0, 1000, 800);
  const transformationMatrix = scaleMatrix.multiply(panMatrix);

  canvas.fillStyle = wallColor;
  for (const [cx, cy] of wallCells) {
    const vector = transformationMatrix.multiply(
      Matrix.vectorMatrix([cx, cy, 1])
    );
    const endVector = transformationMatrix.multiply(
      Matrix.vectorMatrix([cx + 1, cy + 1, 1])
    );
    canvas.fillRect(
      vector.get(0, 0),
      vector.get(1, 0),
      endVector.get(0, 0) - vector.get(0, 0),
      endVector.get(1, 0) - vector.get(1, 0)
    );
  }
  canvas.fillStyle = sandColor;
  for (const [cx, cy] of sandCells) {
    const vector = transformationMatrix.multiply(
      Matrix.vectorMatrix([cx, cy, 1])
    );
    const endVector = transformationMatrix.multiply(
      Matrix.vectorMatrix([cx + 1, cy + 1, 1])
    );
    canvas.fillRect(
      vector.get(0, 0),
      vector.get(1, 0),
      endVector.get(0, 0) - vector.get(0, 0),
      endVector.get(1, 0) - vector.get(1, 0)
    );
  }
  canvas.fillStyle = depositedSandColor;
  for (const [cx, cy] of settledSandCells) {
    const vector = transformationMatrix.multiply(
      Matrix.vectorMatrix([cx, cy, 1])
    );
    const endVector = transformationMatrix.multiply(
      Matrix.vectorMatrix([cx + 1, cy + 1, 1])
    );
    canvas.fillRect(
      vector.get(0, 0),
      vector.get(1, 0),
      endVector.get(0, 0) - vector.get(0, 0),
      endVector.get(1, 0) - vector.get(1, 0)
    );
  }
  requestAnimationFrame(renderFunction);
}

requestAnimationFrame(renderFunction);

applyGestures(canvasElement, panMatrix, scaleMatrix);
