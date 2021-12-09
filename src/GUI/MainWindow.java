package GUI;
import api.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public class MainWindow extends JFrame implements ActionListener {
    private static final int ARROW_SIZE = 7;

    JPanel panel;
    AffineTransform tx = new AffineTransform();
    Line2D.Double line = new Line2D.Double(0,0,100,100);
    Polygon arrowHead = new Polygon();
    HashMap<String, Edge> edgeMap = new HashMap<>();
    ArrayList<NodeV> nodes;
    DirectedWeightedGraph graph;
    private double scaleX = 0;
    private double scaleY = 0;
    private double maxX = Integer.MIN_VALUE;
    private double minX = Integer.MAX_VALUE;
    private double maxY = Integer.MIN_VALUE;
    private double minY = Integer.MAX_VALUE;
    private int incrementY = 100;
    private int incrementX = 100;


    JMenu menu;
    JMenuBar mb ;
    JMenuItem exit;

    MainWindow(int width, int height, DirectedWeightedGraph g){

        this.graph= g;

        panel = new JPanel();
//        getContentPane().add(panel);
        nodes = new ArrayList();
        setSize(width,height);
        setLayout(null);
        setVisible(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                System.exit(0);
            }
        });
        ImageIcon icon = new ImageIcon("node.png");
        setIconImage(icon.getImage());
        drawGraph();
        mb = new JMenuBar();
        menu = new JMenu("Menu Main");
        exit = new JMenuItem("Exit");
        menu.add(exit);
        mb.add(menu);
        exit.addActionListener(this);
        this.setJMenuBar(mb);


        setLayout(null);
        setVisible(true);

    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);  // fixes the immediate problem.
        Graphics2D g2 = (Graphics2D) g;
        double factorX = getWidth() / scaleX * 0.8;
        double factorY = getHeight() / scaleY * 0.8;

        Color color = Color.BLACK;
        g2.setColor(color);
        for (NodeV p : nodes) {
            double x = (p.getLocation().x()-minX) * factorX + incrementX;
            double y = (p.getLocation().y()-minY) * factorY + incrementY;
            Ellipse2D.Double ellipse = new Ellipse2D.Double(x - 3, y - 3, 6, 6);
            g2.fill(ellipse);
            g2.draw(ellipse);
        }

//        color = Color.blue;
        g2.setColor(color);
        Iterator iter = graph.edgeIter();
        while (iter.hasNext()) {
            Edge edge = (Edge) iter.next();
            String key = edge.getKey();
            NodeData srcNode = graph.getNode(edge.getSrc());
            NodeData destNode = graph.getNode(edge.getDest());
            double x1 =incrementX +(srcNode.getLocation().x()-minX)*factorX;
            double y1 =incrementY+ (srcNode.getLocation().y()-minY)*factorY;
            double x2 = incrementX+ (destNode.getLocation().x()-minX)*factorX ;
            double y2 = incrementY+ (destNode.getLocation().y()-minY)*factorY ;
            drawArrow(g, (int)x1, (int)y1, (int)x2, (int)y2);
        }

    }

    private void drawArrowHead(Graphics2D g2d, Line2D line) {
        tx.setToIdentity();
        double angle = Math.atan2(line.getY2()-line.getY1(), line.getX2()-line.getX1());
        tx.translate(line.getX2(), line.getY2());
        tx.rotate((angle-Math.PI/2d));

        Graphics2D g = (Graphics2D) g2d.create();
        g.setTransform(tx);
        g.fill(arrowHead);
        g.dispose();
    }

    public void drawGraph(){
        Iterator iter = graph.nodeIter();
        while(iter.hasNext()){
            NodeV node = (NodeV) iter.next();
            Location loc = (Location) node.getLocation();
            minX = Math.min(minX, loc.x());
            maxX = Math.max(maxX, loc.x());
            minY = Math.min(minY, loc.y());
            maxY = Math.max(maxY, loc.y());
            nodes.add(node);
        }
        scaleX = Math.abs(maxX-minX);
        scaleY = Math.abs(maxY-minY);
    }


    void drawArrow(Graphics g1, int x1, int y1, int x2, int y2) {
        Graphics2D g = (Graphics2D) g1.create();

        double dx = x2 - x1, dy = y2 - y1;
        double angle = Math.atan2(dy, dx);
        int len = (int) Math.sqrt(dx*dx + dy*dy);
        AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
        at.concatenate(AffineTransform.getRotateInstance(angle));
        g.transform(at);

        // Draw horizontal arrow starting in (0, 0)
        g.drawLine(0, 0, len, 0);
        g.fillPolygon(new int[] {len, len- ARROW_SIZE, len- ARROW_SIZE, len},
                new int[] {0, -ARROW_SIZE, ARROW_SIZE, 0}, 4);
    }


    @Override
    public void actionPerformed(ActionEvent e) {


        if (e.getSource() == this.exit){
            System.exit(0);
        }

    }
}
