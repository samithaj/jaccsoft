/*
 * BaseController.java		1.0.0		09/2009
 * This file contains the base class of all controller classes of the JAccounting
 * application.
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

package jaccounting.controllers;

import jaccounting.JAccounting;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * BaseController is the base class of all controller class of the application.
 * It abstract out property change support and action methods trigggering without
 * user event for the controllers. The property change support is used to coordinate
 * actions enabling/disabling throughout all gui elements from which actions
 * might be triggered -e.g menu items, buttons, shortcut keys.
 *
 * @author	    Boubacar Diallo
 * @version	    1.0.0
 * @since	    1.0.0
 */
public abstract class BaseController {

    protected PropertyChangeSupport support; // delagate for property change support


    /**
     * Default no argument constructor. Initializes the property change support
     * for this controller.
     *
     * @since	    1.0.0
     */
    protected BaseController() {
	support = new PropertyChangeSupport(this);
    }


    /**
     * Triggers an action without the need for a user event as its source.
     *
     * @param action		the name of the controller's action to trigger
     * @since			1.0.0
     */
    public void runAction(String action) {
        ActionEvent evt = new ActionEvent("no source", ActionEvent.ACTION_PERFORMED, "no command");
        JAccounting.getApplication().getContext().getActionMap(this.getClass(), this)
	    .get(action).actionPerformed(evt);
    }

    /**
     * Adds a listener for property changes of the controller.
     *
     * @param l		    the object that wants to listen in
     * @since		    1.0.0
     */
    public void addPropertyChangeListener(PropertyChangeListener l) {
	support.addPropertyChangeListener(l);
    }

    /**
     * Removes a listener for property changes of the controller.
     *
     * @param l		    the object that wwas listenning in
     * @since		    1.0.0
     */
    public void removePropertyChangeListener(PropertyChangeListener l) {
	support.removePropertyChangeListener(l);
    }
}
