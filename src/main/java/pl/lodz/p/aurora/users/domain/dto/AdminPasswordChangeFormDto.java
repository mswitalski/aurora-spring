package pl.lodz.p.aurora.users.domain.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AdminPasswordChangeFormDto {

    @NotNull(message = "{Default.NotNull}")
    @Size(min = 3, max = 60, message = "{Default.Size.MinMax}")
    private String newPassword;

    @NotNull(message = "{Default.NotNull}")
    @Size(min = 3, max = 60, message = "{Default.Size.MinMax}")
    private String newPasswordRepeated;

    public AdminPasswordChangeFormDto() {
    }

    public AdminPasswordChangeFormDto(String newPassword, String newPasswordRepeated) {
        this.newPassword = newPassword;
        this.newPasswordRepeated = newPasswordRepeated;
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
