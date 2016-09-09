package cn.zzuzl.controller;

import cn.zzuzl.common.Constants;
import cn.zzuzl.common.util.NetUtil;
import cn.zzuzl.dto.Result;
import cn.zzuzl.model.Student;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("students")
public class StudentController {
    @Resource
    private NetUtil netUtil;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public Result listStudent(@RequestParam(value = "_page", required = false, defaultValue = "1") Integer _page,
                              @RequestParam(value = "_perPage", required = false, defaultValue = "20") Integer _perPage,
                              @RequestParam(value = "_sortDir", required = false, defaultValue = "asc") String _sortDir,
                              @RequestParam(value = "_sortField", required = false, defaultValue = "schoolNum") String _sortField) {
        List<Student> studentList = new ArrayList<Student>();
        studentList.add(new Student("a", "b", "c", "d", "d"));
        studentList.add(new Student("1", "2", "3", "4", "5"));
        studentList.add(new Student("h", "ju", "i", "o", "p"));
        Result<Student> result = new Result<Student>(_page, _perPage);
        result.setList(studentList);
        return result;
    }

    @RequestMapping(value = "/{schoolNum}", method = RequestMethod.GET)
    @ResponseBody
    public Result getById(@PathVariable("schoolNum") String schoolNum) {
        Result result = new Result(true);
        result.getData().put(Constants.STU, new Student());
        return result;
    }
}
