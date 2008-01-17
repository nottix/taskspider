/**
 * @author Simone Notargiacomo, Giuseppe Schipani
 */
package taskspider.util.debug;

/**
 * @author Simone Notargiacomo, Giuseppe Schipani
 *
 */
public class Debug {

	private static int debugLevel = -1;
	
	public static void println(String string, int level) {
		if(debugLevel == -1) 
			debugLevel = Integer.parseInt(taskspider.util.properties.PropertiesReader.getProperty("debugLevel"));
		
		if(level <= debugLevel)
			System.out.println(string);
	}
}
