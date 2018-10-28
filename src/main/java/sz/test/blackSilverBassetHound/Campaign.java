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

    @SuppressWarnings("unused")
    public String getReplyTo() {
        return replyTo;
    }

    private int emailThreads = 25;

    @SuppressWarnings("WeakerAccess")
    public static final String RECIPIENT_FILE = "recipientFile";
    @SuppressWarnings("WeakerAccess")
    public static final String RECIPIENT_FILE_OFFSET = "recipientFileOffset";

    private DataStore store = null;

    @SuppressWarnings("WeakerAccess")
    public String getCampaignRoot() {
        return campaignRoot;
    }

    @SuppressWarnings("unused")
    public String getCampaignDir() {
        return campaignDir;
    }

    @SuppressWarnings("WeakerAccess")
    public String getCampaign() {
        return campaign;
    }

    void processRecipientsFile(String recipientFile, String emailFile) {
        //noinspection OverlyLongLambda
        new Thread(
                () -> {
                    try {
                        long recipientsFileOffset = 0L;
                        String oldRecipientFile = store.get(RECIPIENT_FILE);
                        if ((oldRecipientFile != null) && (oldRecipientFile.equals(recipientFile))) {
                            long o = store.getLong(RECIPIENT_FILE_OFFSET, -1L);
                            if (o >= 0L) {
                                recipientsFileOffset = o;
                            }
                        }
                        RecipientsParser parser = new RecipientsParser(recipientFile);
                        parser.seek(recipientsFileOffset);
                        EmailTextHandler emth = new EmailTextHandler(emailFile);
                        ThreadPoolTaskExecutor ex = new ThreadPoolTaskExecutor();
                        ex.setCorePoolSize(emailThreads);
                        ex.setMaxPoolSize(emailThreads);
                        ex.setQueueCapacity(emailThreads * 2);
                        ex.setDaemon(true);
                        ex.setWaitForTasksToCompleteOnShutdown(true);
                        ex.initialize();
                        long lastTime = System.currentTimeMillis();
                        LOG.info("Campaign.processRecipientsFile(): start processing file " + recipientFile);
                        for (; ; ) {
                            long pos = parser.getFilePointer();
                            if ((pos % 1000L) == 0L) {
                                store.set(RECIPIENT_FILE_OFFSET, pos);
                            }
                            long now = System.currentTimeMillis();
                            if ((lastTime + 10000L) <= now) {
                                LOG.info("Campaign.processRecipientsFile(): position " + pos);
                                lastTime = now;
                            }
                            List<String> fieldList = parser.readParsedLine();
                            if (fieldList == null)
                                break;
                            String email = emth.preprocess(fieldList);
                            Runnable emt = new EmailTask(fieldList.get(EMAIL_ADDRESS_INDEX), replyTo, email);
                            for (; ; ) {
                                try {
                                    ex.execute(emt);
                                    break;
                                } catch (TaskRejectedException tre) {
                                    //noinspection BusyWait
                                    Thread.sleep(200L);
                                }
                            }
                        }
                        LOG.info("Campaign.processRecipientsFile(): finished processing file " + recipientFile);
                        ex.shutdown();
                    } catch (InterruptedException e) {
                        LOG.error("Campaign.processRecipientsFile.run(): interrupted, ", e);
                    } catch (Exception e) {
                        LOG.error("Campaign.processRecipientsFile.run(): caught exception, ", e);
                    }
                }).start();
    }

    @SuppressWarnings("unused")
    @PostConstruct
    public void init() {
        this.campaign = config.getCampaign();
        this.campaignRoot = config.getCampaignRoot();
        this.campaignDir = campaignRoot + '/' + campaign;
        this.store = new DataStore(campaignDir);
        this.emailThreads = config.getEmailThreads();
        this.replyTo = config.getReplyTo();
        LOG.info("DEBUG " + replyTo);
    }

    public Campaign() {
    }
}
