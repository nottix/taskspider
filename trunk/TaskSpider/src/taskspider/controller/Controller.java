/**
 * 
 */
package taskspider.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Vector;
import java.util.Hashtable;

import javax.swing.JLabel;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;

import taskspider.retrival.core.Indexer;
import taskspider.retrival.core.TermSearcher;
import taskspider.retrival.wordnet.Syns2Index;
import taskspider.spider.core.Spider;
import taskspider.spider.core.SpiderExplorer;
import taskspider.spider.core.RootsSites;
import org.apache.lucene.search.Hits;
import taskspider.util.debug.Debug;
import websphinx.Link;

/**
 * @author Simone Notargiacomo, Giuseppe Schipani
 *
 */
public class Controller extends Thread{

	private Spider spider;
	private Vector<Link> links;
	private SpiderExplorer spiderExplorer;
	private Indexer indexer;
	private String task;
	private int maxLevel;
	private Vector<Document> docs;
	private TermSearcher searcher;
	private boolean interrupt;
	private JLabel messageLabel = null;
	
	public Controller() {
		links = null;
		maxLevel = 3;
		interrupt = false;
		task = null;
		searcher = null;
		this.setDaemon(true);
	}
	
	public void setMessage(JLabel message) {
		this.messageLabel = message;
	}
	
	public void setLinks(Vector<Link> urls) {
		links = urls;
	}
	
	public void setTask(String task) {
		this.task = task;
	}
	
	public void setMaxLevel(int maxLevel) {
		if(maxLevel>=0 || maxLevel<=5)
			this.maxLevel = maxLevel;
	}		
	
	public int search(String task, String query) {
		if(searcher==null)
			searcher = new TermSearcher(task);
		return searcher.search(query);
	}
	
	public int search(String query) {
		if(searcher==null)
			searcher = new TermSearcher(task);
		return searcher.search(query);
	}
	
	public Hits getResult() {
		return searcher.getResult();
	}
	
	public Vector<Document> getGroupResult() {
		int index;
		Hits hits = this.getResult();
		Hashtable<Integer, Document> scanned = new Hashtable<Integer, Document>();
		Vector<Document> result = new Vector<Document>();
		try {
			for(int i=0; i<hits.length(); i++) {
				while((index=getElementWith(hits.doc(i), hits, scanned))>=0) {
					result.add(hits.doc(i));
					Debug.println(hits.doc(i).get("url"), 1);
				}
				Debug.println("null", 1);
				result.add(null);
			}
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
			while((start!=end || retry<4) && !interrupt) {
				if(start==end)
					retry++;
				else
					retry=0;
				System.out.println("start: "+start+", end: "+end);
				ret = indexer.indexDocs(spiderExplorer.getDocs(), start, end);
				Thread.sleep(2000);
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
	
	public WebGraph getWebGraph() {
		return indexer.getWebGraph();
	}
	
	public void setScale(double val) {
		indexer.getWebGraph().setScale(val);
	}
	
}
