// Java Document
package taskspider.bean;

import java.util.*;
import java.io.IOException;
import java.lang.*;
import org.apache.lucene.document.*;
import org.apache.lucene.index.CorruptIndexException;

import taskspider.controller.*;
import org.apache.lucene.search.Hits;

public class TaskBean{
	
	private static Vector<Document> results = null;
	private static Hits hits = null;
	private static Controller controller = null;
	private static String task = "";
	private static String query = "";
	private static int first = 0; 
	private static int start, end, size;
	
	//costruttore,  inutile ma serve a tomcat, altrimenti crea errore
	public TaskBean(){}
	
	/*
	 * Do in input i parametri task e query, Qui dovresti fare l'operazione de
	 * taskspider e poi riempirmi due vettori.
	 */
	public static String doSearch(String taskSearch, String querySearch){
		if(controller==null)
			controller = new Controller();
		
		if(taskSearch.equals(task) && querySearch.equals(query)) {
			first++;
			return "0";
		}
		first = 0;
		int ret;
		if((ret=controller.search(taskSearch, querySearch))>0) {
			task = taskSearch;
			query = querySearch;
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return String.valueOf(ret);
			//return "1";
		}
		return "0";
		
		
	}
	
	/*
	 * Non ricordo a che ci serviva....
	 */
	public static void doCancel(){
		controller.interrupt();
	}
	
	/*
	 * Con questo prendo un vettore di risultati e lo stampo a centro 
	 * della pagina. Non paginiamo, li stampiamo tutti in sequenza.
	 */
	public static Vector<Document> getResults(String indexString){
		int index = Integer.parseInt(indexString);
		if(controller==null)
			return new Vector<Document>();
		if(first==0) {
			hits = controller.getResult();
			results = new Vector<Document>();
			size = hits.length()/4;
			
			try {
				for(int i=0; i<4; i++) {
					results.add(hits.doc(i));
				}
			} catch (CorruptIndexException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else {
			start = index*4;
			end = start+4;
			if(end<=hits.length()) {
				results = new Vector<Document>();
				try {
					for(int i=start; i<end; i++) {
						results.add(hits.doc(i));
					}
				} catch (CorruptIndexException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return results;
	} 
	
	/*
	 * Questo mi da i documenti per l'albero, purtoppo non lo posso provare
	 * perch non riesco ad avere il sitema funzionante sul server tomcat
	 */
	public static Vector<Document> getTree(){
		if(controller==null) {
			Vector<Document> docs = new Vector<Document>();
			docs.add(new Document());
			return docs;
		}
		return (Vector<Document>)controller.getGroupResult();
	} 
	
	public static String getArg(String name, String queryString) {
		System.out.println("QUERYSTRING: "+queryString+", name: "+name);
		
		int start = queryString.indexOf(name)+name.length()+1;
		if(start<0)
			return null;
		int end = queryString.indexOf("&", start);
		if(end<0)
			end = queryString.length()-1;
		
		System.out.println("NAME: "+queryString.substring(start, end));
			
		return queryString.substring(start, end);
	}
	
	public static String printTail(String indexString) {
		int index = Integer.parseInt(indexString);
		String ret = "";
		for(int i=0; i<size; i++) {
			if(index==i)
				ret += "<a class=\"description\"> "+(i+1)+" </a>";
			else
				ret += "<a href=\"http://localhost:8180/taskspider/index.jsp?task="+task+"&query="+query+"&frame=1&index="+i+"&\" class=\"description\"> "+(i+1)+" </a>";
		}
		return ret;
	}


}