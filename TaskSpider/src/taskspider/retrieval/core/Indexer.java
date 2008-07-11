package taskspider.retrieval.core;

import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;

import taskspider.spider.core.SpiderExplorer;
import taskspider.util.debug.Debug;
import taskspider.util.properties.PropertiesReader;
import taskspider.view.gui.TaskGraph;

public class Indexer {
	
	private String indexPath, indexTempPath;
	private Directory indexDir;
	private Directory indexTempDir;
	private IndexSearcher isearcher;
	private Hits result;
	private IndexWriter writer, writerTemp;
	private String task;
	private SpiderExplorer spiderExplorer;
	private TaskGraph graph;
	
	public Indexer(String filename, SpiderExplorer spiderExplorer) {
		try {
			task = filename;
			indexPath = PropertiesReader.getProperty("indexPath")+filename;
			indexDir = FSDirectory.getDirectory(indexPath);
			indexTempPath = PropertiesReader.getProperty("indexTempPath")+filename;
			indexTempDir = FSDirectory.getDirectory(indexTempPath);
			this.spiderExplorer = spiderExplorer;
			graph = new TaskGraph();
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
	
	private Hits getResult() {
		return this.result;
	}
	
	private Query genQuery(String taskString) {
		StringTokenizer tokens = new StringTokenizer(taskString);
		String token;
		String out = "";
		while(tokens.hasMoreTokens()) {
			token = tokens.nextToken(" ");
			if(token.length()>=3)
				out += token+" ";
		}
		taskString = out;
		
		String query = "";
		
		tokens = new StringTokenizer(taskString);
		task = "";
		query += "(";
		while(tokens.hasMoreTokens()) {
			task = tokens.nextToken();
			query += "title:"+task+"*";
			if(tokens.hasMoreTokens())
				query += " AND ";
		}
		query += ") OR ";
		
		tokens = new StringTokenizer(taskString);
		task = "";
		query += "(";
		while(tokens.hasMoreTokens()) {
			task = tokens.nextToken();
			query += "keywords:"+task;
			if(tokens.hasMoreTokens())
				query += " AND ";
		}
		query += ") OR ";
			
		tokens = new StringTokenizer(taskString);
		task = "";
		query += "(";
		while(tokens.hasMoreTokens()) {
			task = tokens.nextToken();
			query += "body:"+task;
			if(tokens.hasMoreTokens())
				query += " AND ";
		}
		query += ")";
		
		QueryParser parser = new QueryParser("body", new StandardAnalyzer());
		Query ret = null;
		try {
			ret = parser.parse(query);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return ret;
	}
	
	private int search(String queryString, String field) {
		try {
			isearcher = new IndexSearcher(indexTempDir);
			
			result = isearcher.search(genQuery(queryString));
			Debug.println("Query: "+queryString+", Expanded: "+genQuery(queryString).toString(), 1);
			Debug.println("Hits: "+result.length(), 1);
			
			return result.length();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public int indexDocs(Vector<Document> docs, int start, int end) {
		try {
			writerTemp = new IndexWriter(indexTempDir, new StandardAnalyzer(), true);
			Debug.println("Indice temp creato", 2);
			for(int i=start; i<end; i++) {
				writerTemp.addDocument(docs.get(i));
			}
			writerTemp.docCount();
			writerTemp.close();

			int ret = updateIndex();
			//indexWriter.optimize(); se non ci sono più documenti da aggiungere
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
	
	public TaskGraph getWebGraph() {
		return this.graph;
	}
	
	public int updateIndex() {
		try {
			if(search(task, "url")!=0) {
				if(!IndexReader.indexExists(indexPath))
					writer = new IndexWriter(indexDir, new StandardAnalyzer(), true);
				else
					writer = new IndexWriter(indexDir, new StandardAnalyzer(), false);
				
				int num;
				for(int i=0; i<result.length(); i++) {
					if( (num=isDocPresent(result.doc(i)))==-1 ) {
						writer.addDocument(result.doc(i));
						graph.addPage(spiderExplorer.getPage(result.doc(i).get("url")));
						Debug.println("DOC: "+result.doc(i).get("url"), 1);
						Debug.println("DATE: "+result.doc(i).get("date"), 1);
					}
					else {
						Document doc;
						graph.addPage(spiderExplorer.getPage(result.doc(i).get("url")));
						if((doc=getDocument(num))!=null) {
							if(doc.get("url").equals(result.doc(i).get("url"))) {
								if( result.doc(i).get("date").equals("0") ) {
									
								}
								else if( ((new Long(doc.get("date"))).longValue() >
									(new Long(result.doc(i).get("date"))).longValue()) ) {
								
									writer.updateDocument(new Term("url", result.doc(i).get("url")), doc);
									Debug.println("ELSEDOC: "+result.doc(i).get("url"), 1);
								}
							}
						}
						
					}
				}
				writer.close();
				return result.length();
			}
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public Document getDocument(int i) {
		try {
			IndexReader indexReader = IndexReader.open(indexDir);
			if(i>=indexReader.numDocs()) {
				indexReader.close();
				return null;
			}
			
			Document doc = indexReader.document(i);
			indexReader.close();
			return doc;
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public int getDocument() {
		try {
			IndexReader indexReader = IndexReader.open(indexDir);
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

	public boolean isDocPresentTemp(Document doc) {
		try {
			IndexReader indexReader = IndexReader.open(indexTempDir);

			for(int i=0; i<indexReader.numDocs(); i++) {
				if(indexReader.document(i).get("url").equals(doc.get("url"))) {
					indexReader.close();
					return true;
				}
			}
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public int isDocPresent(Document doc) {
		try {
			IndexReader indexReader = IndexReader.open(indexDir);

			for(int i=0; i<indexReader.numDocs(); i++) {
				if(indexReader.document(i).get("url").equals(doc.get("url"))) {
					indexReader.close();
					return i;
				}
			}
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return -1;
	}
}
