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

	@Autowired
	private Campaign campaign;

	@Autowired
	private ApplicationProperties config;

	@PostConstruct
	public void init() throws Exception {
		LOG.info("Application.init(): Campaign " + campaign.getCampaign());
		LOG.info("Application.init(): CampaignRoot " + campaign.getCampaignRoot());
		String recipientFile = config.getRecipientFile();
		if( (recipientFile==null) || (recipientFile.length()==0) )
		{
			LOG.error("Application.init(): recipient file not specified ( -DrecipientFile=someFile ). Exit.");
			return;
		}
		String emailFile = config.getEmailFile();
		if( (emailFile==null) || (emailFile.length()==0) )
		{
			LOG.error("Application.init(): email file not specified ( -DemailFile=someFile ). Exit.");
			return;
		}
		campaign.processRecipientsFile(recipientFile, emailFile);
	}

	public static void main(String[] args) {
		LOG.info("Application.main(): started");
		SpringApplication.run(Application.class, args);
		LOG.info("Application.main(): finished. Process expected to continue from here.");
	}
}
