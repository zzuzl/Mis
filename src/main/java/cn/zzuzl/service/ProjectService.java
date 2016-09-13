package cn.zzuzl.service;

import cn.zzuzl.dto.Result;
import cn.zzuzl.model.Project;
import cn.zzuzl.model.query.ProjectQuery;

/**
 * Created by Administrator on 2016/9/11.
 */
public interface ProjectService {
    Result<Project> searchProject(ProjectQuery query);

    Result<Project> searchProjectWithItems(ProjectQuery query);

    Project getById(Integer id);

    Result insertProject(Project project);

    Result updateProject(Project project);

    Result updateInvalid(Integer id);
}
