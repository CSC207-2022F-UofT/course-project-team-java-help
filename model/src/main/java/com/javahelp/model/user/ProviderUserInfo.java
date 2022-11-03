package com.javahelp.model.user;

/**
 * Stores the information of a provider.
 */
public class ProviderUserInfo extends UserInfo {
    private String practiceName;
    private boolean certified;
    private String address;
    private String phoneNumber;

    /**
     * Constructs a ProviderInfo object.
     *
     * @param emailAddress: the email address of the provider.
     * @param address: the address of the provider.
     * @param phoneNumber: the phone number of the provider.
     * @param practiceName: the name of the provider.
     */
    public ProviderUserInfo(String emailAddress, String address, String phoneNumber,
                            String practiceName) {
        super(emailAddress);
        this.practiceName = practiceName;
        this.certified = false;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    /**
     * Gets the name of this provider.
     *
     * @return the name of this provider.
     */
    public String getPracticeName() {
        return practiceName;
    }

    /**
     * Sets the name of this provider.
     *
     * @param practiceName: the new name of this provider.
     */
    public void setPracticeName(String practiceName) {
        this.practiceName = practiceName;
    }

    /**
     * Gets whether this provider is confirmed to be a certified mental health professional.
     *
     * @return whether the provider is certified.
     */
    public boolean isCertified() {
        return certified;
    }

    /**
     * Sets the certification status of this provider.
     *
     * @param certified: true or false; whether this provider is certified.
     */
    public void setCertified(boolean certified) {
        this.certified = certified;
    }

    /**
     * Gets the address of this provider.
     *
     * @return the address of this provider.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the address of this provider.
     *
     * @param address: the new address of this provider.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Gets the phone number of this provider.
     *
     * @return the phone number of this provider.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the phone number of this provider.
     *
     * @param phoneNumber: the new phone number of this provider.
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public UserType getType() {
        return UserType.PROVIDER;
    }
}
