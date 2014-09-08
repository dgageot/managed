package net.codestory.http.servlets;

import net.codestory.http.Cookie;
import net.codestory.http.Cookies;
import net.codestory.http.NewCookie;
import net.codestory.http.convert.TypeConvert;

import java.util.*;

public class ServletCookies implements Cookies {
  private final List<javax.servlet.http.Cookie> cookies;

  public ServletCookies(javax.servlet.http.Cookie[] cookies) {
    this.cookies = (cookies == null) ? Collections.emptyList() : Arrays.asList(cookies);
  }

  @Override
  public Iterator<Cookie> iterator() {
    return cookies.stream().map(this::create).iterator();
  }

  @Override
  public Cookie get(String name) {
    javax.servlet.http.Cookie cookie = cookies.stream().filter(c -> c.getName().equals(name)).findFirst().orElse(null);
    return (cookie == null) ? null : create(cookie);
  }

  @Override
  public String value(String name) {
    Cookie cookie = get(name);
    return (cookie == null) ? null : cookie.value();
  }

  @Override
  public Map<String, String> keyValues() {
    Map<String, String> keyValues = new HashMap<>();
    cookies.forEach(cookie -> keyValues.put(cookie.getName(), cookie.getValue()));
    return keyValues;
  }

  @Override
  public <T> T value(String name, T defaultValue) {
    T value = value(name, (Class<T>) defaultValue.getClass());
    return (value == null) ? defaultValue : value;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T value(String name, Class<T> type) {
    String value = value(name);
    return (value == null) ? null : TypeConvert.fromJson(value, type);
  }

  @Override
  public String value(String name, String defaultValue) {
    String value = value(name);
    return (value == null) ? defaultValue : value;
  }

  @Override
  public int value(String name, int defaultValue) {
    String value = value(name);
    return (value == null) ? defaultValue : Integer.parseInt(value);
  }

  @Override
  public long value(String name, long defaultValue) {
    String value = value(name);
    return (value == null) ? defaultValue : Long.parseLong(value);
  }

  @Override
  public boolean value(String name, boolean defaultValue) {
    String value = value(name);
    return (value == null) ? defaultValue : Boolean.parseBoolean(value);
  }

  private Cookie create(javax.servlet.http.Cookie javaCookie) {
    return new NewCookie(javaCookie.getName(), javaCookie.getValue(), javaCookie.getPath()); // ?
  }
}
