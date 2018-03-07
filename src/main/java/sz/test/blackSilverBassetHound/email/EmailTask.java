package sz.test.blackSilverBassetHound.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmailTask implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(EmailTask.class);

    private String recipientAddress = null;
    private String replyTo = null;
    private String email = null;

    public EmailTask(String recipientAddress, String replyTo, String email)
    {
        this.recipientAddress = recipientAddress;
        this.replyTo = replyTo;
        this.email = email;
    }

    public void run()
    {
        if( LOG.isDebugEnabled() ) {
            LOG.debug("EmailTask.run(): started, recipient " + recipientAddress);
        }
        try{
            Thread.sleep(500L);
        } catch( InterruptedException ie ) {}
        if( LOG.isDebugEnabled() ) {
            LOG.debug("EmailTask.run(): finished");
        }
    }
}
