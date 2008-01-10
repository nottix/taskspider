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
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.RAMDirectory;

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
			Link[] links = { new Link("http://www.maglificiosalerno.it"), new Link("http://www.alessioluffarelli.it")/*, new Link("http://www.google.com"), new Link("http://www.ibm.com")*/ };
			Spider spider = new Spider(links);
			spider.setMaxLevel(3);
			spider.start();
			SpiderExplorer spiderExplorer = new SpiderExplorer(spider);
			spiderExplorer.start();
			//addDocs(spiderExplorer.getDocs());
			//System.out.println(spiderExplorer.getDocs().toString());
			Vector<Document> docs = spiderExplorer.getDocs();
			int start = 0;
			int end = docs.size();
			int retry=0;
			while(start!=end || retry<4) {
				if(start==end)
					retry++;
				else
					retry=0;
				System.out.println("start: "+start+", end: "+end);
				addDocs(spiderExplorer.getDocs(), start, end);
				Thread.sleep(1000);
				start = end;
				end = docs.size();
			}
			spiderExplorer.interrupt();
			System.out.println("STOPPED "+start+" "+end);
			
		}
		catch(MalformedURLException ex) {ex.printStackTrace();}
		catch(InterruptedException ex1) {ex1.printStackTrace();}
	
	}
	
	public static void addDocs(Vector<Document> docs, int start, int end) {
		try {
//			 To store an index on disk, use this instead (note that the 
		    // parameter true will overwrite the index in that directory
		    // if one exists):
		    // Directory directory = FSDirectory.getDirectory("/tmp/testindex", true);
			//Thread.sleep(2000);
			RAMDirectory idx = new RAMDirectory();
			IndexWriter indexWriter = new IndexWriter(idx, new StandardAnalyzer(), true);
			for(int i=start; i<end; i++) {
				//System.out.println("URL: "+docs.get(i).get("url"));
				indexWriter.addDocument(docs.get(i));
			}
			indexWriter.optimize();
			indexWriter.close();			
			
//			 Now search the index:
		    IndexSearcher isearcher = new IndexSearcher(idx);

		    // Parse a simple query that searches for "text":
		    QueryParser parser = new QueryParser("title", new StandardAnalyzer());
		    Query query = parser.parse("Alessio");
		    Hits hits = isearcher.search(query);
		    //assertEquals(1, hits.length());
		    System.out.println("Hits: "+hits.length());
		    // Iterate through the results:
		    for (int i = 0; i < hits.length(); i++)
		    {
		      Document hitDoc = hits.doc(i);
		      System.out.println("Res: "+hitDoc.get("url"));
		      //assertEquals("This is the text to be indexed.", hitDoc.get("fieldname"));
		    }

		    isearcher.close();
		    idx.close();
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} /*catch (InterruptedException e){
			
		}*/
	}

}
