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

/**
 * @author Simone Notargiacomo, Giuseppe Schipani
 */
public class Testing {

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
		if(link!=null) {
			for(int i=0; i<link.length; i++) {
				Page page = link[i].getPage();
				if(page!=null) {
					//System.out.println("Link: "+link[i].toURL()+", size: "+link.length);
					Text[] text = page.getWords();
					Region[] region = page.getTokens();
					if(text==null)
						continue;
//					System.out.println("Text: ");
//					for(int j=0; j<text.length; j++) {
//						System.out.print(text[j].toString()+", ");
//					}
//					for(int j=0; j<text.length; j++) {
//						System.out.print(region[j].toString()+", ");
//					}
//					System.out.println();
					System.out.print(i+" - ");
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
