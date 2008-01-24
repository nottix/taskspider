/**
 * @author Simone Notargiacomo, Giuseppe Schipani
 */
package taskspider.spider.core;

import taskspider.controller.WebGraph;
import taskspider.data.document.*;
import taskspider.util.properties.PropertiesReader;
import websphinx.Link;
import websphinx.Page;
import websphinx.Region;
import websphinx.Text;
import java.util.*;
import org.apache.lucene.document.*;
import java.util.Hashtable;

/**
 * @author Simone Notargiacomo, Giuseppe Schipani
 *
 */
public class SpiderExplorer extends Thread {
	private Spider spider;
	private DocsManager docsManager;
	private boolean interrupt;
	private Hashtable<String, Page> pageTable;
	
	public SpiderExplorer(Spider spider) {
		this.spider = spider;
		docsManager = new DocsManager();
		pageTable = new Hashtable<String, Page>();
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
			//System.out.println("Roots: "+link.length);
			if(num==spider.getPagesVisited())
				out++;
			else
				out=0;
			
			if(out>6) {
				spider.stop();
			}
			else {
				num = spider.getPagesVisited();
				//System.out.println("Page: "+num);
				deepScan(link);
			/*if(link!=null) {
				for(int i=0; i<link.length; i++) {
					Page page = link[i].getPage();
					if(page!=null) {
						Link[] link2 = page.getLinks();
						System.out.println("Link: "+link2[1].toURL()+", size: "+link2.length);
					}
					if(link[i].getStatus()==LinkEvent.DOWNLOADED || link[i].getStatus()==LinkEvent.VISITED) {
						Page page = link[i].getPage();
						System.out.println("Content: "+page.getContent());
					}
					System.out.print(i+", "+link[i].getHost()+", "+link[i].toDescription());
				}
				System.out.println();
			}*/
			}
		}
		spider.stop();
	}
	
	public void interrupt() {
		System.out.println("SET");
		setInterrupt();
		spider.stop();
	}
	
	private void deepScan(Link[] link) {
		if(link!=null) {
			for(int i=0; i<link.length && !getInterrupt(); i++) {
				Page page = link[i].getPage();
				if(page!=null) {
					docsManager.addDocument(page);
					if(!pageTable.containsKey(page.getOrigin().toURL()))
						pageTable.put(page.getOrigin().toURL(), page);
					deepScan(page.getLinks());
				}
				/*if(link[i].getStatus()==LinkEvent.DOWNLOADED || link[i].getStatus()==LinkEvent.VISITED) {
					Page page = link[i].getPage();
					System.out.println("Content: "+page.getContent());
				}*/
				//System.out.print(i+", "+link[i].getHost()+", "+link[i].toDescription());
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
