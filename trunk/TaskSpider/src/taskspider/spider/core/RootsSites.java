/**
 * @author Simone Notargiacomo, Giuseppe Schipani
 */
package taskspider.spider.core;
import java.net.MalformedURLException;
import java.util.*;
import com.google.soap.search.*;
import websphinx.*;

/**
 * @author Simone Notargiacomo, Giuseppe Schipani
 * 
 */

public class RootsSites {
	private GoogleSearchResult results;
	private Vector<Link> sitesSource;
	
	public RootsSites(String searchKey){
		sitesSource = new Vector<Link>();
		GoogleSearch gg = new GoogleSearch();
		gg.setKey("Qte9k4VQFHKgauaxU4FdkjjJwazm3vrg");
		
		gg.setQueryString(searchKey);
		
		try {
			this.results = gg.doSearch();
		}catch (Exception f) {
		    System.out.println("Chiamata alle api fallita!");
		}
		
	}
	
	public Vector<Link> getRoots(){
		
		GoogleSearchResultElement[] re = results.getResultElements();
		
		try {
			for ( int i = 0; i < re.length; i++ ) {
			 sitesSource.add(new Link(re[i].getURL()));
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		return this.sitesSource;
	}
	
	public static void main(String[] args){
		RootsSites root = new RootsSites("coldplay");
		System.out.println(root.getRoots().toString().replace(",", "\n"));
	}
}
