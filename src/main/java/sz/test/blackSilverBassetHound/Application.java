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

	private String inputFile = null;

	@PostConstruct
	public void init() throws Exception {
		LOG.info("Application.init(): Campaign " + campaign.getCampaign());
		LOG.info("Application.init(): CampaignRoot " + campaign.getCampaignRoot());
		this.inputFile = System.getProperty("inputFile");
		if( (this.inputFile==null) || (this.inputFile.length()==0) )
		{
			LOG.error("Application.init(): input file not specified ( -DinputFile=someFile ). Exit.");
			return;
		}
		campaign.processRecipientsFile(inputFile);
	}

	public static void main(String[] args) {
		LOG.info("Application.main(): started");
		SpringApplication.run(Application.class, args);
		LOG.info("Application.main(): finished. Process expected to continue from here.");
	}
}
