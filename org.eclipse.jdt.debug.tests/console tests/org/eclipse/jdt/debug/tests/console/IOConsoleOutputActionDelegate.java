/*******************************************************************************
 *  Copyright (c) 2004, 2011 IBM Corporation and others.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 * 
 *  Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.debug.tests.console;

import java.io.OutputStream;
import java.io.PrintStream;

import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.IDebugUIConstants;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IActionDelegate2;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.IOConsole;

/**
 * Tests the output action delegate for the console view
 */
public class IOConsoleOutputActionDelegate implements IActionDelegate2, IWorkbenchWindowActionDelegate{

    /**
     * @see org.eclipse.ui.IActionDelegate2#init(org.eclipse.jface.action.IAction)
     */
    public void init(IAction action) {}

    /**
     * @see org.eclipse.ui.IActionDelegate2#dispose()
     */
    public void dispose() {}

    /**
     * @see org.eclipse.ui.IActionDelegate2#runWithEvent(org.eclipse.jface.action.IAction, org.eclipse.swt.widgets.Event)
     */
    public void runWithEvent(IAction action, Event event) {
        run(action);
    }

    /**
     * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
     */
    public void run(IAction action) {
        IOConsole console = new IOConsole("Test IOConsole", null, DebugUITools.getImageDescriptor(IDebugUIConstants.IMG_ACT_RUN)); //$NON-NLS-1$
		IConsoleManager manager = ConsolePlugin.getDefault().getConsoleManager();
		manager.addConsoles(new IConsole[]{console});
		OutputStream out = console.newOutputStream(); 
		final PrintStream stream = new PrintStream(out);
		Runnable r = new Runnable() {
			public void run() {
			    long start = System.currentTimeMillis();
				for (int i = 0; i < 1000; i++) {
					stream.print(Integer.toString(i));
					stream.print(": Testing..."); //$NON-NLS-1$
					stream.print("one..."); //$NON-NLS-1$
					stream.println("two.... three...."); //$NON-NLS-1$
				}
				stream.println("Total time: " + (System.currentTimeMillis()-start)); //$NON-NLS-1$
			}
		};
		new Thread(r).start();
    }

    /**
     * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction, org.eclipse.jface.viewers.ISelection)
     */
    public void selectionChanged(IAction action, ISelection selection) {}

    /**
     * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#init(org.eclipse.ui.IWorkbenchWindow)
     */
    public void init(IWorkbenchWindow window) {}
}
