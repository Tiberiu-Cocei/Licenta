package utility;

public class ApiResponse {

    private String json;

    private Integer code;

    private String message;

    ApiResponse(String json, int code, String message) {
        this.json = json;
        this.code = code;
        this.message = message;
    }

    public String getJson() {
        return json;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
