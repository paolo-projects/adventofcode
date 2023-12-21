import Vector from "./point";

export default class GraphNode {
    constructor(public position: Vector, public siblings: GraphNode[] = []) {
    }
}