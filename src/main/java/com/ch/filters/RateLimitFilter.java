package com.ch.filters;

import com.google.common.util.concurrent.RateLimiter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Created by Aaron.Witter on 10/04/2016.
 */
public class RateLimitFilter implements Filter {
  @SuppressWarnings("PMD")
  public void init(FilterConfig filterConfig) throws ServletException {

  }

  /**
   * Limits the rate of concurrent requests to the apps resources.
   * @param servletRequest HttpServletRequest
   * @param servletResponse HttpServletResponse
   * @param chain FilterChain
   * @throws IOException class of exceptions produced by failed or interrupted I/O operations
   * @throws ServletException general exception a servlet can throw when it encounters difficulty
   */
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
      throws IOException, ServletException {

    // limiting the submission of requests to 1 per second
    RateLimiter limiter = RateLimiter.create(1.0);

    //acquires the limiter after the block has expired
    limiter.acquire();

    //causes the resource at the end of the chain to be invoked.
    chain.doFilter(servletRequest, servletResponse);
  }

  @SuppressWarnings("PMD")
  public void destroy() {

  }
}
