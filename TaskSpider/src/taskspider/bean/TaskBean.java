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
	
	private Vector<Document> results = null;
	private Hits hits = null;
	private Controller controller = null;
	
	//costruttore,  inutile ma serve a tomcat, altrimenti crea errore
	public TaskBean(){}
	
	/*
	 * Do in input i parametri task e query, Qui dovresti fare l'operazione de
	 * taskspider e poi riempirmi due vettori.
	 */
	public String doSearch(String task, String query){
		if(controller==null)
			controller = new Controller();
		
		int ret;
		if((ret=controller.search(task, query))>0) {
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return String.valueOf(ret);
			//return "1";
		}
		return "0";
		
		
	}
	
	public String toString() {
		return "OK";
	}
	
	/*
	 * Non ricordo a che ci serviva....
	 */
	public void doCancel(){
		controller.interrupt();
	}
	
	/*
	 * Con questo prendo un vettore di risultati e lo stampo a centro 
	 * della pagina. Non paginiamo, li stampiamo tutti in sequenza.
	 */
	public Vector<Document> getResults(){
		if(controller==null)
			return new Vector<Document>();
		hits = controller.getResult();
		Vector<Document> res = new Vector<Document>();
		
		try {
			for(int i=0; i<hits.length(); i++) {
				res.add(hits.doc(i));
			}
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return res;
	} 
	
	/*
	 * Questo mi da i documenti per l'albero, purtoppo non lo posso provare
	 * perch non riesco ad avere il sitema funzionante sul server tomcat
	 */
	public Vector<Document> getTree(){
//		Vector<Document> V = new Vector<Document>();
//		Document D = new Document();
//		D.add(new Field("url",null));
//		D.add(new Field("url","http://www.sito1.it/index.php"));
//		D.add(new Field("url","http://www.sito1.it/altrapagina.php"));
//		D.add(new Field("url",null));
//		D.add(new Field("url","http://www.sito2.it/simone.php"));
//		D.add(new Field("url","http://www.sito2.it/giuseppe.html"));
//		D.add(new Field("url",null));
//		D.add(new Field("url","http://www.sito3.it/"));
//		D.add(new Field("url","http://www.sito3.it/pippo.jsp"));
//		D.add(new Field("url",null));
		if(controller==null) {
			Vector<Document> docs = new Vector<Document>();
			docs.add(new Document());
			return docs;
		}
		results = (Vector<Document>)controller.getGroupResult();
		
		return this.results;
	} 


}