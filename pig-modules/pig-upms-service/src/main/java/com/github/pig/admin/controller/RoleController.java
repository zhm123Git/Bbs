/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */

package com.github.pig.admin.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.github.pig.admin.common.PageParams;
import com.github.pig.admin.model.dom.RoleDom;
import com.github.pig.admin.model.dto.DeptDTO;
import com.github.pig.admin.model.dto.RoleDTO;
import com.github.pig.admin.model.dto.RoleMenuDTo;
import com.github.pig.admin.model.entity.SysRole;
import com.github.pig.admin.service.SysRoleDeptService;
import com.github.pig.admin.service.SysRoleMenuService;
import com.github.pig.admin.service.SysRoleService;
import com.github.pig.admin.service.SysUserRoleService;
import com.github.pig.common.constant.CommonConstant;
import com.github.pig.common.util.ListResponse;
import com.github.pig.common.util.Query;
import com.github.pig.common.util.R;
import com.github.pig.common.web.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author lengleng
 * @date 2017/11/5
 */
@Api(value = "角色",description="角色")
@RestController
@RequestMapping("/role")
public class RoleController extends BaseController {
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysRoleMenuService sysRoleMenuService;
    @Autowired
    private SysUserRoleService sysUserRoleService;
    @Autowired
    private SysRoleDeptService sysRoleDeptService;


//    /**
//     * 通过ID查询角色信息
//     *
//     * @param id ID
//     * @return 角色信息
//     */
//    @GetMapping("/{id}")
//    public SysRole role(@PathVariable Integer id) {
//        return sysRoleService.selectById(id);
//    }


    /**
     * 通过ID查询角色信息
     *
     * @param roleDTO
     * @return 角色信息
     */
    @ApiOperation(value = "通过主键获取角色信息,例如:{\"roleId\": 16}",notes = "返回角色信息")
    @ApiImplicitParam(name = "roleDTO", value = "", required = true, dataType = "RoleDTO")
    @PostMapping("/getroleById")
    public SysRole getroleById(@RequestBody RoleDTO roleDTO) {
        return sysRoleService.selectById(roleDTO.getRoleId());
    }


    /**
     * 添加角色
     *
     * @param roleDto 角色信息
     * @return success、false
     */
    @ApiOperation(value = "添加角色信息,例如:{\n" +
            "  \"delFlag\": \"0\",\n" +
            "  \"roleCode\": \"测试服浏览\",\n" +
            "  \"roleDesc\": \"角色描述\",\n" +
            "  \"roleName\": \"角色名称\"\n" +
            "}",notes = "返回true,false")
    @ApiImplicitParam(name = "roleDto", value = "", required = true, dataType = "RoleDTO")
    @PostMapping("/addRole")
    public R<Boolean> addRole(@RequestBody RoleDTO roleDto) {
        return new R<>(sysRoleService.insertRole(roleDto));
    }

    /**
     * 修改角色
     *
     * @param roleDto 角色信息
     * @return success/false
     */
    @ApiOperation(value = "修改角色信息,例如:{\n" +
            "  \"roleId\": 16,\n" +
            "  \"roleName\": \"gfgdfgdfgdfg\"\n" +
            "}",notes = "返回true,false")
    @ApiImplicitParam(name = "roleDto", value = "", required = true, dataType = "RoleDTO")
    @PostMapping("/roleUpdate")
    public R<Boolean> roleUpdate(@RequestBody RoleDTO roleDto) {
        return new R<>(sysRoleService.updateRoleById(roleDto));
    }

