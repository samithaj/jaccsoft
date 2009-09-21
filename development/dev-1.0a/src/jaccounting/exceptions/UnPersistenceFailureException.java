/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jaccounting.exceptions;

/**
 *
 * @author bouba
 */
public class UnPersistenceFailureException extends GenericException {

    /**
     * Creates a new instance of <code>UnPersistenceFailureException</code> without detail message.
     */
    public UnPersistenceFailureException() {
	super(ErrorCode.UNPERSISTENCE_FAILURE);
    }

}
