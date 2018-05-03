package web.util;

public enum HttpVersion {
    HTTP_0_9("HTTP/0.9"),
    HTTP_1_0("HTTP/1.0"),
    HTTP_1_1("HTTP/1.1"),
    HTTP_2_0("HTTP/2.0");

    public String getName() {
        return name;
    }

    private String name;

    HttpVersion(String name) {
        this.name = name;
    }
}
