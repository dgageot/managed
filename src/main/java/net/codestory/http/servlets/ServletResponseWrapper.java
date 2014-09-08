package net.codestory.http.servlets;

import net.codestory.http.Cookie;
import net.codestory.http.Response;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class ServletResponseWrapper implements Response {
  private final HttpServletResponse response;

  public ServletResponseWrapper(HttpServletResponse response) {
    this.response = response;
  }

  @Override
  public void close() throws IOException {
  }

  @Override
  public void commit() throws IOException {
    response.flushBuffer(); // ?
  }

  @Override
  public OutputStream outputStream() throws IOException {
    return response.getOutputStream();
  }

  @Override
  public PrintStream printStream() throws IOException {
    return new PrintStream(response.getOutputStream()); // Memoize ??
  }

  @Override
  public void setContentLength(long length) {
    response.setContentLength((int) length);
  }

  @Override
  public void setValue(String name, String value) {
    response.addHeader(name, value);
  }

  @Override
  public void setStatus(int statusCode) {
    response.setStatus(statusCode);
  }

  @Override
  public void setCookie(Cookie cookie) {
    javax.servlet.http.Cookie javaCookie = new javax.servlet.http.Cookie(cookie.name(), cookie.value());
    javaCookie.setDomain(cookie.domain());
    javaCookie.setMaxAge(cookie.expiry());
    javaCookie.setPath(cookie.path());
    javaCookie.setSecure(cookie.isSecure());

    response.addCookie(javaCookie);
  }

  @Override
  public <T> T unwrap(Class<T> type) {
    return type.isInstance(response) ? (T) response : null;
  }
}
