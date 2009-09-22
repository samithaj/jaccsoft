/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jaccounting.models;

import jaccounting.GenericException;
import jaccounting.ErrorCode;

/**
 *
 * @author bouba
 */
public class InvalidAccountTypeException extends GenericException {

    /**
     * Creates a new instance of <code>InvalidAccountTypeException</code> without detail message.
     */
    public InvalidAccountTypeException() {
	super(ErrorCode.INVALID_ACCOUNT_TYPE);
    }

}
