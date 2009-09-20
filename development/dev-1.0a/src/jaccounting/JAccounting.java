/*
 * JAccounting.java	    1.0.0	    09/2009
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

import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;
import jaccounting.controllers.MainController;

/**
 * JAccounting is the main class of the application.
 * Only one instance of this class is available when the application is running.
 * The instance is responsible for creating and displaying the application's main
 * window as well as starting all initial tasks. It also serves as the knower
 * of the <code>ModelsMngr</code>, the <code>MainView</code> and the
 * <code>ProgressReporter</code>.
 *
 * @author	    Boubacar Diallo
 * @version	    1.0.0
 * @see		    MainView
 * @see		    ModelsMngr
 * @see		    ProgressReporter
 * @since	    1.0.0
 */
public class JAccounting extends SingleFrameApplication {
   
    /**
     * Starts the application. This method gets invoked the super class
     * after launching the application. It creates and shows a
     * <code>MainView</code> of the application before loading the default file.
     *
     * @see		jaccounting.controllers.MainController#loadDefaultFile()
     * @since		1.0.0
     */
    @Override
    protected void startup() {
        show(new MainView(this));
	getProgressReporter().reportUsingKey("messages.loadingDefaultFile");
	MainController.getInstance().runAction("loadDefaultFile");
	getProgressReporter().reportFinished();
    }

    /**
     * Gets the {@code MainView} of the application.
     * @return		the MainView of this application
     * @see		MainView
     * @since		1.0.0
     */
    @Override
    public MainView getMainView() {
        return (MainView) super.getMainView();
    }

    /**
     * Gets the {@code ModelsMngr} of the application
     * @return		the ModelsMngr of this application
     * @see		ModelsMngr
     * @since		1.0.0
     */
    public ModelsMngr getModelsMngr() {
	return ModelsMngr.getInstance();
    }

    /**
     * Gets the {@code ProgressReporter} of the application
     * @return		the ProgressReporter of this application
     * @see		ProgressReporter
     * @since		1.0.0
     */
    public ProgressReporter getProgressReporter() {
	return ProgressReporter.getInstance();
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of JAccounting
     * @since		1.0.0
     */
    public static JAccounting getApplication() {
        return Application.getInstance(JAccounting.class);
    }

    /**
     * Main method launching the application.
     * @param args	the command line arguments
     * @since		1.0.0
     */
    public static void main(String[] args) {
        launch(JAccounting.class, args);
    }

}
