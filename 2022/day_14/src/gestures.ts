import { Matrix } from "./math/matrix";
export function applyGestures(
  canvasElement: HTMLCanvasElement,
  panMatrix: Matrix,
  scaleMatrix: Matrix
) {
  let isDragging = false;
  let startPos = [0, 0];
  const mousePanningFactor = 0.2;

  canvasElement.addEventListener("mousedown", (event) => {
    isDragging = true;
    startPos = [event.clientX, event.clientY];
  });
  canvasElement.addEventListener("mousemove", (event) => {
    if (isDragging) {
      const delta = [event.clientX - startPos[0], event.clientY - startPos[1]];
      startPos = [event.clientX, event.clientY];
      panMatrix.set(0, 2, panMatrix.get(0, 2) + delta[0] * mousePanningFactor);
      panMatrix.set(1, 2, panMatrix.get(1, 2) + delta[1] * mousePanningFactor);
    }
  });
  canvasElement.addEventListener("mouseleave", (event) => {
    isDragging = false;
  });
  canvasElement.addEventListener("mouseup", (event) => {
    isDragging = false;
  });
  canvasElement.addEventListener("wheel", (event) => {
    const delta = event.deltaY * -0.001;
    scaleMatrix.set(0, 0, scaleMatrix.get(0, 0) + delta);
    scaleMatrix.set(1, 1, scaleMatrix.get(1, 1) + delta);
  });
}
