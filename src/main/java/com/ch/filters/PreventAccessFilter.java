package com.ch.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;


@SuppressWarnings("PMD")
public class PreventAccessFilter implements Filter {

  public PreventAccessFilter() {
  }

  public void init(FilterConfig filterConfig) throws ServletException {
  }

  /**
   * Prevents access to all requests.
   *
   * @param servletRequest  HttpServletRequest
   * @param servletResponse HttpServletResponse
   * @param chain           FilterChain
   * @throws IOException      class of exceptions produced by failed or interrupted I/O operations
   * @throws ServletException general exception a servlet can throw when it encounters difficulty
   */
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
    throws IOException, ServletException {
    //Does nothing other than set status to 401 and does not pass on the request along the chain
    
    HttpServletResponse response = (HttpServletResponse)servletResponse;
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
  }

  public void destroy() {

  }
}
