package cn.zzuzl.service.impl;

import cn.zzuzl.dao.ProjectDao;
import cn.zzuzl.dto.Result;
import cn.zzuzl.model.Item;
import cn.zzuzl.model.Project;
import cn.zzuzl.model.query.ProjectQuery;
import cn.zzuzl.service.ProjectService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/11.
 */
@Service("projectService")
public class ProjectServiceImpl implements ProjectService {
    @Resource
    private ProjectDao projectDao;

    public Result<Project> searchProject(ProjectQuery query) {
        Result<Project> result = new Result<Project>(0, 0);
        List<Project> list = projectDao.searchProject(query);
        result.setList(list);
        if (list != null) {
            result.setTotalItem(list.size());
        }
        return result;
    }

    public Result<Project> searchProjectWithItems(ProjectQuery query) {
        Result<Project> result = new Result<Project>(0, 0);
        List<Project> list = projectDao.searchProject(query);
        result.setList(list);
        if (list != null) {
            for (Project project : list) {
                project.setItemList(projectDao.getItems(project.getId()));
            }
            result.setTotalItem(list.size());
        }
        return result;
    }

    public Project getById(Integer id) {
        Project project = projectDao.getById(id);
        if (project != null) {
            project.setItemList(projectDao.getItems(project.getId()));
        }
        return project;
    }

    @Transactional(rollbackFor = Exception.class)
    public Result insertProject(Project project) {
        Result result = new Result(true);
        if (project.getMinScore() == null || project.getMaxScore() == null
                || project.getMinScore() > project.getMaxScore()) {
            result.setSuccess(false);
            result.setError("分值设置有误");
        } else if (projectDao.insertProject(project) < 1) {
            result.setSuccess(false);
            result.setError("添加失败");
        } else {
            // 准备数据，插入items
            List<Item> items = project.getItemList();
            if (items != null && items.size() > 0) {
                for (Item item : items) {
                    if (item.getMinScore() == null || item.getMaxScore() == null
                            || item.getMinScore() > item.getMaxScore()) {
                        throw new RuntimeException("分值设置有误");
                    }
                    item.setProject(project);
                    item.setOperator(project.getOperator());
                }
                if (projectDao.batchInsertItem(items) < items.size()) {
                    throw new RuntimeException("添加失败");
                }
            }
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public Result updateProject(Project project) {
        Result result = new Result(true);

        if (project.getMinScore() == null || project.getMaxScore() == null
                || project.getMinScore() > project.getMaxScore()) {
            result.setSuccess(false);
            result.setError("分值设置有误");
        } else if (projectDao.updateProject(project) < 1) {
            result.setSuccess(false);
            result.setError("修改失败");
        } else {
            // 准备数据，更新、插入、删除items
            List<Item> items = project.getItemList();
            List<Integer> ids = null;
            if (items != null && items.size() > 0) {
                ids = new ArrayList<Integer>();
                List<Item> insertList = new ArrayList<Item>();
                for (Item item : items) {
                    if (item.getMinScore() == null || item.getMaxScore() == null
                            || item.getMinScore() > item.getMaxScore()) {
                        throw new RuntimeException("分值设置有误");
                    }
                    item.setProject(project);
                    item.setOperator(project.getOperator());
                    if (item.getId() == null) {
                        insertList.add(item);
                    } else if (projectDao.updateItem(item) != 1) {
                        throw new RuntimeException("更新失败");
                    }
                    ids.add(item.getId());
                }
                if (projectDao.batchInsertItem(insertList) < insertList.size()) {
                    throw new RuntimeException("更新失败");
                }
            }

            // 删除不存在的item
            projectDao.updateItemInvalid(ids, project.getId());
        }
        return result;
    }

    public Result updateInvalid(Integer id) {
        Result result = new Result(true);
        if (projectDao.updateInvalid(id) < 1) {
            result.setSuccess(false);
            result.setError("删除失败");
        }
        return result;
    }
}
