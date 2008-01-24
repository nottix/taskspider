/**
 * TaskSpider Testing class
 * @author Simone Notargiacomo, Giuseppe Schipani
 */
package taskspider.spider.testing;

import websphinx.searchengine.*;
import websphinx.*;

import java.net.*;
import java.io.*;
import java.util.*;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.*;

import taskspider.spider.core.*;
import taskspider.util.properties.*;
import taskspider.data.document.*;
import taskspider.retrival.core.*;
import taskspider.retrival.wordnet.*;

/**
 * @author Simone Notargiacomo, Giuseppe Schipani
 */
public class Testing {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		try {
//			if(!IndexReader.indexExists(taskspider.util.properties.PropertiesReader.getProperty("wordnetIndexPath"))) {
//				Syns2Index.generateIndex();
//			}
//			Link[] links = { new Link("http://www.mtv.com/") /*new Link("http://www.mtv.it")/*new Link("http://www.beppegrillo.it") *//*new Link("http://www.maglificiosalerno.it"),*/ /*new Link("http://www.alessioluffarelli.it")/*, new Link("http://www.google.com"), new Link("http://www.ibm.com")*/ };
//			Spider spider = new Spider(links);
//			spider.setMaxLevel(1);
//			spider.start();
//			SpiderExplorer spiderExplorer = new SpiderExplorer(spider);
//			spiderExplorer.start();
//			Indexer indexer = new Indexer("musica", spiderExplorer);
//			Vector<Document> docs = spiderExplorer.getDocs();
//			TermSearcher searcher = new TermSearcher("musica");
//			int start = 0;
//			int end = docs.size();
//			indexer.indexDocs(docs, start, end);
//			int retry=0;
//			while(start!=end || retry<4) {
//				if(start==end)
//					retry++;
//				else
//					retry=0;
//				System.out.println("start: "+start+", end: "+end);
//				
//				indexer.indexDocs(spiderExplorer.getDocs(), start, end);
//				Thread.sleep(2000);
//				start = end;
//				end = docs.size();
//				searcher.search("url:music AND body:music");
//				
//			}
//			spiderExplorer.interrupt();
//			System.out.println("STOPPED "+start+" "+end);
//			indexer.getDocument();
//			
//		}
//		catch(MalformedURLException ex) {ex.printStackTrace();}
//		catch(InterruptedException ex1) {ex1.printStackTrace();}
	
	}

}
