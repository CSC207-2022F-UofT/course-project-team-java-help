package userinfo;

/**
 * Stores the information of a provider.
 */
public class ProviderInfo extends UserInfo {
    private String practiceName;
    private boolean certified;

    /**
     * Constructs a ProviderInfo object.
     *
     * @param address: the address of the provider
     * @param phoneNumber: the phone number of the provider
     * @param practiceName: the name of the provider
     */
    public ProviderInfo(String address, String phoneNumber, String practiceName) {
        super(address, phoneNumber);
        this.practiceName = practiceName;
        this.certified = false;
    }

    public String getPracticeName() {
        return practiceName;
    }

    public void setPracticeName(String practiceName) {
        this.practiceName = practiceName;
    }

    /**
     * Returns whether this provider is confirmed to be a certified mental health professional.
     *
     * @return whether the provider is certified
     */
    public boolean isCertified() {
        return certified;
    }

    /**
     * Sets the certification status of this provider.
     *
     * @param certified: true or false; whether this provider is certified
     */
    public void setCertified(boolean certified) {
        this.certified = certified;
    }

    @Override
    public String getType() {
        return "PROVIDER";
    }
}
