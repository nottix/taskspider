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

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Link[] links = { new Link("http://www.beppegrillo.it/")/*, new Link("http://www.google.com"), new Link("http://www.ibm.com")*/ };
			Spider spider = new Spider(links);
			spider.start();
			SpiderExplorer spiderExplorer = new SpiderExplorer(spider);
			spiderExplorer.start();
			spiderExplorer.getDocs();
			Thread.sleep(12000);
			spiderExplorer.interrupt();
			System.out.println("STOPPED");
			
		}
		catch(MalformedURLException ex) {ex.printStackTrace();}
		catch(InterruptedException ex1) {ex1.printStackTrace();}
	
	}

}
