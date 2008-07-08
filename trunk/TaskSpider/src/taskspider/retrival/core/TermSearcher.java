/**
 * @author Simone Notargiacomo, Giuseppe Schipani
 */
package taskspider.retrival.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import taskspider.retrival.queryexpansion.QueryExpansion;
import taskspider.retrival.wordnet.SynExpand;
import taskspider.util.debug.Debug;
import taskspider.util.properties.PropertiesReader;

/**
 * @author Simone Notargiacomo, Giuseppe Schipani
 *
 */
public class TermSearcher {
	public static int ROCCHIO = 1;
	public static int WORDNET = 0;
	
	private String indexPath;
	private Directory indexDir;
	private IndexSearcher isearcher;
	private Hits result;
	private String task;
	private Query expandedQuery;
	
	public TermSearcher(String filename) {
		try {
			indexPath = PropertiesReader.getProperty("indexPath")+filename;
			Debug.println("IndexPath: "+indexPath, 1);
			indexDir = FSDirectory.getDirectory(indexPath);
			this.task = filename;
			isearcher = null;
			result = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		
	public Hits getResult() {
		return this.result;
	}

	/**
	 * http://lucene.apache.org/java/docs/queryparsersyntax.html
	 * 
	 * @param queryString
	 * @return
	 */
	public int search(String queryString, int type) {
		try {
			if(IndexReader.indexExists(indexPath)) {
				isearcher = new IndexSearcher(indexDir);
				
				queryString = queryString.replaceAll("%20", " ");

				StandardAnalyzer analyzer = new StandardAnalyzer();
				QueryParser parser = new QueryParser("body", analyzer);
				Query query = parser.parse(queryString);
				expandedQuery = query;

				Debug.println("Normal: "+query.toString(), 1);
				result = isearcher.search(query);
				Debug.println("Search hits: "+result.length(), 1);

				if(type!=-1) {
					expandedQuery = this.expandQuery(query, query.toString(), result, isearcher, analyzer, type);
					Debug.println("Expanded: "+expandedQuery.toString(), 1);
					result = isearcher.search(expandedQuery);
					Debug.println("Search with expanded query hits: "+result.length(), 1);
				}

				return result.length();
			}
			else {
				Debug.println("Doesn't exist", 1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public Query getExpandedQuery() {
		return expandedQuery;
	}
	
	public void close() {
		try {
			isearcher.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Query expandQuery(Query query, String queryString, Hits hits, IndexSearcher searcher, StandardAnalyzer analyzer, int type) {
		if(type==TermSearcher.WORDNET) {
			try {
				String wnIndexPath = PropertiesReader.getProperty("wordnetIndexPath");
				Directory wnIndexDir = FSDirectory.getDirectory(wnIndexPath);
				IndexSearcher wnSearcher = new IndexSearcher(wnIndexDir);
				
				StringTokenizer tokens = new StringTokenizer(queryString);
				Query[] queries = new Query[tokens.countTokens()];
				String sub, fieldSub, termSub;
				int dots, i=0;
				
				while(tokens.hasMoreTokens()) {
					sub = tokens.nextToken();
					dots = sub.indexOf(":");
					termSub = sub.substring(dots+1);
					fieldSub = sub.substring(0, dots);
					Debug.println("Field: "+fieldSub+", Term: "+termSub, 1);
					queries[i++]=SynExpand.expand(termSub, wnSearcher, analyzer, fieldSub, 0);
					
				}
				
				return query.combine(queries);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else if(type==TermSearcher.ROCCHIO) {
	        try {
				Similarity similarity = query.getSimilarity( searcher );
				
				File file = new File("/home/avenger/Programs/taskspider/conf/config.properties");
				FileInputStream fis = new FileInputStream(file);
				Properties properties = new Properties();
				properties.load(fis);
	
				queryString = queryString.replaceAll("body:", "");
				QueryExpansion queryExpansion = new QueryExpansion( analyzer, searcher, similarity, properties );
				Query retQuery = queryExpansion.expandQuery( queryString, hits, properties );
				return retQuery;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}
}

