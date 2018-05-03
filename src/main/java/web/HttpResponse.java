package web;

import web.util.HttpAnswerCode;
import web.util.HttpHeader;
import web.util.HttpVersion;

import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private HttpVersion version = HttpVersion.HTTP_1_1;
    private HttpAnswerCode code = HttpAnswerCode.CODE_200;

    private Map<String, String> headers = new HashMap<>();

    private  String content;

    public HttpResponse() {
        headers.put(HttpHeader.Connection.getName(), "close");
        headers.put(HttpHeader.ContentType.getName(), "text/html; charset=utf-8");
    }

    public void setContent(String content) {
        this.content = content;

        int contentLength = content.getBytes().length;
        headers.put(HttpHeader.ContentLength.getName(), Integer.toString(contentLength));
    }

    public void setVersion(HttpVersion version) {
        this.version = version;
    }

    public HttpVersion getVersion() {
        return version;
    }

    public void setCode(HttpAnswerCode code) {
        this.code = code;
    }

    public HttpAnswerCode getCode() {
        return code;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        appendStartLine(sb);
        sb.append("\n");

        if(headers.size() > 0){
            appendHeaders(sb);
        }

        sb.append("\n");

        sb.append(content);

        return sb.toString();
    }

    private void appendStartLine(StringBuilder sb){
        sb.append(version.getName()).append(" ");
        sb.append(code.getCode()).append(" ");
        sb.append(code.getDescription());
    }

    private void appendHeaders(StringBuilder sb){
        for(Map.Entry<String, String> header: headers.entrySet()){
            String key = header.getKey();
            String value = header.getValue();

            sb.append(key).append(": ").append(value);
            sb.append("\n");
        }
    }
}
