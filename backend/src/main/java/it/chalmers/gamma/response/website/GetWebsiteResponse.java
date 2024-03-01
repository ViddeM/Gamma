package it.chalmers.gamma.response.website;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import it.chalmers.gamma.domain.dto.website.WebsiteDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class GetWebsiteResponse {

    @JsonUnwrapped
    private final WebsiteDTO website;

    public GetWebsiteResponse(WebsiteDTO website) {
        this.website = website;
    }

    public WebsiteDTO getWebsite() {
        return this.website;
    }

    public GetWebsiteResponseObject toResponseObject() {
        return new GetWebsiteResponseObject(this);
    }

    public static class GetWebsiteResponseObject extends ResponseEntity<GetWebsiteResponse> {
        GetWebsiteResponseObject(GetWebsiteResponse body) {
            super(body, HttpStatus.OK);
        }
    }
}
