import { CellPosition } from "../math/space";

interface StartEvent {
  type: "start";
}

interface FinishedEvent {
  type: "finished";
}

interface DrawingEvent {
  type: "drawing";
  wallCells: string[];
  sandCells: string[];
  settledSandCells: string[];
}

interface FreeFallReachedEvent {
  type: "free-fall-reached";
  settled: number;
}

interface EmitReachedEvent {
  type: "reached-emit";
  settled: number;
}

export type WorkerEvent =
  | StartEvent
  | FinishedEvent
  | DrawingEvent
  | FreeFallReachedEvent
  | EmitReachedEvent;
