package top.xearthlydust.enums;

public enum ThreadCodeEnum {
    NORMAL_WORKING(2000, "正常运行"),
    START_PRODUCE(2010, "开始产生数据"),
    STOP_PRODUCE(2011, "停止产生数据");


    private final String INFO;
    private final Integer CODE;

    ThreadCodeEnum(Integer CODE, String INFO) {
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
