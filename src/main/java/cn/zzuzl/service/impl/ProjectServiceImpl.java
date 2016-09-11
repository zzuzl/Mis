package cn.zzuzl.service.impl;

import cn.zzuzl.dao.ProjectDao;
import cn.zzuzl.dto.Result;
import cn.zzuzl.model.Project;
import cn.zzuzl.model.query.ProjectQuery;
import cn.zzuzl.service.ProjectService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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

    public Project getById(Integer id) {
        return projectDao.getById(id);
    }

    public Result insertProject(Project project) {
        Result result = new Result(true);
        if (project.getMinScore() == null || project.getMaxScore() == null
                || project.getMinScore() > project.getMaxScore()) {
            result.setSuccess(false);
            result.setError("分值设置有误");
        } else if (projectDao.insertProject(project) < 1) {
            result.setSuccess(false);
            result.setError("添加失败");
        }
        return result;
    }

    public Result updateProject(Project project) {
        Result result = new Result(true);
        if (projectDao.updateProject(project) < 1) {
            result.setSuccess(false);
            result.setError("修改失败");
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
