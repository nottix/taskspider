/**
 * TaskSpider Testing class
 * @author Simone Notargiacomo, Giuseppe Schipani
 */
package taskspider.spider.testing;

import websphinx.searchengine.*;
import websphinx.*;

import java.net.*;
import java.io.*;

import taskspider.spider.core.*;
import taskspider.data.document.*;

/**
 * @author Simone Notargiacomo, Giuseppe Schipani
 */
public class Testing {
	
	private static DocsManager docsManager = new DocsManager();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			int num=0, out=0;
			Link[] links = { new Link("http://www.google.com"), new Link("http://www.ibm.com") };
			Spider spider = new Spider(links);
			spider.start();
			while(true) {
				Link[] link = spider.getExploredRoots();
				System.out.println("Roots: "+link.length);
				if(num==spider.getPagesVisited())
					out++;
				else
					out=0;
				
				if(out>6) {
					spider.stop();
					break;
				}
				num = spider.getPagesVisited();
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
				Thread.sleep(2000);
			}
		}
		catch(MalformedURLException ex) {ex.printStackTrace();}
		catch(InterruptedException ex) {ex.printStackTrace();}
		catch(IOException ex) {ex.printStackTrace();}
	
	}
	
	public static void deepScan(Link[] link) {
		String url;
		String words = "";
		String tokens = "";
		if(link!=null) {
			for(int i=0; i<link.length; i++) {
				Page page = link[i].getPage();
				if(page!=null) {
					Text[] text = page.getWords();
					Region[] region = page.getTokens();
					if(text==null || region==null)
						continue;
					for(int j=0; j<text.length; j++) {
						words += text[j].toString()+", ";
					}
					for(int j=0; j<region.length; j++) {
						tokens += region[j].toString()+", ";
					}
					url = link[i].toURL();
					System.out.println("URL: "+url);
					System.out.println("Words: "+words);
					System.out.println("Tokens: "+tokens);
					System.out.println();
					docsManager.addDocument(url, words, tokens);

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

}
