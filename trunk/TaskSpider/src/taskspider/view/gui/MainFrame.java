/**
 * 
 */
package taskspider.view.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.color.*;
import java.awt.Color;
import java.net.MalformedURLException;
import java.util.StringTokenizer;
import java.util.Vector;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.JScrollBar;

import org.jgraph.JGraph;
import org.jgraph.event.GraphSelectionEvent;
import org.jgraph.event.GraphSelectionListener;
import org.jgraph.graph.DefaultGraphCell;
import org.lobobrowser.gui.BrowserPanel;
import org.lobobrowser.main.PlatformInit;

import taskspider.controller.BrowserControl;
import taskspider.controller.Controller;
import taskspider.util.debug.Debug;
import taskspider.util.properties.PropertiesReader;
import edu.uci.ics.jung.visualization.control.*;
import websphinx.Link;

import org.apache.lucene.document.Document;

import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.algorithms.layout.*;
import edu.uci.ics.jung.algorithms.layout.util.Relaxer;

/**
 * @author avenger
 *
 */
public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	private JPanel jPanel = null;

	private JPanel jPanel1 = null;

	private JPanel jPanel2 = null;

	private JPanel jPanel3 = null;

	private JMenuBar jJMenuBar = null;

	private JMenu jMenu = null;

	private JLabel jLabel = null;

	private JTextField taskField = null;

	private JLabel jLabel1 = null;

	private JLabel jLabel2 = null;

	private JTextField queryField = null;

	private JTextField rootsField = null;

	private JCheckBox rootsCheck = null;

	private JLabel jLabel3 = null;

	private JComboBox deepCombo = null;

	private JSplitPane jSplitPane = null;

	private JButton searchButton = null;

	private JPanel jPanel4 = null;

	private JButton cancelButton = null;

	private JPanel graphScroll = null;
	
	private VisualizationViewer<String,String> graph = null;  //  @jve:decl-index=0:
	
	private Controller controller = null;

	private JButton updateButton = null;

	private JSlider zoomSlider = null;

	private String selectedCell = "";  //  @jve:decl-index=0:

	private JLabel messageLabel = null;
	
	private BrowserPanel htmlPanel = null;

	private JCheckBox browserCheck = null;

	private JPanel jPanel5 = null;

	private JCheckBox frameCheck = null;
	
	//private HtmlRendererContext rendererContext = null;

	/**
	 * This is the default constructor
	 */
	public MainFrame() {
		super();
		initialize();
		this.setProperties();
		//this.pack();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(1282, 795);
		this.setLocation(new Point(200, 200));
		this.setJMenuBar(getJJMenuBar());
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setContentPane(getJContentPane());
		this.setTitle("TaskSpider");
		this.setVisible(true);
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BoxLayout(getJContentPane(), BoxLayout.X_AXIS));
			jContentPane.add(getJSplitPane(), null);
		}
		return jContentPane;
	}
	
	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
			gridBagConstraints17.gridx = 1;
			gridBagConstraints17.gridy = 5;
			GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
			gridBagConstraints15.gridx = 1;
			gridBagConstraints15.gridy = 9;
			messageLabel = new JLabel();
			messageLabel.setText("");
			messageLabel.addPropertyChangeListener("text",
					new java.beans.PropertyChangeListener() {
						public void propertyChange(java.beans.PropertyChangeEvent e) {
							if(e.getNewValue().equals("Pages indexed")) {
								getGraph(); 
//								controller.search(queryField.getText());
//								try {
//									Thread.sleep(2000);
//								} catch (InterruptedException e2) {
//									// TODO Auto-generated catch block
//									e2.printStackTrace();
//								}
								//controller.getGroupResult();
								if(browserCheck.isSelected()) {
									try {
										htmlPanel.navigate(controller.getQueryString(taskField.getText(), queryField.getText(), frameCheck.isSelected() ? "1" : "0"));
									} catch (MalformedURLException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
								}
								else {
									BrowserControl.displayURL("", controller.getQueryString(taskField.getText(), queryField.getText(), frameCheck.isSelected() ? "1" : "0"));
								}
							}
						}
					});
			GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
			gridBagConstraints14.fill = GridBagConstraints.BOTH;
			gridBagConstraints14.gridy = 8;
			gridBagConstraints14.weightx = 1.0;
			gridBagConstraints14.insets = new Insets(4, 0, 4, 0);
			gridBagConstraints14.gridx = 1;
			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.gridx = 1;
			gridBagConstraints21.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints21.insets = new Insets(4, 0, 4, 0);
			gridBagConstraints21.gridy = 7;
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints11.gridy = 4;
			gridBagConstraints11.weightx = 1.0;
			gridBagConstraints11.insets = new Insets(4, 0, 4, 0);
			gridBagConstraints11.gridx = 1;
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.gridx = 0;
			gridBagConstraints10.gridy = 4;
			jLabel3 = new JLabel();
			jLabel3.setText("Deep level:");
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridx = 1;
			gridBagConstraints6.weightx = 1.0;
			gridBagConstraints6.insets = new Insets(4, 0, 4, 0);
			gridBagConstraints6.gridy = 2;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints5.gridy = 3;
			gridBagConstraints5.weightx = 1.0;
			gridBagConstraints5.insets = new Insets(4, 0, 4, 0);
			gridBagConstraints5.gridx = 1;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints4.gridy = 1;
			gridBagConstraints4.weightx = 1.0;
			gridBagConstraints4.insets = new Insets(4, 0, 4, 0);
			gridBagConstraints4.gridx = 1;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.weightx = 0.0;
			gridBagConstraints3.weighty = 1.0;
			gridBagConstraints3.gridy = 3;
			jLabel2 = new JLabel();
			jLabel2.setText("Manual roots:");
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.weighty = 1.0;
			gridBagConstraints2.gridy = 1;
			jLabel1 = new JLabel();
			jLabel1.setText("Query:");
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.weighty = 0.0;
			gridBagConstraints.insets = new Insets(4, 0, 4, 0);
			gridBagConstraints.gridx = 1;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.weighty = 1.0;
			gridBagConstraints1.gridy = 0;
			jLabel = new JLabel();
			jLabel.setText("Task:");
			jPanel = new JPanel();
			jPanel.setLayout(new GridBagLayout());
			jPanel.add(jLabel, gridBagConstraints1);
			jPanel.add(getTaskField(), gridBagConstraints);
			jPanel.add(jLabel1, gridBagConstraints2);
			jPanel.add(jLabel2, gridBagConstraints3);
			jPanel.add(getQueryField(), gridBagConstraints4);
			jPanel.add(getRootsField(), gridBagConstraints5);
			jPanel.add(getRootsCheck(), gridBagConstraints6);
			jPanel.add(jLabel3, gridBagConstraints10);
			jPanel.add(getDeepCombo(), gridBagConstraints11);
			jPanel.add(getJPanel4(), gridBagConstraints21);
			jPanel.add(getZoomSlider(), gridBagConstraints14);
			jPanel.add(messageLabel, gridBagConstraints15);
			jPanel.add(getJPanel5(), gridBagConstraints17);
		}
		return jPanel;
	}

	/**
	 * This method initializes jPanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.fill = GridBagConstraints.BOTH;
			gridBagConstraints12.gridy = 0;
			gridBagConstraints12.weightx = 1.0;
			gridBagConstraints12.weighty = 1.0;
			gridBagConstraints12.gridx = 0;
			jPanel1 = new JPanel();
			jPanel1.setLayout(new GridBagLayout());
			jPanel1.setPreferredSize(new Dimension(600, 400));
			jPanel1.add(getGraphScroll(), gridBagConstraints12);
		}
		return jPanel1;
	}

	/**
	 * This method initializes jPanel2	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel2() {
		if (jPanel2 == null) {
			jPanel2 = new JPanel();
			jPanel2.setLayout(new BoxLayout(getJPanel2(), BoxLayout.Y_AXIS));
			jPanel2.add(getJPanel(), null);
			jPanel2.add(getJPanel1(), null);
		}
		return jPanel2;
	}

	/**
	 * This method initializes jPanel3	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel3() {
		if (jPanel3 == null) {
			jPanel3 = new JPanel();
			jPanel3.setLayout(new BoxLayout(getJPanel3(), BoxLayout.Y_AXIS));
			jPanel3.add(getHtmlPanel(), null);
		}
		return jPanel3;
	}

	/**
	 * This method initializes jJMenuBar	
	 * 	
	 * @return javax.swing.JMenuBar	
	 */
	private JMenuBar getJJMenuBar() {
		if (jJMenuBar == null) {
			jJMenuBar = new JMenuBar();
			jJMenuBar.add(getJMenu());
		}
		return jJMenuBar;
	}

	/**
	 * This method initializes jMenu	
	 * 	
	 * @return javax.swing.JMenu	
	 */
	private JMenu getJMenu() {
		if (jMenu == null) {
			jMenu = new JMenu();
			jMenu.setText("File");
		}
		return jMenu;
	}

	/**
	 * This method initializes taskField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTaskField() {
		if (taskField == null) {
			taskField = new JTextField();
			taskField.setPreferredSize(new Dimension(400, 19));
			taskField.setColumns(20);
		}
		return taskField;
	}

	/**
	 * This method initializes queryField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getQueryField() {
		if (queryField == null) {
			queryField = new JTextField();
			queryField.setPreferredSize(new Dimension(400, 19));
			queryField.setColumns(20);
		}
		return queryField;
	}

	/**
	 * This method initializes rootsField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getRootsField() {
		if (rootsField == null) {
			rootsField = new JTextField();
			rootsField.setPreferredSize(new Dimension(400, 19));
			rootsField.setEnabled(true);
			rootsField.setColumns(20);
			rootsField.addPropertyChangeListener("enabled",
					new java.beans.PropertyChangeListener() {
						public void propertyChange(java.beans.PropertyChangeEvent e) {
							rootsField.setText(""); 
						}
					});
		}
		return rootsField;
	}

	/**
	 * This method initializes rootsCheck	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getRootsCheck() {
		if (rootsCheck == null) {
			rootsCheck = new JCheckBox();
			rootsCheck.setText("Manual inserting roots (separate with \" ; \")");
			rootsCheck.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if(rootsCheck.isSelected())
						rootsField.setEnabled(true);
					else
						rootsField.setEnabled(false);
				}
			});
		}
		return rootsCheck;
	}

	/**
	 * This method initializes deepCombo	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getDeepCombo() {
		if (deepCombo == null) {
			deepCombo = new JComboBox();
			deepCombo.setMaximumRowCount(5);
			deepCombo.insertItemAt("1", 0);
			deepCombo.insertItemAt("2", 1);
			deepCombo.insertItemAt("3", 2);
			deepCombo.insertItemAt("4", 3);
			deepCombo.insertItemAt("5", 4);
			deepCombo.setSelectedIndex(2);
		}
		return deepCombo;
	}

	/**
	 * This method initializes jSplitPane	
	 * 	
	 * @return javax.swing.JSplitPane	
	 */
	private JSplitPane getJSplitPane() {
		if (jSplitPane == null) {
			jSplitPane = new JSplitPane();
			jSplitPane.setOneTouchExpandable(true);
			jSplitPane.setLeftComponent(getJPanel2());
			jSplitPane.setRightComponent(getJPanel3());
		}
		return jSplitPane;
	}

	private BrowserPanel getHtmlPanel() {
		if(htmlPanel == null) {
			try {
//				String uri = "http://lobobrowser.org/java-browser.jsp";
//				URL url = new URL(uri);
//				URLConnection connection = url.openConnection();
//				InputStream in = connection.getInputStream();
//				Reader reader = new InputStreamReader(in);
//
//				htmlPanel = new HtmlPanel();
//				rendererContext = new LocalHtmlRendererContext(htmlPanel);
//				//SimpleHtmlRendererContext rendererContext = new SimpleHtmlRendererContext(htmlPanel);
//				htmlPanel.setPreferredSize(new Dimension(800, 600));
//				htmlPanel.setBackground(Color.black);
//				htmlPanel.setBorder(BorderFactory.createTitledBorder(new EtchedBorder(EtchedBorder.RAISED), "Embedded browser"));
//				htmlPanel.setLayout(new BoxLayout(getJPanel3(), BoxLayout.Y_AXIS));
//				htmlPanel.add(new JButton(), null);
//				
//				// InputSourceImpl constructor with URI recommended
//				// so the renderer can resolve page component URLs.
//				InputSource is = new InputSourceImpl(reader, uri);
//				DocumentBuilderImpl builder = new DocumentBuilderImpl(rendererContext.getUserAgentContext(), rendererContext);
//				Document document = builder.parse(is);
//				in.close();
//
//				// Set the document in the HtmlPanel. This
//				// is what lets the document render.
//				htmlPanel.setDocument(document, rendererContext);
				
				htmlPanel = new BrowserPanel();
				
//				htmlPanel.navigate("http://www.google.com");

				htmlPanel.setEnabled(true);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return htmlPanel;
	}
	
	/**
	 * This method initializes searchButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getSearchButton() {
		if (searchButton == null) {
			searchButton = new JButton();
			searchButton.setText("Start & Search");
			searchButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if(!taskField.getText().equals("")) {
						graph = null;
						controller = new Controller();
						controller.setMessage(messageLabel);
						controller.setTask(taskField.getText());
						if(!rootsField.getText().equals("")) {
							StringTokenizer tokens = new StringTokenizer(rootsField.getText().replaceAll(";", " "));
							String temp;
							Vector<String> links = new Vector<String>();
							while(tokens.hasMoreTokens()) {
								temp = tokens.nextToken();
								Debug.println("LINK: "+temp, 1);
								links.add(temp);
							}
							controller.setLinks(links);
						}
						controller.start();
						messageLabel.setText("Searching...");
					}
				}
			});
		}
		return searchButton;
	}

	/**
	 * This method initializes jPanel4	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel4() {
		if (jPanel4 == null) {
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.weightx = 1.0;
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.weightx = 1.0;
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.gridx = -1;
			gridBagConstraints7.anchor = GridBagConstraints.CENTER;
			gridBagConstraints7.weightx = 1.0;
			gridBagConstraints7.gridy = -1;
			jPanel4 = new JPanel();
			jPanel4.setLayout(new GridBagLayout());
			jPanel4.add(getSearchButton(), gridBagConstraints7);
			jPanel4.add(getCancelButton(), gridBagConstraints9);
			jPanel4.add(getJButton(), gridBagConstraints13);
		}
		return jPanel4;
	}

	/**
	 * This method initializes cancelButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getCancelButton() {
		if (cancelButton == null) {
			cancelButton = new JButton();
			cancelButton.setText("Stop");
			cancelButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					controller.stopProcess();
					messageLabel.setText("Search stopped!");
				}
			});
		}
		return cancelButton;
	}

	/**
	 * This method initializes graphScroll	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JPanel getGraphScroll() {
		if (graphScroll == null) { 
			graphScroll = new JPanel();
			
			graphScroll.setLayout(new GridBagLayout());
			
			
			
//			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
//			gridBagConstraints13.weightx = 1.0;
//			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
//			gridBagConstraints9.weightx = 1.0;
//			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
//			gridBagConstraints7.gridx = -1;
//			gridBagConstraints7.anchor = GridBagConstraints.CENTER;
//			gridBagConstraints7.weightx = 1.0;
//			gridBagConstraints7.gridy = -1;
//			jPanel4 = new JPanel();
//			jPanel4.setLayout(new GridBagLayout());
//			jPanel4.add(getSearchButton(), gridBagConstraints7);
//			jPanel4.add(getCancelButton(), gridBagConstraints9);
//			jPanel4.add(getJButton(), gridBagConstraints13);
		}
		return graphScroll;
	}

	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton() {
		if (updateButton == null) {
			updateButton = new JButton();
			updateButton.setText("Search");
			updateButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					//getGraph();
					
//					graph = null;
//					getGraph();
					
//					Layout<String, String> layout = graph.getGraphLayout();
//					layout.initialize();
//					Relaxer relaxer = graph.getModel().getRelaxer();
//					if(relaxer != null) {
////					if(layout instanceof IterativeContext) {
//						relaxer.stop();
//						relaxer.prerelax();
//						relaxer.relax();
//					}

					if(controller==null)
						controller = new Controller();
					System.out.println("RESULT: "+controller.search(queryField.getText()));
					if(browserCheck.isSelected()) {
						try {
							htmlPanel.navigate(controller.getQueryString(taskField.getText(), queryField.getText(), frameCheck.isSelected() ? "1" : "0"));
						} catch (MalformedURLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					else {
						BrowserControl.displayURL("", controller.getQueryString(taskField.getText(), queryField.getText(), frameCheck.isSelected() ? "1" : "0"));
					}
					
//					controller.search(queryField.getText());
//					Vector<Document> docs = controller.getGroupResult();
//					for(int i=0; i<docs.size(); i++) {
//						if(docs.get(i)==null)
//							continue;
//						System.out.println("docs["+i+"]: "+docs.get(i).get("url"));
//					}
				}
			});
		}
		return updateButton;
	}

	private VisualizationViewer<String,String> getGraph() {
		if(graph==null) {
			if(graphScroll!=null && controller.getWebGraph()!=null) {
//				controller.setScale(((double)zoomSlider.getValue())/10);
				graph = controller.getWebGraph().getVisualization();
				controller.getWebGraph().setBrowserCheck(browserCheck.isSelected());
				controller.getWebGraph().setHtmlPanel(htmlPanel);
				GraphZoomScrollPane scroll = new GraphZoomScrollPane(graph);
//				graphScroll.setViewportView(graph);
				
				GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
				gridBagConstraints13.weightx = 1.0;
				gridBagConstraints13.weighty = 1.0;
				gridBagConstraints13.fill = GridBagConstraints.BOTH;
				
				graphScroll.removeAll();
				graphScroll.add(scroll, gridBagConstraints13);
				graph.setBackground(Color.white);
				
				JScrollBar bar = scroll.getHorizontalScrollBar();
				System.out.println("VALUE1: "+bar.getMaximum());
				bar.setValue(bar.getMaximum()/2);
				System.out.println("VALUE2: "+bar.getMinimum());
				bar = scroll.getVerticalScrollBar();
				bar.setValue(bar.getMaximum()/2);
				
				//graphScroll.add(graph);
				Debug.println("Graph updated", 1);
			}
//			graph.addGraphSelectionListener(new GraphSelectionListener() {
//				public void valueChanged(GraphSelectionEvent e) {
//
//				}
//			});
			
//			graph.addMouseListener(new java.awt.event.MouseListener() {
//				public void mouseClicked(MouseEvent e) {
//					if(e.getClickCount()==2) {
//						DefaultGraphCell cell = (DefaultGraphCell)graph.getSelectionCellAt(e.getPoint());
//						if(cell!=null) {
//							Debug.println("Selected cell is: "+(selectedCell=cell.toString()),1);
//							if(browserCheck.isSelected()) {
//								try {
//									htmlPanel.navigate(selectedCell);
//								} catch (MalformedURLException e1) {
//									e1.printStackTrace();
//								}
//							}
//							else
//								BrowserControl.displayURL("", selectedCell);
//						}
//						else
//							selectedCell = "";
//						
//					}
//			    }
//				
//			    public void mousePressed(MouseEvent e) {   
//			    }
//			    
//			    public void mouseReleased(MouseEvent e) {
//			    }
//			    
//			    public void mouseEntered(MouseEvent e) {  
//			    }
//			    
//			    public void mouseExited(MouseEvent e) {  
//			    }
//			});
//			
//			graph.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
//				public void mouseWheelMoved(MouseWheelEvent e) {
//					Debug.println("Wheel: "+e.getWheelRotation()+", SCALE: "+graph.getScale(), 1);
//					if(e.getWheelRotation()<0) {
//						graph.setScale(graph.getScale()+0.1);
//						zoomSlider.setValue((int)(graph.getScale()*10));
//					}
//					else {
//						graph.setScale(graph.getScale()-0.1);
//						zoomSlider.setValue((int)(graph.getScale()*10));
//					}
//					graphScroll.setViewportView(graph);
//					
//				}
//			});

		}
		else {
//			controller.setScale(((double)zoomSlider.getValue())/10);
//			graphScroll.setViewportView(graph);
			Debug.println("Graph updated", 1);
		}
		return graph;
	}
	
	/**
	 * This method initializes zoomSlider	
	 * 	
	 * @return javax.swing.JSlider	
	 */
	private JSlider getZoomSlider() {
		if (zoomSlider == null) {
			zoomSlider = new JSlider();
			zoomSlider.setMinimum(1);
			zoomSlider.setMaximum(20);
			zoomSlider.setValue(10);
			zoomSlider.addChangeListener(new javax.swing.event.ChangeListener() {
				public void stateChanged(javax.swing.event.ChangeEvent e) {
					ScalingControl scaler = new CrossoverScalingControl();
					if(graph!=null)//TODO
						scaler.scale(graph, (float)0.1*zoomSlider.getValue(), graph.getCenter());
				}
			});
		}
		return zoomSlider;
	}

	/**
	 * This method initializes browserCheck	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getBrowserCheck() {
		if (browserCheck == null) {
			browserCheck = new JCheckBox();
			browserCheck.setText("Embedded browser");
			browserCheck.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					controller.getWebGraph().setBrowserCheck(browserCheck.isSelected());
				}
			});
		}
		return browserCheck;
	}
	
	private void setProperties() {
		if(PropertiesReader.getProperty("manualRoots").equals("1"))
			rootsCheck.setSelected(true);
		else
			rootsCheck.setSelected(false);
		
		if(PropertiesReader.getProperty("embeddedBrowser").equals("1"))
			browserCheck.setSelected(true);
		else
			browserCheck.setSelected(false);
		
		if(PropertiesReader.getProperty("browserWithFrame").equals("1"))
			frameCheck.setSelected(true);
		else
			frameCheck.setSelected(false);
	}

	/**
	 * This method initializes jPanel5	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel5() {
		if (jPanel5 == null) {
			GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
			gridBagConstraints16.gridx = 1;
			gridBagConstraints16.gridy = 0;
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.gridx = -1;
			gridBagConstraints8.gridy = -1;
			jPanel5 = new JPanel();
			jPanel5.setLayout(new GridBagLayout());
			jPanel5.add(getBrowserCheck(), gridBagConstraints8);
			jPanel5.add(getFrameCheck(), gridBagConstraints16);
		}
		return jPanel5;
	}

	/**
	 * This method initializes frameCheck	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getFrameCheck() {
		if (frameCheck == null) {
			frameCheck = new JCheckBox();
			frameCheck.setText("Browser with frame");
		}
		return frameCheck;
	}

	public static void main(String args[]) {
		try {
//			PlatformInit.getInstance().init(args, true);
			PlatformInit.getInstance().initExtensions();
			PlatformInit.getInstance().initProtocols();
			PlatformInit.getInstance().initSecurity();
			String[] arg = {"-debug"};
			PlatformInit.getInstance().initLogging(arg);
//			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MainFrame thisClass = new MainFrame();
				thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				thisClass.setVisible(true);
			}
		});
	}
	
//	private static class LocalHtmlRendererContext extends SimpleHtmlRendererContext {
//		// Override methods here to implement browser functionality
//		public LocalHtmlRendererContext(HtmlPanel contextComponent) {
//			super(contextComponent);
//		}
//	}

}  //  @jve:decl-index=0:visual-constraint="10,11"
