/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jaccounting;

import java.util.Enumeration;
import java.util.Vector;
import org.jdesktop.application.ResourceMap;

/**
 *
 * @author bouba
 */
public class ProgressReporter {

    private int currentLevel;
    private Vector<String> messages;

    private ProgressReporter() {
	messages = new Vector();
	currentLevel = -1;
    }

    private static class ProgressReporterHolder {
        private static final ProgressReporter INSTANCE   = new ProgressReporter();
    }

    public static ProgressReporter getInstance() {
        return ProgressReporterHolder.INSTANCE;
    }

    public void reportUsingKey(String pKey) {
	report(JAccounting.getApplication().getContext().
		getResourceMap(this.getClass()).getString(pKey));
    }

    public void reportFinished() {
	report(JAccounting.getApplication().getContext().
		getResourceMap(this.getClass()).getString("messages.notWorking"));
    }

    public void report(String pMessage) {
	JAccounting.getApplication().getMainView().getStatusMessageLabel().setText(pMessage);
    }

    public void report(int pPercent) {
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
    }

}
