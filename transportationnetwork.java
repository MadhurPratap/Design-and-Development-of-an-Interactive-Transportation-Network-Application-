import javax.swing.*;
import java.awt.event.*;
import java.util.*;
public class TransportNetwork {
    static final int V = 5;
    int graph[][] = new int[V][V];
    JTextField sourceField;
    JTextArea outputArea;
    // Dijkstra with path display
    void dijkstra(int src) {
        int dist[] = new int[V];
        int parent[] = new int[V];
        boolean visited[] = new boolean[V];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[src] = 0;
        parent[src] = -1;
        for (int count = 0; count < V - 1; count++) {
            int u = minDistance(dist, visited);
            visited[u] = true;
            for (int v = 0; v < V; v++) {
                if (!visited[v] && graph[u][v] != 0 &&
                        dist[u] + graph[u][v] < dist[v]) {
                    dist[v] = dist[u] + graph[u][v];
                    parent[v] = u;
                }
            }
        }
        String result = "Shortest Paths:\n";
        for (int i = 0; i < V; i++) {
            result += "City " + src + " -> " + i + " = " + dist[i] + " | Path: ";
            printPath(parent, i);
            result += "\n";
        }
        outputArea.setText(result);
    }
    void printPath(int parent[], int j) {
        if (j == -1) return;
        printPath(parent, parent[j]);
        outputArea.append(j + " ");
    }
    int minDistance(int dist[], boolean visited[]) {
        int min = Integer.MAX_VALUE, min_index = -1;
        for (int v = 0; v < V; v++) {
            if (!visited[v] && dist[v] <= min) {
                min = dist[v];
                min_index = v;
            }
        }
        return min_index;
    }
    // BFS + Reachability
    void bfs(int src) {
        boolean visited[] = new boolean[V];
        Queue<Integer> q = new LinkedList<>();
        visited[src] = true;
        q.add(src);
        String traversal = "Traversal Order: ";
        while (!q.isEmpty()) {
            int node = q.poll();
            traversal += node + " ";
            for (int i = 0; i < V; i++) {
                if (graph[node][i] != 0 && !visited[i]) {
                    visited[i] = true;
                    q.add(i);
                }
            }
        }
        String reach = "\nReachability:\n";
        for (int i = 0; i < V; i++) {
            if (visited[i])
                reach += "City " + i + " is reachable\n";
            else
                reach += "City " + i + " is NOT reachable\n";
        }
        outputArea.setText(traversal + reach);
    }
    // Prim MST
    void primMST() {
        int parent[] = new int[V];
        int key[] = new int[V];
        boolean mstSet[] = new boolean[V];
        Arrays.fill(key, Integer.MAX_VALUE);
        key[0] = 0;
        parent[0] = -1;
        for (int count = 0; count < V - 1; count++) {
            int u = minDistance(key, mstSet);
            mstSet[u] = true;
            for (int v = 0; v < V; v++) {
                if (graph[u][v] != 0 && !mstSet[v] &&
                        graph[u][v] < key[v]) {
                    parent[v] = u;
                    key[v] = graph[u][v];
                }
            }
        }

        String result = "Minimum Cost Network (MST):\n";
        int total = 0;
        for (int i = 1; i < V; i++) {
            result += parent[i] + " - " + i + " = " + graph[i][parent[i]] + "\n";
            total += graph[i][parent[i]];
        }
        result += "Total Cost = " + total;
        outputArea.setText(result);
    }
    public static void main(String[] args) {
        TransportNetwork obj = new TransportNetwork();
        JFrame f = new JFrame("Transportation Network System");
        JLabel l1 = new JLabel("Enter Source City (0-4):");
        l1.setBounds(20, 20, 200, 25);
        obj.sourceField = new JTextField();
        obj.sourceField.setBounds(200, 20, 50, 25);
        JButton b1 = new JButton("Shortest Path");
        JButton b2 = new JButton("Traversal + Reachability");
        JButton b3 = new JButton("Minimum Cost Network");
        b1.setBounds(20, 60, 230, 30);
        b2.setBounds(20, 100, 230, 30);
        b3.setBounds(20, 140, 230, 30);
        obj.outputArea = new JTextArea();
        JScrollPane sp = new JScrollPane(obj.outputArea);
        sp.setBounds(20, 180, 340, 200);

        f.add(l1);
        f.add(obj.sourceField);
        f.add(b1);
        f.add(b2);
        f.add(b3);
        f.add(sp);
        f.setSize(400, 450);
        f.setLayout(null);
        f.setVisible(true);
        obj.graph = new int[][]{
                {0, 2, 0, 6, 0},
                {2, 0, 3, 8, 5},
                {0, 3, 0, 0, 7},
                {6, 8, 0, 0, 9},
                {0, 5, 7, 9, 0}
        };
        b1.addActionListener(e -> {
            int src = Integer.parseInt(obj.sourceField.getText());
            obj.dijkstra(src);
        });
        b2.addActionListener(e -> {
            int src = Integer.parseInt(obj.sourceField.getText());
            obj.bfs(src);


        b3.addActionListener(e -> obj.primMST());
    }
}
