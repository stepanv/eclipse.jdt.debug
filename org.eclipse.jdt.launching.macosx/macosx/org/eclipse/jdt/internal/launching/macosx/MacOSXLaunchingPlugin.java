/* * (c) Copyright IBM Corp. 2000, 2001. * All Rights Reserved. */package org.eclipse.jdt.internal.launching.macosx;import java.io.*;import org.eclipse.core.runtime.IPluginDescriptor;import org.eclipse.core.runtime.Plugin;
public class MacOSXLaunchingPlugin extends Plugin {	
	private static MacOSXLaunchingPlugin fgPlugin;	
	/**	 * Constructor for MacOSXLaunchingPlugin	 */	public MacOSXLaunchingPlugin(IPluginDescriptor descriptor) {
		super(descriptor);
		fgPlugin= this;	}
		public static MacOSXLaunchingPlugin getDefault() {		return fgPlugin;	}		/**	 * Convenience method which returns the unique identifier of this plugin.	 */	public static String getUniqueIdentifier() {		if (getDefault() == null) {			// If the default instance is not yet initialized,			// return a static identifier. This identifier must			// match the plugin id defined in plugin.xml			return "org.eclipse.jdt.launching.macosx"; //$NON-NLS-1$		}		return getDefault().getDescriptor().getUniqueIdentifier();	}		static String createWrapper(Class where, String filename) {		/*		 * In order to build an application bundle under MacOS X we need a small stub		 * that reads the various artefacts of a bundle and starts the Java VM. We copy		 * the stub either from the running Eclipse or from the JavaVM		 * framework. Here we create the appropriate pathname.		 */		String javaApplStub= System.getProperty("sun.boot.library.path");	//$NON-NLS-1$		int pos= javaApplStub.indexOf(':');		if (pos > 0)			javaApplStub= javaApplStub.substring(0, pos);			String expected= "/Contents/Resources/Java";	//$NON-NLS-1$		if (javaApplStub.endsWith(expected)) {			javaApplStub= javaApplStub.substring(0, javaApplStub.length()-expected.length());			javaApplStub+= "/Contents/MacOS/";	//$NON-NLS-1$		} else {			javaApplStub= "/System/Library/Frameworks/JavaVM.framework/Versions/A/Resources/MacOS/"; //$NON-NLS-1$		}		javaApplStub= "JAVASTUB=\""+ javaApplStub + "\"\n";	//$NON-NLS-1$ //$NON-NLS-2$					String output= "/tmp/start_carbon.sh";	//$NON-NLS-1$		FileOutputStream os= null;		try {			os= new FileOutputStream(output);		} catch (FileNotFoundException ex) {			return null;		}								InputStream is= null;		try {			os.write("#!/bin/sh\n".getBytes());	//$NON-NLS-1$			os.write(javaApplStub.getBytes());			is= where.getResourceAsStream(filename);			if (is != null) {				while (true) {					int c= is.read();					if (c == -1)						break;					os.write(c);				}			}			os.flush();		} catch (IOException io) {			return null;		} finally {			if (is != null) {				try {					is.close();				} catch (IOException e) {				}			}			try {				os.close();			} catch(IOException e) {			}		}				return output;	}}
