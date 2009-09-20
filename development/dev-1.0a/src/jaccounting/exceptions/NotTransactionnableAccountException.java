/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jaccounting.exceptions;

/**
 *
 * @author bouba
 */
public class NotTransactionnableAccountException extends GenericException {

    /**
     * Creates a new instance of <code>NotTransactionnableAccountException</code> without detail message.
     */
    public NotTransactionnableAccountException() {
	super(ErrorCode.NOT_TRANSACTIONNABLE_ACCOUNT);
    }

}
