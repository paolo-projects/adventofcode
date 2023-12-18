using System.Runtime.ExceptionServices;
using System.Text.RegularExpressions;

public class GameMap {
    public List<Node> Nodes {get; set;}
    public Dictionary<string, Node> NodesMap {get; set;}
    public Node? StartNode {get; set;}

    public GameMap() {
        Nodes = new List<Node>();
        NodesMap = new Dictionary<string, Node>();
    }

    public static GameMap ParseMap(string map) {
        var reader = new StringReader(map);
        string? line;

        var associations = new Dictionary<string, (string, string)>();
        var gameMap = new GameMap();
        var pattern = new Regex("^(.{3}) = \\((.{3}), (.{3})\\)$");

        while((line = reader.ReadLine()) != null) {
            var match = pattern.Match(line);
            if(match != null && match.Success) {
                var node = new Node(match.Groups[1].Value);
                gameMap.NodesMap[match.Groups[1].Value] = node;
                gameMap.Nodes.Add(node);
                associations[match.Groups[1].Value] = (match.Groups[2].Value, match.Groups[3].Value);
            }
        }

        foreach(Node node in gameMap.Nodes) {
            node.Left = gameMap.Nodes.Find((n) => n.Label == associations[node.Label].Item1);
            node.Right = gameMap.Nodes.Find((n) => n.Label == associations[node.Label].Item2);
        }

        gameMap.StartNode = gameMap.Nodes.Find((n) => n.Label == "AAA");

        return gameMap;
    }
}