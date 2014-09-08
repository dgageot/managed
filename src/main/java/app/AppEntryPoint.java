package app;

import net.codestory.http.misc.Env;
import net.codestory.http.servlets.OneFilterToRuleThemAll;

import static java.lang.Integer.parseInt;
import static java.util.stream.IntStream.range;

public class AppEntryPoint extends OneFilterToRuleThemAll {
  public AppEntryPoint() {
    super(routes -> {
      routes.get("/hello", "Hello World");
      routes.get("/count/:n", (context, n) -> range(1, parseInt(n)).sum());
    });
  }

  @Override
  protected Env createEnv() {
    return new Env(true, false, true, true);
  }
}
