package it.chalmers.gamma.adapter.primary.thymeleaf;

import it.chalmers.gamma.app.client.ClientFacade;
import it.chalmers.gamma.app.client.domain.ClientAuthorityFacade;
import it.chalmers.gamma.app.client.domain.authority.ClientAuthorityRepository;
import it.chalmers.gamma.app.supergroup.SuperGroupFacade;
import it.chalmers.gamma.app.user.UserFacade;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
public class ClientsController {

    private final ClientFacade clientFacade;
    private final ClientAuthorityFacade clientAuthorityFacade;
    private final SuperGroupFacade superGroupFacade;
    private final UserFacade userFacade;

    public ClientsController(ClientFacade clientFacade,
                             ClientAuthorityFacade clientAuthorityFacade,
                             SuperGroupFacade superGroupFacade,
                             UserFacade userFacade) {
        this.clientFacade = clientFacade;
        this.clientAuthorityFacade = clientAuthorityFacade;
        this.superGroupFacade = superGroupFacade;
        this.userFacade = userFacade;
    }

    @GetMapping("/clients")
    public ModelAndView getClients(@RequestHeader(value = "HX-Request", required = false) boolean htmxRequest) {
        List<ClientFacade.ClientDTO> clients = this.clientFacade.getAll();

        ModelAndView mv = new ModelAndView();
        if(htmxRequest) {
            mv.setViewName("pages/clients");
        } else {
            mv.setViewName("index");
            mv.addObject("page", "pages/clients");
        }
        mv.addObject("clients", clients);

        return mv;
    }

    @GetMapping("/clients/{id}")
    public ModelAndView getClients(@RequestHeader(value = "HX-Request", required = false) boolean htmxRequest, @PathVariable("id") UUID clientUid) {
        Optional<ClientFacade.ClientDTO> client = this.clientFacade.get(clientUid);

        if(client.isEmpty()) {
            throw new RuntimeException();
        }

        List<ClientAuthorityFacade.ClientAuthorityDTO> clientAuthorities = this.clientAuthorityFacade.getAll(clientUid);

        ModelAndView mv = new ModelAndView();
        if(htmxRequest) {
            mv.setViewName("pages/client-details");
        } else {
            mv.setViewName("index");
            mv.addObject("page", "pages/client-details");
        }
        mv.addObject("client", client.get());
        mv.addObject("clientAuthorities", clientAuthorities);

        return mv;
    }

    public static final class CreateClient {
        private String redirectUrl;
        private String prettyName;
        private String svDescription;
        private String enDescription;
        private boolean generateApiKey;
        private boolean emailScope;

        private List<UUID> restrictions;

        public CreateClient() {
            this("",
                    "",
                    "",
                    "",
                    false,
                    false,
                    new ArrayList<>());
        }

        public CreateClient(String redirectUrl,
                            String prettyName,
                            String svDescription,
                            String enDescription,
                            boolean generateApiKey,
                            boolean emailScope,
                            List<UUID> restrictions) {
            this.redirectUrl = redirectUrl;
            this.prettyName = prettyName;
            this.svDescription = svDescription;
            this.enDescription = enDescription;
            this.generateApiKey = generateApiKey;
            this.emailScope = emailScope;
            this.restrictions = restrictions;
        }

        public String getRedirectUrl() {
            return redirectUrl;
        }

        public void setRedirectUrl(String redirectUrl) {
            this.redirectUrl = redirectUrl;
        }

        public String getPrettyName() {
            return prettyName;
        }

        public void setPrettyName(String prettyName) {
            this.prettyName = prettyName;
        }

        public String getSvDescription() {
            return svDescription;
        }

        public void setSvDescription(String svDescription) {
            this.svDescription = svDescription;
        }

        public String getEnDescription() {
            return enDescription;
        }

        public void setEnDescription(String enDescription) {
            this.enDescription = enDescription;
        }

        public boolean isGenerateApiKey() {
            return generateApiKey;
        }

        public void setGenerateApiKey(boolean generateApiKey) {
            this.generateApiKey = generateApiKey;
        }

        public boolean isEmailScope() {
            return emailScope;
        }

        public void setEmailScope(boolean emailScope) {
            this.emailScope = emailScope;
        }

        public List<UUID> getRestrictions() {
            return restrictions;
        }

        public void setRestrictions(List<UUID> restrictions) {
            this.restrictions = restrictions;
        }
    }

    @GetMapping("/clients/create")
    public ModelAndView createClient(@RequestHeader(value = "HX-Request", required = false) boolean htmxRequest) {
        ModelAndView mv = new ModelAndView();

        if (htmxRequest) {
            mv.setViewName("pages/create-client");
        } else {
            mv.setViewName("index");
            mv.addObject("page", "pages/create-client");
        }

        mv.addObject("form", new CreateClient());

        return mv;
    }

    @GetMapping("/clients/create/new-restriction")
    public ModelAndView newRestrictionRow(@RequestHeader(value = "HX-Request", required = true) boolean htmxRequest) {
        ModelAndView mv = new ModelAndView();

        mv.setViewName("partial/add-restriction-to-client");

        mv.addObject("superGroups", this.superGroupFacade.getAll()
                .stream()
                .collect(
                        Collectors.toMap(
                                SuperGroupFacade.SuperGroupDTO::id,
                                SuperGroupFacade.SuperGroupDTO::prettyName
                        )
                )
        );

        return mv;
    }

