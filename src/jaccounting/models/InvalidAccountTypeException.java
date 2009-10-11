/*
 * InvalidAccountTypeException.java	    1.0.0	    09/2009
 * This file contains the exception class of the JAccounting application indicating
 * an invalid account type.
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

import jaccounting.GenericException;
import jaccounting.ErrorCode;

/**
 * InvalidAccountTypeException is the exception class thrown to indicate a wrong
 * Account type. This exception corresponds to the application error code {@link
 * jaccounting.ErrorCode#INVALID_ACCOUNT_TYPE }.
 *
 * @author	    Boubacar Diallo
 * @version	    1.0.0
 * @see		    Account
 * @see		    jaccounting.ErrorCode#INVALID_ACCOUNT_TYPE
 * @since	    1.0.0
 */
public class InvalidAccountTypeException extends GenericException {

    /**
     * Sole Constructor. Sets the exception's application error code to {@link
     * jaccounting.ErrorCode#INVALID_ACCOUNT_TYPE }.
     *
     * @see		    jaccounting.ErrorCode#INVALID_ACCOUNT_TYPE
     * @since		    1.0.0
     */
    public InvalidAccountTypeException() {
	super(ErrorCode.INVALID_ACCOUNT_TYPE);
    }

}
