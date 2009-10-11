/*
 * BaseModel.java	    1.0.0	    09/2009
 * This file contains the account model class of the JAccounting application.
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

package jaccounting.models;

import jaccounting.JAccounting;
import java.util.Observable;

/**
 * BaseModel is the base class for all models except the Data model of the application.
 * It provides abstraction for observability support. A BaseModel  marks the Data
 * object of the application as changed when it changes before notifying its
 * observers.
 *
 * @author	    Boubacar Diallo
 * @version	    1.0.0
 * @see		    Data
 * @since	    1.0.0
 */
public class BaseModel extends Observable {

    /**
     * Sets this model as changed and marks the application Data object as changed.
     *
     * @see		    Data
     * @since		    1.0.0
     */
    @Override
    protected void setChanged() {
	super.setChanged();
	JAccounting.getApplication().getModelsMngr().getData().setDataChanged();
    }

    /**
     * Sets this model as changed and notifies its observers.
     *
     * @see		    #setChanged()
     * @since		    1.0.0
     */
    protected void setChangedAndNotifyObservers() {
	setChanged();
	notifyObservers();
    }

}
