package sk.lorman.jee7.newservice.infrastructure.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

/**
 * Test class for the producer {@link AuthenticatedUserProducer}.
 */
public class AuthenticatedUserProducerTest {

  @Test
  public void getUser() {
    AuthenticatedUser authenticatedUser = new AuthenticatedUser("johndoe1", "John", "Doe");

    AuthenticatedUserProducer producer = new AuthenticatedUserProducer();
    producer.observesAuthenticationEvent(authenticatedUser);
    assertThat(producer.getUser()).isEqualTo(authenticatedUser);
  }
}
