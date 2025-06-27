package csc365;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

class GraphEdge {
    int from;
    int to;
    boolean isPath;

    GraphEdge(int from, int to, boolean isPath) {
        this.from = from;
        this.to = to;
        this.isPath = isPath;
    }
}

public class GraphVisualization extends JFrame {

    private List<GraphEdge> edges;

    GraphVisualization(List<GraphEdge> edges) {
        this.edges = edges;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        for (GraphEdge edge : edges) {
            if (edge.isPath) {
                g.setColor(Color.BLUE);  // Color for edges in the path
            } else {
                g.setColor(Color.GRAY);  // Color for edges not in the path
            }
            // Draw the edge line (for simplicity, assuming nodes are represented by points)
            g.drawLine(edge.from * 50, edge.from * 50, edge.to * 50, edge.to * 50);
        }
    }

    public static void main(String[] args) {
        List<GraphEdge> edges = new ArrayList<>();
        edges.add(new GraphEdge(0, 1, true));
        edges.add(new GraphEdge(1, 2, true));
        edges.add(new GraphEdge(2, 3, false));
        edges.add(new GraphEdge(3, 4, false));
        edges.add(new GraphEdge(4, 5, true));

        SwingUtilities.invokeLater(() -> new GraphVisualization(edges));
    }
}