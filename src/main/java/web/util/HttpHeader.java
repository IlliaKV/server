package web.util;

public enum HttpHeader {
    Connection("Connection"),
    ContentLength("Content-Length"),
    ContentType("Content-type");

    private String name;

    HttpHeader(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
