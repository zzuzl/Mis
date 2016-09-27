package cn.zzuzl.service.impl;

import cn.zzuzl.dao.ProjectDao;
import cn.zzuzl.dto.Result;
import cn.zzuzl.model.Item;
import cn.zzuzl.model.Project;
import cn.zzuzl.model.query.ItemQuery;
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

        List<Integer> projectIdList = new ArrayList<Integer>();
        ItemQuery itemQuery = new ItemQuery();
        itemQuery.setProjectIds(projectIdList);
        if (list != null) {
            for (Project project : list) {
                projectIdList.clear();
                projectIdList.add(project.getId());
                project.setItemList(projectDao.searchItems(itemQuery));
            }
            result.setTotalItem(list.size());
        }
        return result;
    }

    public Project getById(Integer id) {
        Project project = projectDao.getById(id);
        if (project != null) {
            List<Integer> projectIdList = new ArrayList<Integer>();
            projectIdList.add(id);
            ItemQuery itemQuery = new ItemQuery();
            itemQuery.setProjectIds(projectIdList);
            project.setItemList(projectDao.searchItems(itemQuery));
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
        } else {
            projectDao.insertProject(project);
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
                projectDao.batchInsertItem(items);
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
        } else {
            projectDao.updateProject(project);
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
                    } else {
                        projectDao.updateItem(item);
                    }
                    ids.add(item.getId());
                }
                projectDao.batchInsertItem(insertList);
            }

            // 删除不存在的item
            projectDao.updateItemInvalid(ids, project.getId());
        }
        return result;
    }

    public Result updateInvalid(Integer id) {
        Result result = new Result(true);
        projectDao.updateInvalid(id);
        return result;
    }
}
