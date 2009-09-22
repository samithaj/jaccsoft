/*
 * GenericException.java	    1.0.0	    09/2009
 * This file contains the default exception class of the JAccounting application.
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
 * GenericException is the default application exception class. Application
 * exception save the exception thrower the trouble of providing an error message
 * and instead make the exception smart enough to provide a default message of
 * its meaning.
 *
 * @author	    Boubacar Diallo
 * @version	    1.0.0
 * @see		    ErrorCode
 * @since	    1.0.0
 */
public class GenericException extends Exception {

    /** the application error code associated with the exception */
    protected ErrorCode appErrorCode;


    /**
     * No argument constructor. Sets this exception's application error code
     * to {@link ErrorCode#UNKNOWN UNKNOWN}.
     *
     * @see			ErrorCode#UNKNOWN
     * @since			1.0.0
     */
    public GenericException() {
	super();
	appErrorCode = ErrorCode.UNKNOWN;
    }

    /**
     * Constructs a GenericException object with the specified application error
     * code.
     *
     * @param pCode		the exception's application error code
     * @see			ErrorCode
     * @since			1.0.0
     */
    public GenericException(ErrorCode pCode) {
	super();
	appErrorCode = pCode;
    }

    /**
     * Constructs a GenericException object with the specified error message. The
     * application error code is set to {@link ErrorCode#UNKNOWN UNKNOWN}.
     *
     * @param message		the error message for this exception
     * @see			ErrorCode#UNKNOWN
     * @since			1.0.0
     */
    public GenericException(String message) {
	super(message);
	appErrorCode = ErrorCode.UNKNOWN;
    }


    /**
     * Gets the error message for this exception. This methods effectively grabs
     * the message from its application error code.
     *
     * @return		    the exception's message
     * @see		    ErrorCode#message()
     * @since		    1.0.0
     */
    @Override
    public String getMessage() {
	return appErrorCode.message();
    }

}
