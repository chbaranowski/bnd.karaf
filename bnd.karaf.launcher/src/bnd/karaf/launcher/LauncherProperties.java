package bnd.karaf.launcher;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

public class LauncherProperties {

	final static String LAUNCH_SERVICES = "launch.services";
	final static String LAUNCH_STORAGE_DIR = "launch.storage.dir";
	final static String LAUNCH_KEEP = "launch.keep";
	final static String LAUNCH_RUNBUNDLES = "launch.bundles";
	final static String LAUNCH_SYSTEMPACKAGES = "launch.system.packages";
	final static String LAUNCH_TRACE = "launch.trace";
	final static String LAUNCH_TIMEOUT = "launch.timeout";
	final static String LAUNCH_ACTIVATORS = "launch.activators";

	public boolean services;
	public File storageDir = new File("");
	public boolean keep;
	public final List<String> runbundles = new ArrayList<String>();
	public String systemPackages;
	public boolean trace;
	public long timeout;
	public final List<String> activators = new ArrayList<String>();
	public Map<String, String> runProperties = new HashMap<String, String>();
	
	public LauncherProperties() {}
	
	public LauncherProperties(Properties settings) {
		services = Boolean.valueOf(settings.getProperty(LAUNCH_SERVICES));
		storageDir = new File(settings.getProperty(LAUNCH_STORAGE_DIR));
		keep = Boolean.valueOf(settings.getProperty(LAUNCH_KEEP));
		runbundles.addAll(split(settings.getProperty(LAUNCH_RUNBUNDLES), ","));
		systemPackages = settings.getProperty(LAUNCH_SYSTEMPACKAGES);
		trace = Boolean.valueOf(settings.getProperty(LAUNCH_TRACE));
		timeout = Long.parseLong(settings.getProperty(LAUNCH_TIMEOUT));
		activators.addAll(split(settings.getProperty(LAUNCH_ACTIVATORS), " ,"));
		Map<String, String> map = (Map) settings;
		runProperties.putAll(map);
	}

	public Properties getProperties() {
		Properties p = new Properties();
		p.setProperty(LAUNCH_SERVICES, services + "");
		p.setProperty(LAUNCH_STORAGE_DIR, storageDir.getAbsolutePath());
		p.setProperty(LAUNCH_KEEP, keep + "");
		p.setProperty(LAUNCH_RUNBUNDLES, join(runbundles, ","));
		if (systemPackages != null)
			p.setProperty(LAUNCH_SYSTEMPACKAGES, systemPackages + "");
		p.setProperty(LAUNCH_TRACE, trace + "");
		p.setProperty(LAUNCH_TIMEOUT, timeout + "");
		p.setProperty(LAUNCH_ACTIVATORS, join(activators, ","));

		for (Map.Entry<String, String> entry : runProperties.entrySet()) {
			if (entry.getValue() == null) {
				if (entry.getKey() != null)
					p.remove(entry.getKey());
			} else {
				p.put(entry.getKey(), entry.getValue());
			}

		}
		return p;
	}

	private static String join(List<?> runbundles2, String string) {
		StringBuffer sb = new StringBuffer();
		String del = "";
		for (Object r : runbundles2) {
			sb.append(del);
			sb.append(r);
			del = string;
		}
		return sb.toString();
	}
	
	private Collection<? extends String> split(String property, String string) {
		List<String> result = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(property, string);
		while (st.hasMoreTokens()) {
			result.add(st.nextToken());
		}
		return result;
	}
}
