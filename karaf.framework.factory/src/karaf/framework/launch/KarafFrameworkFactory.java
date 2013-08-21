package karaf.framework.launch;

import java.util.Map;

import org.apache.karaf.main.Main;
import org.osgi.framework.launch.Framework;
import org.osgi.framework.launch.FrameworkFactory;

public class KarafFrameworkFactory implements FrameworkFactory {
  
	@Override
	public Framework newFramework(Map<String, String>  configuration) {
		System.out.println("init factory");
		System.getProperties().putAll(configuration);

		if(!configuration.containsKey("karaf.framework.factory"))
		  System.setProperty("karaf.framework.factory", "org.apache.felix.framework.FrameworkFactory");

		Main main = new Main(new String[0]);
		try {
		  // launch karaf
			main.launch();
			
			Framework framework = main.getFramework();
			System.out.println("return " + framework);
      return framework;
		} catch (Throwable exp) {
		  try {
        main.destroy();
      } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException(e);
      }
			exp.printStackTrace();
		  throw new RuntimeException(exp);
		}
	}

}
