/**
 * @author Simone Notargiacomo, Giuseppe Schipani
 */
package taskspider.spider.core;

import java.io.IOException;
import websphinx.*;
import taskspider.util.properties.PropertiesReader;

/**
 * @author Simone Notargiacomo, Giuseppe Schipani
 *
 */
public class Spider {
	private Crawler crawler;
	private EventLog logger;
	private String logPath;
	private Thread thread;
	
	public Spider(Link[] links) {
		if(crawler==null)
			crawler = new Crawler();
		
		for(int i=0; i<links.length; i++) {
			crawler.addRoot(links[i]);
		}
		
		crawler.addClassifier(new StandardClassifier());
		if((logPath = PropertiesReader.getProperty("logPath"))==null)
			System.out.println("Error in properties file");
		createLogger(logPath);
		createThread();
		
	}
	
	private EventLog createLogger(String path) {
		try {
			if(logger==null)
				logger = new EventLog(path);
			crawler.addCrawlListener(logger);
			crawler.addLinkListener(logger);
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        return logger;
	}
	
	private Thread createThread() {
		if(thread==null) {
			thread = new Thread (crawler, crawler.getName ());
        	thread.setDaemon (true);
		}
        return thread;
	}
	
	public void start() {
		try {
			thread.start();
			Thread.sleep(Integer.parseInt(PropertiesReader.getProperty("threadDelay")));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void stop() {
		thread.interrupt();
	}
	
	public Link[] getExploredRoots() {
		return crawler.getCrawledRoots();
	}
	
	public int getPagesVisited() {
		return crawler.getPagesVisited();
	}
}
