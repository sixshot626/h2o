package h2o.jenkov.container.script;

import h2o.jenkov.container.ContainerException;

/**
 * @author Jakob Jenkov - Copyright 2004-2006 Jenkov Development
 */
public class ParserException extends ContainerException {

    public ParserException(String errorContext, String errorCode, String errorMessage) {
        super(errorContext, errorCode, errorMessage);
    }

    public ParserException(String errorContext, String errorCode, String errorMessage, Throwable cause) {
        super(errorContext, errorCode, errorMessage, cause);
    }
}
