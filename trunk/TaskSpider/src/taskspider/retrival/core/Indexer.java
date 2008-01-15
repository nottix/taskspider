package taskspider.retrival.core;

import java.io.IOException;
import java.util.*;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.*;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;

import taskspider.util.properties.PropertiesReader;

public class Indexer {
	
	private String indexPath, indexTempPath;
	private Directory indexDir;
	private Directory indexTempDir;
	private IndexSearcher isearcher;
	private Hits result;
	private IndexWriter writer, writerTemp;
	private String task;
	
	public Indexer(String filename) {
		try {
			task = filename;
			indexPath = PropertiesReader.getProperty("indexPath")+filename;
			indexDir = FSDirectory.getDirectory(indexPath);
			indexTempPath = PropertiesReader.getProperty("indexTempPath")+filename;
			indexTempDir = FSDirectory.getDirectory(indexTempPath);
			isearcher = null;
			result = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		try {
			isearcher.close();
			indexDir.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Hits getResult() {
		return this.result;
	}
	
	public int search(String queryString, String field) {
		try {
			if(isearcher==null)
				isearcher = new IndexSearcher(indexTempDir);
			QueryParser parser = new QueryParser(field, new StandardAnalyzer());
			Query query = parser.parse(queryString);
			result = isearcher.search(query);
			System.out.println("Hits: "+result.length());
			// Iterate through the results:
			/*for (int i = 0; i < result.length(); i++)
			{
				Document hitDoc = result.doc(i);
				System.out.println("Res: "+hitDoc.get("url"));
			}*/
			return result.length();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public int indexDocs(Vector<Document> docs, int start, int end) {
		try {
			if(!IndexReader.indexExists(indexTempPath))
				writerTemp = new IndexWriter(indexTempDir, new StandardAnalyzer(), true);
			else
				writerTemp = new IndexWriter(indexTempDir, new StandardAnalyzer(), false);
			
			//writerTemp = new IndexWriter(indexTempDir, new StandardAnalyzer(), true);
			for(int i=start; i<end; i++) {
				//System.out.println("URL: "+docs.get(i).get("url"));
				if(!isDocPresent(docs.get(i)))
					writerTemp.addDocument(docs.get(i));
			}
			
			freeTask();
			//indexWriter.optimize(); se non ci sono più documenti da aggiungere
			int ret = writerTemp.docCount();
			writerTemp.close();
			return ret;
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public int freeTask() {
		try {
			if(search(task, "url")!=0) {
				if(!IndexReader.indexExists(indexPath))
					writer = new IndexWriter(indexDir, new StandardAnalyzer(), true);
				else
					writer = new IndexWriter(indexDir, new StandardAnalyzer(), false);
				
				for(int i=0; i<result.length(); i++) {
					writer.addDocument(result.doc(i));
					System.out.println("DOC: "+result.doc(i).get("url"));
				}
				writer.close();
			}
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public int getDocument() {
		try {
			IndexReader indexReader = IndexReader.open(indexDir);
			//System.out.println("READER: "+indexReader.numDocs());
			for(int i=0; i<indexReader.numDocs(); i++) {
				System.out.println("READER: "+indexReader.document(i));
			}
			System.out.println("SIZE: "+indexReader.numDocs());
			return indexReader.numDocs();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public boolean isDocPresent(Document doc) {
		try {
			IndexReader indexReader = IndexReader.open(indexTempDir);

			for(int i=0; i<indexReader.numDocs(); i++) {
				if(indexReader.document(i).get("url").equals(doc.get("url")))
					return true;
			}
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}
