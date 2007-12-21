/**
 * TaskSpider Testing class
 * @author Simone Notargiacomo
 */
package taskspider.crawler.testing;

import websphinx.searchengine.*;
import websphinx.*;
import java.net.*;

/**
 * @author Simone Notargiacomo, Giuseppe Schipani
 */
public class Testing {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Crawler crawler = new Crawler();
		try {
			int num=0, out=0;
			crawler.addRoot(new Link("http://www.docmirror.net/it/linux/howto/apps/Alsa-sound/"));
			crawler.addClassifier(new StandardClassifier());

	        Thread thread = new Thread (crawler, crawler.getName ());
	        thread.setDaemon (true);
	        thread.start ();
			while(true) {
				Link[] link = crawler.getCrawledRoots();
				if(num==crawler.getPagesVisited())
					out++;
				else
					out=0;
				
				if(out>6) {
					thread.interrupt();
					break;
				}
				num = crawler.getPagesVisited();
				System.out.println("Page: "+num);
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
				Thread.sleep(1000);
			}
		}
		catch(MalformedURLException ex) {}
		catch(InterruptedException ex) {}
	
	}
	
	public static void deepScan(Link[] link) {
		if(link!=null) {
			for(int i=0; i<link.length; i++) {
				Page page = link[i].getPage();
				if(page!=null) {
					//System.out.println("Link: "+link[i].toURL()+", size: "+link.length);
					Text[] text = page.getWords();
					if(text==null)
						continue;
					System.out.println("Text: ");
					for(int j=0; j<text.length; j++) {
						System.out.print(text[j].toString()+", ");
					}
					System.out.println();
					deepScan(page.getLinks());
				}
				/*if(link[i].getStatus()==LinkEvent.DOWNLOADED || link[i].getStatus()==LinkEvent.VISITED) {
					Page page = link[i].getPage();
					System.out.println("Content: "+page.getContent());
				}*/
				//System.out.print(i+", "+link[i].getHost()+", "+link[i].toDescription());
			}
			//System.out.println();
		}
	}

}
