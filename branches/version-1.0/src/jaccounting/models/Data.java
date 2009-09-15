/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jaccounting.models;

import java.util.Observable;

/**
 *
 * @author bouba
 */
public class Data extends Observable {

    protected Journal journal;

    protected GeneralLedger generalLedger;

    public Data() {
        this(new Journal(), new GeneralLedger());
    }

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
