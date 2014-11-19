package com.pentalog.twitter.manager.exceptions;

/**
 * User: mcsere
 * Date: 11/19/2014
 * Time: 6:39 PM
 */
public class SlaveCrashedException extends Exception {

    public SlaveCrashedException(Throwable e) {
        super(e);
    }
}
