package ru.vtb.asaf.sfera.exception;

public class AsafSferaRuntimeException extends RuntimeException {

    public AsafSferaRuntimeException(String message) {
        super(message);
        System.err.println(message);
    }
}
