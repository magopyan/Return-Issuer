package backoffice_credentials;

public class BackOfficeUser {

    private String email;
    private String password;

    private static BackOfficeUser BOUserInstance = null;

    private BackOfficeUser() {}

    public static BackOfficeUser getBOUserInstance() {

        if(BOUserInstance == null) {
            BOUserInstance = new BackOfficeUser();
        }
        return BOUserInstance;
    }

    public void initBOUser(String email, String password) {
        BOUserInstance.email = email;
        BOUserInstance.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
