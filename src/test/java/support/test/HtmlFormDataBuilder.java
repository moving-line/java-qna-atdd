package support.test;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;

public class HtmlFormDataBuilder {
    private HttpHeaders headers;
    private MultiValueMap<String, Object> params;

    public HtmlFormDataBuilder(HttpHeaders headers) {
        this.headers = headers;
        this.params = new LinkedMultiValueMap<>();
    }

    public HtmlFormDataBuilder addParameter(String key, Object value) {
        this.params.add(key, value);
        return this;
    }

    public static HtmlFormDataBuilder urlEncodedForm() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.TEXT_HTML));
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return new HtmlFormDataBuilder(headers);
    }

    public HttpEntity<MultiValueMap<String, Object>> build() {
        return new HttpEntity<>(params, headers);
    }

    public HtmlFormDataBuilder put() {
        addParameter("_method", "put");
        return this;
    }

    public HtmlFormDataBuilder delete() {
        addParameter("_method", "delete");
        return this;
    }
}
