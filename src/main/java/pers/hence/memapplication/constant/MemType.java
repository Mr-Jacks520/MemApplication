package pers.hence.memapplication.constant;

/**
 * @author https://github.com/Mr-Jacks520
 * @date 2023/3/28 10:27
 * @description 记忆内容类型
 */
public enum MemType {

    /**
     * 记忆内容支持文件格式
     */
    TXT(0, "TXT"),
    PDF(1, "PDF"),
    WORD(2, "WORD"),
    IMAGE(3, "IMAGE"),
    MUSIC(4, "MUSIC")
    ;

    private int type;

    private String path;

    MemType(int type, String path) {
        this.type = type;
        this.path = path;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
