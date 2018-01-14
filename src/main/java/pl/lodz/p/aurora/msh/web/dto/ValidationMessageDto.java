package pl.lodz.p.aurora.msh.web.dto;

/**
 * DTO class for validation message that are returned to client application.
 */
public class ValidationMessageDto {

    private String messageContent;
    private String fieldName;

    public ValidationMessageDto(String messageContent, String fieldName) {
        this.messageContent = messageContent;
        this.fieldName = fieldName;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public String getFieldName() {
        return fieldName;
    }
}
