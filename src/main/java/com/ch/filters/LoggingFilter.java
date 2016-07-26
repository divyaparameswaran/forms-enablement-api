/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2011-2015 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * http://glassfish.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package com.ch.filters;

import org.glassfish.jersey.message.MessageUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Priority;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;



/**
 * Modified version of org.glassfish.jersey.filter.LoggingFilter.
 * <p/>
 * Can be used on client or server side. Has the highest priority.
 *
 * @author Pavel Bucek (pavel.bucek at oracle.com)
 * @author Martin Matula
 */
@PreMatching
@Priority(Integer.MIN_VALUE)
@SuppressWarnings("ClassWithMultipleLoggers")
public final class LoggingFilter implements ContainerRequestFilter, ClientRequestFilter, ContainerResponseFilter, 
  ClientResponseFilter, WriterInterceptor {

  private static final String NOTIFICATION_PREFIX = "* ";
  private static final String REQUEST_PREFIX = "> ";
  private static final String RESPONSE_PREFIX = "< ";
  private static final String ENTITY_LOGGER_PROPERTY = LoggingFilter.class.getName() + ".entityLogger";
  private static final String LOGGING_ID_PROPERTY = LoggingFilter.class.getName() + ".id";

  private static final Comparator<Map.Entry<String, List<String>>> COMPARATOR = new Comparator<Map.Entry<String, List<String>>>() {

    @Override
    public int compare(final Map.Entry<String, List<String>> o1, final Map.Entry<String, List<String>> o2) {
      return o1.getKey().compareToIgnoreCase(o2.getKey());
    }
  };

  private static final int DEFAULT_MAX_ENTITY_SIZE = 8 * 1024;

  //
  @SuppressWarnings("NonConstantLogger")
  private final Logger logger;
  private final AtomicLong id = new AtomicLong(0);
  private final boolean printEntity;
  private final int maxEntitySize;
  private final List<String> fineLevelRequestPaths;

  /**
   * Create a logging filter with custom logger and custom settings of entity
   * logging.
   *
   * @param logger      the logger to log requests and responses.
   * @param printEntity if true, entity will be logged as well up to the default maxEntitySize, which is 8KB
   */
  @SuppressWarnings("BooleanParameter")
  public LoggingFilter(final Logger logger, final boolean printEntity, List<String> fineLevelRequestPaths) {
    this.logger = logger;
    this.printEntity = printEntity;
    this.maxEntitySize = DEFAULT_MAX_ENTITY_SIZE;
    this.fineLevelRequestPaths = fineLevelRequestPaths;
  }

  private void log(final StringBuilder builder, Level logLevel) {
    if (logger != null) {
      logger.log(logLevel,builder.toString());
    }
  }

  private StringBuilder prefixId(final StringBuilder builder, final long id) {
    builder.append(Long.toString(id)).append(" ");
    return builder;
  }

  private void printRequestLine(final StringBuilder builder, final String note, final long id, 
    final String method, final URI uri) {
    prefixId(builder, id).append(NOTIFICATION_PREFIX).append(note).append(" on thread ")
    .append(Thread.currentThread().getName()).append("\n");
    prefixId(builder, id).append(REQUEST_PREFIX).append(method)
    .append(" ").append(uri.toASCIIString()).append("\n");
  }

  private void printResponseLine(final StringBuilder builder, final String note, final long id,
    final int status) {
    prefixId(builder, id).append(NOTIFICATION_PREFIX).append(note).append(" on thread ")
    .append(Thread.currentThread().getName()).append("\n");
    prefixId(builder, id).append(RESPONSE_PREFIX).append(Integer.toString(status)).append("\n");
  }

  private void printPrefixedHeaders(final StringBuilder builder, final long id, final String prefix,
    final MultivaluedMap<String, String> headers) {
    for (final Map.Entry<String, List<String>> headerEntry : getSortedHeaders(headers.entrySet())) {
      final List<?> val = headerEntry.getValue();
      final String header = headerEntry.getKey();

      if (val.size() == 1) {
        prefixId(builder, id).append(prefix).append(header).append(": ").append(val.get(0)).append("\n");
      } else {
        final StringBuilder sb = new StringBuilder();
        boolean add = false;
        for (final Object s : val) {
          if (add) {
            sb.append(',');
          }
          add = true;
          sb.append(s);
        }
        prefixId(builder, id).append(prefix).append(header).append(": ").append(sb.toString()).append("\n");
      }
    }
  }

  private Set<Map.Entry<String, List<String>>> getSortedHeaders(final Set<Map.Entry<String, List<String>>> headers) {
    final TreeSet<Map.Entry<String, List<String>>> sortedHeaders = new TreeSet<Map.Entry<String, List<String>>>(COMPARATOR);
    sortedHeaders.addAll(headers);
    return sortedHeaders;
  }

  private InputStream logInboundEntity(final StringBuilder builder, InputStream stream, final Charset charset) throws IOException {
    InputStream markableStream;
    if (stream.markSupported()) {
      markableStream = stream;
    } else {
      markableStream = new BufferedInputStream(stream);
    }
    markableStream.mark(maxEntitySize + 1);
    final byte[] entity = new byte[maxEntitySize + 1];
    final int entitySize = stream.read(entity);
    builder.append(new String(entity, 0, Math.min(entitySize, maxEntitySize), charset));
    if (entitySize > maxEntitySize) {
      builder.append("...more...");
    }
    builder.append('\n');
    markableStream.reset();
    return markableStream;
  }

  @Override
  public void filter(final ClientRequestContext context) throws IOException {
    final long id = this.id.incrementAndGet();
    context.setProperty(LOGGING_ID_PROPERTY, id);

    final StringBuilder builder = new StringBuilder();

    printRequestLine(builder, "Sending client request", id, context.getMethod(), context.getUri());
    printPrefixedHeaders(builder, id, REQUEST_PREFIX, context.getStringHeaders());

    if (printEntity && context.hasEntity()) {
      final OutputStream stream = new LoggingStream(builder, context.getEntityStream());
      context.setEntityStream(stream);
      context.setProperty(ENTITY_LOGGER_PROPERTY, stream);
      // not calling log(builder) here - it will be called by the interceptor
    } else {
      log(builder, Level.INFO);
    }
  }

  @Override
  public void filter(final ClientRequestContext requestContext, final ClientResponseContext responseContext) throws IOException {
    final Object requestId = requestContext.getProperty(LOGGING_ID_PROPERTY);
    final long id = requestId == null ? this.id.incrementAndGet() : (Long) requestId;

    final StringBuilder builder = new StringBuilder();

    printResponseLine(builder, "Client response received", id, responseContext.getStatus());
    printPrefixedHeaders(builder, id, RESPONSE_PREFIX, responseContext.getHeaders());

    if (printEntity && responseContext.hasEntity()) {
      responseContext.setEntityStream(logInboundEntity(builder, responseContext.getEntityStream(), 
        MessageUtils.getCharset(responseContext.getMediaType())));
    }

    log(builder, Level.INFO);
  }

  @Override
  public void filter(final ContainerRequestContext context) throws IOException {
    
    final long id = this.id.incrementAndGet();
    context.setProperty(LOGGING_ID_PROPERTY, id);

    final StringBuilder builder = new StringBuilder();

    printRequestLine(builder, "Server has received a request", id, context.getMethod(), context.getUriInfo().getRequestUri());
    printPrefixedHeaders(builder, id, REQUEST_PREFIX, context.getHeaders());

    if (printEntity && context.hasEntity()) {
      context.setEntityStream(logInboundEntity(builder, context.getEntityStream(), 
        MessageUtils.getCharset(context.getMediaType())));
    }

    log(builder, determineLogLevelForPath(context.getUriInfo().getPath()));
  }

  @Override
  public void filter(final ContainerRequestContext requestContext, final ContainerResponseContext responseContext) 
          throws IOException {
    final Object requestId = requestContext.getProperty(LOGGING_ID_PROPERTY);
    final long id = requestId == null ? this.id.incrementAndGet() : (Long) requestId;

    final StringBuilder builder = new StringBuilder();

    printResponseLine(builder, "Server responded with a response", id, responseContext.getStatus());
    printPrefixedHeaders(builder, id, RESPONSE_PREFIX, responseContext.getStringHeaders());

    if (printEntity && responseContext.hasEntity()) {
      final OutputStream stream = new LoggingStream(builder, responseContext.getEntityStream());
      responseContext.setEntityStream(stream);
      requestContext.setProperty(ENTITY_LOGGER_PROPERTY, stream);
      // not calling log(builder) here - it will be called by the interceptor
    } else {
      log(builder, determineLogLevelForPath(requestContext.getUriInfo().getPath()));
    }
  }

  private Level determineLogLevelForPath(String path) {

    Level logLevel;
    if (this.fineLevelRequestPaths.contains(path)) {
      logLevel = Level.FINE;
    } else {
      logLevel = Level.INFO;
    }
    
    return logLevel;
  }
  
  @Override
  public void aroundWriteTo(final WriterInterceptorContext writerInterceptorContext) throws IOException, WebApplicationException {
    final LoggingStream stream = (LoggingStream) writerInterceptorContext.getProperty(ENTITY_LOGGER_PROPERTY);
    writerInterceptorContext.proceed();
    if (stream != null) {
      log(stream.getStringBuilder(MessageUtils.getCharset(writerInterceptorContext.getMediaType())), Level.INFO);
    }
  }

  private class LoggingStream extends FilterOutputStream {

    private final StringBuilder builder;
    private final ByteArrayOutputStream baos = new ByteArrayOutputStream();

    LoggingStream(final StringBuilder builder, final OutputStream inner) {
      super(inner);

      this.builder = builder;
    }

    StringBuilder getStringBuilder(final Charset charset) {
      // write entity to the builder
      final byte[] entity = baos.toByteArray();

      builder.append(new String(entity, 0, Math.min(entity.length, maxEntitySize), charset));
      if (entity.length > maxEntitySize) {
        builder.append("...more...");
      }
      builder.append('\n');

      return builder;
    }

    @Override
    public void write(final int in) throws IOException {
      if (baos.size() <= maxEntitySize) {
        baos.write(in);
      }
      out.write(in);
    }
  }
}
