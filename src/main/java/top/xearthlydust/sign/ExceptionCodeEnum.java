package top.xearthlydust.sign;

public enum ExceptionCodeEnum {
    LEFT_NON_EXISTENT(4000, "目标节点左节点不存在"),
    RIGHT_NON_EXISTENT(4001, "目标节点右节点不存在"),
    DECODE_FAILED(4010, "解码出错"),
    ENCODE_FAILED(4020, "编码出错"),
    CHECK_FREQ_FAILED(4030, "构建文件流字符频率时出错");

    private final String INFO;
    private final Integer CODE;

    ExceptionCodeEnum(Integer CODE, String INFO) {
        this.INFO = INFO;
        this.CODE = CODE;
    }

    public Integer getCode() {
        return this.CODE;
    }

    public String getInfo() {
        return this.INFO;
    }

}
