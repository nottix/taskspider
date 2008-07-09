package taskspider.bean;

import java.io.IOException;
import java.util.Vector;
import taskspider.util.properties.*;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.search.Hits;

import taskspider.controller.Controller;

public class TaskBean{

	private static Vector<Document> results = null;
	private static Hits hits = null;
	private static Controller controller = null;
	private static String task = "";
	private static String query = "";
	private static String qString = "";
	//private static int first = 0; 
	private static int start, end, size=0;
	private static int total = 0;
	private static String jspUrl = PropertiesReader.getProperty("jspUrl");

	//costruttore,  inutile ma serve a tomcat, altrimenti crea errore
	public TaskBean(){}

	public static int getTotal() {
		return total;
	}

	/*
	 * Da' in input i parametri task e query.
	 */
	public static String doSearch(String taskSearch, String querySearch, String redo_str, String typeStr, String exp){
		int type = Integer.parseInt(typeStr);
		int ret;
		boolean redo;
		if(redo_str.indexOf("1") >= 0)
			redo = true;
		else
			redo = false;
		if(redo) {
			controller = new Controller();
			if(taskSearch.indexOf("%20")>0)
				taskSearch = taskSearch.replaceAll("%20", " ");
			if(exp.equals("expanded"))
				type=-1;
			if((ret=controller.search(taskSearch, querySearch, type))>=0) {
				total = ret;
				if(taskSearch.indexOf(" ")>0)
					taskSearch = taskSearch.replaceAll(" ", "%20");
				task = taskSearch;
				query = querySearch;

				return String.valueOf(ret);
			}
		}
		return "0";


	}

	public static void doCancel(){
		controller.interrupt();
	}

	public static Vector<Document> getResults(String indexString){
		int index = Integer.parseInt(indexString);
		results = new Vector<Document>();
		if(controller==null) {
			System.out.println("NULL");
			return results;
		}
		if(index==0) {
			System.out.println("here");
			hits = controller.getResult();
			results = new Vector<Document>();
			if(hits!=null && hits.length()>0) {
				size = (hits.length()/4)+((hits.length()%4)>0 ? 1 : 0);
				try {
					for(int i=0; i<4 && i<hits.length(); i++) {
						results.add(hits.doc(i));
					}
				} catch (CorruptIndexException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else
				size = 0;
		}
		else {
			start = index*4;
			end = start+4;
			try {
				for(int i=start; i<end && i<hits.length(); i++) {
					if(hits.doc(i)!=null)
						results.add(hits.doc(i));
				}
			} catch (CorruptIndexException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return results;
	} 

	public static Vector<Document> getTree(){
		if(controller==null || total==0) {
			Vector<Document> docs = new Vector<Document>();
			docs.add(new Document());
			return docs;
		}
		return (Vector<Document>)controller.getGroupResult();
	} 

	public static String getArg(String name, String queryString) {
		System.out.println("QUERYSTRING: "+queryString+", name: "+name);
		qString = queryString;

		int start = queryString.indexOf(name);
		if(start<0)
			return null;
		start += name.length()+1;
		int end = queryString.indexOf("&", start);
		if(end<0)
			end = queryString.length()-1;

		System.out.println("NAME: "+queryString.substring(start, end));

		return queryString.substring(start, end);
	}

	public static String printTail(String indexString) {
		if(indexString==null)
			return "";
		int index = Integer.parseInt(indexString);
		String ret = "";
		for(int i=0; i<size; i++) {
			if(index==i)
				ret += "<a class=\"description\"> "+(i+1)+" </a>";
			else {
				if(qString!=null && !qString.equals(""))
					ret += "<a href=\""+jspUrl+"?task="+task+"&query="+query+"&do=0&frame="+(getArg("frame", qString).equals("1") ? "1" : "0")+"&index="+i+"&\" class=\"description\"> "+(i+1)+" </a>";
				else
					ret += "<a href=\""+jspUrl+"?task="+task+"&query="+query+"&do=0&frame=1&index="+i+"&\" class=\"description\"> "+(i+1)+" </a>";
			}
		}
		return ret;
	}


}