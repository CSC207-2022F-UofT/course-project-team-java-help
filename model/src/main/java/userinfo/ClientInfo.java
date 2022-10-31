package userinfo;

/**
 * Stores the information of a client.
 */
public class ClientInfo extends UserInfo {
    private String firstName;
    private String lastName;

    /**
     * Constructs a ClientInfo object.
     *
     * @param address: the address of this client
     * @param phoneNumber: the phone number of this client
     * @param firstName: the first name of this client
     * @param lastName: the last name of this client
     */
    public ClientInfo(String address, String phoneNumber, String firstName, String lastName) {
        super(address, phoneNumber);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String getType() {
        return "CLIENT";
    }
}
