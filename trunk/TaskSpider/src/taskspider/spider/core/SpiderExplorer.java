/**
 * @author Simone Notargiacomo, Giuseppe Schipani
 */
package taskspider.spider.core;

import java.util.Hashtable;
import java.util.Vector;

import org.apache.lucene.document.Document;

import taskspider.data.document.DocsManager;
import taskspider.util.properties.PropertiesReader;
import websphinx.Link;
import websphinx.Page;

/**
 * @author Simone Notargiacomo, Giuseppe Schipani
 *
 */
public class SpiderExplorer extends Thread {
	private Spider spider;
	private DocsManager docsManager;
	private boolean interrupt;
	private Hashtable<String, Page> pageTable;
	private int explorerDelay;
	
	public SpiderExplorer(Spider spider) {
		this.spider = spider;
		docsManager = new DocsManager();
		pageTable = new Hashtable<String, Page>();
		explorerDelay = Integer.parseInt(PropertiesReader.getProperty("explorerDelay"));
		resetInterrupt();
		this.setDaemon(true);
		
	}
	
	private void resetInterrupt() {
		this.interrupt = false;
	}
	
	private void setInterrupt() {
		this.interrupt = true;
	}
	
	private boolean getInterrupt() {
		return this.interrupt;
	}
	
	public void run() {
		while(!interrupt) {
			int num=0, out=0;
			Link[] link = spider.getExploredRoots();
			if(num==spider.getPagesVisited())
				out++;
			else
				out=0;
			
			if(out>10) {
				spider.stop();
				interrupt = true;
			}
			else {
				num = spider.getPagesVisited();
				deepScan(link);
			}
			try {
				Thread.sleep(explorerDelay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		spider.stop();
	}
	
	public void stopProcess() {
		setInterrupt();
		spider.stop();
		this.interrupt();
	}
	
	private void deepScan(Link[] link) {
		if(link!=null) {
			for(int i=0; i<link.length && !getInterrupt(); i++) {
				Page page = link[i].getPage();
				if(page!=null) {
					docsManager.addDocument(page);
					if(!pageTable.containsKey(page.getOrigin().toURL())) {
						pageTable.put(page.getOrigin().toURL(), page);
					}
					deepScan(page.getLinks());
				}
			}
		}
	}
	
	public Page getPage(String url) {
		return pageTable.get(url);
	}
	
	public Vector<Document> getDocs() {
		return docsManager.getDocs();
	}
	
	public Document getDocument(int index) {
		return docsManager.getDocument(index);
	}

}
