package sk.lorman.jee7.newservice.infrastructure.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalTime;

/**
 * Test class for the adapter {@link LocalTimeAdapter}.
 */
@RunWith(MockitoJUnitRunner.class)
public class LocalTimeAdapterTest {

  private LocalTimeAdapter adapter;

  @Before
  public void setUp() throws Exception {
    adapter = new LocalTimeAdapter();
  }

  @Test
  public void marshall() throws Exception {
    assertThat(adapter.marshal(LocalTime.of(12, 34, 56))).isEqualTo("12:34:56");
  }

  @Test
  public void unmarshall() throws Exception {
    assertThat(adapter.unmarshal("12:34:56")).isEqualTo(LocalTime.of(12,34, 56));
  }
}
