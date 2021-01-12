package it.chalmers.gamma.goldapps;

import it.chalmers.gamma.domain.GroupType;
import it.chalmers.gamma.group.FKITGroupDTO;
import it.chalmers.gamma.supergroup.FKITSuperGroupDTO;
import it.chalmers.gamma.post.PostDTO;
import it.chalmers.gamma.user.ITUserDTO;
import it.chalmers.gamma.response.GoldappsReponse;
import it.chalmers.gamma.group.GroupService;
import it.chalmers.gamma.supergroup.FKITSuperGroupService;
import it.chalmers.gamma.membership.MembershipService;
import it.chalmers.gamma.post.PostService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/goldapps")
public class GoldappsController {

    private final FKITSuperGroupService fkitSuperGroupService;
    private final GroupService groupService;
    private final MembershipService membershipService;
    private final PostService postService;

    public GoldappsController(FKITSuperGroupService fkitSuperGroupService,
                              GroupService groupService,
                              MembershipService membershipService,
                              PostService postService) {
        this.fkitSuperGroupService = fkitSuperGroupService;
        this.groupService = groupService;
        this.membershipService = membershipService;
        this.postService = postService;
    }

    @GetMapping()
    public ResponseEntity<JSONObject> getGoldappsData() {
        List<JSONObject> groupsJSON = new ArrayList<>();
        List<JSONObject> usersJSON = new ArrayList<>();

        // Fetch all Committees
        List<FKITSuperGroupDTO> committees = this.fkitSuperGroupService.getAllGroups().stream()
                .filter(g -> g.getType().equals(GroupType.COMMITTEE)).collect(Collectors.toList());

        List<FKITGroupDTO> groups = committees.stream()
                .flatMap(g -> this.groupService.getAllGroupsWithSuperGroup(g).stream())
                .collect(Collectors.toList());

        GoldappsUserSerializer goldappsUserSerializer = new GoldappsUserSerializer();
        GoldappsGroupSerializer goldappsGroupSerializer = new GoldappsGroupSerializer();

        // Go through all groups and serialize them and their users.
        groups.stream().filter(FKITGroupDTO::isActive).forEach(g -> {
            usersJSON.addAll(
                    this.membershipService.getMembershipsInGroup(g).stream()
                            .map(m -> goldappsUserSerializer.serialize(m.getUser()))
                            .collect(Collectors.toList()));
        });
        groups = this.fkitSuperGroupService.getAllGroups()          // Really ugly solution, should be refactored
                .stream()
                .filter(g -> !g.getType().equals(GroupType.ADMIN))
                .flatMap(g -> this.groupService.getAllGroupsWithSuperGroup(g).stream())
                .distinct()
                .collect(Collectors.toList());

        groups.forEach(g -> groupsJSON.add(
                goldappsGroupSerializer.serialize(g.getEmail(),
                        this.membershipService.getMembershipsInGroup(g).stream()
                                .map(m -> this.getCorrectEmail(m.getUser(), g)).collect(Collectors.toList()))));
        // Construct and send the payload

        List<FKITGroupDTO> activeGroups = this.groupService.getAllActiveGroups();
        groupsJSON.addAll(activeGroups.stream()
                .map(g ->
                    goldappsGroupSerializer.serialize(g.getSuperGroup().getEmail(),
                    this.groupService.getAllGroupsWithSuperGroup(g.getSuperGroup()).stream()
                            .map(FKITGroupDTO::getEmail).collect(Collectors.toList())))
                .collect(Collectors.toList()));
        JSONObject payload = new JSONObject();
        List<JSONObject> groupsJSONWithCustom = addCustomGroups(groupsJSON, goldappsGroupSerializer);
        groupsJSONWithCustom = addAliases(groupsJSON, goldappsGroupSerializer);

        List<JSONObject> usersJSONFiltered = usersJSON.stream().distinct().collect(Collectors.toList());
        payload.put("groups", groupsJSONWithCustom);
        payload.put("users", usersJSONFiltered);

        return new GoldappsReponse(payload);
    }

    private List<JSONObject> addCustomGroups(List<JSONObject> groupsJSON,
                                             GoldappsGroupSerializer goldappsGroupSerializer) {

        groupsJSON.add(goldappsGroupSerializer.serialize("fkit@chalmers.it",
                this.fkitSuperGroupService.getAllGroups().stream().filter(group ->
                        group.getType().equals(GroupType.COMMITTEE) || group.getType().equals(GroupType.SOCIETY))
                        .map(FKITSuperGroupDTO::getEmail).collect(Collectors.toList())));
        this.postService.getAllPosts().stream().filter(post -> !post.getPostName().getSv().equals("medlem"))
                .forEach(post -> this.membershipService.getMembershipsByPost(post)
                        .forEach(membership -> {
                            groupsJSON.add(goldappsGroupSerializer.serialize(
                                    post.getEmailPrefix() + "." + membership
                                            // This works out a technicality, should be rewritten
                                            .getId().getFKITGroup().getSuperGroup().getEmail(),
                                    List.of(this.getCorrectEmail(membership.getId().getITUser().toDTO(),
                                            membership.getId().getFKITGroup().toDTO()))));
                        }));
        groupsJSON.add(goldappsGroupSerializer.serialize("kit@chalmers.it",
                this.fkitSuperGroupService.getAllGroups().stream()
                        .filter(g -> g.getType().equals(GroupType.COMMITTEE))
                        .map(FKITSuperGroupDTO::getEmail).collect(Collectors.toList())));
        return groupsJSON;
    }

    private List<JSONObject> addAliases(List<JSONObject> groupsJson, GoldappsGroupSerializer goldappsGroupSerializer) {
        PostDTO kassor = this.postService.getPostDTO("kassör");

        List<FKITSuperGroupDTO> groups = this.fkitSuperGroupService.getAllGroups()
                .stream().filter(g -> g.getType().equals(GroupType.COMMITTEE))
                .collect(Collectors.toList());

        this.addAliasToJson(goldappsGroupSerializer, groupsJson, groups,
                kassor, "kassorer.kommitteer@chalmers.it");

        PostDTO ordf = this.postService.getPostDTO("ordförande");
        this.addAliasToJson(goldappsGroupSerializer, groupsJson, groups,
                ordf, "ordforanden.kommitteer@chalmers.it");

        List<FKITSuperGroupDTO> committeeGroups = this.fkitSuperGroupService
                .getAllGroups().stream().filter(g -> g.getType().equals(GroupType.COMMITTEE))
                .collect(Collectors.toList());

        this.addAliasToJson(goldappsGroupSerializer, groupsJson, committeeGroups, ordf, "kassorer@chalmers.it");

        this.addAliasToJson(goldappsGroupSerializer, groupsJson, committeeGroups, ordf, "ordforanden@chalmers.it");

        return groupsJson;
    }

    private List<JSONObject> addAliasToJson(GoldappsGroupSerializer goldappsGroupSerializer,
                                            List<JSONObject> groupsJson,
                                            List<FKITSuperGroupDTO> groups,
                                            PostDTO post,
                                            String mail
    ) {
        groupsJson.add(goldappsGroupSerializer.serialize(mail,
                groups.stream().map(group -> post.getEmailPrefix() + "."
                        + group.getEmail()).collect(Collectors.toList())));
        return groupsJson;
    }

    private String getCorrectEmail(ITUserDTO user, FKITGroupDTO group) {
        return this.membershipService.groupIsActiveCommittee(group) ? user.getCid() + "@chalmers.it" : user.getEmail();
    }
}
