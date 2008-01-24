/**
 * 
 */
package taskspider.controller;

import java.net.MalformedURLException;
import java.util.Vector;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;

import taskspider.retrival.core.Indexer;
import taskspider.retrival.core.TermSearcher;
import taskspider.retrival.wordnet.Syns2Index;
import taskspider.spider.core.Spider;
import taskspider.spider.core.SpiderExplorer;
import taskspider.spider.core.RootsSites;
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
	
	public Controller() {
		links = null;
		maxLevel = 3;
		interrupt = false;
		task = null;
		this.setDaemon(true);
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
			indexer.indexDocs(docs, start, end);
			int retry=0;
			while((start!=end || retry<4) && !interrupt) {
				if(start==end)
					retry++;
				else
					retry=0;
				System.out.println("start: "+start+", end: "+end);
				indexer.indexDocs(spiderExplorer.getDocs(), start, end);
				Thread.sleep(2000);
				start = end;
				end = docs.size();
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
