/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jaccounting;

import jaccounting.models.Data;
import java.io.IOException;
import org.jdom.JDOMException;


/**
 *
 * @author bouba
 */
public class ModelsMngr {
    private PersistenceHandler persister;
    protected Data data;
    protected String loadedFilename;

    private ModelsMngr() {
	persister = new PersistenceHandler();
    }

    private static class AccSoftModelHolder {
        private static final ModelsMngr INSTANCE = new ModelsMngr();
    }

    public static ModelsMngr getInstance() {
        return AccSoftModelHolder.INSTANCE;
    }

    private Data generateDefaultData() {
        Data rData = new Data();
        rData.getGeneralLedger().addNewDefaultAccounts();
        return rData;
    }

    private void resetData() {
	data = generateDefaultData();
    }

    public Data getData() {
        return data;
    }

    public void load(String pFilename) throws IOException, JDOMException {
	data = persister.unpersist(pFilename);
	loadedFilename = pFilename;
    }

    public void loadNew(String pFilename) {
	resetData();
	loadedFilename = pFilename;
    }

    public void persit() throws IOException {
	persister.persist(loadedFilename);
    }

    
    
}
