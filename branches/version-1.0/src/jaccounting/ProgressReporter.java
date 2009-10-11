/*
 * ModelsMngr.java	    1.0.0	    09/2009
 * This file contains the main class of the JAccounting application.
 *
 * JAccounting - Basic Double Entry Accounting Software.
 * Copyright (c) 2009 Boubacar Diallo.
 *
 * This software is free: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this software.  If not, see http://www.gnu.org/licenses.
 */

package jaccounting;

//import java.util.Enumeration;
//import java.util.Vector;
//import org.jdesktop.application.ResourceMap;

/**
 * ProgressReporter is the singleton class facilating reporting work status
 * throughout the whole application. It's instance updates the status message
 * label and progress bar on the main frame.
 *
 * @author	    Boubacar Diallo
 * @version	    1.0.0
 * @see		    MainView
 * @since	    1.0.0
 */
public class ProgressReporter {

    //private int currentLevel;		// status messages stack top level number

    //private Vector<String> messages;	// stack of status messages to display

    private ProgressReporter() {
	//messages = new Vector();
	//currentLevel = -1;
    }

    private static class InstanceHolder {
        private static final ProgressReporter INSTANCE   = new ProgressReporter();
    }

    
    /**
     * Gets the sinlge instance of this class.
     *
     * @return			the single instance of this class
     * @since			1.0.0
     */
    public static ProgressReporter getInstance() {
        return InstanceHolder.INSTANCE;
    }

    /**
     * Updates the main frame's status message label text to a new message contained
     * in this class resource bundle identified by a resource key name. This method
     * delegates the work to {@link #report(java.lang.String) }.
     *
     * @param pKey		the resource key name
     * @see			#report(java.lang.String)
     * @since			1.0.0
     */
    public void reportUsingKey(String pKey) {
	report(JAccounting.getApplication().getContext().
		getResourceMap(this.getClass()).getString(pKey));
    }

    /**
     * Updates the main frame's status message label text to a message indicating
     * the work is "Done" and the application is "Ready" for a new work. This method
     * delegates the work to {@link #reportUsingKey(java.lang.String) } by passing
     * it the to such a message contained in this class resource bundle.
     *
     * @see			#reportUsingKey(java.lang.String)
     * @since			1.0.0
     */
    public void reportFinished() {
	reportUsingKey("messages.notWorking");
    }

    /**
     * Updates the main frame's status message label to text to a given new message.
     *
     * @param pMessage		the new status message
     * @see			MainView
     * @since			1.0.0
     */
    public void report(String pMessage) {
	JAccounting.getApplication().getMainView().getStatusMessageLabel().setText(pMessage);
    }

    /*public void report(int pPercent) {
	ResourceMap vRmap = JAccounting.getApplication().getContext().getResourceMap(this.getClass());
	String vMessage = "";

	if (pPercent < 100 ) vMessage = vRmap.getString("messages.working");
	report(pPercent, vMessage);
    }

    public void report(int pPercent, String pMessage) {
	if (pPercent <= 0) {
	    messages.add(pMessage);
	    currentLevel++;
	}
	else if (pPercent >= 100) {
	    messages.remove(currentLevel);
	    currentLevel--;
	}
	else messages.set(currentLevel, pMessage);

	updateView(pPercent);
    }

    private void updateView(int pPercent) {
	MainView vView = JAccounting.getApplication().getMainView();
	boolean vVisible;
	int vValue;
	String vText;

	if (messages.isEmpty()) {
	    ResourceMap vRmap = JAccounting.getApplication().getContext().getResourceMap(this.getClass());
	    
	    vVisible =false;
	    vValue = 0;
	    vText = vRmap.getString("messages.notWorking");
	}
	else {
	    vVisible = true;
	    vValue = pPercent;
	    vText = buildMessage();
	}

	vView.getProgressBar().setVisible(vVisible);
	vView.getProgressBar().setValue(vValue);
	vView.getStatusMessageLabel().setText(vText);
    }

    private String buildMessage() {
	StringBuffer rMessage = new StringBuffer();

	Enumeration vEls = messages.elements();
	while (vEls.hasMoreElements()) {
	    rMessage.append((String)vEls.nextElement());
	    if (vEls.hasMoreElements()) rMessage.append(": ");
	}

	return rMessage.toString();
    }*/

}
