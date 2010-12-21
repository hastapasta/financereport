

import java.util.*;
import java.io.*; 
 
/**
  * Most simple version of Stopable. Simply call
  * ServiceStopper.stop( new SimpleStopper() );
*/ 
public class WindowsCompatibleProperties extends Properties 
{                                                  
    /**
	 * 
	 */
	private static final long serialVersionUID = 3012078410648714538L;

	public void load(InputStream inStream) 
    {
        try {             
            BufferedReader r = new BufferedReader( new InputStreamReader( inStream ) );
            
            while (r.ready() )
            {
                String s = r.readLine().trim();
                
                if ( s.length() == 0 )
                    continue;
                if ( s.startsWith( ";" ) )
                    continue;
                if ( s.startsWith( "[" ) )
                    continue;
                    
                String name;
                String value;
                int pos = s.indexOf( "=" );
                if ( pos == -1 )
                    continue;
                name = s.substring( 0, pos ).trim();
                if ( pos+1 >= s.length() )
                    continue;
                    
                value = s.substring( pos+1).trim();
                    
                setProperty( name, value );                
            }    
            
            r.close();
        } catch ( Exception e )
        {
            System.err.println( "Error loading properties file." );
            e.printStackTrace();
            throw new RuntimeException(e);
        }    
    }    

}