var file = File.ReadAllText("input.txt")!;

var gameMap = GameMap.ParseMap(file);
var directions = new List<Direction>();

var line = new StringReader(file).ReadLine()!;

foreach(char c in line) {
    switch(c) {
        case 'L':
        directions.Add(Direction.LEFT);
        break;
        case 'R':
        directions.Add(Direction.RIGHT);
        break;
        default:
        throw new Exception($"Unsupported direction: {c}");
    }
}

Node currentNode = gameMap.StartNode!;
UInt128 steps = 0;

while(currentNode.Label != "ZZZ") {
    currentNode = directions[(int)(steps % (UInt128)directions.Count)] == Direction.LEFT ? currentNode.Left! : currentNode.Right!;
    steps++;
}

Console.WriteLine($"Total steps: {steps}");

var currentNodes = gameMap.Nodes.Where((n) => n.Label.EndsWith('A')).ToList();
var nodesLength = currentNodes.Count();
var zNodes = 0;
steps = 0;

var t = new Thread(() =>
{
    while (true)
    {
        Console.WriteLine($"Currently at {steps}");
        Thread.Sleep(1000);
    }
});
t.Start();

while(zNodes != nodesLength) {
    var direction = directions[(int)(steps % (UInt128)directions.Count)];
    zNodes = 0;
    currentNodes.Clear();
    foreach(var n in currentNodes)
    {
        var node = direction == Direction.LEFT ? n.Left! : n.Right!;
        if (node.Label.EndsWith('Z'))
        {
            zNodes++;
        }
        currentNodes.Add(node);
    }
    steps++;
}

t.Abort();

Console.WriteLine($"#2: Total steps: {steps}");