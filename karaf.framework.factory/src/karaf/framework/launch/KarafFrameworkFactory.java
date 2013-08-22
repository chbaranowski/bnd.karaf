package karaf.framework.launch;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Map;

import org.osgi.framework.launch.Framework;
import org.osgi.framework.launch.FrameworkFactory;

public class KarafFrameworkFactory implements FrameworkFactory {

  @Override public Framework newFramework(Map<String, String> configuration) {
    System.out.println("init factory");
    System.getProperties().putAll(configuration);
    String karafHome = configuration.get("karaf.home");
    if (karafHome == null || karafHome.trim().isEmpty()) {
      System.out.println("ERROR \"karaf.home\" not set");
      return null;
    }
    
    String argsString = configuration.get("args");
    String[] args = new String[0]; 
    if(argsString!=null)
      args = argsString.split(" ");
    System.out.println("using args = " + Arrays.toString(args));
    
    Object main;
    try {
      URL[] urls = {(new File(karafHome,"lib/karaf.jar").toURI().toURL())};
      System.out.println("using jar = " + urls[0]);
      
      
      try (URLClassLoader cl = new URLClassLoader(urls,new ClassLoader() {
        //exclude this factory;
        @Override public URL getResource(String name) {
          if(("META-INF/services/" + FrameworkFactory.class.getName()).equals(name))
            return null;
          return this.getClass().getClassLoader().getResource(name);
        }
      })) {
        Class<?> mainClass = cl.loadClass("org.apache.karaf.main.Main");

        main = mainClass.getConstructor(String[].class).newInstance((Object) args);
        // main.launch();
        mainClass.getMethod("launch").invoke(main);

        // Framework framework = main.getFramework();
        Framework framework = (Framework) mainClass.getMethod("getFramework").invoke(main);
        System.out.println("return " + framework);
        return framework;
      }
    } catch (Throwable exp) {
      exp.printStackTrace();
      throw new RuntimeException(exp);
    }
  }
  

}
