/**
 * 
 */
package taskspider.controller;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import org.jgraph.JGraph;
import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;

import taskspider.util.debug.Debug;
import websphinx.Link;
import websphinx.Page;

/**
 * @author avenger
 *
 */
public class WebGraph {

	private GraphModel model;
	private GraphLayoutCache view;
	private JGraph graph;
	private DefaultCellViewFactory cellView;
	private Hashtable<String, DefaultGraphCell> cellTable;
	private Hashtable<String, DefaultEdge> edgeTable;
	private int xCoord, yCoord, inc, counter;
	
	public WebGraph() {
		model = new DefaultGraphModel();
		cellView = new DefaultCellViewFactory();
		view = new GraphLayoutCache(model, cellView);
		graph = new JGraph(model, view);
		graph.setEditable(false);
		cellTable = new Hashtable<String, DefaultGraphCell>();
		edgeTable = new Hashtable<String, DefaultEdge>();
		xCoord = 40;
		yCoord = 50;
		inc = 0;
		counter = 0;
	}
	
	public void setScale(double val) {
		graph.setScale(val);
	}
	
	private int getNextX() {
		if(yCoord >= 2000) {
			counter++;
			yCoord = 50;
			inc = 0;
			xCoord = 400*counter;
			return xCoord;
		}
		xCoord += inc;
		inc++;
		return xCoord;
	}
	
	private int getNextY() {
		return yCoord += 40;
	}
	
	public int addNode(String source, String target) {
		
		Debug.println("SOURCE: "+source+", TARGET: "+target, 1);
		
		if(edgeTable.containsKey(source+target) || edgeTable.containsKey(target+source)
				|| source.equals(target) )
			return -1;
//		else if((source.endsWith(".html") || source.endsWith(".htm") ||
//				source.endsWith("/") ||
//				source.endsWith(".php") || source.endsWith(".jsp") ||
//				source.endsWith(".asp") || source.endsWith(".aspx")) &&
//				(target.endsWith(".html") || target.endsWith(".htm") ||
//				target.endsWith(".php") || target.endsWith(".jsp") ||
//				target.endsWith(".asp") || target.endsWith(".aspx")) ) {
//			
		else if(source.indexOf("?")<0 && target.indexOf("?")<0 &&
				source.indexOf(".js")<0 && target.indexOf(".js")<0) { 
			DefaultGraphCell[] cells = new DefaultGraphCell[3];
			if(cellTable.containsKey(source)) {
				cells[0] = cellTable.get(source);
			}
			else
				cells[0] = this.createVertex(source, getNextX(), getNextY(), 300, 20, Color.RED, true);
			
			if(cellTable.containsKey(target))
				cells[1] = cellTable.get(target);
			else
				cells[1] = this.createVertex(target, getNextX(), getNextY(), 300, 20, Color.ORANGE, true);
			
			DefaultEdge edge = new DefaultEdge();
			edge.setSource(cells[0].getChildAt(0));
			edge.setTarget(cells[1].getChildAt(0));
			cells[2] = edge;
			int arrow = GraphConstants.ARROW_NONE;
			GraphConstants.setLineEnd(edge.getAttributes(), arrow);
			GraphConstants.setEndFill(edge.getAttributes(), true);
	
			edgeTable.put(source+target, edge);
			
			graph.getGraphLayoutCache().insert(cells);
			graph.getGraphLayoutCache().toFront(cells);
		}
		 
		return 0;
	}
	
	public DefaultGraphCell createVertex(String name, double x,
			double y, double w, double h, Color bg, boolean raised) {

		// Create vertex with the given name
		DefaultGraphCell cell = new DefaultGraphCell(name);

		// Set bounds
		GraphConstants.setBounds(cell.getAttributes(),
				new Rectangle2D.Double(x, y, w, h));

		// Set fill color
		if (bg != null) {
			GraphConstants.setGradientColor(cell.getAttributes(), bg);
			GraphConstants.setOpaque(cell.getAttributes(), true);
		}

		// Set raised border
		if (raised)
			GraphConstants.setBorder(cell.getAttributes(),
					BorderFactory.createRaisedBevelBorder());
		else
			// Set black border
			GraphConstants.setBorderColor(cell.getAttributes(),
					Color.black);

		// Add a Floating Port
		cell.addPort();

		cellTable.put(name, cell);
		
		return cell;
	}
	
	public int addPage(Page page) {
		String source;

		source = page.getOrigin().toURL();
		Link[] links = page.getLinks();
		if(links!=null) {
			for(int i=0; i<links.length; i++) {
				addNode(source, links[i].getURL().toString());
			}
		}
		
		return 0;
	}
	
	public JGraph getGraph() {
		return this.graph;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		GraphModel model = new DefaultGraphModel();
//		GraphLayoutCache view = new GraphLayoutCache(model,	new DefaultCellViewFactory());
//		JGraph graph = new JGraph(model, view);
//		
//		DefaultGraphCell[] cells = new DefaultGraphCell[3];
//		cells[0] = new DefaultGraphCell(new String("Hello"));
//		GraphConstants.setBounds(cells[0].getAttributes(), new Rectangle2D.Double(20,20,40,20));
//		GraphConstants.setGradientColor(cells[0].getAttributes(), Color.orange);
//		GraphConstants.setOpaque(cells[0].getAttributes(), true);
//		DefaultPort port0 = new DefaultPort();
//		cells[0].add(port0);
//		
//		cells[1] = new DefaultGraphCell(new String("World"));
//		GraphConstants.setBounds(cells[1].getAttributes(), new Rectangle2D.Double(140,140,40,20));
//		GraphConstants.setGradientColor(cells[1].getAttributes(), Color.red);
//		GraphConstants.setOpaque(cells[1].getAttributes(), true);
//		DefaultPort port1 = new DefaultPort();
//		cells[1].add(port1);
//		
//		DefaultEdge edge = new DefaultEdge();
//		edge.setSource(cells[0].getChildAt(0));
//		edge.setTarget(cells[1].getChildAt(0));
//		cells[2] = edge;
//		int arrow = GraphConstants.ARROW_CLASSIC;
//		GraphConstants.setLineEnd(edge.getAttributes(), arrow);
//		GraphConstants.setEndFill(edge.getAttributes(), true);
//
//		graph.getGraphLayoutCache().insert(cells);
//		
//		JFrame frame = new JFrame();
//		frame.getContentPane().add(new JScrollPane(graph));
//		frame.pack();
//		frame.setVisible(true);
		
		WebGraph graph = new WebGraph();
		graph.addNode("test", null);
		
		JFrame frame = new JFrame();
		frame.getContentPane().add(new JScrollPane(graph.getGraph()));
		frame.pack();
		frame.setVisible(true);

	}

}
