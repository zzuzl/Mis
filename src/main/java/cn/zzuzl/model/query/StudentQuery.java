package cn.zzuzl.model.query;

/**
 * Created by Administrator on 2016/9/10.
 */
public class StudentQuery {
    private String _page;
    private String _perPage;
    private String _sortDir;
    private String _sortField;
    private String schoolNum;
    private String name;

    public String get_page() {
        return _page;
    }

    public void set_page(String _page) {
        this._page = _page;
    }

    public String get_perPage() {
        return _perPage;
    }

    public void set_perPage(String _perPage) {
        this._perPage = _perPage;
    }

    public String get_sortDir() {
        return _sortDir;
    }

    public void set_sortDir(String _sortDir) {
        this._sortDir = _sortDir;
    }

    public String get_sortField() {
        return _sortField;
    }

    public void set_sortField(String _sortField) {
        this._sortField = _sortField;
    }

    public String getSchoolNum() {
        return schoolNum;
    }

    public void setSchoolNum(String schoolNum) {
        this.schoolNum = schoolNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
