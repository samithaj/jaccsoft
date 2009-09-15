/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jaccounting.exceptions;

/**
 *
 * @author bouba
 */
public class GenericException extends Exception {

    protected ErrorCode mCode;

    public GenericException(ErrorCode pCode) {
	super();
	mCode = pCode;
    }

    @Override
    public String getMessage() {
	return getCodeMessage(this.mCode);
    }

    public static String getCodeMessage(ErrorCode pCode) {
	return pCode.message();
    }
}
