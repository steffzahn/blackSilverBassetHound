package sz.test.blackSilverBassetHound;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import sz.test.blackSilverBassetHound.db.DataStore;
import sz.test.blackSilverBassetHound.email.EmailTask;
import sz.test.blackSilverBassetHound.email.EmailTextHandler;
import sz.test.blackSilverBassetHound.io.RecipientsParser;

import javax.annotation.PostConstruct;
import java.util.List;

import static sz.test.blackSilverBassetHound.io.RecipientsParser.EMAIL_ADDRESS_INDEX;

@Component
public class Campaign {

    @Autowired
    private ApplicationProperties config;

    private static final Logger LOG = LoggerFactory.getLogger(Campaign.class);

    private String campaignRoot = null;
    private String campaignDir = null;
    private String campaign = null;
    private String replyTo = null;

    public String getReplyTo() {
        return replyTo;
    }

    private int emailThreads = 25;

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

    public void processRecipientsFile(String recipientFile, String emailFile) throws Exception
    {
        new Thread() {
            @Override
            public void run()
            {
                try {
                    long recipientsFileOffset = 0L;
                    String oldRecipientFile = store.get(RECIPIENT_FILE);
                    if (oldRecipientFile != null && (oldRecipientFile.equals(recipientFile))) {
                        long o = store.getLong(RECIPIENT_FILE_OFFSET, -1L);
                        if (o >= 0) {
                            recipientsFileOffset = o;
                        }
                    }
                    RecipientsParser parser = new RecipientsParser(recipientFile);
                    parser.seek(recipientsFileOffset);
                    EmailTextHandler emth = new EmailTextHandler(emailFile);
                    ThreadPoolTaskExecutor ex = new ThreadPoolTaskExecutor();
                    ex.setCorePoolSize(emailThreads);
                    ex.setMaxPoolSize(emailThreads);
                    ex.setQueueCapacity(emailThreads*2);
                    ex.setDaemon(true);
                    ex.setWaitForTasksToCompleteOnShutdown(true);
                    ex.initialize();
                    LOG.info("Campaign.processRecipientsFile(): start processing file " + recipientFile);
                    for (;;) {
                        long pos = parser.getFilePointer();
                        if( pos % 1000 == 0)
                        {
                            store.set(RECIPIENT_FILE_OFFSET, pos);
                        }
                        if( recipientsFileOffset+200000 <= pos)
                        {
                            LOG.info("Campaign.processRecipientsFile(): position "+pos );
                            recipientsFileOffset = pos;
                        }
                        List<String> fieldList = parser.readParsedLine();
                        if (fieldList == null)
                            break;
                        String email = emth.preprocess(fieldList);
                        EmailTask emt = new EmailTask(fieldList.get(EMAIL_ADDRESS_INDEX), replyTo, email);
                        for(;;)
                        {
                            try{
                                ex.execute(emt);
                                break;
                            } catch( TaskRejectedException tre )
                            {
                                Thread.sleep(200L);
                            }
                        }
                    }
                    LOG.info("Campaign.processRecipientsFile(): finished processing file " + recipientFile);
                    ex.shutdown();
                } catch( Exception e )
                {
                    LOG.error( "Campaign.processRecipientsFile.run(): caught exception, ", e);
                }
            }
        }.start();
    }

    @PostConstruct
    public void init()
    {
        this.campaign = config.getCampaign();
        this.campaignRoot = config.getCampaignRoot();
        this.campaignDir = campaignRoot + "/" + campaign;
        this.store = new DataStore(campaignDir);
        this.emailThreads = config.getEmailThreads();
        this.replyTo = config.getReplyTo();
        LOG.info( "DEUBG "+replyTo );
    }

    public Campaign()
    {
    }
}
