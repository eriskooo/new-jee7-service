package sk.lorman.jee7.newservice.infrastructure.util;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * JAXB XML adapter for marshalling the JDK 1.8 type {@code java.time.ZonedDateTime}.
 */
public class LocalTimeAdapter extends XmlAdapter<String, LocalTime> {

  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

  @Override
  public LocalTime unmarshal(final String v) {
    return LocalTime.parse(v, formatter);
  }

  @Override
  public String marshal(final LocalTime v) {
    return v.format(formatter);
  }
}
