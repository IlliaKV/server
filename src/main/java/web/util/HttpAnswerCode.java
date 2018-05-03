package web.util;

public enum HttpAnswerCode {
    CODE_200(200, "OK"),
    CODE_404(404, "NOT FOUND"),
    CODE_503(503, "FORBIDDEN");

    private  int code;
    private String description;

    HttpAnswerCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
