package sk.lorman.jee7.newservice.infrastructure.rest.jwt;

import sk.lorman.jee7.newservice.infrastructure.qualifier.Authenticated;

import com.auth0.jwt.interfaces.DecodedJWT;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;

/**
 * Produces the current decoded jwt id for the application.
 */
@RequestScoped
public class JwtProducer {

  private static final Logger LOG = LoggerFactory.getLogger(JwtProducer.class);

  private DecodedJWT jwt;

  @Produces
  @RequestScoped
  @Authenticated
  public DecodedJWT getJwt() {
    return jwt;
  }

  public void observeJwt(@Observes @Authenticated final DecodedJWT decodedJwt) {
    LOG.debug("Observed JWT ({}, {})", decodedJwt.getId(), decodedJwt.getToken());
    this.jwt = decodedJwt;
  }
}
