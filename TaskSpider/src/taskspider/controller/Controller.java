/**
 * 
 */
package taskspider.controller;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JLabel;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.Query;

import taskspider.retrival.core.Indexer;
import taskspider.retrival.core.TermSearcher;
import taskspider.retrival.wordnet.Syns2Index;
import taskspider.spider.core.RootsSites;
import taskspider.spider.core.Spider;
import taskspider.spider.core.SpiderExplorer;
import taskspider.util.debug.Debug;
import taskspider.util.properties.PropertiesReader;
import taskspider.view.gui.TaskGraph;

/**
 * @author Simone Notargiacomo, Giuseppe Schipani
 *
 */
public class Controller extends Thread{

	private Spider spider;
	private Vector<String> links;
	private SpiderExplorer spiderExplorer;
	private Indexer indexer;
	private String task;
	private int maxLevel;
	private Vector<Document> docs;
	private TermSearcher searcher;
	private boolean interrupt;
	private JLabel messageLabel = null;
	private String jspUrl;
	private int indexerDelay;
	
	public Controller() {
		links = null;
		maxLevel = 3;
		interrupt = false;
		task = null;
		searcher = null;
		jspUrl = PropertiesReader.getProperty("jspUrl");
		indexerDelay = Integer.parseInt(PropertiesReader.getProperty("indexerDelay"));
		this.setDaemon(true);
	}
	
	public String getQueryString(String task, String query, String frame, String type, String exp) {
		task = task.replaceAll(" ", "%20");
		query = query.replaceAll(" ", "%20");
		return this.jspUrl+"?task="+task+"&query="+query+"&frame="+frame+"&index=0&do=1&type="+type+"&exp="+exp+"&";
	}
	
	public void setMessage(JLabel message) {
		this.messageLabel = message;
	}
	
	public void setLinks(Vector<String> urls) {
		links = urls;
	}
	
	public void setTask(String task) {
		this.task = task;
	}
	
	public void setMaxLevel(int maxLevel) {
		if(maxLevel>=0 || maxLevel<=5)
			this.maxLevel = maxLevel;
	}		
	
	public int search(String task, String query, int type) {
//		if(searcher==null)
			searcher = new TermSearcher(task);
		return searcher.search(query, type);
	}
	
	public Query getExpandedQuery() {
		return searcher.getExpandedQuery();
	}
	
	public int search(String query, int type) {
		if(searcher==null)
			searcher = new TermSearcher(task);
		return searcher.search(query, type);
	}
	
	public Hits getResult() {
		return searcher.getResult();
	}
	
	public Vector<Document> getGroupResult() {
		int index, aux=0;
		Hits hits = this.getResult();
		Hashtable<Integer, Document> scanned = new Hashtable<Integer, Document>();
		Vector<Document> result = new Vector<Document>();
		result.add(null);
		try {
			for(int i=0; i<hits.length(); i++) {
				while((index=getElementWith(hits.doc(i), hits, scanned))>=0) {
					result.add(hits.doc(index));
					Debug.println(hits.doc(index).get("url"), 1);
					aux = 1;
				}
				if(aux==1) {
					Debug.println("null", 1);
					result.add(null);
				}
				aux=0;
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private int getElementWith(Document doc, Hits hits, Hashtable<Integer, Document> scanned) {
		try {
			for(int i=0; i<hits.length(); i++) {
				int start, end;
				start = hits.doc(i).get("url").indexOf(".")+1;
				end = hits.doc(i).get("url").indexOf(".", start);
				if(start<0 || end<0)
					continue;
				
				Debug.println("KKKK: "+hits.doc(i).get("url").substring(start, end), 3);
				if(doc.get("url").indexOf(hits.doc(i).get("url").substring(start, end))>=0) {
					if(!scanned.containsKey(new Integer(i))) {
						scanned.put(new Integer(i), hits.doc(i));
						return i;
					}
						
				}
			}
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return -1;
	}
	
	public void startTaskSpider() {
		try {
			if(!IndexReader.indexExists(taskspider.util.properties.PropertiesReader.getProperty("wordnetIndexPath"))) {
				Syns2Index.generateIndex();
			}
			
			if(task==null) {
				Debug.println("Insert task", 1);
				return;
			}
			
			if(links==null) {
				RootsSites roots = new RootsSites(task);
				links = roots.getRoots();
				Debug.println("ROOTS: "+links.toString(), 1);
			}
			//links = {/*new Link("http://www.mtv.com/") *//*new Link("http://www.mtv.it")/*new Link("http://www.beppegrillo.it") *//*new Link("http://www.maglificiosalerno.it"),*/ new Link("http://www.alessioluffarelli.it")/*, new Link("http://www.google.com"), new Link("http://www.ibm.com")*/ };
			spider = new Spider(links);
			spider.setMaxLevel(maxLevel);
			spider.start();
			Debug.println("Spider started", 1);
			spiderExplorer = new SpiderExplorer(spider);
			spiderExplorer.start();
			Debug.println("SpiderExplorer started", 1);
			indexer = new Indexer(task, spiderExplorer);
			docs = spiderExplorer.getDocs();
			searcher = new TermSearcher(task);
			int start = 0;
			int end = docs.size();
			int ret = indexer.indexDocs(docs, start, end);
			int retry=0;
			while((start!=end || retry<10) && !interrupt) {
				if(start==end)
					retry++;
				else
					retry=0;
				System.out.println("start: "+start+", end: "+end);
				ret = indexer.indexDocs(spiderExplorer.getDocs(), start, end);
				Thread.sleep(indexerDelay);
				start = end;
				end = docs.size();
				if(ret!=0) {
					this.messageLabel.setText("Pages indexed");
				}
				//searcher.search("url:foto AND body:foto");
			}
		}
		catch(InterruptedException ex1) {ex1.printStackTrace();}
//		spiderExplorer.interrupt();
//		System.out.println("STOPPED "+start+" "+end);
//		indexer.getDocument();
	}
	
	public void stopProcess() {
		this.interrupt = true;
		spiderExplorer.stopProcess();
		this.interrupt();
	}
	
	public void run() {
		interrupt = false;
		this.startTaskSpider();
	}
	
	public TaskGraph getWebGraph() {
		return indexer.getWebGraph();
	}
	
//	public void setScale(double val) {
//		indexer.getWebGraph().setScale(val);
//	}
	
}
