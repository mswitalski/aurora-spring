package pl.lodz.p.aurora.min.outlook.dto;

public class EventBody {

    private String contentType = "text";
    private String content = "";

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
