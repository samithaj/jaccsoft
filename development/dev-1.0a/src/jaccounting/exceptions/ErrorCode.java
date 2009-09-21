/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jaccounting.exceptions;

import jaccounting.JAccounting;
import org.jdesktop.application.ResourceMap;

/**
 *
 * @author bouba
 */
public enum ErrorCode {

    INVALID_ACCOUNT_TYPE,
    NEGATIVE_ACCOUNT_NUMBER,
    EMPTY_ACCOUNT_NAME,
    NOT_ALPHANUMERIC_ACCOUNT_NAME,
    NOT_TRANSACTIONNABLE_ACCOUNT,

    NEGATIVE_TRANSACTION_AMOUNT,

    UNPERSISTENCE_FAILURE,
    INVALID_NUMBER_FORMAT,
    UNKNOWN;

    public String message() {
	ResourceMap vRmap = JAccounting.getApplication().getContext().getResourceMap(this.getClass());
	return vRmap.getString(this.toString());
    }
}
