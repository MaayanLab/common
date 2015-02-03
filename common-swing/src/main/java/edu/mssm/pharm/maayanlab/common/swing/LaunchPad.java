package edu.mssm.pharm.maayanlab.common.swing;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LaunchPad {
	
	static Logger log = Logger.getLogger(LaunchPad.class.getSimpleName());
	public static final String APP_MAIN_CLASS = "app.main.class";
	public static final String APP_VM_OPTIONS = "app.vm.options";
    public static final String PROPERTY_FILE = "launch.properties";
	public static final String JAVA_PATH = "bin" + File.separator + "java";
	
    // Holds the child process
    static Process child;

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
    	if (!Boolean.getBoolean("verbose"))
            log.setLevel(Level.WARNING);
    	
        File java = new File(System.getProperty("java.home"), JAVA_PATH);
        log.info("using java in " + java.getParent());

        // Integrate version check into launcher
        VersionCheck.jvm();
        
        // get the class path URLs
        URL launcherUrl = ((URLClassLoader) LaunchPad.class.getClassLoader()).getURLs()[0];

        // locate current directory
        File topLevelDir = new File(launcherUrl.toURI());
        if (topLevelDir.isFile()) {
            topLevelDir = topLevelDir.getParentFile();
        }
        log.info("dir: " + topLevelDir);
        
        // build the class path
        String classpath = topLevelDir.getPath().concat(File.pathSeparator);
        
        // load properties file
        Properties launchProps = new Properties();
        try {
        	InputStream in = LaunchPad.class.getClassLoader().getResourceAsStream(PROPERTY_FILE);
        	try {
        		launchProps.load(in);
        	} finally {
        		in.close();
        	}
        } catch (IOException e) {
        	log.severe("Cannot load config.");
        	System.exit(1);
        }
        	
        if (topLevelDir.isDirectory()) {
            File[] libs = topLevelDir.listFiles();
            for (File lib : libs) {
            	// only run jar files
            	if (lib.isFile() && lib.getName().endsWith(".jar"))
            		classpath = classpath.concat(lib.getAbsolutePath()).concat(File.pathSeparator);
            }
        }
        log.info("classpath = " + classpath);
        
        ArrayList<String> vmOptions = new ArrayList<String>();
        String vmOptionsString = launchProps.getProperty(APP_VM_OPTIONS, "");
        log.info("vmoptions = " + vmOptionsString); 
        for (StringTokenizer st = new StringTokenizer(vmOptionsString); st.hasMoreTokens();) {
            String token = st.nextToken();
            vmOptions.add(token);
        }

        String mainClass = launchProps.getProperty(APP_MAIN_CLASS);        
        log.info("main class = " + mainClass);

        // flag to follow child process output
        Boolean follow = Boolean.parseBoolean(launchProps.getProperty("app.follow", "true"));

        ArrayList<String> cmd = new ArrayList<String>();
        cmd.add(java.getAbsolutePath());
        cmd.addAll(vmOptions);
        cmd.add("-cp");
        cmd.add(classpath);
        cmd.add(mainClass);
        for (int i = 0; i < args.length; i++) {
            cmd.add(args[i]);
        }
        log.info("command line:\n" + cmd);
        ProcessBuilder pb = new ProcessBuilder(cmd);

        pb.redirectErrorStream(true);
        child = pb.start();

        if (follow) {
            log.info("starting follower thread");
            new FollowerThread().start();
        }
        System.out.println(Runtime.getRuntime().maxMemory());
        System.out.println("Bootstrap terminating");
        System.exit(0);
    }
    
    /**
     * Writes on stdout the output of the child thread
     */
    static class FollowerThread extends Thread {

        public FollowerThread() {
            setPriority(MIN_PRIORITY);
        }

        @Override
        public void run() {
            InputStream stream = child.getInputStream();
            byte[] buf = new byte[1024];
            try {
                int read = stream.read(buf);
                while (read >= 0) {
                    System.out.write(buf, 0, read);
                    read = stream.read(buf);
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } finally {
            }
        }
    }
}
