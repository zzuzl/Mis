package cn.zzuzl.dto;

/**
 * Created by Administrator on 2016/9/24.
 */
public class RecordCountVO {
    private int recordCount;
    private int personCount;

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    public int getPersonCount() {
        return personCount;
    }

    public void setPersonCount(int personCount) {
        this.personCount = personCount;
    }

    public void incPersonCount() {
        personCount++;
    }

    public void addRecordCount(int n) {
        recordCount += n;
    }
}
