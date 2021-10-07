package it.chalmers.gamma.app.facade;

import it.chalmers.gamma.app.AccessGuard;
import it.chalmers.gamma.app.domain.common.Text;
import it.chalmers.gamma.app.domain.group.EmailPrefix;
import it.chalmers.gamma.app.domain.post.Post;
import it.chalmers.gamma.app.domain.post.PostId;
import it.chalmers.gamma.app.port.repository.GroupRepository;
import it.chalmers.gamma.app.port.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PostFacade extends Facade {

    private final PostRepository postRepository;
    private final GroupRepository groupRepository;

    public PostFacade(AccessGuard accessGuard,
                      PostRepository postRepository,
                      GroupRepository groupRepository) {
        super(accessGuard);
        this.postRepository = postRepository;
        this.groupRepository = groupRepository;
    }

    public record NewPost(String svText, String enText, String emailPrefix) { }

    public void create(NewPost newPost) {
        this.postRepository.save(
                new Post(
                        PostId.generate(),
                        0,
                        new Text(newPost.svText, newPost.enText),
                        new EmailPrefix(newPost.emailPrefix)
                )
        );
    }

    public record UpdatePost(UUID postId, int version, String svText, String enText, String emailPrefix) { }

    public void update(UpdatePost updatePost) throws PostRepository.PostNotFoundException {
        Post oldPost = this.postRepository.get(new PostId(updatePost.postId))
                .orElseThrow();
        Post newPost = oldPost.with()
                .version(updatePost.version)
                .name(new Text(
                        updatePost.svText,
                        updatePost.enText
                ))
                .emailPrefix(new EmailPrefix(updatePost.emailPrefix))
                .build();

        this.postRepository.save(newPost);
        throw new UnsupportedOperationException();
    }

    public void delete(UUID postId) throws PostRepository.PostNotFoundException {
        this.postRepository.delete(new PostId(postId));
    }

    public record PostDTO(UUID id,
                          int version,
                          String svText,
                          String enText,
                          String emailPrefix) {
        public PostDTO(Post post) {
            this(post.id().value(),
                    post.version(),
                    post.name().sv().value(),
                    post.name().en().value(),
                    post.emailPrefix().value());
        }
    }

    public Optional<PostDTO> get(UUID postId) {
        accessGuard.requireSignedIn();
        return this.postRepository.get(new PostId(postId)).map(PostDTO::new);
    }

    public List<PostDTO> getAll() {
        accessGuard.requireSignedIn();
        return this.postRepository.getAll()
                .stream()
                .map(PostDTO::new)
                .toList();
    }

    public List<GroupFacade.GroupDTO> getPostUsages(UUID postId) {
        return this.groupRepository.getAllByPost(new PostId(postId))
                .stream()
                .map(GroupFacade.GroupDTO::new)
                .toList();
    }

}
