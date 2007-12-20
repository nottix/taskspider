/**
 * TaskSpider Testing class
 * @author Simone Notargiacomo
 */
package taskspider.crawler.testing;

import websphinx.searchengine.*;
import websphinx.*;
import java.net.*;

/**
 * @author Simone Notargiacomo
 */
public class Testing {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Crawler crawler = new Crawler();
		try {
			crawler.addRoot(new Link("http://www.cs.cmu.edu/~rcm"));
			crawler.addClassifier(new StandardClassifier());
			
	        Thread thread = new Thread (crawler, crawler.getName ());
	        thread.setDaemon (true);
	        thread.start ();
			while(true) {
				Link[] link = crawler.getCrawledRoots();
				System.out.println("Page: "+crawler.getPagesVisited()+", Link: ");
				if(link!=null) {
					for(int i=0; i<link.length; i++) {
						System.out.print(i+", "+link[i].getHost()+", "+link[i].toDescription());
					}
					System.out.println();
				}
				Thread.sleep(1000);
			}
		}
		catch(MalformedURLException ex) {}
		catch(InterruptedException ex) {}
	
	}

}
