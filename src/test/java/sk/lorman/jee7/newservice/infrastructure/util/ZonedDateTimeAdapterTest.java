package sk.lorman.jee7.newservice.infrastructure.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Test class for the JAXB adapter {@link ZonedDateTimeAdapter}.
 */
public class ZonedDateTimeAdapterTest {

  private static final ZonedDateTime VALUE = ZonedDateTime.of(2017, 1, 31, 12, 34, 56, 0, ZoneId.of("UTC"));

  private ZonedDateTimeAdapter adapter;

  @Before
  public void setUp() throws Exception {
    adapter = new ZonedDateTimeAdapter();
  }

  @Test
  public void marshall() throws Exception {
    assertThat(adapter.marshal(VALUE)).isEqualTo("2017-01-31T12:34:56Z");
  }

  @Test
  public void unMarshall() throws Exception {
    assertThat(adapter.unmarshal("2017-01-31T12:34:56Z")).isEqualTo(VALUE);
  }
}
