package it.chalmers.gamma.response.website;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class WebsiteAddedResponse extends ResponseEntity<String> {
    public WebsiteAddedResponse() {
        super("WEBSITE_ADDED", HttpStatus.ACCEPTED);
    }
}
