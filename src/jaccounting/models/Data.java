/*
 * Data.java	    1.0.0	    09/2009
 * This file contains the main data class of the JAccounting application.
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

import java.util.Observable;

/**
 * Data is the main application data class. A Data object represents a typical
 * accounting data collection with a list of accounts in a general ledger and
 * a list of transactions in a journal.
 *
 * @author	    Boubacar Diallo
 * @version	    1.0.0
 * @see		    GeneralLedger
 * @see		    Journal
 * @since	    1.0.0
 */
public class Data extends Observable {

    protected Journal journal;		    // the list of transactions

    protected GeneralLedger generalLedger;  // the list of accounts

    /**
     * Default No agrument constructor; initializes this Data object to a new
     * Journal and a new GeneralLedger by calling the full argument constructor.
     *
     * @see		    #Data(jaccounting.models.Journal,
     *				    jaccounting.models.GeneralLedger)
     * @see		    Journal
     * @see		    GeneralLedger
     * @since		    1.0.0
     */
    public Data() {
        this(new Journal(), new GeneralLedger());
    }

    /**
     * Full argument constructor.
     *
     * @param pJournal		    the journal
     * @param pGeneralLedger	    the general ledger
     * @since			    1.0.0
     */
    public Data(Journal pJournal, GeneralLedger pGeneralLedger) {
        this.journal = pJournal;
        this.generalLedger = pGeneralLedger;
    }


    public GeneralLedger getGeneralLedger() {
        return generalLedger;
    }

    public Journal getJournal() {
        return journal;
    }

    void setDataChanged() {
	setChanged();
	notifyObservers();
    }

}
