package top.xeartglydust.util.exception;

import top.xeartglydust.sign.ExceptionCodeEnum;

public class NodeException extends Exception {
    private final ExceptionCodeEnum exceptionCode;

    public NodeException(ExceptionCodeEnum exceptionCode) {
        super(exceptionCode.getInfo());
        this.exceptionCode = exceptionCode;
    }

    public ExceptionCodeEnum getExceptionCode() {
        return this.exceptionCode;
    }

    @Override
    public String toString() {
        return "NodeException{" +
                "exceptionCode=" + exceptionCode +
                ", message=" + getMessage() +
                '}';
    }
}
