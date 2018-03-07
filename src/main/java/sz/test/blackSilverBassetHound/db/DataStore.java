package sz.test.blackSilverBassetHound.db;

import java.util.HashMap;
import java.util.Map;

public class DataStore implements AutoCloseable {
    Map<String,String> mockStore = null;

    public DataStore(String dir )
    {
        mockStore = new HashMap<>();
    }

    public String get(String key)
    {
        String result = mockStore.get(key);
        if( (result!=null) && (result.length()==0) )
            result = null;
        return result;
    }

    public String get(String key, String def)
    {
        String s = get(key);
        if( s == null)
            return def;
        return s;
    }

    public long getLong(String key, long def)
    {
        String s = get(key);
        long v = def;
        if( s!=null )
        {
            try{
                v = Long.parseLong( s );
            } catch( NumberFormatException nfe) {}
        }
        return v;
    }

    public void set( String key, String value )
    {
        mockStore.put( key, value);
    }

    public void close()
    {

    }
}
