/**
 * @author Simone Notargiacomo, Giuseppe Schipani
 */
package taskspider.spider.core;

import java.util.Vector;

import com.google.soap.search.GoogleSearch;
import com.google.soap.search.GoogleSearchResult;
import com.google.soap.search.GoogleSearchResultElement;

/**
 * @author Simone Notargiacomo, Giuseppe Schipani
 * 
 */

public class RootsSites {
	private GoogleSearchResult results;
	private Vector<String> sitesSource;
	
	public RootsSites(String searchKey){
		sitesSource = new Vector<String>();
		GoogleSearch gg = new GoogleSearch();
		gg.setKey("Qte9k4VQFHKgauaxU4FdkjjJwazm3vrg");
		
		gg.setQueryString(searchKey);
		
		try {
			this.results = gg.doSearch();
		}catch (Exception f) {
		    System.out.println("Chiamata alle api fallita!");
		}
		
	}
	
	public Vector<String> getRoots(){
		
		GoogleSearchResultElement[] re = results.getResultElements();
		
		for ( int i = 0; i < (re.length>10 ? 10 : re.length); i++ ) {
			sitesSource.add(re[i].getURL().toString());
			// System.out.println("url: "+re[i].getURL());
		}
		
		return this.sitesSource;
	}
	
	public static void main(String[] args){
		RootsSites root = new RootsSites("coldplay");
		for(int i=0; i<10; i++) {
			System.out.println("i: "+i+", elem: "+root.getRoots().get(i));
		}
		System.out.println(root.getRoots().toString());
	}
}
