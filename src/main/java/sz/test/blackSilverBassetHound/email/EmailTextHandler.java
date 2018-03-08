package sz.test.blackSilverBassetHound.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static sz.test.blackSilverBassetHound.io.RecipientsParser.EMAIL_ADDRESS_INDEX;
import static sz.test.blackSilverBassetHound.io.RecipientsParser.FIRSTNAME_INDEX;
import static sz.test.blackSilverBassetHound.io.RecipientsParser.LASTNAME_INDEX;

public class EmailTextHandler {

    private static final Logger LOG = LoggerFactory.getLogger(EmailTextHandler.class);

    private String email = null;

    public EmailTextHandler( String fn ) throws Exception
    {
        BufferedReader reader = Files.newBufferedReader(Paths.get( fn ));
        StringBuilder sb = new StringBuilder(80);
        for(;;) {
            String line = reader.readLine();
            if (line == null) {
                break;
            }
            sb.append(line).append('\n');
        }
        email = sb.toString();
    }

    public String preprocess( List<String> fields )
    {
        String s = email.replace("${FirstName}", fields.get(FIRSTNAME_INDEX))
                .replace("${LastName}", fields.get(LASTNAME_INDEX))
                .replace("${EmailAddress}", fields.get(EMAIL_ADDRESS_INDEX));
        if( LOG.isDebugEnabled() ) {
            LOG.debug("EmailTextHandler.preprocess():" + s);
        }
        return s;
    }
}
