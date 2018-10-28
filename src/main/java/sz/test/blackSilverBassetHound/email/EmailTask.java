package sz.test.blackSilverBassetHound.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmailTask implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(EmailTask.class);

    private final String recipientAddress;

    public EmailTask(String recipientAddress, @SuppressWarnings("unused") String replyTo, @SuppressWarnings("unused") String email)
    {
        this.recipientAddress = recipientAddress;
    }

    public void run()
    {
        if( LOG.isDebugEnabled() ) {
            LOG.debug("EmailTask.run(): started, recipient " + recipientAddress);
        }
        try{
            Thread.sleep(500L);
        } catch( InterruptedException ignored) {}
        if( LOG.isDebugEnabled() ) {
            LOG.debug("EmailTask.run(): finished");
        }
    }
}
