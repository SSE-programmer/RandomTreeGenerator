import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.graphstream.ui.swing_viewer.ViewPanel;
import org.graphstream.ui.swing_viewer.SwingViewer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RenderTree {

    private static double cameraSpeed = 4.0;

    private static GenerationType type = GenerationType.DEGREE;

    private void createConnections(Graph graph, Node node) {

        graph.addNode(String.valueOf(node.number)).setAttribute("label", String.valueOf(node.number));

        graph.addEdge(String.valueOf(node.parent.number) + String.valueOf(node.number),
                String.valueOf(node.parent.number), String.valueOf(node.number));

        for (int i = 0; i < node.children.size(); i++) {
            createConnections(graph, node.children.get(i));
        }
    }

    JPanel panel = new JPanel();

    private void generate(JFrame frame, int value, int count, double probability) {
        ViewPanel view_panel;

        panel.setLayout(new GridLayout());

        frame.add(panel, BorderLayout.CENTER);

        Graph graph = new SingleGraph("Tree");

        Node root;

        RandomTreeGenerator generator = new RandomTreeGenerator(count);

        generator.PROBABILITY = probability;

        if (type == GenerationType.DEGREE) {
            root = generator.generationWithDegree(value);
        } else {
            root = generator.generationWithDepth(value);
        }

        graph.setAttribute( "ui.stylesheet", styleSheet );
        graph.setAttribute( "ui.antialias" );
        graph.setAttribute( "ui.quality" );

        graph.addNode(String.valueOf(root.number)).setAttribute("label", String.valueOf(root.number));

        for (int i = 0; root.children != null && i < root.children.size(); i++) {
            createConnections(graph, root.children.get(i));
        }

        SwingViewer viewer = new SwingViewer(graph, SwingViewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        viewer.enableAutoLayout();
        view_panel = (ViewPanel) viewer.addDefaultView(false);
        Rectangle rec = panel.getBounds();
        view_panel.setBounds(0, 0, rec.width, rec.height);
        view_panel.setPreferredSize(new Dimension(rec.width, rec.height));
        panel.add(view_panel);

        view_panel.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent mwe) {
                RenderTree.zoomGraphMouseWheelMoved(mwe, view_panel);
            }
        });

        view_panel.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case (KeyEvent.VK_W) -> {
                        double x = view_panel.getCamera().getViewCenter().x;
                        double y = view_panel.getCamera().getViewCenter().y + cameraSpeed * view_panel.getCamera().getViewPercent();
                        double z = view_panel.getCamera().getViewCenter().z;

                        view_panel.getCamera().setViewCenter(x, y, z);
                    }
                    case (KeyEvent.VK_A) -> {
                        double x = view_panel.getCamera().getViewCenter().x - cameraSpeed * view_panel.getCamera().getViewPercent();
                        double y = view_panel.getCamera().getViewCenter().y;
                        double z = view_panel.getCamera().getViewCenter().z;

                        view_panel.getCamera().setViewCenter(x, y, z);
                    }
                    case (KeyEvent.VK_S) -> {
                        double x = view_panel.getCamera().getViewCenter().x;
                        double y = view_panel.getCamera().getViewCenter().y - cameraSpeed * view_panel.getCamera().getViewPercent();
                        double z = view_panel.getCamera().getViewCenter().z;

                        view_panel.getCamera().setViewCenter(x, y, z);
                    }
                    case (KeyEvent.VK_D) -> {
                        double x = view_panel.getCamera().getViewCenter().x + cameraSpeed * view_panel.getCamera().getViewPercent();
                        double y = view_panel.getCamera().getViewCenter().y;
                        double z = view_panel.getCamera().getViewCenter().z;

                        view_panel.getCamera().setViewCenter(x, y, z);
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
    }

    public void render(Node tree) {
        System.setProperty("org.graphstream.ui", "swing");

        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setBounds(0, 0, 700, 500);
        frame.setPreferredSize(new Dimension(700, 500));

        JLabel label1 = new JLabel("Значение основного параметра:");
        JTextField valueField = new JTextField("5");

        JLabel label2 = new JLabel("Количество вершин:");
        JTextField countField = new JTextField("50");

        JLabel label3 = new JLabel("Вероятность появления вершины:");
        JTextField probabilityField = new JTextField("0.5");

        valueField.setColumns(5);
        countField.setColumns(5);
        probabilityField.setColumns(5);

        frame.add(panel, BorderLayout.CENTER);

        generate(frame, Integer.parseInt(valueField.getText()),
                Integer.parseInt(countField.getText()),
                Double.parseDouble(probabilityField.getText()));

        JPanel paramPanel = new JPanel();
        frame.add(paramPanel, BorderLayout.EAST);

        JRadioButton rbtnDegree = new JRadioButton("Генерация по степени вершин");
        rbtnDegree.setSelected(true);
        JRadioButton rbtnDepth = new JRadioButton("Генерация по глубине дерева");

        ButtonGroup group = new ButtonGroup();

        JButton button = new JButton("Генерация");

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.remove(panel);

                panel = new JPanel();

                generate(frame, Integer.parseInt(valueField.getText()),
                        Integer.parseInt(countField.getText()),
                        Double.parseDouble(probabilityField.getText()));

                frame.paint(frame.getGraphics());
                frame.setVisible(true);
            }
        });

        paramPanel.setPreferredSize(new Dimension(300, frame.getHeight()));

        paramPanel.add(rbtnDegree);
        paramPanel.add(rbtnDepth);
        paramPanel.add(label1);
        paramPanel.add(valueField);
        paramPanel.add(label2);
        paramPanel.add(countField);
        paramPanel.add(label3);
        paramPanel.add(probabilityField);
        paramPanel.add(button);

        group.add(rbtnDegree);
        group.add(rbtnDepth);

        rbtnDegree.setBounds(400,0,250,50);
        rbtnDepth.setBounds(700,0,250,50);

        frame.setVisible(true);

        rbtnDegree.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                type = GenerationType.DEGREE;
                countField.setEnabled(true);
            }
        });

        rbtnDepth.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                type = GenerationType.DEPTH;
                countField.setEnabled(false);
            }
        });
    }

    public static void zoomGraphMouseWheelMoved(MouseWheelEvent mwe, ViewPanel view_panel){
        if (Event.ALT_MASK != 0) {
            if (mwe.getWheelRotation() > 0) {
                double new_view_percent = view_panel.getCamera().getViewPercent() + 0.05;
                view_panel.getCamera().setViewPercent(new_view_percent);
            } else if (mwe.getWheelRotation() < 0) {
                double current_view_percent = view_panel.getCamera().getViewPercent();
                if(current_view_percent > 0.05){
                    view_panel.getCamera().setViewPercent(current_view_percent - 0.05);
                }
            }
        }
    }

    private String styleSheet = ""
            + "graph {"
            + "	canvas-color: white; "
            + "	fill-mode: plain; "
            + "	fill-color: black; "
            + "	padding: 60px; "
            + "}"
            + ""
            + "node {"
            + "	shape: box;"
            + "	size: 10px;"
            + "	size-mode: fit;"
            + "	fill-color: #FFF;"
            + "	stroke-mode: plain;"
            + "	stroke-color: grey;"
            + "	stroke-width: 3px;"
            + "	padding: 5px, 1px;"
            + "	shadow-mode: none;"
            + "	text-style: normal;"
            + "	text-font: 'Droid Sans';"
            + "}"
            + ""
            + "node:clicked {"
            + "	stroke-mode: plain;"
            + "	stroke-color: red;"
            + "}"
            + ""
            + "node:selected {"
            + "	stroke-mode: plain;"
            + "	stroke-color: blue;"
            + "}"
            + ""
            + "edge {"
            + "	shape: freeplane;"
            + "	size: 3px;"
            + "	fill-color: grey;"
            + "	fill-mode: plain;"
            + "	shadow-mode: none;"
            + "	shadow-color: rgba(0,0,0,100);"
            + "	shadow-offset: 3px, -3px;"
            + "	shadow-width: 0px;"
            + "	arrow-shape: arrow;"
            + "	arrow-size: 20px, 6px;"
            + "}";
}
