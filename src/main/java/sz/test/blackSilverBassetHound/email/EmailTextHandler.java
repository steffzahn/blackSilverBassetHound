package sz.test.blackSilverBassetHound.email;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class EmailTextHandler {

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
        return email;
    }
}
