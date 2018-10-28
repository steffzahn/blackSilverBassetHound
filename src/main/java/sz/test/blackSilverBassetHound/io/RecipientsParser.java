package sz.test.blackSilverBassetHound.io;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class RecipientsParser {

    private static final Logger LOG = LoggerFactory.getLogger(RecipientsParser.class);

    public static final int EMAIL_ADDRESS_INDEX = 0;
    public static final int FIRSTNAME_INDEX = 1;
    public static final int LASTNAME_INDEX = 2;

    private long filePointer = 0L;
    private BufferedReader reader;

    public RecipientsParser( String fn ) throws Exception
    {
        reader = Files.newBufferedReader(Paths.get( fn ));
    }

    protected RecipientsParser() {
        throw new IllegalStateException("snh");
    }

    public long getFilePointer()
    {
        return this.filePointer;
    }

    public void seek(long filePointer) throws Exception
    {
        while( this.filePointer < filePointer )
        {
            if( reader.readLine()==null ) {
                break;
            }
            this.filePointer++;
        }
    }

    private static int copyItem(StringBuilder sb, String source, int startIndex)
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
        } else {
            startIndex = ix;
            while(  (ix<sourceLength  ) && (source.charAt(ix) != ';') )
            {
                ix++;
            }
            sb.append( source, startIndex, ix );
            ix++;
        }
        return ix;
    }

    public List<String> readParsedLine() throws Exception
    {
        String line = reader.readLine();
        if( line==null ) {
            return null;
        }
        this.filePointer++;
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

    @Override
    public String toString() {
        return "RecipientsParser{" +
                "filePointer=" + filePointer +
                ", reader=" + reader +
                '}';
    }
}
