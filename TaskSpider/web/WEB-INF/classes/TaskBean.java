// Java Document
package taskspider;

import java.util.*;
import java.lang.*;
import org.apache.lucene.document.*;

public class TaskBean{
	
	Private Vector<Document> results = new Vector<Document>();
	
	//costruttore, è inutile ma serve a tomcat, altrimenti crea errore
	public TaskBean(){}
	
	/*
	 * Do in input i parametri task e query, Qui dovresti fare l'operazione de
	 * taskspider e poi riempirmi due vettori.
	 */
	public void doSearch(String task, String query){
	
		
	}
	
	/*
	 * Non ricordo a che ci serviva....
	 */
	public void doCancel(){

	}
	
	/*
	 * Con questo prendo un vettore di risultati e lo stampo a centro 
	 * della pagina. Non paginiamo, li stampiamo tutti in sequenza.
	 */
	public Vector<Document> getResults(){

		
		return this.results;
	} 
	
	/*
	 * Questo mi da i documenti per l'albero, purtoppo non lo posso provare
	 * perchè non riesco ad avere il sitema funzionante sul server tomcat
	 */
	public Vector<Document> getTree(){
		
		Vector<Document> V = new Vector<Document>();
		Document D = new Document();
		D.add(new Field("url",null));
		D.add(new Field("url","http://www.sito1.it/index.php"));
		D.add(new Field("url","http://www.sito1.it/altrapagina.php"));
		D.add(new Field("url",null));
		D.add(new Field("url","http://www.sito2.it/simone.php"));
		D.add(new Field("url","http://www.sito2.it/giuseppe.html"));
		D.add(new Field("url",null));
		D.add(new Field("url","http://www.sito3.it/"));
		D.add(new Field("url","http://www.sito3.it/pippo.jsp"));
		D.add(new Field("url",null));
		
		return D;
	} 


}