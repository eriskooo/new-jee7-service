package sk.lorman.jee7.newservice.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * Test class for the producer {@link EntityManagerProducer}.
 */
@RunWith(MockitoJUnitRunner.class)
public class EntityManagerProducerTest {

  @InjectMocks
  private EntityManagerProducer producer;

  @Mock
  private EntityManager entityManager;

  @Mock
  private EntityManagerFactory entityManagerFactory;

  @Test
  public void close() {
    producer.close(entityManager);
    Mockito.verify(entityManager).close();
  }

  @Test
  public void createEntityManager() {
    Mockito.when(producer.createEntityManager()).thenReturn(entityManager);
    assertThat(producer.createEntityManager()).isEqualTo(entityManager);
  }
}
