package bnd.karaf.launcher;

public class LauncherConstants {

	public final static String	LAUNCHER_PROPERTIES		= "launcher.properties";
	public final static String	LAUNCHER_ARGUMENTS		= "launcher.arguments";
	public final static String	LAUNCHER_READY			= "launcher.ready";

	// MUST BE ALIGNED WITH ProjectLauncher! Donot want to create coupling
	// so cannot refer.
	public final static int		OK						= 0;
	public final static int		ERROR					= -2;
	public final static int		WARNING					= -1;
	public final static int		TIMEDOUT				= -3;
	public final static int		UPDATE_NEEDED			= -4;
	public final static int		CANCELED				= -5;
	public final static int		DUPLICATE_BUNDLE		= -6;
	public final static int		RESOLVE_ERROR			= -7;
	public final static int		ACTIVATOR_ERROR			= -8;
	// Start custom errors from here
	public final static int		CUSTOM_LAUNCHER			= -128;

}
