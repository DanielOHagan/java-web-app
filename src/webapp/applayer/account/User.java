package webapp.applayer.account;

public class User {

    /* Simple POJO class to store data from the database that will be displayed to the user */

    private final int mId;
    private final String mEmail;

    private String mUsername;

    public User(int id, String email, String username) {
        mId = id;
        mEmail = email;
        mUsername = username;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getUsername() {
        return mUsername;
    }

    public int getId() {
        return mId;
    }
}