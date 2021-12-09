package GUI;

import GUI.MainWindow;
import api.DirectedWeightedGraph;

import javax.swing.*;
import java.awt.*;

public class GraphicsController {

    MainWindow frame;
    DirectedWeightedGraph directedGraph;

    public GraphicsController(DirectedWeightedGraph directedGraph){
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int height = dimension.height;
        int width = dimension.width;
        frame = new MainWindow(width,height, directedGraph);
        ImageIcon icon = new ImageIcon("node.png");
        frame.setIconImage(icon.getImage());
        this.directedGraph = directedGraph;

    }


    private void drawGraph(){
//        Iterator iter = directedGraph.edgeIter();
//        while(iter.hasNext()){
//            Edge edge = (Edge) iter.next();
//            frame.drawEdge(ed);
//        }

    }

    public void run() {}
}
