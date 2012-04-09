package karaf.framework.launch;

import java.util.Map;
import java.util.Properties;

import org.apache.karaf.main.BootstrapLogManager;
import org.apache.karaf.main.Main;
import org.osgi.framework.launch.Framework;
import org.osgi.framework.launch.FrameworkFactory;

public class KarafFrameworkFactory implements FrameworkFactory {

	@Override
	public Framework newFramework(Map configuration) {
		String karafHomeFolder = (String) configuration.get("karaf.home");
		if (karafHomeFolder == null) {
			return null;
		}
		System.setProperty("karaf.home", karafHomeFolder);
		System.setProperty("karaf.base", karafHomeFolder);
		System.setProperty("karaf.data", karafHomeFolder + "/data");
		System.setProperty("karaf.history", karafHomeFolder + "/data/history.txt");
		System.setProperty("karaf.instances", karafHomeFolder + "/instances");
		System.setProperty("karaf.startLocalConsole", "true");
		System.setProperty("karaf.startRemoteShell", "false");
		System.setProperty("karaf.lock", "false");
		System.setProperty("karaf.framework.factory", "org.apache.felix.framework.FrameworkFactory");

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
