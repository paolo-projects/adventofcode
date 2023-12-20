using System.Diagnostics;

var file = File.ReadAllText("input.txt")!;

var gameMap = GameMap.ParseMap(file);
var directions = new List<Direction>();

var line = new StringReader(file).ReadLine()!;

foreach (char c in line)
{
    switch (c)
    {
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
uint steps = 0;

for (steps = 0; currentNode.Label != "ZZZ"; steps++)
{
    currentNode = directions[(int)(steps % directions.Count)] == Direction.LEFT ? currentNode.Left! : currentNode.Right!;
}

Console.WriteLine($"Total steps: {steps}");

var initialNodes = gameMap.Nodes.Where((n) => n.Label.EndsWith('A')).ToList();
var currentNodes = new List<Node>(initialNodes);
var periodicities = initialNodes.Select((n) => null as UInt128?).ToList();
var periodicitiesLists = initialNodes.Select((n) => new List<uint>()).ToList();

for (steps = 0; !periodicitiesLists.All((pl) => pl.Count > 5); steps++)
{
    var direction = directions[(int)(steps % directions.Count)];
    currentNodes = currentNodes.Select((n) => direction == Direction.LEFT ? n.Left! : n.Right!).ToList();

    if (steps > 0)
    {
        for (int i = 0; i < periodicities.Count(); i++)
        {
            if (currentNodes[i].Label.EndsWith('Z'))
            {
                if (periodicities[i] == null)
                {
                    periodicities[i] = steps + 1;
                }
                periodicitiesLists[i].Add(steps + 1);
            }
        }
    }
}

foreach (var p in periodicitiesLists)
{
    for (int i = 1; i < p.Count(); i++)
    {
        Debug.Assert(p[i] % p[0] == 0);
    }
}

/*var (lcm_m, lcm_n) = Utils.Lcm(periodicities.Select((p) => p ?? 0));
Console.WriteLine($"LCM N = {lcm_n}");

foreach (var p in periodicities)
{
    Console.WriteLine($"n = {((UInt128)lcm_n * lcm_m) / (UInt128)p!}");
}

Console.WriteLine($"done: {(UInt128)lcm_n * lcm_m}");*/
UInt128 lcm = Utils.LcmEucl(periodicities.Select(n => n ?? 0));
foreach (var p in periodicities)
{
    Console.WriteLine($"REST = {lcm % p}");
}

Console.WriteLine($"Done. N = {lcm}");