    @PostMapping("/clients")
    public ModelAndView createClient(@RequestHeader(value = "HX-Request", required = true) boolean htmxRequest,
                                     CreateClient form,
                                     BindingResult bindingResult) {
        ModelAndView mv = new ModelAndView();

        ClientFacade.ClientAndApiKeySecrets secrets = this.clientFacade.create(new ClientFacade.NewClient(
           form.redirectUrl,
           form.prettyName,
           form.svDescription,
           form.enDescription,
           form.generateApiKey,
           form.emailScope,
           new ClientFacade.NewClientRestrictions(
                   form.restrictions
           )
        ));

        mv.setViewName("pages/client-credentials");
        mv.addObject("clientUid", secrets.clientUid());
        mv.addObject("clientSecret", secrets.clientSecret());
        mv.addObject("apiKeyToken", secrets.apiKeyToken());

        return mv;
    }

    @GetMapping("/clients/{id}/authorities")
    public ModelAndView getEditAuthorities(@RequestHeader(value = "HX-Request", required = true) boolean htmxRequest, @PathVariable("id") UUID clientUid) {
        Optional<ClientFacade.ClientDTO> client = this.clientFacade.get(clientUid);

        if(client.isEmpty()) {
            throw new RuntimeException();
        }

        List<ClientAuthorityFacade.ClientAuthorityDTO> clientAuthorities = clientAuthorityFacade.getAll(client.get().clientUid());

        ModelAndView mv = new ModelAndView();

        mv.setViewName("pages/client-details :: client-authorities");
        mv.addObject("client", client.get());
        mv.addObject("clientAuthorities", clientAuthorities);

        return mv;
    }

    @GetMapping("/clients/{id}/new-authority")
    public ModelAndView newAuthority(@RequestHeader(value = "HX-Request", required = true) boolean htmxRequest,
                                     @PathVariable("id") UUID clientUid) {
        Optional<ClientFacade.ClientDTO> client = this.clientFacade.get(clientUid);

        if(client.isEmpty()) {
            throw new RuntimeException();
        }

        ModelAndView mv = new ModelAndView();

        mv.setViewName("partial/add-authority-to-client");
        mv.addObject("client", client.get());

        return mv;
    }

    @GetMapping("/clients/authority/new-super-group")
    public ModelAndView newSuperGroupAuthority(@RequestHeader(value = "HX-Request", required = true) boolean htmxRequest) {
        ModelAndView mv = new ModelAndView();

        mv.setViewName("partial/add-super-group-authority-to-client");

        mv.addObject("superGroups", this.superGroupFacade.getAll()
                .stream()
                .collect(
                        Collectors.toMap(
                                SuperGroupFacade.SuperGroupDTO::id,
                                SuperGroupFacade.SuperGroupDTO::prettyName
                        )
                )
        );

        return mv;
    }

    @GetMapping("/clients/authority/new-user")
    public ModelAndView newUserToAuthority(@RequestHeader(value = "HX-Request", required = true) boolean htmxRequest) {
        ModelAndView mv = new ModelAndView();

        mv.setViewName("partial/add-user-authority-to-client");

        mv.addObject("users", this.userFacade.getAll()
                .stream()
                .collect(
                        Collectors.toMap(
                                UserFacade.UserDTO::id,
                                UserFacade.UserDTO::nick
                        )
                )
        );

        return mv;
    }

    public static final class CreateAuthority {
        private List<UUID> superGroups;
        private List<UUID> users;
        private String authority;

        public List<UUID> getSuperGroups() {
            return superGroups;
        }

        public void setSuperGroups(List<UUID> superGroups) {
            this.superGroups = superGroups;
        }

        public List<UUID> getUsers() {
            return users;
        }

        public void setUsers(List<UUID> users) {
            this.users = users;
        }

        public String getAuthority() {
            return authority;
        }

        public void setAuthority(String authority) {
            this.authority = authority;
        }
    }

    @PostMapping("/clients/{id}/authority")
    public ModelAndView createAuthority(@RequestHeader(value = "HX-Request", required = true) boolean htmxRequest,
                                        @PathVariable("id") UUID clientUid,
                                        CreateAuthority form,
                                        HttpServletResponse response) {

        // TODO: Move this to one call in the facade.
        try {
            this.clientAuthorityFacade.create(clientUid, form.authority);
        } catch (ClientAuthorityRepository.ClientAuthorityAlreadyExistsException e) {
            throw new RuntimeException(e);
        }

        form.superGroups.forEach(superGroup -> {
            try {
                this.clientAuthorityFacade.addSuperGroupToClientAuthority(clientUid, form.authority, superGroup);
            } catch (ClientAuthorityFacade.ClientAuthorityNotFoundException |
                     ClientAuthorityFacade.SuperGroupNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        form.users.forEach(user -> {
            try {
                this.clientAuthorityFacade.addUserToClientAuthority(clientUid, form.authority, user);
            } catch (ClientAuthorityFacade.ClientAuthorityNotFoundException |
                     ClientAuthorityFacade.UserNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

       response.addHeader("HX-Trigger", "authorities-updated");

        return new ModelAndView("common/empty");
    }

}
