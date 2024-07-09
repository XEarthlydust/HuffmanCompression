package top.xearthlydust.util;

import top.xearthlydust.enums.ExceptionCodeEnum;

public class NodeException extends Exception {
    private final ExceptionCodeEnum exceptionCode;

    public NodeException(ExceptionCodeEnum exceptionCode) {
        super(exceptionCode.getInfo());
        this.exceptionCode = exceptionCode;
    }

    public Integer getExceptionCode() {
        return this.exceptionCode.getCode();
    }

    @Override
    public String toString() {
        return "NodeException{" +
                "exceptionCode=" + exceptionCode.getCode() +
                ", message=" + getMessage() +
                '}';
    }
}
