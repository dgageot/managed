package net.codestory.http.servlets;

import net.codestory.http.Configuration;
import net.codestory.http.Context;
import net.codestory.http.Request;
import net.codestory.http.Response;
import net.codestory.http.compilers.CompilerException;
import net.codestory.http.errors.ErrorPage;
import net.codestory.http.errors.HttpException;
import net.codestory.http.misc.Env;
import net.codestory.http.payload.Payload;
import net.codestory.http.payload.PayloadWriter;
import net.codestory.http.reload.RoutesProvider;
import net.codestory.http.routes.RouteCollection;
import net.codestory.http.templating.Site;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.NoSuchElementException;

public class OneFilterToRuleThemAll implements Filter {
  private final static Logger LOG = LoggerFactory.getLogger(OneFilterToRuleThemAll.class);

  private final Env env;
  private final RoutesProvider routesProvider;

  public OneFilterToRuleThemAll(Configuration configuration) {
    this.env = createEnv();
    this.routesProvider = RoutesProvider.fixed(env, configuration);
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    System.out.println("init");
  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    handle(new net.codestory.http.servlets.ServletRequestWrapper((HttpServletRequest) servletRequest), new net.codestory.http.servlets.ServletResponseWrapper((HttpServletResponse) servletResponse));
  }

  protected void handle(Request request, Response response) {
    try {
      RouteCollection routes = routesProvider.get();

      Context context = routes.createContext(request, response);
      PayloadWriter payloadWriter = routes.createPayloadWriter(request, response);

      Payload payload = routes.apply(context);
      if (payload.isError()) {
        payload = errorPage(payload);
      }

      payloadWriter.writeAndClose(payload);
    } catch (Exception e) {
      // Cannot be created by routes since it was not initialized properly
      // TODO: get rid of new Site() here
      //
      PayloadWriter payloadWriter = new PayloadWriter(env, new Site(env), request, response);
      handleServerError(payloadWriter, e);
    }
  }

  protected void handleServerError(PayloadWriter payloadWriter, Exception e) {
    try {
      if (e instanceof CompilerException) {
        LOG.error(e.getMessage());
      } else if (!(e instanceof HttpException) && !(e instanceof NoSuchElementException)) {
        e.printStackTrace();
      }

      Payload errorPage = errorPage(e).withHeader("reason", e.getMessage());
      payloadWriter.writeAndClose(errorPage);
    } catch (IOException error) {
      LOG.warn("Unable to serve an error page", error);
    }
  }

  protected Payload errorPage(Payload payload) {
    return errorPage(payload, null);
  }

  protected Payload errorPage(Exception e) {
    int code = 500;
    if (e instanceof HttpException) {
      code = ((HttpException) e).code();
    } else if (e instanceof NoSuchElementException) {
      code = 404;
    }

    return errorPage(new Payload(code), e);
  }

  protected Payload errorPage(Payload payload, Exception e) {
    Exception shownError = env.prodMode() ? null : e;
    return new ErrorPage(payload, shownError).payload();
  }

  protected Env createEnv() {
    return new Env();
  }

  @Override
  public void destroy() {
  }
}