    @ApiOperation(value = "删除角色信息,例如:{\"roleId\": 15}",notes = "返回true,false")
    @ApiImplicitParam(name = "roleDto", value = "", required = true, dataType = "RoleDTO")
    @PostMapping("/roleDel")
    public R<Boolean> roleDel(@RequestBody RoleDTO roleDTO) {
        Integer id  = roleDTO.getRoleId();
        SysRole sysRole = sysRoleService.selectById(id);
        sysRole.setDelFlag(CommonConstant.STATUS_DEL);
        //删除中间表数据,role_user,role_dept,role_menu
        sysRoleMenuService.deleteByRoleId(id);
        sysUserRoleService.deleteByRoleId(id);
        sysRoleDeptService.deleteByRoleId(id);
        return new R<>(sysRoleService.updateById(sysRole));
    }

    /**
     * 获取角色列表
     *
     * @param deptId 部门ID
     * @return 角色列表
     */
    @GetMapping("/roleList/{deptId}")
    public List<SysRole> roleList(@PathVariable Integer deptId) {
        return sysRoleService.selectListByDeptId(deptId);

    }

    /**
     * 根据部门Id获取角色列表
     * @param deptDTO
     * @return
     */
    @ApiOperation(value = "根据部门获取角色信息,例如:{\"deptId\": 1}",notes = "返回角色信息")
    @ApiImplicitParam(name = "deptDTO", value = "", required = true, dataType = "DeptDTO")
    @PostMapping("/getRoleListByDeptId")
    public R<List<SysRole>> getRoleListByDeptId(@RequestBody DeptDTO deptDTO){
        return  new R<>(sysRoleService.selectListByDeptId(deptDTO.getDeptId()));
    }


    /**
     * 分页查询角色信息
     *
     * @param params 分页对象
     * @return 分页对象
     */
    @RequestMapping("/rolePage")
    public Page rolePage(@RequestParam Map<String, Object> params) {
        params.put(CommonConstant.DEL_FLAG, CommonConstant.STATUS_NORMAL);
        return sysRoleService.selectwithDeptPage(new Query<>(params), new EntityWrapper<>());
    }



    /**
     * 查询角色列表
     *
     * @param roleDom
     * @return UseVo 对象
     */
    @ApiOperation(value = "查询角色列表,例子:{\"limit\": 10,\"offset\": 0}",notes = "返回角色列表")
    @ApiImplicitParam(name = "roleDom", value = "角色", required = true, dataType = "RoleDom")
    @PostMapping("/findRolePage")
    public R<ListResponse<RoleDTO>> findRolePage(@RequestBody RoleDom roleDom) {
        PageParams pageParams = new PageParams();
        pageParams.setLimit(roleDom.getLimit());
        pageParams.setOffset(roleDom.getOffset());
        Pair<List<RoleDTO>, Long> pair = sysRoleService.getAllUsers(roleDom,pageParams);
        ListResponse<RoleDTO> response = ListResponse.build(pair.getKey(), pair.getValue());
        return new R<>(response);
    }



    /**
     * 更新角色菜单
     *
     * @param roleId  角色ID
     * @param menuIds 菜单结合
     * @return success、false
     */
    @PutMapping("/roleMenuUpd")
    public R<Boolean> roleMenuUpd(Integer roleId, @RequestParam(value = "menuIds", required = false) String menuIds) {
        SysRole sysRole = sysRoleService.selectById(roleId);
        return new R<>(sysRoleMenuService.insertRoleMenus(sysRole.getRoleCode(), roleId, menuIds));
    }




    /**
     * 更新角色菜单
     *
     * @param roleMenuDTo
     * @return success、false
     */
    @ApiOperation(value = "更新角色的菜单信息,例如:1",notes = "返回true,false")
    @ApiImplicitParam(name = "roleMenuDTo", value = "", required = true, dataType = "RoleMenuDTo")
    @PostMapping("/roleMenuUpdate")
    public R<Boolean> roleMenuUpdate(@RequestBody RoleMenuDTo roleMenuDTo) {
        SysRole sysRole = sysRoleService.selectById(roleMenuDTo.getRoleId());
        return new R<>(sysRoleMenuService.insertRoleMenus(sysRole.getRoleCode(), roleMenuDTo.getRoleId(), roleMenuDTo.getMenuIds()));
    }

}
