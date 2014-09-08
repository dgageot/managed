package net.codestory.http.servlets;

import net.codestory.http.Query;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class ServletQuery implements Query {
  private final HttpServletRequest request;

  public ServletQuery(HttpServletRequest request) {
    this.request = request;
  }

  @Override
  public String get(String name) {
    String[] parameterValues = request.getParameterValues(name);
    if ((parameterValues == null) || (parameterValues.length == 0)) {
      return null;
    }
    return parameterValues[0];
  }

  @Override
  public Iterable<String> all(String name) {
    return Arrays.asList(request.getParameterValues(name));
  }

  @Override
  public int getInteger(String name) {
    String value = get(name);
    return (value != null) ? Integer.parseInt(value) : 0;
  }

  @Override
  public float getFloat(String name) {
    String value = get(name);
    return (value != null) ? Float.parseFloat(value) : 0.0f;
  }

  @Override
  public boolean getBoolean(String name) {
    String value = get(name);
    return (value != null) ? Boolean.valueOf(value) : false;
  }

  @Override
  public Map<String, String> keyValues() {
    Map<String, String> keyValues = new HashMap<>();
    Enumeration parameterNames = request.getParameterNames();
    while(parameterNames.hasMoreElements()) {
      String parameterName = (String) parameterNames.nextElement();
      keyValues.put(parameterName, get(parameterName));
    }
     return keyValues;
  }

  @Override
  public <T> T unwrap(Class<T> type) {
    return type.isInstance(request) ? (T) request : null;
  }
}
