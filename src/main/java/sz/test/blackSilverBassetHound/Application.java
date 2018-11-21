package sz.test.blackSilverBassetHound;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class Application {

    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "unused"})
    @Autowired
    private Campaign campaign;

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "unused"})
    @Autowired
    private ApplicationProperties config;

    public Application() { }

    @SuppressWarnings("unused")
    @PostConstruct
    public void init() {
        LOG.info("Application.init(): Campaign " + campaign.getCampaign());
        LOG.info("Application.init(): CampaignRoot " + campaign.getCampaignRoot());
        String recipientFile = config.getRecipientFile();
        if ((recipientFile == null) || recipientFile.isEmpty()) {
            LOG.error("Application.init(): recipient file not specified ( -DrecipientFile=someFile ). Exit.");
            return;
        }
        String emailFile = config.getEmailFile();
        if ((emailFile == null) || emailFile.isEmpty()) {
            LOG.error("Application.init(): email file not specified ( -DemailFile=someFile ). Exit.");
            return;
        }
        this.campaign.processRecipientsFile(recipientFile, emailFile);
    }

    public static void main(final String[] args) {
        LOG.info("Application.main(): started");
        //noinspection resource
        SpringApplication.run(Application.class, args);
        LOG.info("Application.main(): finished. Process expected to continue from here.");
    }

    @Override
    public String toString() {
        return "Application{" +
                "campaign=" + campaign +
                ", config=" + config +
                '}';
    }
}
