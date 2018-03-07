package sz.test.blackSilverBassetHound;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties
@ConfigurationProperties(value = "")
public class ApplicationProperties {

    private String campaign;
    private String campaignRoot;
    private String recipientFile;
    private String replyTo;
    private String emailFile;
    private int emailThreads;

    public String getCampaign() {
        return campaign;
    }

    public void setCampaign(String campaign) {
        this.campaign = campaign;
    }

    public String getCampaignRoot() {
        return campaignRoot;
    }

    public void setCampaignRoot(String campaignRoot) {
        this.campaignRoot = campaignRoot;
    }

    public String getRecipientFile() {
        return recipientFile;
    }

    public void setRecipientFile(String recipientFile) {
        this.recipientFile = recipientFile;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    public String getEmailFile() {
        return emailFile;
    }

    public void setEmailFile(String emailFile) {
        this.emailFile = emailFile;
    }

    public int getEmailThreads() {
        return emailThreads;
    }

    public void setEmailThreads(int emailThreads) {
        this.emailThreads = emailThreads;
    }
}
