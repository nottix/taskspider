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
			crawler.addRoot(new Link("http://taskspider.googlecode.com"));
			crawler.addClassifier(new StandardClassifier());
			
	        Thread thread = new Thread (crawler, crawler.getName ());
	        thread.setDaemon (true);
	        thread.start ();
			//crawler.run();
			while(true) {
				Link[] link = crawler.getRoots();
				System.out.println("Page: "+crawler.getPagesVisited()+", Link: ");
				if(link!=null) {
					for(int i=0; i<link.length; i++) {
						System.out.print(link[i]+", ");
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
