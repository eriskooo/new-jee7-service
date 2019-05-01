package sk.lorman.jee7.newservice.infrastructure.web;

/**
 * Custom HTTP header.
 */
public final class CustomHttpHeader {

  public static final String X_REQUEST_ID = "X-REQUEST-ID";

  public static final String X_SUPERUSER_AUTHORIZATION = "X-SUPERUSER-AUTHORIZATION";

  private CustomHttpHeader() {
    super();
  }
}
