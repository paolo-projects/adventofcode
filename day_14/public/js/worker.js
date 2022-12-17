importScripts("/js/space.js");

onmessage = (e) => {
    if(e.data.type === "start") {
        start();
    }
}

console.log("Worker loaded")

async function start() {
    console.log("Worker started")

    const input = await (await fetch("/input.txt")).text();
    let running = true;
    let checkFreeFall = true;
    let settledSand = 0;
    const emitPosition = [500, 0];
    
    function onSandSettled([x, y]) {
        settledSand++;
        if(x == emitPosition[0] && y == emitPosition[1]) {
            postMessage({
                type: "reached-emit",
                settled: settledSand
            });
            running = false;
            postMessage({
                type: "finished"
            });
        }
    }
    
    function onSandFreeFalling() {
        postMessage({
            type: "free-fall-reached",
            settled: settledSand
        });
    }

    function sendDrawing() {
        postMessage({
            type: "drawing",
            wallCells: Array.from(space.wallCells.values),
            sandCells: Array.from(space.sandCells.values),
            settledSandCells: Array.from(space.settledSandCells.values),
        });
    }

    const space = Space.parseInput(input, emitPosition, onSandSettled);
    while(running) {
        if(space.emitSand(checkFreeFall)) {
            console.log("free fall reached");
            checkFreeFall = false;
            onSandFreeFalling();
        }
        sendDrawing();
    }
}