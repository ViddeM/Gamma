package it.chalmers.gamma.service;

import it.chalmers.gamma.db.entity.FKITGroup;
import it.chalmers.gamma.db.repository.FKITGroupRepository;
import it.chalmers.gamma.domain.dto.group.FKITGroupDTO;

import it.chalmers.gamma.domain.dto.group.FKITSuperGroupDTO;
import it.chalmers.gamma.response.group.GroupDoesNotExistResponse;
import it.chalmers.gamma.util.UUIDUtil;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class FKITGroupService {

    private final FKITGroupRepository repo;

    private final FKITSuperGroupService superGroupService;

    public FKITGroupService(FKITGroupRepository repo, FKITSuperGroupService superGroupService) {

        this.repo = repo;
        this.superGroupService = superGroupService;
    }


    public FKITGroupDTO createGroup(FKITGroupDTO fkitGroupDTO) {
        FKITGroup fkitGroup = new FKITGroup();
        UUID id = fkitGroupDTO.getId();
        if (id == null) {
            id = UUID.randomUUID();
        }
        fkitGroup.setId(id);
        fkitGroup.setName(fkitGroupDTO.getName());
        fkitGroup.setFunction(fkitGroupDTO.getFunction());
        fkitGroup.setDescription(fkitGroupDTO.getDescription());
        return saveGroup(fkitGroup,
                fkitGroup.getPrettyName() == null ? fkitGroupDTO.getName() : fkitGroupDTO.getPrettyName(),
                fkitGroupDTO.getBecomesActive(), fkitGroupDTO.getBecomesInactive(),
                fkitGroupDTO.getEmail(), fkitGroupDTO.getAvatarURL(), fkitGroupDTO.getSuperGroup());
    }

    public FKITGroupDTO editGroup(String id, FKITGroupDTO fkitGroupDTO) {
        FKITGroup group = this.fromDTO(this.getGroup(id));
        if (group == null) {
            return null;
        }
        group.getFunction().setSv(fkitGroupDTO.getFunction() == null
                ? group.getFunction().getSv() : fkitGroupDTO.getFunction().getSv());

        group.getFunction().setEn(fkitGroupDTO.getFunction() == null
                ? group.getFunction().getEn() : fkitGroupDTO.getFunction().getEn());

        if (fkitGroupDTO.getDescription() != null && group.getDescription() != null) {
            group.getDescription().setSv(fkitGroupDTO.getDescription().getSv());
            group.getDescription().setEn(fkitGroupDTO.getDescription().getEn());
        }
        return saveGroup(group, fkitGroupDTO.getPrettyName(), fkitGroupDTO.getBecomesActive(),
                fkitGroupDTO.getBecomesInactive(),
                fkitGroupDTO.getEmail(), fkitGroupDTO.getAvatarURL(), fkitGroupDTO.getSuperGroup());
    }

    private FKITGroupDTO saveGroup(FKITGroup group, String prettyName,
                                   Calendar becomesActive, Calendar becomesInactive,
                                   String email, String avatarURL, FKITSuperGroupDTO superGroup) {
        group.setPrettyName(prettyName == null ? group.getPrettyName() : prettyName);
        group.setEmail(email == null ? group.getEmail() : email);
        group.setAvatarURL(avatarURL == null ? group.getAvatarURL() : avatarURL);
        group.setBecomesActive(becomesActive == null ? group.getBecomesActive() : becomesActive);
        group.setBecomesInactive(becomesInactive == null ? group.getBecomesInactive() : becomesInactive);
        group.setSuperGroup(superGroup == null ? group.getSuperGroup() : this.superGroupService.getGroup(superGroup));
        return this.repo.save(group).toDTO();
    }

    public List<FKITGroupDTO> getAllGroupsWithSuperGroup(FKITSuperGroupDTO superGroupDTO) {
        return this.repo.findAllBySuperGroup(this.superGroupService.getGroup(superGroupDTO)).stream()
                .map(FKITGroup::toDTO)
                .collect(Collectors.toList());
    }

    public List<FKITGroupDTO> getAllActiveGroups() {
        return this.getGroups().stream().filter(FKITGroupDTO::isActive).collect(Collectors.toList());
    }

    public List<FKITGroupDTO> getActiveGroups(FKITSuperGroupDTO superGroup) {
        return this.getAllGroupsWithSuperGroup(superGroup).stream()
                .filter(FKITGroupDTO::isActive)
                .collect(Collectors.toList());
    }

    public boolean groupExists(String name) {
        if (UUIDUtil.validUUID(name)) {
            return this.repo.existsById(UUID.fromString(name));
        }
        return this.repo.existsFKITGroupByName(name);
    }

    public void removeGroup(String name) {
        this.repo.deleteByName(name);
    }

    public void removeGroup(UUID groupId) {
        this.repo.deleteById(groupId);
    }

    public List<FKITGroupDTO> getGroups() {
        return this.repo.findAll().stream().map(FKITGroup::toDTO).collect(Collectors.toList());
    }

    public FKITGroupDTO getGroup(String name) {
        if (UUIDUtil.validUUID(name)) {
            return this.repo.findById(UUID.fromString(name))
                    .orElseThrow(GroupDoesNotExistResponse::new).toDTO();
        }
        return this.repo.findByName(name.toLowerCase())
                .orElseThrow(GroupDoesNotExistResponse::new)
                .toDTO();
    }

    public void editGroupAvatar(FKITGroupDTO groupDTO, String url) {
        FKITGroup group = this.fromDTO(groupDTO);
        if (group == null) {
            throw new GroupDoesNotExistResponse();
        }
        group.setAvatarURL(url);
        this.repo.save(group);
    }

    protected FKITGroup fromDTO(FKITGroupDTO group) {
        return this.repo.findById(group.getId()).orElse(null);
    }

}
