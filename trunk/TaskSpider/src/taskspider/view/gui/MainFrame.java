/**
 * 
 */
package taskspider.view.gui;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.GridBagLayout;
import javax.swing.JSplitPane;
import java.awt.GridBagConstraints;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import javax.swing.JTextField;
import javax.swing.BoxLayout;
import java.awt.Rectangle;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import java.awt.Insets;
import javax.swing.JRadioButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.WindowConstants;
import java.awt.Point;
import java.net.MalformedURLException;
import java.awt.Event;
import java.awt.event.*;
import java.util.Vector;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;

import taskspider.controller.*;
import taskspider.retrival.core.Indexer;
import taskspider.retrival.core.TermSearcher;
import taskspider.retrival.wordnet.Syns2Index;
import taskspider.spider.core.Spider;
import taskspider.spider.core.SpiderExplorer;
import taskspider.util.debug.*;
import websphinx.Link;
import java.util.StringTokenizer;
import javax.swing.JSlider;
import org.jgraph.*;
import org.jgraph.event.*;
import org.jgraph.graph.*;
import javax.swing.plaf.metal.*;
import javax.swing.*;
import javax.swing.JTree;

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

	private JScrollPane jScrollPane = null;

	private JButton searchButton = null;

	private JPanel jPanel4 = null;

	private JButton cancelButton = null;

	private JScrollPane graphScroll = null;
	
	private JGraph graph = null;  //  @jve:decl-index=0:
	
	private Controller controller = null;

	private JButton jButton = null;

	private JSlider zoomSlider = null;

	private String selectedCell = "";  //  @jve:decl-index=0:

	private JLabel messageLabel = null;

	private JTree resultTree = null;
	
	/**
	 * This is the default constructor
	 */
	public MainFrame() {
		super();
		initialize();
		this.pack();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
//		try {
//		    UIManager.setLookAndFeel(javax.swing.plaf.metal.DefaultMetalTheme);
//		} catch (Exception e) {
//		   System.out.println(e.toString());
//		}
		this.setSize(1282, 795);
		this.setLocation(new Point(200, 200));
		this.setJMenuBar(getJJMenuBar());
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setContentPane(getJContentPane());
		this.setTitle("JFrame");
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
			GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
			gridBagConstraints15.gridx = 1;
			gridBagConstraints15.gridy = 8;
			messageLabel = new JLabel();
			messageLabel.setText("");
			messageLabel.addPropertyChangeListener("text",
					new java.beans.PropertyChangeListener() {
						public void propertyChange(java.beans.PropertyChangeEvent e) {
							getGraph(); 
						}
					});
			GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
			gridBagConstraints14.fill = GridBagConstraints.BOTH;
			gridBagConstraints14.gridy = 7;
			gridBagConstraints14.weightx = 1.0;
			gridBagConstraints14.insets = new Insets(4, 0, 4, 0);
			gridBagConstraints14.gridx = 1;
			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.gridx = 1;
			gridBagConstraints21.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints21.insets = new Insets(4, 0, 4, 0);
			gridBagConstraints21.gridy = 6;
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
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.fill = GridBagConstraints.BOTH;
			gridBagConstraints8.gridy = 0;
			gridBagConstraints8.weightx = 1.0;
			gridBagConstraints8.weighty = 1.0;
			gridBagConstraints8.gridx = 0;
			jPanel3 = new JPanel();
			jPanel3.setLayout(new GridBagLayout());
			jPanel3.add(getJScrollPane(), gridBagConstraints8);
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
			rootsField.setEnabled(false);
			rootsField.setColumns(20);
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

	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setPreferredSize(new Dimension(200, 600));
			jScrollPane.setViewportView(getResultTree());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes searchButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getSearchButton() {
		if (searchButton == null) {
			searchButton = new JButton();
			searchButton.setText("Search");
			searchButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if(!taskField.getText().equals("")) {
						graph = null;
						controller = new Controller();
						controller.setMessage(messageLabel);
						controller.setTask(taskField.getText());
						if(!rootsField.getText().equals("")) {
							StringTokenizer tokens = new StringTokenizer(rootsField.getText().replaceAll(";", " "));
							Vector<Link> links = new Vector<Link>();
							try {
								while(tokens.hasMoreTokens()) {
									links.add(new Link(tokens.nextToken()));
								}
							} catch (MalformedURLException e1) {
								e1.printStackTrace();
							}
							controller.setLinks(links);
						}
						controller.start();
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
			cancelButton.setText("Cancel");
			cancelButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					controller.stopProcess();
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
	private JScrollPane getGraphScroll() {
		if (graphScroll == null) { 
			graphScroll = new JScrollPane();
		}
		return graphScroll;
	}

	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setText("Update");
			jButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					getGraph();
				}
			});
		}
		return jButton;
	}

	private JGraph getGraph() {
		if(graph==null) {
			if(graphScroll!=null && controller.getWebGraph()!=null) {
				controller.setScale(((double)zoomSlider.getValue())/10);
				graph = controller.getWebGraph().getGraph();
				graphScroll.setViewportView(graph);
				Debug.println("Graph updated", 1);
			}
			graph.addGraphSelectionListener(new GraphSelectionListener() {
				public void valueChanged(GraphSelectionEvent e) {

				}
			});
			
			graph.addMouseListener(new java.awt.event.MouseListener() {
				public void mouseClicked(MouseEvent e) {
					if(e.getClickCount()==2) {
						DefaultGraphCell cell = (DefaultGraphCell)graph.getSelectionCellAt(e.getPoint());
						if(cell!=null) {
							Debug.println("Selected cell is: "+(selectedCell=cell.toString()),1);
							BrowserControl.displayURL("", selectedCell);
						}
						else
							selectedCell = "";
						
					}
			    }

			    public void mousePressed(MouseEvent e) {   
			    }
			    
			    public void mouseReleased(MouseEvent e) {
			    }
			    
			    public void mouseEntered(MouseEvent e) {  
			    }
			    
			    public void mouseExited(MouseEvent e) {  
			    }
			});

		}
		else {
			controller.setScale(((double)zoomSlider.getValue())/10);
			graphScroll.setViewportView(graph);
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
			zoomSlider.setMinimum(0);
			zoomSlider.setMaximum(10);
			zoomSlider.setValue(5);
			zoomSlider.addChangeListener(new javax.swing.event.ChangeListener() {
				public void stateChanged(javax.swing.event.ChangeEvent e) {
					getGraph();
				}
			});
		}
		return zoomSlider;
	}

	/**
	 * This method initializes resultTree	
	 * 	
	 * @return javax.swing.JTree	
	 */
	private JTree getResultTree() {
		if (resultTree == null) {
			resultTree = new JTree();
		}
		return resultTree;
	}

	public static void main(String args[]) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MainFrame thisClass = new MainFrame();
				thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				thisClass.setVisible(true);
			}
		});
	}

}  //  @jve:decl-index=0:visual-constraint="10,11"
