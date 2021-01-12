package it.chalmers.gamma.client.response;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import it.chalmers.gamma.client.ITClientDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class GetITClientResponse {
    @JsonUnwrapped
    private final ITClientDTO itClient;

    public GetITClientResponse(ITClientDTO itClient) {
        this.itClient = itClient;
    }

    public ITClientDTO getItClient() {
        return this.itClient;
    }

    public GetITClientResponseObject toResponseObject() {
        return new GetITClientResponseObject(this);
    }

    public static class GetITClientResponseObject extends ResponseEntity<GetITClientResponse> {
        GetITClientResponseObject(GetITClientResponse body) {
            super(body, HttpStatus.OK);
        }
    }
}
