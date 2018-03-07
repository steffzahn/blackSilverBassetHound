package sz.test.blackSilverBassetHound;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import sz.test.blackSilverBassetHound.db.DataStore;
import sz.test.blackSilverBassetHound.io.RecipientsParser;

import java.util.List;

@Component
public class Campaign {

    private static final Logger LOG = LoggerFactory.getLogger(Campaign.class);

    private String campaignRoot = null;
    private String campaignDir = null;
    private String campaign = null;

    public static final String RECIPIENT_FILE = "recipientFile";
    public static final String RECIPIENT_FILE_OFFSET = "recipientFileOffset";

    private DataStore store = null;

    public String getCampaignRoot() {
        return campaignRoot;
    }

    public String getCampaignDir() {
        return campaignDir;
    }

    public String getCampaign() {
        return campaign;
    }

    private String recipientsFile = null;
    private long recipientsFileOffset = 0L;
    private RecipientsParser parser = null;

    public void processRecipientsFile(String fn) throws Exception
    {
        this.recipientsFile = fn;
        String oldRecipientFile = store.get(RECIPIENT_FILE);
        if( oldRecipientFile!=null && (oldRecipientFile.equals(fn)) )
        {
            long o = store.getLong(RECIPIENT_FILE_OFFSET, -1L);
            if( o>=0 )
            {
                this.recipientsFileOffset = o;
            }
        }
        parser = new RecipientsParser( fn );
        parser.seek(this.recipientsFileOffset);
        LOG.info("Campaign.processRecipientsFile(): start processing file " + fn);
        for( ;; )
        {
            List<String> lineList = parser.readParsedLine();
            if( lineList == null )
                break;
        }
        LOG.info("Campaign.processRecipientsFile(): finished processing file " + fn);
    }

    public Campaign()
    {
        this.campaign = System.getProperty("campaign","campaign");
        this.campaignRoot = System.getProperty("campaignRoot",".");
        this.campaignDir = campaignRoot + "/" + campaign;
        this.store = new DataStore(campaignDir);
    }
}
