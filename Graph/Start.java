import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class Start {
    JButton button;
    MyJPanel panel;
    JFrame frame = new JFrame("Graph");
    List<Integer> vertexX = new ArrayList<>();
    List<Integer> vertexY = new ArrayList<>();
    Map<List<Integer>, Integer> edges = new HashMap<>();
    int unitX;
    int unitY;
    int rangeX = 1;
    int rangeY = 1;
    int vertexRadius = 3;
    int offset = 40;
    float scale = 0; // width / height
    float weight;
    int width;
    boolean first = false;

    private void setScaling() {
        scale = 0;
        int min = vertexX.get(0);
        int max = vertexX.get(0);
        for(Integer i : vertexX) {
            if(i < min)
                min = i;
            else if(i > max)
                max = i;
        }
        rangeX = max - min;
        if(min > 1 || min < 0)
            for(int i=0; i<vertexX.size(); i++) {
                int old = vertexX.get(i);
                vertexX.remove(i);
                vertexX.add(i, old - min + 1);
            }

        // Y ----------------------------
        min = vertexY.get(0);
        max = vertexY.get(0);
        for(Integer i : vertexY) {
            if(i < min)
                min = i;
            else if(i > max)
                max = i;
        }
        rangeY = max - min;
        if(min > 1 || min < 0)
            for(int i=0; i<vertexY.size(); i++) {
                int old = vertexY.get(i);
                vertexY.remove(i);
                vertexY.add(i, old - min + 1);
            }

        // Map -------------------------
        weight = -1;
        for(Map.Entry<List<Integer>, Integer> entry : edges.entrySet()) {
            if(entry.getValue() < weight || weight == -1)
                weight = entry.getValue();
        }
        int s = 1;
        while(weight / s >= 10)
            s *= 10;
        weight /= s;
        for(Map.Entry<List<Integer>, Integer> entry : edges.entrySet())
            edges.replace(entry.getKey(), entry.getValue() / s);

        // width ----------------------
        if(rangeX < 10)
            width = 100;
        else if(rangeX < 20)
            width = 60;
        else
            width = 40;
        frame.setMinimumSize(null);
        frame.setSize(rangeX * width + offset, rangeY * width + offset + button.getHeight());
    }

    class Graph implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            Scanner scanner = chooseFile();
            if(scanner == null)
                return;
            readFile(scanner);
            setScaling();
            first = true;
            panel.repaint();
        }
    }

    class MyJPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if(first) {
                scale = (float)panel.getWidth() / (float)panel.getHeight();
                first = false;
            }
            panel.removeAll();
            InnerJPanel inner = new InnerJPanel();
            inner.setSize((int)(panel.getHeight() * scale), panel.getHeight());
            inner.setLocation((panel.getWidth() - inner.getWidth()) / 2, 0);
            panel.add(inner);
            frame.setMinimumSize(new Dimension(inner.getWidth(), 200));
            unitX = (inner.getWidth() - offset) / rangeX;
            unitY = (inner.getHeight() - offset) / rangeY;
        }
    }

    class InnerJPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            removeAll();
            g2d.setStroke(new BasicStroke(3));
            for(int i=0; i<vertexX.size(); i++)
                g2d.drawOval((vertexX.get(i) - 1) * unitX + offset/2, getHeight() - (vertexY.get(i) - 1) * unitY - offset/2,
                        2 * vertexRadius, 2 * vertexRadius);
            for(Map.Entry<List<Integer>, Integer> entry : edges.entrySet()) {
                List<Integer> pair = entry.getKey();
                g2d.setStroke(new BasicStroke(entry.getValue()/weight));
                g2d.drawLine((vertexX.get(pair.get(0)) - 1) * unitX + offset/2 + vertexRadius,
                        getHeight() - (vertexY.get(pair.get(0)) - 1) * unitY - offset/2 + vertexRadius,
                        (vertexX.get(pair.get(1)) - 1) * unitX + offset/2 + vertexRadius,
                        getHeight() - (vertexY.get(pair.get(1)) - 1) * unitY - offset/2 + vertexRadius);
            }
        }
    }

    private void readFile(Scanner scanner) {
        vertexY.clear();
        vertexX.clear();
        edges.clear();
        int vertexes = Integer.parseInt(scanner.nextLine());
        for (int i = 0; i < vertexes; i++) {
            String[] line = scanner.nextLine().split("\\s+");
            vertexX.add(Integer.parseInt(line[0]));
            vertexY.add(Integer.parseInt(line[1]));
        }
        int edge = Integer.parseInt(scanner.nextLine());
        for (int i = 0; i < edge; i++) {
            String[] line = scanner.nextLine().split("\\s+");
            List<Integer> pair = new ArrayList<>();
            pair.add(Integer.parseInt(line[0]) - 1);
            pair.add(Integer.parseInt(line[1]) - 1);
            edges.put(pair, Integer.parseInt(line[2]));
        }
    }

    private Scanner chooseFile() {
        JFileChooser fileChooser = new JFileChooser();
        int file = fileChooser.showOpenDialog(null);
        Scanner scanner = null;
        try {
            if (file == JFileChooser.APPROVE_OPTION)
                scanner = new Scanner(fileChooser.getSelectedFile());
        } catch (FileNotFoundException ex) {
            return null;
        }
        return scanner;
    }

    private void work() {
        button = new JButton( "Load" );
        button.addActionListener(new Graph());
        panel = new MyJPanel();

        frame.setSize(500, 500);
        frame.getContentPane().add(panel);
        frame.getContentPane().add(BorderLayout.PAGE_END, button);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        Start start = new Start();
        start.work();
    }
}
