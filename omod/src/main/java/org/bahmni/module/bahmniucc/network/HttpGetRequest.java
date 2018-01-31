package org.bahmni.module.bahmniucc.network;

/**
 * Created by ucc-ian on 02/Jan/2018.
 */
public class HttpGetRequest implements NetworkRequest {

    private HttpRequest httpRequest;
    private String url;
    private String contentType;
    private String authorizationHeader;

    public HttpGetRequest(HttpRequest httpRequest, String url,  String authorizationHeader, String contentType) {
        this.httpRequest = httpRequest;
        this.url = url;
        this.contentType = contentType;
        this.authorizationHeader=authorizationHeader;
    }

    @Override
    public String execute() {
        return httpRequest.httpGetRequest(url, authorizationHeader, contentType);
    }


}
