package pl.lodz.p.aurora.common.domain.dto;

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

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}