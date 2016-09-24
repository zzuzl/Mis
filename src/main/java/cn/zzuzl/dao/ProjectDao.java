package cn.zzuzl.dao;

import cn.zzuzl.model.Item;
import cn.zzuzl.model.Project;
import cn.zzuzl.model.query.ItemQuery;
import cn.zzuzl.model.query.ProjectQuery;
import org.apache.ibatis.annotations.Param;

import javax.validation.constraints.NotNull;
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

    List<Item> searchItems(ItemQuery query);

    int batchInsertItem(@NotNull @Param("items") List<Item> items);

    int updateItemInvalid(@Param("ids") List<Integer> ids,
                          @Param("projectId") Integer projectId);

    int updateItem(Item item);

    Item getItemById(@Param("id") Integer id);
}
