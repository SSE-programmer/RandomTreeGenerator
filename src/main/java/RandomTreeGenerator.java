import java.util.ArrayList;

public class RandomTreeGenerator {

    public int maxNumberOfVertices;
    public int numberOfVertices = 0;

    private final int MIN_NUMBER_OF_VERTICES = 1;
    private final int MAX_NUMBER_OF_VERTICES = 500;

    public double PROBABILITY = 0.5;


    public RandomTreeGenerator() {
        setMaxNumberOfVertices(10);
    }

    public RandomTreeGenerator(int maxNumberOfVertices) {
        setMaxNumberOfVertices(maxNumberOfVertices);
    }

    public void setMaxNumberOfVertices(int maxNumberOfVertices) {
        this.maxNumberOfVertices = maxNumberOfVertices;

        if (maxNumberOfVertices < MIN_NUMBER_OF_VERTICES)
            maxNumberOfVertices = MIN_NUMBER_OF_VERTICES;

        if (maxNumberOfVertices > MAX_NUMBER_OF_VERTICES)
            maxNumberOfVertices = MAX_NUMBER_OF_VERTICES;
    }

    public Node generationWithDegree(int degree) {
        Node root = new Node(null, this);

        ArrayList<Node> lvl1 = new ArrayList<>();

        lvl1.add(root);

        root.nodeAtLevels.add(lvl1);

        root.nextGeneration(GenerationType.DEGREE, this, degree, root);

        return root;
    }

    public Node generationWithDepth(int depth) {
        Node root = new Node(null, this);

        ArrayList<Node> lvl1 = new ArrayList<>();

        lvl1.add(root);

        root.nodeAtLevels.add(lvl1);

        root.nextGeneration(GenerationType.DEPTH, this, depth, root);

        return root;
    }

    public static void main(String[] args) {
        RandomTreeGenerator rtg = new RandomTreeGenerator(50);

        Node root = rtg.generationWithDegree(5);

        RenderTree render = new RenderTree();

        render.render(root);
    }
}
