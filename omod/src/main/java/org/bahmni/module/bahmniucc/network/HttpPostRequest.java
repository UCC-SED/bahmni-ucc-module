package org.bahmni.module.bahmniucc.network;

/**
 * Created by ucc-ian on 02/Jan/2018.
 */
public class HttpPostRequest implements NetworkRequest {

    private HttpRequest httpRequest;
    private String url;
    private String content;
    private String contentType;
    private String token;

    public HttpPostRequest(HttpRequest httpRequest, String url, String token, String content, String contentType) {
        this.httpRequest = httpRequest;
        this.token=token;
        this.content=content;
        this.contentType=contentType;
        this.url=url;
    }

    @Override
    public String execute() {
        return httpRequest.httpPostRequest(url, content, token, contentType);


    }
}
