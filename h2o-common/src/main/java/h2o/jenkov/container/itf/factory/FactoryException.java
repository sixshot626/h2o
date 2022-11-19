package h2o.jenkov.container.itf.factory;

import h2o.jenkov.container.ContainerException;

/**

 */
public class FactoryException extends ContainerException {
    public FactoryException(String errorContext, String errorCode, String errorMessage) {
        super(errorContext, errorCode, errorMessage);
    }

    public FactoryException(String errorContext, String errorCode, String errorMessage, Throwable cause) {
        super(errorContext, errorCode, errorMessage, cause);
    }

}
