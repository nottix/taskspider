/**
 * TaskGraph.java
 *
 * Created on March 8, 2007, 7:49 PM; Updated May 29, 2007
 *
 * Copyright March 8, 2007 Grotto Networking
 */

package taskspider.view.gui;

import edu.uci.ics.jung.algorithms.layout.*;
import edu.uci.ics.jung.graph.*;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.control.LayoutScalingControl;
import edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin;
import edu.uci.ics.jung.visualization.decorators.*;
import java.awt.Dimension;
import javax.swing.JFrame;
import java.awt.event.*;
import java.awt.geom.*;
import edu.uci.ics.jung.algorithms.importance.*;

import org.apache.commons.collections15.Transformer;
import org.lobobrowser.gui.BrowserPanel;

import taskspider.util.debug.Debug;
import websphinx.Link;
import websphinx.Page;

import java.awt.*;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;
import edu.uci.ics.jung.visualization.control.*;
import edu.uci.ics.jung.visualization.*;
import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.util.Relaxer;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.VisualizationServer.Paintable;
import edu.uci.ics.jung.visualization.picking.PickedState;
import java.net.*;
import taskspider.controller.*;

/**
 *
 * @author Simone Notargiacomo, Giuseppe Schipani
 */
public class TaskGraph {
    private Graph<String, String> g;
    private VisualizationViewer<String,String> vv;
    private int counter;
    private Layout<String, String> layout;
    private Transformer<String,Paint> vertexPaint;
    private Transformer<String,Shape> vertexShape;
    private Transformer<String,Font> vertexFont;
    private DefaultModalGraphMouse gm;
    private boolean browserCheck = true;
    private BrowserPanel htmlPanel = null;
    private int oldNum = 0;
    
    public void setBrowserCheck(boolean browserCheck) {
    	this.browserCheck = browserCheck;
    }
    
    public void setHtmlPanel(BrowserPanel htmlPanel) {
    	this.htmlPanel = htmlPanel;
    }
    
    /** Creates a new instance of SimpleGraphView */
    public TaskGraph() {
    	g = new DirectedSparseMultigraph<String, String>();
    	counter = 0;
    	oldNum = 0;
    	
    	vertexPaint = new Transformer<String,Paint>() {
            public Paint transform(String i) {
                return Color.GREEN;
            }
        };
        
        vertexShape = new Transformer<String,Shape>() {
            public Shape transform(String i) {
//                return new Rectangle(i.length()*15, 40);
            	return new Rectangle(40, 40);
            }
        };
        
        vertexFont = new Transformer<String,Font>() {
            public Font transform(String i) {
                return new Font("Arial", Font.BOLD, 20);
            }
        };
        
        gm = new DefaultModalGraphMouse();
        gm.setMode(ModalGraphMouse.Mode.PICKING);
//        gm.add(new GraphMouseHandler());
        gm.add(new PickingGraphMousePlugin<String,String>() {
        	
        	public void mouseClicked(MouseEvent e) {
                VisualizationViewer<String,String> vv2 = (VisualizationViewer)e.getSource();
                GraphElementAccessor<String,String> pickSupport = vv2.getPickSupport();
                PickedState<String> pickedVertexState = vv2.getPickedVertexState();
                PickedState<String> pickedEdgeState = vv2.getPickedEdgeState();
                if(e.getButton()==e.BUTTON1 && e.getClickCount()==2 && pickSupport != null && pickedVertexState != null) {
                    Layout<String,String> layout = vv2.getGraphLayout();
                    if(e.getModifiers() == modifiers) {
                        //rect.setFrameFromDiagonal(down,down);
                        // p is the screen point for the mouse event
                        Point2D p = e.getPoint();
                        // take away the view transform
                        Point2D ip = p;//vv.getRenderContext().getBasicTransformer().inverseViewTransform(p);

                        vertex = pickSupport.getVertex(layout, ip.getX(), ip.getY());
                        if(vertex!=null) {
	                        String selectedCell = vertex.toString();
	                        
	                        System.out.println(vertex.toString());
	                        
	                        if(browserCheck) {
	            				try {
	            					htmlPanel.navigate(selectedCell);
	            				} catch (MalformedURLException e1) {
	            					e1.printStackTrace();
	            				}
	            			}
	            			else
	            				BrowserControl.displayURL("", selectedCell);
                        }
                    }
        		}
        	}
        });
        gm.add(new TranslatingGraphMousePlugin(MouseEvent.BUTTON3_MASK));
    }
    
    public int addNode(String source, String target) {
    	if(g.containsEdge(source+target) || g.containsEdge(target+source))
    		return -1;
    	Debug.println("SOURCE: "+source+", TARGET: "+target, 3);
    	if(source.indexOf("?")<0 && target.indexOf("?")<0 &&
				source.indexOf(".js")<0 && target.indexOf(".js")<0/* &&
				!g.containsVertex(target)*/) { 
    		counter++;
//    		g.addVertex(source);
    		//g.addVertex(target);
    		g.addEdge(source+target, source, target);
    	}
    	return 0;
    }

