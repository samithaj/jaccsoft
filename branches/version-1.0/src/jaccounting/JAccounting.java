/*
 * JAccounting.java
 */

package jaccounting;

import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

import jaccounting.controllers.MainController;
import org.jdesktop.application.ApplicationContext;

/**
 * The main class of the application.
 */
public class JAccounting extends SingleFrameApplication {
   
    /**
     * At startup create and show the main frame of the application.
     */
    @Override protected void startup() {
        show(new MainView(this));
	MainController.getInstance().runAction("loadDefaultFile");
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override protected void configureWindow(java.awt.Window root) {
    }

    public MainView getView() {
        return (MainView)getMainView();
    }

    public ModelsMngr getModelsMngr() {
	return ModelsMngr.getInstance();
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of JAccounting
     */
    public static JAccounting getApplication() {
        return Application.getInstance(JAccounting.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        launch(JAccounting.class, args);
    }

}
