package ru.vtb.asaf.sfera.exception;

public class AsafSferaException extends Exception {

    public AsafSferaException(String message) {
        super(message);
        System.err.println(message);
    }
}
