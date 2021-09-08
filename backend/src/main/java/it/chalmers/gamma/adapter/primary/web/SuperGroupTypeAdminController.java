package it.chalmers.gamma.adapter.primary.web;

import it.chalmers.gamma.app.SuperGroupFacade;
import it.chalmers.gamma.domain.supergroup.SuperGroup;
import it.chalmers.gamma.domain.supergroup.SuperGroupType;
import it.chalmers.gamma.app.supergroup.SuperGroupTypeRepository;
import it.chalmers.gamma.util.response.AlreadyExistsResponse;
import it.chalmers.gamma.util.response.ErrorResponse;
import it.chalmers.gamma.util.response.NotFoundResponse;
import it.chalmers.gamma.util.response.SuccessResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/internal/admin/supergrouptype")
public class SuperGroupTypeAdminController {

    private final SuperGroupFacade superGroupFacade;

    public SuperGroupTypeAdminController(SuperGroupFacade superGroupFacade) {
        this.superGroupFacade = superGroupFacade;
    }

    @GetMapping
    public List<SuperGroupType> getSuperGroupTypes() {
        return this.superGroupFacade.getAllTypes();
    }

    @GetMapping("/{type}/usage")
    public List<SuperGroup> getSuperGroupTypeUsage(@PathVariable("type") SuperGroupType type) {
        return this.superGroupFacade.getAllSuperGroupsByType(type);
    }

    private record AddSuperGroupType(SuperGroupType type) { }
    
    @PostMapping
    public SuperGroupTypeAddedResponse addSuperGroupType(@RequestBody AddSuperGroupType request) {
        try {
            this.superGroupFacade.addType(request.type);
        } catch (SuperGroupTypeRepository.SuperGroupTypeAlreadyExistsException e) {
            throw new SuperGroupTypeAlreadyExistsResponse();
        }
        return new SuperGroupTypeAddedResponse();
    }

    @DeleteMapping("/{name}")
    public SuperGroupTypeRemovedResponse removeSuperGroupType(@PathVariable("name") SuperGroupType name) {
        try {
            this.superGroupFacade.removeType(name);
        } catch (SuperGroupTypeRepository.SuperGroupTypeNotFoundException e) {
           throw new SuperGroupTypeDoesNotExistResponse();
        } catch (SuperGroupTypeRepository.SuperGroupTypeHasUsagesException e) {
            throw new SuperGroupTypeIsUsedResponse();
        }
        return new SuperGroupTypeRemovedResponse();
    }

    private static class SuperGroupTypeAddedResponse extends SuccessResponse { }

    private static class SuperGroupTypeRemovedResponse extends SuccessResponse { }

    private static class SuperGroupTypeAlreadyExistsResponse extends AlreadyExistsResponse { }

    private static class SuperGroupTypeDoesNotExistResponse extends NotFoundResponse { }

    private static class SuperGroupTypeIsUsedResponse extends ErrorResponse {
        private SuperGroupTypeIsUsedResponse() {
            super(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

}
