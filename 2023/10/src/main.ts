import { readFileSync } from "fs";
import GameMap from "./map";

const input = readFileSync("./input.txt", {encoding: 'utf-8'});
const map = new GameMap(input);

map.getFarthest();