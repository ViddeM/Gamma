package it.chalmers.gamma.bootstrap;

import it.chalmers.gamma.app.common.Text;
import it.chalmers.gamma.app.group.domain.EmailPrefix;
import it.chalmers.gamma.app.post.domain.Post;
import it.chalmers.gamma.app.post.domain.PostRepository;
import it.chalmers.gamma.app.post.domain.PostId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PostBootstrap {

    private static final Logger LOGGER = LoggerFactory.getLogger(PostBootstrap.class);

    private final MockData mockData;
    private final PostRepository postRepository;
    private final boolean mocking;

    public PostBootstrap(MockData mockData,
                         PostRepository postRepository,
                         @Value("${application.mocking}") boolean mocking) {
        this.mockData = mockData;
        this.postRepository = postRepository;
        this.mocking = mocking;
    }

    public void createPosts() {
        if (!this.mocking || !this.postRepository.getAll().isEmpty()) {
            return;
        }

        LOGGER.info("========== POST BOOTSTRAP ==========");

        mockData.posts().forEach(mockPost ->
                this.postRepository.save(
                        new Post(
                                new PostId(mockPost.id()),
                                0,
                                new Text(
                                        mockPost.postName().sv(),
                                        mockPost.postName().en()
                                ),
                                EmailPrefix.none()
                        )
                )
        );
        LOGGER.info("Posts created");
        LOGGER.info("==========                ==========");
    }

}
