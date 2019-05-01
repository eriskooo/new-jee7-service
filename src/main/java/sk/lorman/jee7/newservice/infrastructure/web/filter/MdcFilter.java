/*
 * Copyright (C) open knowledge GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 */
package sk.lorman.jee7.newservice.infrastructure.web.filter;

import static sk.lorman.jee7.newservice.infrastructure.web.CustomHttpHeader.X_REQUEST_ID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

/**
 * Filter that add header values into MDC.
 *
 * By being hold inside MDC the parameters are accessible by the logging framework for log messages and log file separation.
 */
@WebFilter(urlPatterns = "/*")
public class MdcFilter implements Filter {

  private static final Logger LOG = LoggerFactory.getLogger(MdcFilter.class);

  private static final String METHOD_OPTIONS = "OPTIONS";

  private static final String MDC_KEY_REQUEST_ID = "request-id";

  @Override
  public void init(final FilterConfig filterConfig) {
    LOG.trace("initialize ...");
  }

  @Override
  public void doFilter(final ServletRequest req, final ServletResponse response, final FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) req;

    if (this.isHttpMethodOptions(request)) {
      chain.doFilter(request, response);
      return;
    }

    try {
      String requestId = request.getHeader(X_REQUEST_ID);
      if (requestId != null) {
        MDC.put(MDC_KEY_REQUEST_ID, requestId);
      }

      chain.doFilter(request, response);
    } finally {
      MDC.remove(MDC_KEY_REQUEST_ID);
    }
  }

  private boolean isHttpMethodOptions(final HttpServletRequest request) {
    return METHOD_OPTIONS.equals(request.getMethod());
  }

  @Override
  public void destroy() {
    LOG.trace("destroy ...");
  }
}
