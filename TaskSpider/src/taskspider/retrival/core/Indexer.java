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
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;

import taskspider.util.properties.PropertiesReader;
import taskspider.util.debug.*;

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
	
	private Query genQuery(String taskString) {
		BooleanQuery booleanQuery = new BooleanQuery();
//		WildcardQuery queryUrl = new WildcardQuery(new Term("url", "*"+taskString+"*"));
//		booleanQuery.add(queryUrl, BooleanClause.Occur.SHOULD);
//		WildcardQuery queryTitle = new WildcardQuery(new Term("title", "*"+taskString+"*"));
//		booleanQuery.add(queryTitle, BooleanClause.Occur.SHOULD);
//		WildcardQuery queryKeywords = new WildcardQuery(new Term("keywords", "*"+taskString+"*"));
//		booleanQuery.add(queryKeywords, BooleanClause.Occur.SHOULD);
//		
		WildcardQuery queryBody = new WildcardQuery(new Term("body", "*"+taskString+"*"));
		booleanQuery.add(queryBody, BooleanClause.Occur.SHOULD);
		
		return booleanQuery;
	}
	
	public int search(String queryString, String field) {
		try {
			if(isearcher==null)
				isearcher = new IndexSearcher(indexTempDir);
			//QueryParser parser = new QueryParser(field, new StandardAnalyzer());
			//Query query = parser.parse(queryString);
			
			//WildcardQuery query = new WildcardQuery(new Term(field, "*"+queryString+"*"));
			result = isearcher.search(genQuery(queryString));
			Debug.println("Hits: "+result.length(), 1);
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
		}
		return 0;
	}
	
	public int indexDocs(Vector<Document> docs, int start, int end) {
		try {
			//if(!IndexReader.indexExists(indexTempPath)) {
				writerTemp = new IndexWriter(indexTempDir, new StandardAnalyzer(), true);
				Debug.println("Indice temp creato", 2);
			//}
			//else {
			//	Debug.println("Indice temp esistente", 2);
			//	writerTemp = new IndexWriter(indexTempDir, new StandardAnalyzer(), false);
			//}
			
			//writerTemp = new IndexWriter(indexTempDir, new StandardAnalyzer(), true);
			for(int i=start; i<end; i++) {
				//System.out.println("URL: "+docs.get(i).get("url"));
				
//				if(!isDocPresentTemp(docs.get(i))) {
//					Debug.println("Doc non presente: "+docs.get(i), 1);
					writerTemp.addDocument(docs.get(i));
//				}
			}
			int ret = writerTemp.docCount();
			writerTemp.close();
			
			updateIndex();
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
	
	public int updateIndex() {
		try {
			if(search(task, "url")!=0) {
				if(!IndexReader.indexExists(indexPath))
					writer = new IndexWriter(indexDir, new StandardAnalyzer(), true);
				else
					writer = new IndexWriter(indexDir, new StandardAnalyzer(), false);
				
				for(int i=0; i<result.length(); i++) {
					if(!isDocPresent(result.doc(i))) {
						writer.addDocument(result.doc(i));
						Debug.println("DOC: "+result.doc(i).get("url"), 1);
					}
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
	
	public boolean isDocPresent(Document doc) {
		try {
			IndexReader indexReader = IndexReader.open(indexDir);

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
}
