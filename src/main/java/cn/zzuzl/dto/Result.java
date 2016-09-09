package cn.zzuzl.dto;

import cn.zzuzl.common.Constants;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhanglei53 on 2016/7/28.
 */
public class Result<E> implements Serializable {
    private boolean success = true;
    private Map<String, Object> data = new HashMap<String, Object>();
    private String error;
    private List<E> list = new ArrayList<E>();
    private int totalPage;
    private int totalItem;
    private int page;
    private int pageSize;

    private Result() {}

    public Result(boolean success) {
        this.success = success;
    }

    public Result(int page,int pageSize) {
        this.page = page;
        this.pageSize = pageSize;
        resetPage();
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
        this.error = null;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public int getTotalItem() {
        return totalItem;
    }

    public void setTotalItem(int totalItem) {
        this.totalItem = totalItem;
        resetPage();
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
        resetPage();
    }

    public List<E> getList() {
        return list;
    }

    public void setList(List<E> list) {
        this.list = list;
    }

    private void resetPage() {
        if (page <= 0) {
            page = 1;
        }
        if (pageSize <= 0) {
            pageSize = Constants.SMALL_COUNT;
        }
        totalPage = totalItem % pageSize == 0 ? totalItem / pageSize : totalItem / pageSize + 1;
        if (page > totalPage) {
            page = totalPage;
        }
    }
}
