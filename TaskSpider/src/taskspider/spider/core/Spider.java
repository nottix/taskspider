/**
 * @author Simone Notargiacomo, Giuseppe Schipani
 */
package taskspider.spider.core;

import java.io.IOException;

import websphinx.*;

/**
 * @author Simone Notargiacomo, Giuseppe Schipani
 *
 */
public class Spider {
	private Crawler crawler;
	private EventLog logger;
	private String pathLog;
	private Thread thread;
	
	public Spider(Link[] links) {
		if(crawler==null)
			crawler = new Crawler();
		
		for(int i=0; i<links.length; i++) {
			crawler.addRoot(links[i]);
		}
		
		crawler.addClassifier(new StandardClassifier());
		pathLog = "/tmp/taskspider";
		createLogger(pathLog);
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
		thread.start();
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
