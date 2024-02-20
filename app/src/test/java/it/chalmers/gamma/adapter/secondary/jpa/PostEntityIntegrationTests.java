package it.chalmers.gamma.adapter.secondary.jpa;

import static it.chalmers.gamma.utils.DomainUtils.chair;
import static it.chalmers.gamma.utils.DomainUtils.treasurer;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import it.chalmers.gamma.adapter.secondary.jpa.group.PostEntityConverter;
import it.chalmers.gamma.adapter.secondary.jpa.group.PostRepositoryAdapter;
import it.chalmers.gamma.adapter.secondary.jpa.util.MutableEntity;
import it.chalmers.gamma.app.post.domain.PostId;
import it.chalmers.gamma.app.post.domain.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@Import({PostRepositoryAdapter.class, PostEntityConverter.class})
public class PostEntityIntegrationTests extends AbstractEntityIntegrationTests {

  @Autowired private PostRepositoryAdapter postRepositoryAdapter;

  @BeforeEach
  public void clearSecurityContext() {
    SecurityContextHolder.clearContext();
  }

  @Test
  public void Given_ValidPost_Expect_save_To_Work() {
    this.postRepositoryAdapter.save(chair);

    assertThat(this.postRepositoryAdapter.get(chair.id())).get().isEqualTo(chair.withVersion(1));
  }

  @Test
  public void Given_SamePost_Expect_save_To_Throw() {
    this.postRepositoryAdapter.save(chair);

    assertThatExceptionOfType(MutableEntity.StaleDomainObjectException.class)
        .isThrownBy(() -> this.postRepositoryAdapter.save(chair));
  }

  @Test
  public void Given_Post_Expect_delete_To_Work() throws PostRepository.PostNotFoundException {
    this.postRepositoryAdapter.save(chair);
    this.postRepositoryAdapter.delete(chair.id());
    assertThat(this.postRepositoryAdapter.get(chair.id())).isEmpty();
  }

  @Test
  public void Given_NoPost_Expect_delete_To_Throw() {
    assertThatExceptionOfType(PostRepository.PostNotFoundException.class)
        .isThrownBy(() -> this.postRepositoryAdapter.delete(PostId.generate()));
  }

  @Test
  public void Given_TwoPosts_Expect_getAll_To_Work() {
    this.postRepositoryAdapter.save(chair);
    this.postRepositoryAdapter.save(treasurer);

    assertThat(this.postRepositoryAdapter.getAll())
        .containsExactlyInAnyOrder(chair.withVersion(1), treasurer.withVersion(1));
  }
}
