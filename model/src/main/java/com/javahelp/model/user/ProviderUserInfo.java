package com.javahelp.model.user;

/**
 * Stores the information of a provider.
 */
public class ProviderUserInfo extends UserInfo {
    private String practiceName;
    private boolean certified = false;
    private String address;
    private String phoneNumber;
    private Gender gender = Gender.UNMENTIONED;

    /**
     * Constructs a {@link ProviderUserInfo} object given their {@link Gender}.
     *
     * @param emailAddress the email address of the provider
     * @param address the address of the provider
     * @param phoneNumber the phone number of the provider
     * @param practiceName the name of the provider
     * @param gender the given gender of the provider
     */
    public ProviderUserInfo(String emailAddress, String address, String phoneNumber,
                            String practiceName, Gender gender) {
        this(emailAddress, address, phoneNumber, practiceName);
        setGender(gender);
    }

    /**
     * Constructs a {@link ProviderUserInfo} object without given certification status and
     * {@link Gender}.
     *
     * @param emailAddress the email address of the provider
     * @param address the address of the provider
     * @param phoneNumber the phone number of the provider
     * @param practiceName the name of the provider
     */
    public ProviderUserInfo(String emailAddress, String address, String phoneNumber,
                            String practiceName) {
        super(emailAddress);
        setPracticeName(practiceName);
        setAddress(address);
        setPhoneNumber(phoneNumber);
    }

    /**
     * Constructs a {@link ProviderUserInfo} object given certification status.
     *
     * @param emailAddress the email address of the provider
     * @param address the address of the provider
     * @param phoneNumber the phone number of the provider
     * @param practiceName the name of the provider
     * @param certified the given certification status of this provider
     */
    public ProviderUserInfo(String emailAddress, String address, String phoneNumber,
                            String practiceName, boolean certified) {
        this(emailAddress, address, phoneNumber, practiceName);
        setCertified(certified);
    }

    /**
     * Constructs a {@link ProviderUserInfo} object given certification status.
     *
     * @param emailAddress the email address of the provider
     * @param address the address of the provider
     * @param phoneNumber the phone number of the provider
     * @param practiceName the name of the provider
     * @param certified the given certification status of this provider
     * @param gender the given gender of the provider
     */
    public ProviderUserInfo(String emailAddress, String address, String phoneNumber,
                            String practiceName, boolean certified, Gender gender) {
        this(emailAddress, address, phoneNumber, practiceName, gender);
        setCertified(certified);
    }


    /**
     * Gets the name of this provider.
     *
     * @return the {@link String} name of this provider.
     */
    public String getPracticeName() {
        return practiceName;
    }

    /**
     * Sets the name of this provider.
     *
     * @param practiceName the new name of this provider.
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
     * @param certified true or false; whether this provider is certified.
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
     * @param address the new address of this provider.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Gets the phone number of this provider.
     *
     * @return the {@link String} phone number of this provider.
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

    /**
     * Gets the {@link Gender} of this provider
     *
     * @return the current {@link Gender} of this client
     */
    public Gender getGender(){
        return gender;
    }

    /**
     * Sets an updated status of this provider's {@link Gender}
     *
     * @param gender an updated {@link Gender} of this client
     */
    public void setGender(Gender gender){
        this.gender = gender;
    }

    @Override
    public UserType getType() {
        return UserType.PROVIDER;
    }
}
