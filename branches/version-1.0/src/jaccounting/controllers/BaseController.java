/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jaccounting.controllers;

import jaccounting.JAccounting;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 *
 * @author bouba
 */
public abstract class BaseController {

    protected PropertyChangeSupport support;

    public void runAction(String action) {
        ActionEvent evt = new ActionEvent("no source", ActionEvent.ACTION_PERFORMED, "no command");
        JAccounting.getApplication().getContext().getActionMap(this.getClass(),
                                                              this)
                                                              .get(action).actionPerformed(evt);
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
	support.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
	support.removePropertyChangeListener(l);
    }
}
