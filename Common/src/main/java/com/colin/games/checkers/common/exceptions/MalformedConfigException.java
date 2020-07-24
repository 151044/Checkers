package com.colin.games.checkers.common.exceptions;

public class MalformedConfigException extends RuntimeException{

    /**
     * Creates a new instance of <code>MalformedConfigException</code> without detail message.
     */
    public MalformedConfigException() {
    }

    /**
     * Constructs an instance of <code>MalformedConfigException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public MalformedConfigException(String msg) {
        super(msg);
    }

    /**
     * Constructs an instance of <code>MalformedConfigException</code> without detail message and the specified exception as cause.
     * @param exc The cause of this exception
     */
    public MalformedConfigException(Exception exc){
        super(exc);
    }
}
