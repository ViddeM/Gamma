package it.chalmers.gamma.service;

import it.chalmers.gamma.db.entity.Website;
import it.chalmers.gamma.db.repository.WebsiteRepository;

import it.chalmers.gamma.domain.dto.website.WebsiteDTO;
import it.chalmers.gamma.response.website.WebsiteNotFoundResponse;
import it.chalmers.gamma.util.UUIDUtil;
import java.util.List;
import java.util.UUID;

import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class WebsiteService {

    private final WebsiteRepository repository;

    public WebsiteService(WebsiteRepository repository) {
        this.repository = repository;
    }

    /**
     * adds a possible website to the database.
     *
     * @param name       the name of the website
     * @param prettyName the display-name of the website
     */
    public WebsiteDTO addPossibleWebsite(String name, String prettyName) {
        Website website = new Website();
        website.setPrettyName(prettyName == null ? name.toLowerCase() : prettyName);
        website.setName(name);
        return this.repository.save(website).toDTO();
    }

    public WebsiteDTO getWebsite(String websiteName) {
        if (UUIDUtil.validUUID(websiteName)) {
            return this.repository.findById(UUID.fromString(websiteName))
                    .orElseThrow(WebsiteNotFoundResponse::new).toDTO();
        }
        return this.repository.findByName(websiteName.toLowerCase())
                .orElseThrow(WebsiteNotFoundResponse::new).toDTO();
    }

    protected Website getWebsite(WebsiteDTO websiteDTO) {
        return this.repository.findById(websiteDTO.getId()).orElse(null);
    }

    public void editWebsite(WebsiteDTO websiteDTO, String name, String prettyName) {
        Website website = this.getWebsite(websiteDTO);
        website.setName(name.toLowerCase());
        website.setPrettyName(prettyName == null ? name.toLowerCase() : prettyName);
        this.repository.save(website);
    }

    public void deleteWebsite(String id) {
        this.repository.deleteById(UUID.fromString(id));
    }


    public boolean websiteExists(UUID id) {
        return this.repository.existsById(id);
    }

    public List<WebsiteDTO> getAllWebsites() {
        return this.repository.findAll().stream().map(Website::toDTO).collect(Collectors.toList());
    }
}
