package karaf.framework.launch;

import java.util.Map;

import org.apache.karaf.main.Main;
import org.osgi.framework.launch.Framework;
import org.osgi.framework.launch.FrameworkFactory;

public class KarafFrameworkFactory implements FrameworkFactory {
  static{
    System.out.println("load Karaf factory");
  }
  
  {
    System.out.println("instantiate Karaf factory");
  }
  
	@Override
	public Framework newFramework(Map<String, String>  configuration) {
		// the karaf installation folder must be setup
		// TODO: find a better way to define the karaf.home folder?
		System.out.println("init factory");
	  String karafHomeFolder = (String) configuration.get("karaf.home");
		if (karafHomeFolder == null) {
			return null;
		}
		
		String karafBaseFolder = (String) configuration.get("karaf.base");
		if (karafBaseFolder == null) {
		  karafBaseFolder = karafHomeFolder;
		}
		String karafDataFolder = (String) configuration.get("karaf.data");
		if (karafDataFolder == null) {
		  karafDataFolder = karafBaseFolder+"/data";
		}
		
		//exec "$JAVA" $JAVA_OPTS -Djava.endorsed.dirs="${JAVA_ENDORSED_DIRS}" -Djava.ext.dirs="${JAVA_EXT_DIRS}" 
		
		// setup some system properties to start karaf
		//-Dkaraf.instances="${KARAF_HOME}/instances" 
		System.setProperty("karaf.instances", karafHomeFolder + "/instances");
		//-Dkaraf.home="$KARAF_HOME" 
		System.setProperty("karaf.home", karafHomeFolder);
		//-Dkaraf.base="$KARAF_BASE" 
		System.setProperty("karaf.base", karafBaseFolder);
		//-Dkaraf.data="$KARAF_DATA" 
		System.setProperty("karaf.data", karafDataFolder);
		//-Djava.io.tmpdir="$KARAF_DATA/tmp" 
		System.setProperty("java.io.tmpdir", karafDataFolder + "/tmp");
		//-Djava.util.logging.config.file="$KARAF_BASE/etc/java.util.logging.properties" 
		System.setProperty("java.util.logging.config.file", karafBaseFolder + "/etc/java.util.logging.properties");
//		System.setProperty("karaf.history", karafHomeFolder + "/data/history.txt");
//		System.setProperty("karaf.startLocalConsole", "true");
//		System.setProperty("karaf.startRemoteShell", "false");
//		System.setProperty("karaf.lock", "false");
		//$KARAF_OPTS //$OPTS 
		
		// set the framework factory to karaf to felix.
		// TODO: make this configurable to change the karaf OSGi framework to equinox
		System.setProperty("karaf.framework.factory", "org.apache.felix.framework.FrameworkFactory");

		try {
		  // launch karaf
		  Main main = new Main(new String[0]);
			main.launch();
			
			Framework framework = main.getFramework();
			System.out.println("return " + framework);
      return framework;
		} catch (Exception exp) {
			exp.printStackTrace();
		  throw new RuntimeException(exp);
		}
	}

}
