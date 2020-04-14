package wxs.admin.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import wxs.admin.doman.service.RoleService;
import wxs.admin.vo.role.RoleReqVo;
import wxs.common.response.AppResult;
import wxs.common.response.Result;

/**
 * @author : imperater
 * @date : 2020/4/7
 */
@Controller
@RequestMapping("/role")
@Slf4j
public class RoleController {

    @Autowired
    private RoleService roleService;

    @RequestMapping("/getRoleList")
    @ResponseBody
    public AppResult getRoleList(@RequestBody RoleReqVo roleReqVo){
        return Result.success(roleService.getRoleListByVo(roleReqVo));
    }

    /**
     * 新增角色
     * @param roleReqVo
     * @return
     */
    @RequestMapping("/saveRole")
    @ResponseBody
    public AppResult saveRole(@RequestBody RoleReqVo roleReqVo){
        try { ;
            roleService.saveRole(roleReqVo);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("添加角色报错:{}",e.getMessage());
            return Result.failed("添加失败");
        }
        return Result.success("添加成功");
    }


    /**
     * 修改角色
     * @param roleReqVo
     * @return
     */
    @RequestMapping("/updateRole")
    @ResponseBody
    public AppResult updateRole(@RequestBody RoleReqVo roleReqVo){
        try { ;
            roleService.updateRole(roleReqVo);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("修改角色报错:{}",e.getMessage());
            return Result.failed("修改失败");
        }
        return Result.success("修改成功");
    }


    /**
     * 根据roleId获取详情
     * @param roleId
     * @return
     */
    @RequestMapping("/getRoleInfoByRoleId")
    @ResponseBody
    public AppResult getRoleInfoByRoleId(@RequestBody String  roleId){
        return Result.success(roleService.getRoleInfoByRoleId(roleId));
    }
}
