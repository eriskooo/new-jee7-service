package sk.lorman.jee7.newservice.infrastructure.util;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * JAXB XML adapter for marshalling the JDK 1.8 type {@code java.time.ZonedDateTime}.
 */
public class ZonedDateTimeAdapter extends XmlAdapter<String, ZonedDateTime> {

  @Override
  public ZonedDateTime unmarshal(final String v) {
    try {
      return ZonedDateTime.parse(v);
    } catch (DateTimeParseException e) {
      return ZonedDateTime.parse(v);
    }
  }

  @Override
  public String marshal(final ZonedDateTime v) {
    return v.format(DateTimeFormatter.ISO_INSTANT);
  }
}
