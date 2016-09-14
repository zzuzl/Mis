package cn.zzuzl.common.enums;

public enum SortDirEnum {
    ASC("ASC"),
    DESC("DESC");

    private String title;

    private SortDirEnum(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
