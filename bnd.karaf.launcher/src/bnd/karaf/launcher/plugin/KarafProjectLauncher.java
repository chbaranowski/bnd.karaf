package bnd.karaf.launcher.plugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.Collection;

import aQute.bnd.build.Project;
import aQute.bnd.build.ProjectLauncher;
import aQute.lib.osgi.Processor;
import bnd.karaf.launcher.LauncherConstants;
import bnd.karaf.launcher.LauncherProperties;

public class KarafProjectLauncher extends ProjectLauncher {

	final private Project project;
	final private File propertiesFile;
	boolean isPrepared = false;
	
	public KarafProjectLauncher(Project project) throws Exception {
		super(project);
		this.project = project;
		project.trace("created a Karaf launcher plugin");
		propertiesFile = File.createTempFile("launch", ".properties", project.getTarget());
		project.trace(MessageFormat.format(
				"launcher plugin using temp launch file {0}",
				propertiesFile.getAbsolutePath()));
		addRunVM("-D" + LauncherConstants.LAUNCHER_PROPERTIES + "=" + propertiesFile.getAbsolutePath());
	}

	@Override
	public String getMainTypeName() {
		return "bnd.karaf.launcher.Launcher";
	}

	@Override
	public void update() throws Exception {
		updateFromProject();
		writeProperties();
	}

	@Override
	public int launch() throws Exception {
		prepare();
		return super.launch();
	}

	@Override
	public void prepare() throws Exception {
		if (isPrepared)
			return;
		writeProperties();
		isPrepared = true;
	}

	void writeProperties() throws Exception {
		LauncherProperties settings = getSettings(getRunBundles());
		OutputStream out = new FileOutputStream(propertiesFile);
		try {
			settings.getProperties().store(out, "Launching " + project);
		} finally {
			out.close();
		}
	}

	private LauncherProperties getSettings(Collection<String> runbundles) throws Exception, FileNotFoundException, IOException {
		LauncherProperties properties = new LauncherProperties();
		properties.runProperties = getRunProperties();
		properties.storageDir = getStorageDir();
		properties.keep = isKeep();
		properties.runbundles.addAll(getRunBundles());
		properties.trace = getTrace();
		properties.timeout = getTimeout();
		properties.services = super.getRunFramework() == SERVICES ? true : false;
		properties.activators.addAll(getActivators());
		if (!getSystemPackages().isEmpty()) {
			try {
				properties.systemPackages = Processor.printClauses(getSystemPackages());
			} catch (Throwable e) {
				// ignore for now
			}
		}
		return properties;
	}

}