    public TaskGraph getTaskGraph() {
//    	layout = new ISOMLayout((DirectedSparseGraph<String, String>)g);
//    	layout = new FRLayout2((DirectedSparseMultigraph<String, String>)g);
    	layout = new FRLayout((DirectedSparseMultigraph<String, String>)g);
    	((FRLayout)layout).setAttractionMultiplier(0.2);
    	((FRLayout)layout).setRepulsionMultiplier(0.4);
    	((FRLayout)layout).setMaxIterations(750);
    	layout.setSize(new Dimension(counter*600,counter*600)); // sets the initial size of the layout space
    	vv = new VisualizationViewer<String,String>(layout);
    	vv.setPreferredSize(new Dimension(450,450)); //Sets the viewing area size
    	
        vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
        vv.getRenderContext().setVertexShapeTransformer(vertexShape);

//        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
//        vv.getRenderContext().setVertexFontTransformer(vertexFont);

        vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);

//        this.setScale(0.5);
        vv.setGraphMouse(gm);
        vv.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(javax.swing.event.ChangeEvent e) {
				
				//System.out.println("COUNTER: "+counter+", OLDNUM: "+oldNum);
				if((counter>oldNum) && ((FRLayout)layout).done()) {
					layout.initialize();
					Relaxer relaxer = vv.getModel().getRelaxer();
					if(relaxer != null) {
						relaxer.stop();
						relaxer.prerelax();
						relaxer.relax();
					}
					oldNum = counter;
				}
//				Relaxer relaxer = vv.getModel().getRelaxer();
//				if(relaxer != null) {
////				if(layout instanceof IterativeContext) {
//					relaxer.stop();
//					relaxer.prerelax();
//					relaxer.relax();
//				}
			}
        });
    	
    	return this;
    }
    
    public VisualizationViewer<String, String> getVisualization() {
    	this.getTaskGraph();
    	return this.vv;
    }
    
	public int addPage(Page page) {
		String source, target;
		
		source = page.getOrigin().toURL();
		Link[] links = page.getLinks();
		if(links!=null) {
			for(int i=0; i<links.length; i++) {
				addNode(source, links[i].getURL().toString());
			}
		}
		
		return 0;
	}
	
	public void setScale(double value) {
		if(vv!=null) {
			LayoutScalingControl control = new LayoutScalingControl();
			System.out.println("coord: "+vv.getCenter().toString());
			control.scale(vv, (float)value, vv.getCenter());
		}
	}
	
	public Graph<String, String> getGraph() {
		return this.g;
	}
    
    public static void main(String[] args) {
        TaskGraph sgv = new TaskGraph(); //We create our graph in here
       
//        for(int i=1; i<100; i++) {
//        	String source = "aaaaaaaaaaaaaaa"+((int)((Math.random()+1)*100))%i;
//        	String target = "bbbbbbbbbbbbbbbbbbb"+((int)((Math.random()+1)*100))%i;
//        	sgv.addNode(source, target);
//        	System.out.println("SOURCE: "+source+", TARGET: "+target);
////        	System.out.println("aaaaaaaaaaaaaaa"+i+": "+sgv.getGraph().inDegree("aaaaaaaaaaaaaaa"+i));
//        }
        
        for(int i=90; i<150; i++) {
        	sgv.addNode("aaa3", "bbb"+i);
        }
        for(int i=0; i<50; i++) {
        	sgv.addNode("aaa1", "bbb"+i);
        }
        for(int i=50; i<100; i++) {
        	sgv.addNode("aaa2", "bbb"+i);
        }
        
        for(int i=140; i<250; i++) {
        	sgv.addNode("aaa4", "bbb"+i);
        }
        
        JFrame frame = new JFrame("Simple Graph View");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(sgv.getTaskGraph().getVisualization()); 
        frame.pack();
        frame.setVisible(true);   
       
    }
    
    private static class GraphMouseHandler extends PickingGraphMousePlugin<String, String> {
    	public GraphMouseHandler() {
    		super();
    	}
    	
    	public void mouseClicked(MouseEvent e) {
    		
    		if(e.getButton()==e.BUTTON1 && e.getClickCount()==2) {
    			System.out.println("OK");
    			e.getPoint();
    			String ver = this.vertex;
    			System.out.println(ver);
//    			Debug.println("Selected cell is: "+(selectedCell=cell.toString()),1);
//    			if(browserCheck.isSelected()) {
//    				try {
//    					htmlPanel.navigate(selectedCell);
//    				} catch (MalformedURLException e1) {
//    					e1.printStackTrace();
//    				}
//    			}
//    			else
//    				BrowserControl.displayURL("", selectedCell);
//    		}
//    		else
    		
//    			selectedCell = "";
    		}
    	}

    }
    
}
