package userinfo;

/**
 * Stores the information of a user.
 */
public abstract class UserInfo {
    private String address;
    private String phoneNumber;

    /**
     * Constructs a UserInfo object.
     *
     * @param address: the address of the user
     * @param phoneNumber: the phone number of the user
     */
    public UserInfo(String address, String phoneNumber){
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Gets the type of the user.
     * @return "CLIENT" or "PROVIDER" depending on the type of this user
     */
    public abstract String getType();
}
