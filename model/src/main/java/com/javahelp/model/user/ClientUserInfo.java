package com.javahelp.model.user;

/**
 * Stores the information of a client.
 */
public class ClientUserInfo extends UserInfo {
    private String firstName;
    private String lastName;
    private String address;
    private String phoneNumber;
    private Gender gender;

    /**
     * Constructs a ClientInfo object.
     *
     * @param emailAddress the email address of this client.
     * @param address the address of this client.
     * @param phoneNumber the phone number of this client.
     * @param firstName the first name of this client.
     * @param lastName the last name of this client.
     * @param gender the gender of this client
     */
    public ClientUserInfo(String emailAddress, String address, String phoneNumber, String firstName,
                          String lastName, Gender gender) {
        this(emailAddress, address, phoneNumber, firstName, lastName);
        this.setGender(gender);
    }

    /**
     * Constructs a ClientInfo object but without given Gender, so the Gender will be set as
     * UNMENTIONED as a default
     *
     * @param emailAddress the email address of this client.
     * @param address the address of this client.
     * @param phoneNumber the phone number of this client.
     * @param firstName the first name of this client.
     * @param lastName the last name of this client.
     */
    public ClientUserInfo(String emailAddress, String address, String phoneNumber, String firstName,
                          String lastName) {
        super(emailAddress);
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.setGender(Gender.UNMENTIONED);
    }

    /**
     * Gets the first name of this client.
     *
     * @return the first name of this client.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of this client.
     *
     * @param firstName: the new first name of this client.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the last name of this client.
     *
     * @return the last name of this client.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of this client.
     *
     * @param lastName: the new last name of this client.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the address of this client.
     *
     * @return the address of this client.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the address of this client.
     *
     * @param address: the new address of this client.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Gets the phone number of this client.
     *
     * @return the phone number of this client.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the phone number of this client.
     *
     * @param phoneNumber: the new phone number of this client.
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Gets the gender of this provider
     *
     * @return the current gender of this client
     */
    public Gender getGender(){
        return gender;
    }

    /**
     * Sets a new gender to this client
     *
     * @param gender an updated gender of this client
     */
    public void setGender(Gender gender){
        this.gender = gender;
    }

    @Override
    public UserType getType() {
        return UserType.CLIENT;
    }
}
