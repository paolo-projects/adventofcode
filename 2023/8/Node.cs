public class Node {
    public string Label {get; set;}
    public Node? Left {get; set;}
    public Node? Right {get; set;}

    public Node(string label) {
        Label = label;
    }
}