/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.debug.ui;


import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

public class ConditionalBreakpointErrorDialog extends ErrorDialog {
	
	public ConditionalBreakpointErrorDialog(Shell parentShell, String message, IStatus status) {
		super(parentShell, DebugUIMessages.getString("ConditionalBreakpointErrorDialog.Conditional_Breakpoint_Error_1"), message, status, IStatus.ERROR); //$NON-NLS-1$
	}

	/**
	 * @see org.eclipse.jface.dialogs.Dialog#createButtonsForButtonBar(org.eclipse.swt.widgets.Composite)
	 */
	protected void createButtonsForButtonBar(Composite parent) {
		// create Edit and Cancel buttons
		createButton(parent, IDialogConstants.OK_ID, DebugUIMessages.getString("ConditionalBreakpointErrorDialog.&Edit_Condition_2"), true); //$NON-NLS-1$
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

}