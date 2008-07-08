package taskspider.controller;

/**
 * @author Khalili, Notargiacomo, Tavernese 
 */

import java.io.IOException;

public class BrowserControl
{
	
    /**
     * Mostra un file nel browser di sistema. In caso di sistema Windows recupera
     * il browser predefinito. In caso di sistema Linux utilizza il browser Firefox
     * che in genere si trova su tutte le distribuzioni.
     * 
     * @param url URL del file (deve iniziare con "http://" oppure "file://").
     */
    public static void displayURL(String path, String prog)
    {
        boolean windows = isWindowsPlatform();
        String cmd = null;
        try
        {
            if(windows)
            {
                cmd = WIN_PATH + " " + WIN_FLAG + " " + path + "\\" + prog;
                cmd = cmd.replaceAll("/", "\\\\");
                Runtime.getRuntime().exec(cmd);
            }
            else
            {
                cmd = UNIX_PATH + " " + prog;
                cmd = cmd.replaceAll("\\\\", "/");
                Runtime.getRuntime().exec(cmd);
            }
        }
        catch(IOException x)
        {
            System.err.println("Could not invoke browser, command=" + cmd);
            System.err.println("Caught: " + x);
        }
    }
    /**
     * Prova a determinare su quale sistema operativo si trova l'utente
     *
     * @return true se si trova su un sistema Windows
     */
    public static boolean isWindowsPlatform()
    {
        String os = System.getProperty("os.name");
        if ( os != null && os.startsWith(WIN_ID))
            return true;
        else
            return false;
    }

    private static final String WIN_ID = "Windows";
    private static final String WIN_PATH = "rundll32";
    private static final String WIN_FLAG = "url.dll,FileProtocolHandler";

    private static final String UNIX_PATH = taskspider.util.properties.PropertiesReader.getProperty("unixBrowserPath");
    private static final String UNIX_FLAG = "";
}
