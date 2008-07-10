/**
 * @author Simone Notargiacomo, Giuseppe Schipani
 */
package taskspider.retrival.testing;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.lucene.search.Hits;
import org.apache.lucene.search.Query;
import org.apache.lucene.document.Field;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.document.Document;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

import java.io.*;

/**
 * @author Simone Notargiacomo, Giuseppe Schipani
 *
 */
public class Testing {
	public static Document[] genDocs() {
		try {
			File path = new File("/home/avenger/amsn_received");
			Document[] docs = new Document[path.listFiles().length];
			Document doc = new Document();
			String title, content;
			
			for(int i=0; i<path.listFiles().length; i++) {
				System.out.println("File: "+path.listFiles()[i].getPath());
				title = path.listFiles()[i].getPath();
				FileInputStream fis=new FileInputStream(path.listFiles()[i]);
				InputStreamReader isr=new InputStreamReader(fis);
				BufferedReader br=new BufferedReader(isr);
				char[] buf = new char[6000];
				br.read(buf);
				System.out.println("content: "+String.valueOf(buf));
				content = String.valueOf(buf);
				if(title==null || content==null || path.listFiles()[i].isDirectory())
					break;
				//docs[i].add(new Field("title", new StringReader(title)));
				doc.add(new Field("content", content, Field.Store.YES, Field.Index.TOKENIZED));
				docs[i] = doc;
			}
			return docs;
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static void addDocs() {
		try {
//			 To store an index on disk, use this instead (note that the 
		    // parameter true will overwrite the index in that directory
		    // if one exists):
		    // Directory directory = FSDirectory.getDirectory("/tmp/testindex", true);
			
			Document[] docs = genDocs();
			RAMDirectory idx = new RAMDirectory();
			IndexWriter indexWriter = new IndexWriter(idx, new StandardAnalyzer(), true);
			for(int i=0; i<docs.length; i++) {
				indexWriter.addDocument(docs[i]);
			}
			indexWriter.optimize();
			indexWriter.close();			
			
//			 Now search the index:
		    IndexSearcher isearcher = new IndexSearcher(idx);

		    // Parse a simple query that searches for "text":
		    QueryParser parser = new QueryParser("content", new StandardAnalyzer());
		    Query query = parser.parse("makeindex");
		    Hits hits = isearcher.search(query);
		    //assertEquals(1, hits.length());

		    // Iterate through the results:
		    for (int i = 0; i < hits.length(); i++)
		    {
		      Document hitDoc = hits.doc(i);
		      System.out.println("Res: "+hitDoc.getField("content"));
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
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//addDocs();
		
		String body = "simone hhh sim->sim() ss trans() pluto var.var(); anti pippo";
		System.out.println("before: "+body);
		body = body.replaceAll("[\\w&&[\\S]]*[\\W&&[\\S]]+[\\w&&[\\S]]*", "");
		System.out.println("after: "+body);

	}

}
