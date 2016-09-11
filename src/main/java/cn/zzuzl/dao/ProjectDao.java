package cn.zzuzl.dao;

import cn.zzuzl.model.Project;
import cn.zzuzl.model.query.ProjectQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2016/9/11.
 */
public interface ProjectDao {
    List<Project> searchProject(ProjectQuery query);

    Project getById(@Param("id") Integer id);

    int insertProject(Project project);

    int updateProject(Project project);

    int updateInvalid(@Param("id") Integer id);
}
