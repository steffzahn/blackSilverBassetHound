package sz.test.blackSilverBassetHound.db;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;


public class DataStore implements AutoCloseable {
    private final Map<String,String> mockStore;

    public DataStore(@SuppressWarnings("unused") String dir )
    {
        mockStore = new HashMap<>();
    }

    public @Nullable String get(String key)
    {
        String result;
        synchronized(mockStore) {
            result = mockStore.get(key);
        }
        if( (result!=null) && result.isEmpty() )
            result = null;
        return result;
    }

    @SuppressWarnings("unused")
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
            } catch( NumberFormatException ignored) {}
        }
        return v;
    }

    @SuppressWarnings("WeakerAccess")
    public void set(String key, String value )
    {
        synchronized (mockStore) {
            mockStore.put(key, value);
        }
    }

    public void set( String key, final long value )
    {
        set( key, Long.toString(value) );
    }

    public void close()
    {

    }
}
