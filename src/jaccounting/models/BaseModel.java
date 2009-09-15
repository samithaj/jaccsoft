/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jaccounting.models;

import jaccounting.JAccounting;
import java.util.Observable;

/**
 *
 * @author bouba
 */
public class BaseModel extends Observable {

    @Override
    public void setChanged() {
	super.setChanged();
	JAccounting.getApplication().getModelsMngr().getData().setDataChanged();
    }

    protected void setChangedAndNotifyObservers() {
	setChanged();
	notifyObservers();
    }

}
