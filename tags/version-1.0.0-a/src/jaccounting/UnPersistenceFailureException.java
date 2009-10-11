/*
 * UnPersistenceFailureException.java	    1.0.0	    09/2009
 * This file contains the exception class of the JAccounting application indicating
 * problems while loading data files.
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

/**
 * UnPersistenceFailureException is the exception class thrown to indicate an
 * error while loading application data from a file. This exception corresponds
 * to the application error code {@link ErrorCode#UNPERSISTENCE_FAILURE}.
 *
 * @author	    Boubacar Diallo
 * @version	    1.0.0
 * @see		    PersistenceHandler
 * @see		    ErrorCode#UNPERSISTENCE_FAILURE
 * @since	    1.0.0
 */
public class UnPersistenceFailureException extends GenericException {

    /**
     * Sole Constructor. Sets the exception's application error code to {@link
     * ErrorCode#UNPERSISTENCE_FAILURE}.
     * 
     * @see		    ErrorCode#UNPERSISTENCE_FAILURE
     * @since		    1.0.0
     */
    public UnPersistenceFailureException() {
	super(ErrorCode.UNPERSISTENCE_FAILURE);
    }

}
