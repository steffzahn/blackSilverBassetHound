package sz.test.blackSilverBassetHound.io;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class RecipientsParser {

    private static final Logger LOG = LoggerFactory.getLogger(RecipientsParser.class);

    public static final int EMAIL_ADDRESS_INDEX = 0;
    public static final int FIRSTNAME_INDEX = 1;
    public static final int LASTNAME_INDEX = 2;

    private long pos = 0L;
    private BufferedReader reader = null;

    public RecipientsParser( String fn ) throws Exception
    {
        reader = Files.newBufferedReader(Paths.get( fn ));
    }

    public long getFilePointer()
    {
        return this.pos;
    }

    public void seek(long pos) throws Exception
    {
        while( this.pos < pos )
        {
            if( reader.readLine()==null ) {
                break;
            }
            this.pos++;
        }
    }

    private int copyItem( StringBuilder sb, String source, int startIndex)
    {
        int sourceLength = source.length();
        int ix= startIndex;
        if( (ix<sourceLength  ) && (source.charAt(ix) == '"') )
        {
            ix++;
            startIndex = ix;
            while(  (ix<sourceLength  ) && (source.charAt(ix) != '"') )
            {
                ix++;
            }
            if( ix<sourceLength )
            {
                sb.append( source, startIndex, ix );
            }
            ix++;
            return ix;
        } else {
            startIndex = ix;
            while(  (ix<sourceLength  ) && (source.charAt(ix) != ';') )
            {
                ix++;
            }
            sb.append( source, startIndex, ix );
            ix++;
            return ix;
        }
    }

    public List<String> readParsedLine() throws Exception
    {
        String line = reader.readLine();
        if( line==null ) {
            return null;
        }
        this.pos++;
        List<String> result = new ArrayList<>(3);
        StringBuilder sb = new StringBuilder();
        int lineLength = line.length();
        int ix = 0;
        while( ix < line.length() )
        {
            sb.setLength(0);
            ix = copyItem( sb, line, ix );
            result.add( sb.toString() );
            if(ix<lineLength)
            {
                if(line.charAt(ix) == ';')
                {
                    ix++;
                } else {
                    LOG.error("RecipientsParser.readParsedLine(): syntax error in line: "+line );
                    return null;
                }
            }
        }
        return result;
    }
}
