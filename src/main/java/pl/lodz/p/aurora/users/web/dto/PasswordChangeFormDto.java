package pl.lodz.p.aurora.users.web.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * DTO containing data from change password form.
 */
public class PasswordChangeFormDto {

    @NotNull(message = "{Default.NotNull}")
    @Size(min = 3, max = 60, message = "{Default.Size.MinMax}")
    private String currentPassword;

    @NotNull(message = "{Default.NotNull}")
    @Size(min = 3, max = 60, message = "{Default.Size.MinMax}")
    private String newPassword;

    @NotNull(message = "{Default.NotNull}")
    @Size(min = 3, max = 60, message = "{Default.Size.MinMax}")
    private String newPasswordRepeated;

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPasswordRepeated() {
        return newPasswordRepeated;
    }

    public void setNewPasswordRepeated(String newPasswordRepeated) {
        this.newPasswordRepeated = newPasswordRepeated;
    }
}
