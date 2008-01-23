/**
 * @author Simone Notargiacomo, Giuseppe Schipani
 */
package taskspider.retrival.core;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.StringTokenizer;
import java.io.*;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.Similarity;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import taskspider.retrival.wordnet.*;
import taskspider.util.debug.Debug;
import taskspider.util.properties.PropertiesReader;

/**
 * @author Simone Notargiacomo, Giuseppe Schipani
 *
 */
public class TermSearcher {
	private String indexPath;
	private Directory indexDir;
	private IndexSearcher isearcher;
	private Hits result;
	private String task;
	
	public TermSearcher(String filename) {
		try {
			indexPath = PropertiesReader.getProperty("indexPath")+filename;
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
	public int search(String queryString) {
		try {
			if(IndexReader.indexExists(indexPath)) {
				isearcher = new IndexSearcher(indexDir);

				StandardAnalyzer analyzer = new StandardAnalyzer();
				QueryParser parser = new QueryParser("body", analyzer);
				Query query = parser.parse(queryString);

				Debug.println("Normal: "+query.toString(), 1);
				result = isearcher.search(query);
				Debug.println("Search hits: "+result.length(), 1);

				Query expandedQuery = this.expandQuery(query, query.toString(), "body");
				Debug.println("Expanded: "+expandedQuery.toString(), 1);
				result = isearcher.search(expandedQuery);
				Debug.println("Search with expanded query hits: "+result.length(), 1);

				isearcher.close();
				return result.length();
			}
			else {
				Debug.println("Doesn't exist", 1);
			}
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	private Query expandQuery(Query query, String queryString, String field) {
		try {
			String wnIndexPath = PropertiesReader.getProperty("wordnetIndexPath");
			Directory wnIndexDir = FSDirectory.getDirectory(wnIndexPath);
			IndexSearcher wnSearcher = new IndexSearcher(wnIndexDir);
			StandardAnalyzer analyzer = new StandardAnalyzer();
			
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
		return null;
	}
}

