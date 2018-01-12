package pl.lodz.p.aurora.users.web.dto;

/**
 * DTO containing data from user login form.
 */
public class UserAccountCredentialsDto {

    private String username;
    private String password;

    public UserAccountCredentialsDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UserAccountCredentialsDto() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
