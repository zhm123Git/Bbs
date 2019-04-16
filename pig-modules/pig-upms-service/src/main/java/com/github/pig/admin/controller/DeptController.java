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
import com.github.pig.admin.common.util.BeanCopierUtils;
import com.github.pig.admin.model.dto.DeptDTO;
import com.github.pig.admin.model.dto.DeptTree;
import com.github.pig.admin.model.entity.SysDept;
import com.github.pig.admin.service.SysDeptService;
import com.github.pig.common.constant.CommonConstant;
import com.github.pig.common.util.R;
import com.github.pig.common.web.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 部门管理 前端控制器
 * </p>
 *
 * @author lengleng
 * @since 2018-01-20
 */
@Api(value = "部门",description = "部门")
@RestController
@RequestMapping("/dept")
public class DeptController extends BaseController {
    @Autowired
    private SysDeptService sysDeptService;

    /**
     * 通过ID查询
     *
     * @param id ID
     * @return SysDept
     */
    @GetMapping("/{id}")
    public SysDept get(@PathVariable Integer id) {
        return sysDeptService.selectById(id);
    }

    /**
     * 通过主键获取部门信息
     * @param deptDTO
     * @return
     */
    @ApiOperation(value = "通过主键获取部门信息,例如:{\"deptId\": 1}",notes = "返回部门信息")
    @ApiImplicitParam(name = "deptDTO", value = "", required = true, dataType = "DeptDTO")
    @PostMapping("/getDeptById")
    public R<SysDept> getDeptById(@RequestBody DeptDTO deptDTO){
        return new R<>(sysDeptService.selectById(deptDTO.getDeptId()));
    }


    /**
     * 返回树形菜单集合
     *
     * @return 树形菜单
     */
    @GetMapping(value = "/tree")
    public List<DeptTree> getTree() {
        SysDept condition = new SysDept();
        condition.setDelFlag(CommonConstant.STATUS_NORMAL);
        return sysDeptService.selectListTree(new EntityWrapper<>(condition));
    }




    /**
     * 返回树形菜单集合
     *
     * @return 树形菜单
     */
    @ApiOperation(value = "获取部门树形结构",notes = "返回部门树")
    @PostMapping(value = "/getDeptTree")
    public R<List<DeptTree>> getDeptTree() {
        SysDept condition = new SysDept();
        condition.setDelFlag(CommonConstant.STATUS_NORMAL);
        return new R<>(sysDeptService.selectListTree(new EntityWrapper<>(condition)));
    }


    /**
     * 添加
     *
     * @param sysDept 实体
     * @return success/false
     */
    @PostMapping
    public Boolean add(@RequestBody SysDept  sysDept) {
        return sysDeptService.insertDept(sysDept);
    }

    /**
     * 添加
     *
     * @param deptDTO
     * @return success/false
     */
    @ApiOperation(value = "添加部门信息,例如:{\n" +
            "  \"delFlag\": \"0\",\n" +
            "  \"name\": \"部门名称\",\n" +
            "  \"parentId\": 1\n" +
            "}",notes = "返回true,false")
    @ApiImplicitParam(name = "deptDTO", value = "", required = true, dataType = "DeptDTO")
    @PostMapping("/addDept")
    public R<Boolean> addDept(@RequestBody DeptDTO  deptDTO) {
        SysDept sysDept = new SysDept();
        BeanCopierUtils.copy(deptDTO,sysDept);
        return new R<>(sysDeptService.insertDept(sysDept));
    }



    /**
     * 删除
     *
     * @param id ID
     * @return success/false
     */
    @DeleteMapping("/{id}")
    public Boolean delete(@PathVariable Integer id) {
        return sysDeptService.deleteDeptById(id);
    }

    /**
     * 通过部门Id删除当前部门以及其子孙部门
     * @param deptDTO
     * @return
     */
    @ApiOperation(value = "删除部门信息,例如:",notes = "返回true,false")
    @ApiImplicitParam(name = "deptDTO", value = "", required = true, dataType = "DeptDTO")
    @PostMapping("/deleteDept")
    public R<Boolean> deleteDept(@RequestBody DeptDTO  deptDTO) {
        return new R<>(sysDeptService.deleteDeptById(deptDTO.getDeptId()));
    }


    /**
     * 编辑
     *
     * @param sysDept 实体
     * @return success/false
     */
    @PutMapping
    public Boolean edit(@RequestBody SysDept sysDept) {
        sysDept.setUpdateTime(new Date());
        return sysDeptService.updateDeptById(sysDept);
    }


    /**
     * 修改
     *
     * @param deptDTO
     * @return success/false
     */
    @ApiOperation(value = "修改部门信息,例如:{\n" +
            "  \"deptId\": 1,\n" +
            "  \"name\": \"山东农信1\"\n" +
            "}",notes = "返回true,false")
    @ApiImplicitParam(name = "deptDTO", value = "", required = true, dataType = "DeptDTO")
    @PostMapping("/editDept")
    public R<Boolean> editDept(@RequestBody DeptDTO  deptDTO) {
        SysDept sysDept = new SysDept();
        BeanCopierUtils.copy(deptDTO,sysDept);
        return new R<>(sysDeptService.updateDeptById(sysDept));
    }

}
