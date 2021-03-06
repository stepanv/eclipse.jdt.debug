/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.launching.sourcelookup;


import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
 
/**
 * A repository of source code. A source location is capable of retrieving
 * source elements.
 * <p>
 * For example, a source location could be a project, zip/archive
 * file, or a directory in the file system.
 * </p>
 * <p>
 * This interface is may be implemented by clients.
 * </p>
 * @since 2.0
 * @deprecated In 3.0, the debug platform provides source lookup facilities that
 *  should be used in place of the Java source lookup support provided in 2.0.
 *  The new facilities provide a source lookup director that coordinates source
 *  lookup among a set of participants, searching a set of source containers.
 *  See the following packages: <code>org.eclipse.debug.core.sourcelookup</code>
 *  and <code>org.eclipse.debug.core.sourcelookup.containers</code>. This interface
 *  has been replaced by
 *  <code>org.eclipse.debug.core.sourcelookup.ISourceContainer</code>.
 */
@Deprecated
public interface IJavaSourceLocation extends IAdaptable {
	
	/**
	 * Returns an object representing the source code
	 * for a type with the specified name, or <code>null</code>
	 * if none could be found. The name is
	 * a fully qualified type name, and may contain the '$'
	 * character when referring to inner types. For example,
	 * <code>java.lang.String</code>. The source element 
	 * returned is implementation specific - for example, a
	 * resource, a local file, a zip file entry, etc.
	 * 
	 * @param name fully qualified name of the type for which
	 * 		source is being searched for
	 * @return source element
	 * @exception CoreException if an exception occurs while searching
	 *  for the specified source element
	 */
	public Object findSourceElement(String name) throws CoreException;
	
	/**
	 * Returns a memento for this source location from which this
	 * source location can be reconstructed.
	 * 
	 * @return a memento for this source location
	 * @exception CoreException if unable to create a memento
	 */
	public String getMemento() throws CoreException;
	
	/**
	 * Initializes this source location from the given memento.
	 * 
	 * @param memento a memento generated by this source location
	 * @exception CoreException if unable to initialize this source
	 * 	location
	 */
	public void initializeFrom(String memento) throws CoreException;

}
