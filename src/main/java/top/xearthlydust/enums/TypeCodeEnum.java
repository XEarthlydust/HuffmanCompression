package top.xearthlydust.enums;

public enum TypeCodeEnum {
    FILE_BEGIN((byte) 0),
    CHUNK_BEGIN((byte) 1);

    private final byte value;

    TypeCodeEnum(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }
}
