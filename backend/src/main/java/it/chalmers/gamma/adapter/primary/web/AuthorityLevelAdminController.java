package it.chalmers.gamma.adapter.primary.web;

import it.chalmers.gamma.app.AuthorityLevelFacade;
import it.chalmers.gamma.domain.authoritylevel.AuthorityLevel;
import it.chalmers.gamma.domain.authoritylevel.AuthorityLevelName;
import it.chalmers.gamma.util.response.AlreadyExistsResponse;
import it.chalmers.gamma.util.response.NotFoundResponse;
import it.chalmers.gamma.util.response.SuccessResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/internal/admin/authority/level")
public final class AuthorityLevelAdminController {

    private final AuthorityLevelFacade authorityLevelFacade;

    public AuthorityLevelAdminController(AuthorityLevelFacade authorityLevelFacade) {
        this.authorityLevelFacade = authorityLevelFacade;
    }

    @PostMapping()
    public AuthorityLevelCreatedResponse addAuthorityLevel(@RequestBody CreateAuthorityLevelRequest request) {
        this.authorityLevelFacade.create(request.authorityLevel);
        return new AuthorityLevelCreatedResponse();
    }

    private record CreateAuthorityLevelRequest(AuthorityLevelName authorityLevel) { }

    @GetMapping
    public List<AuthorityLevel> getAllAuthorityLevels() {
        return this.authorityLevelFacade.getAll();
    }

    @DeleteMapping("/{name}")
    public AuthorityLevelDeletedResponse removeAuthorityLevel(@PathVariable("name") AuthorityLevelName name) {
        this.authorityLevelFacade.delete(name);
        return new AuthorityLevelDeletedResponse();
    }

    @GetMapping("/{name}")
    public AuthorityLevel getAuthorityLevel(@PathVariable("name") AuthorityLevelName name) {
        return this.authorityLevelFacade.get(name)
                .orElseThrow(AuthorityLevelNotFoundResponse::new);
    }

    private static class AuthorityLevelDeletedResponse extends SuccessResponse { }

    private static class AuthorityLevelCreatedResponse extends SuccessResponse { }

    private static class AuthorityLevelNotFoundResponse extends NotFoundResponse { }

    private static class AuthorityLevelAlreadyExistsResponse extends AlreadyExistsResponse { }

}
