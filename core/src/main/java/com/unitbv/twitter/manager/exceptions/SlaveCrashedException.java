package com.unitbv.twitter.manager.exceptions;

/**
 * User: agherasim
 * Date: 11/19/2014
 * Time: 6:39 PM
 */
public class SlaveCrashedException extends Exception {

    public SlaveCrashedException(Throwable e) {
        super(e);
    }
}
