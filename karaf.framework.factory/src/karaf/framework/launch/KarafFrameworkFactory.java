package karaf.framework.launch;

import java.util.Map;

import org.apache.karaf.main.Main;
import org.osgi.framework.launch.Framework;
import org.osgi.framework.launch.FrameworkFactory;

public class KarafFrameworkFactory implements FrameworkFactory {

	@Override
	public Framework newFramework(Map configuration) {
		// the karaf installation folder must be setup
		String karafHomeFolder = (String) configuration.get("karaf.home");
		if (karafHomeFolder == null) {
			return null;
		}
		
		// setup some system properties to start karaf
		System.setProperty("karaf.home", karafHomeFolder);
		System.setProperty("karaf.base", karafHomeFolder);
		System.setProperty("karaf.data", karafHomeFolder + "/data");
		System.setProperty("karaf.history", karafHomeFolder + "/data/history.txt");
		System.setProperty("karaf.instances", karafHomeFolder + "/instances");
		System.setProperty("karaf.startLocalConsole", "true");
		System.setProperty("karaf.startRemoteShell", "false");
		System.setProperty("karaf.lock", "false");
		
		// set the framework factory to karaf to felix.
		// TODO: make this configurable to change the karaf OSGi framework to equinox
		System.setProperty("karaf.framework.factory", "org.apache.felix.framework.FrameworkFactory");

		// launch karaf
		Main main = new Main(new String[0]);
		try {
			main.launch();
			return main.getFramework();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
