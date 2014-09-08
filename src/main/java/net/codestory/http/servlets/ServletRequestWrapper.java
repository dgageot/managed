package net.codestory.http.servlets;

import net.codestory.http.Cookies;
import net.codestory.http.Part;
import net.codestory.http.Query;
import net.codestory.http.Request;
import net.codestory.http.io.InputStreams;
import org.apache.commons.io.Charsets;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.List;

public class ServletRequestWrapper implements Request {
  private final HttpServletRequest request;

  public ServletRequestWrapper(HttpServletRequest request) {
    this.request = request;
  }

  @Override
  public String uri() {
    return request.getRequestURI();
  }

  @Override
  public String method() {
    return request.getMethod();
  }

  @Override
  public String header(String name) {
    return request.getHeader(name);
  }

  @Override
  public String content() throws IOException {
    return InputStreams.readString(request.getInputStream(), Charsets.toCharset(request.getCharacterEncoding()));
  }

  @Override
  public String contentType() {
    return request.getContentType();
  }

  @Override
  public InputStream inputStream() throws IOException {
    return request.getInputStream();
  }

  @Override
  public List<String> headers(String name) {
    return Collections.list(request.getHeaders(name));
  }

  @Override
  public List<String> headerNames() {
    return Collections.list(request.getHeaderNames());
  }

  @Override
  public InetSocketAddress clientAddress() {
    return InetSocketAddress.createUnresolved(request.getRemoteAddr(), request.getRemotePort()); // ?
  }

  @Override
  public boolean isSecure() {
    return request.isSecure();
  }

  @Override
  public Cookies cookies() {
    return new ServletCookies(request.getCookies());
  }

  @Override
  public Query query() {
    return new ServletQuery(request);
  }

  @Override
  public List<Part> parts() {
    return null; // TODO
  }

  @Override
  public <T> T unwrap(Class<T> type) {
    return type.isInstance(request) ? (T) request : null;
  }
}
