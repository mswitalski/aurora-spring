package pl.lodz.p.aurora.users.domain.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * DTO containing data from change password form.
 */
public class PasswordChangeFormDto {

    @NotNull(message = "{PasswordChangeFormDto.currentPassword.NotNull}")
    @Size(min = 3, max = 60, message = "{PasswordChangeFormDto.currentPassword.Size}")
    private String currentPassword;
    @NotNull(message = "{PasswordChangeFormDto.newPassword.NotNull}")
    @Size(min = 3, max = 60, message = "{PasswordChangeFormDto.newPassword.Size}")
    private String newPassword;
    @NotNull(message = "{PasswordChangeFormDto.newPasswordRepeated.NotNull}")
    @Size(min = 3, max = 60, message = "{PasswordChangeFormDto.newPasswordRepeated.Size}")
    private String newPasswordRepeated;

    public PasswordChangeFormDto() {
    }

    public PasswordChangeFormDto(String currentPassword, String newPassword, String newPasswordRepeated) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
        this.newPasswordRepeated = newPasswordRepeated;
    }

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
