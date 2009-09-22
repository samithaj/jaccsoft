/*
 * ErrorCode.java	    1.0.0	    09/2009
 * This file contains the error codes of the JAccounting application.
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

import org.jdesktop.application.ResourceMap;

/**
 * ErrorCode is the enum class of all the application error codes. It takes
 * care of providing a default error message associated with each error code.
 *
 * @author	    Boubacar Diallo
 * @version	    1.0.0
 * @since	    1.0.0
 */
public enum ErrorCode {

    /* Account related errors */
    INVALID_ACCOUNT_TYPE,
    NEGATIVE_ACCOUNT_NUMBER,
    EMPTY_ACCOUNT_NAME,
    NOT_ALPHANUMERIC_ACCOUNT_NAME,
    NOT_TRANSACTIONNABLE_ACCOUNT,

    /** Transaction related errors */
    NEGATIVE_TRANSACTION_AMOUNT,

    /** General errors */
    UNPERSISTENCE_FAILURE,
    INVALID_NUMBER_FORMAT,
    UNKNOWN;

    /**
     * Provides a default error message for error codes. This method grabs the
     * message from this class resource bundle. The message are indexed by the
     * error code {@code String} value.
     *
     * @return		    the default error message
     * @since		    1.0.0
     */
    public String message() {
	ResourceMap vRmap = JAccounting.getApplication().getContext().getResourceMap(this.getClass());
	return vRmap.getString(this.toString());
    }
}
