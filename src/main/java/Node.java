import java.util.ArrayList;

public class Node {

    public Node parent;
    public ArrayList<Node> children = new ArrayList<Node>();
    public int number;

    private int level = 1;

    public ArrayList<ArrayList<Node>> nodeAtLevels = new ArrayList<ArrayList<Node>>();

    public void nextGeneration(GenerationType genType, RandomTreeGenerator generator, int value, Node root) {
        switch (genType) {
            case DEGREE -> {
                while (generator.numberOfVertices < generator.maxNumberOfVertices && nodeAtLevels.size() == level) {
                    ArrayList<Node> levelNodeList = root.nodeAtLevels.get(root.level - 1);

                    for (int i = 0; i < levelNodeList.size(); i++) {
                        for (int j = 0; j < value
                                && generator.numberOfVertices < generator.maxNumberOfVertices; j++) {
                            if (Math.random() < generator.PROBABILITY) {
                                levelNodeList.get(i).children.add(new Node(levelNodeList.get(i), generator));

                                if (root.nodeAtLevels.size() < root.level + 1) {
                                    root.nodeAtLevels.add(new ArrayList<Node>());
                                }

                                root.nodeAtLevels.get(level).add(levelNodeList.get(i).children.get(levelNodeList.get(i).children.size() - 1));
                            }
                        }
                    }

                    root.level++;
                }
            }

            case DEPTH -> {
                while (level < value && nodeAtLevels.size() == level) {
                    ArrayList<Node> levelNodeList = root.nodeAtLevels.get(root.level - 1);

                    for (int i = 0; i < levelNodeList.size(); i++) {
                        for (int j = 0; j < 5; j++) {
                            if (Math.random() < generator.PROBABILITY) {
                                levelNodeList.get(i).children.add(new Node(levelNodeList.get(i), generator));

                                if (root.nodeAtLevels.size() < root.level + 1) {
                                    root.nodeAtLevels.add(new ArrayList<Node>());
                                }

                                root.nodeAtLevels.get(level).add(levelNodeList.get(i).children.get(levelNodeList.get(i).children.size() - 1));
                            }
                        }
                    }

                    root.level++;
                }
            }
        }
    }

    public Node(Node parent, RandomTreeGenerator generator) {
        this.parent = parent;

        generator.numberOfVertices++;
        this.number = generator.numberOfVertices;
    }
}
