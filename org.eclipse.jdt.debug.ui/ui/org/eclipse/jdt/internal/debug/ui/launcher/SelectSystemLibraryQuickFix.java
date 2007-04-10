/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.debug.ui.launcher;


import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.debug.ui.IJavaDebugUIConstants;
import org.eclipse.jdt.internal.debug.ui.JDIDebugUIPlugin;
import org.eclipse.jdt.internal.launching.JREContainer;
import org.eclipse.jdt.internal.launching.JREContainerInitializer;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.PlatformUI;

import com.ibm.icu.text.MessageFormat;

/**
 * Quick fix to select an alternate JRE for a project. 
 */
public class SelectSystemLibraryQuickFix extends JREResolution {
	
	private IPath fUnboundPath;
	private IJavaProject fProject;
	
	public SelectSystemLibraryQuickFix(IPath unboundPath, IJavaProject project) {
		fUnboundPath = unboundPath;
		fProject = project;	
	}

	/**
	 * @see org.eclipse.ui.IMarkerResolution#run(org.eclipse.core.resources.IMarker)
	 */
	public void run(IMarker marker) {
		try {
			handleContainerResolutionError(fUnboundPath, fProject);
		} catch (CoreException e) {
			JDIDebugUIPlugin.statusDialog(LauncherMessages.JREContainerResolution_Unable_to_update_classpath_1, e.getStatus());  
		}
	}
	
	protected void handleContainerResolutionError(final IPath unboundPath, final IJavaProject project) throws CoreException {			
		
		String title = LauncherMessages.JREResolution_Select_System_Library_1; 
		String message = MessageFormat.format(LauncherMessages.JREResolution_Select_a_system_library_to_use_when_building__0__2, new String[]{project.getElementName()}); 
		
		final IVMInstall vm = chooseVMInstall(title, message);
		if (vm == null) {
			return;
		}

		IRunnableWithProgress runnable = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException {
					String vmTypeId = vm.getVMInstallType().getId();
					String vmName = vm.getName();
					String prevId = JREContainerInitializer.getVMTypeId(unboundPath);
					String prevName = JREContainerInitializer.getVMName(unboundPath);
					try {
						IPath newBinding = unboundPath;
						if (!(vmTypeId.equals(prevId) && vmName.equals(prevName))) {
							// update classpath
							IPath newPath = new Path(JavaRuntime.JRE_CONTAINER);
							if (vmTypeId != null) {
								newPath = newPath.append(vmTypeId).append(vmName);
							}
							IClasspathEntry[] classpath = project.getRawClasspath();
							for (int i = 0; i < classpath.length; i++) {
								switch (classpath[i].getEntryKind()) {
									case IClasspathEntry.CPE_CONTAINER:
										if (classpath[i].getPath().equals(unboundPath)) {
											classpath[i] = JavaCore.newContainerEntry(newPath, classpath[i].isExported());
										}
										break;
									case IClasspathEntry.CPE_VARIABLE:
										if(classpath[i].getPath().equals(new Path(JavaRuntime.JRELIB_VARIABLE))) {
											classpath[i] = JavaCore.newContainerEntry(newPath, classpath[i].isExported());
										}
										break;
									default:
										break;
								}
							}
							project.setRawClasspath(classpath, monitor);
							newBinding = newPath;
						}
					JavaCore.setClasspathContainer(unboundPath, new IJavaProject[] {project}, new IClasspathContainer[] {new JREContainer(vm, newBinding, project)}, monitor);
					} catch (CoreException e) {
						throw new InvocationTargetException(e);
					}
			}
		};
		
		try {
			PlatformUI.getWorkbench().getProgressService().busyCursorWhile(runnable);
		} catch (InvocationTargetException e) {
			if (e.getTargetException() instanceof CoreException) {
				throw (CoreException)e.getTargetException();
			}
			throw new CoreException(new Status(IStatus.ERROR,
				JDIDebugUIPlugin.getUniqueIdentifier(),
				IJavaDebugUIConstants.INTERNAL_ERROR,
				LauncherMessages.JREContainerResolution_An_exception_occurred_while_updating_the_classpath__1, e.getTargetException())); 
		} catch (InterruptedException e) {
			// cancelled
		}
	}		
	/**
	 * @see org.eclipse.ui.IMarkerResolution#getLabel()
	 */
	public String getLabel() {
		return MessageFormat.format(LauncherMessages.JREContainerResolution_Select_a_system_library_to_use_when_building__0__2, new String[]{fProject.getElementName()}); 
	}

}
