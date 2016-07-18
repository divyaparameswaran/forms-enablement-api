package com.ch.exception;

import javax.ws.rs.WebApplicationException;

/**
 * Created by Aaron.Witter on 15/07/2016.
 */
public class PackageContentsException extends WebApplicationException {
    private final String property;

    /**
     * Exception thrown when package has incorrect contents.
     *
     * @param property property not found
     */
    public PackageContentsException(String property) {
        this.property = property;
    }

    @Override
    public String getMessage() {
        return String.format("Package %s is incorrect. Please check the contents of the package.", property);
    }
}
