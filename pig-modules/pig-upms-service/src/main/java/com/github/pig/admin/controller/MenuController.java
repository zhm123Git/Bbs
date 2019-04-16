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
import com.github.pig.admin.common.util.TreeUtil;
import com.github.pig.admin.model.dto.MenuDTO;
import com.github.pig.admin.model.dto.MenuTree;
import com.github.pig.admin.model.dto.UserDTO;
import com.github.pig.admin.model.entity.SysMenu;
import com.github.pig.admin.model.entity.SysRole;
import com.github.pig.admin.service.SysMenuService;
import com.github.pig.admin.service.SysRoleService;
import com.github.pig.admin.service.SysUserRoleService;
import com.github.pig.common.constant.CommonConstant;
import com.github.pig.common.util.R;
import com.github.pig.common.vo.MenuVO;
import com.github.pig.common.vo.UserVO;
import com.github.pig.common.web.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.License;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lengleng
 * @date 2017/10/31
 */
@Api(value = "菜单",description="菜单")
@RestController
@RequestMapping("/menu")
public class MenuController extends BaseController {
    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Autowired
    private SysRoleService sysRoleService;

    /**
     * 通过角色名称查询用户菜单
     *
     * @param role 角色名称
     * @return 菜单列表
     */
    @GetMapping("/findMenuByRole/{role}")
    public List<MenuVO> findMenuByRole(@PathVariable String role) {
        return sysMenuService.findMenuByRoleName(role);
    }

    /**
     * 返回当前用户的树形菜单集合
     *
     * @param userVO
     * @return 当前用户的树形菜单
     */
    @GetMapping(value = "/userMenu")
    public List<MenuTree> userMenu(UserVO userVO) {
        // 获取符合条件得菜单
        Set<MenuVO> all = new HashSet<>();
        userVO.getRoleList().forEach(role -> all.addAll(sysMenuService.findMenuByRoleName(role.getRoleCode())));

        List<MenuTree> menuTreeList = all.stream().filter(vo -> CommonConstant.MENU
                .equals(vo.getType()))
                .map(MenuTree::new)
                .sorted(Comparator.comparingInt(MenuTree::getSort))
                .collect(Collectors.toList());
        return TreeUtil.bulid(menuTreeList, -1);
    }



    /**
     * 返回当前用户的树形菜单集合
     *
     * @param
     * @return 当前用户的树形菜单
     */
    @ApiOperation(value = "通过用户主键获取用户拥有的菜单信息,例如:{\"userId\": 1}",notes = "返回当前用户的树形菜单集合")
    @ApiImplicitParam(name = "userDTO", value = "", required = true, dataType = "UserDTO")
    @PostMapping("/getuserMenu")
    public R<List<MenuTree>> getuserMenu(@RequestBody UserDTO userDTO) {
        UserVO userVO = new UserVO();
        List<SysRole> roleList=sysRoleService.getRoleListByUserId(userDTO.getUserId());
        List<com.github.pig.common.vo.SysRole> sysRoles = BeanCopierUtils.copyList(roleList, com.github.pig.common.vo.SysRole.class);
        userVO.setRoleList(sysRoles);
        // 获取符合条件得菜单
        Set<MenuVO> all = new HashSet<>();
        userVO.getRoleList().forEach(role -> all.addAll(sysMenuService.findMenuByRoleName(role.getRoleCode())));

        List<MenuTree> menuTreeList = all.stream().filter(vo -> CommonConstant.MENU
                .equals(vo.getType()))
                .map(MenuTree::new)
                .sorted(Comparator.comparingInt(MenuTree::getSort))
                .collect(Collectors.toList());
        return new R<>(TreeUtil.bulid(menuTreeList, -1));
    }



    /**
     * 返回树形菜单集合
     *
     * @return 树形菜单
     */
    @GetMapping(value = "/allTree")
    public List<MenuTree> getTree() {
        SysMenu condition = new SysMenu();
        condition.setDelFlag(CommonConstant.STATUS_NORMAL);
        return TreeUtil.bulidTree(sysMenuService.selectList(new EntityWrapper<>(condition)), -1);
    }


    /**
     * 返回树形菜单集合
     *
     * @return 树形菜单
     */
    @ApiOperation(value = "获取菜单信息",notes = "返回树形菜单集合")
//    @ApiImplicitParam(name = "", value = "", required = true, dataType = "")
    @PostMapping("/getAllTree")
    public R<List<MenuTree>> getAllTree() {
        SysMenu condition = new SysMenu();
        condition.setDelFlag(CommonConstant.STATUS_NORMAL);
        return new R<>(TreeUtil.bulidTree(sysMenuService.selectList(new EntityWrapper<>(condition)), -1));
    }


    /**
     * 返回角色的菜单集合
     *
     * @param roleName 角色名称
     * @return 属性集合
     */
    @GetMapping("/roleTree/{roleName}")
    public List<Integer> roleTree(@PathVariable String roleName) {
        List<MenuVO> menus = sysMenuService.findMenuByRoleName(roleName);
        List<Integer> menuList = new ArrayList<>();
        for (MenuVO menuVo : menus) {
            menuList.add(menuVo.getMenuId());
        }
        return menuList;
    }

    /**
     * 通过ID查询菜单的详细信息
     *
     * @param id 菜单ID
     * @return 菜单详细信息
     */
    @GetMapping("/{id}")
    public SysMenu menu(@PathVariable Integer id) {
        return sysMenuService.selectById(id);
    }

    /**
     * 通过ID查询菜单的详细信息
     *
     * @param menuDTO
     * @return 菜单详细信息
     */
    @ApiOperation(value = "通过菜单Id获取菜单信息",notes = "返回单条菜单信息")
    @ApiImplicitParam(name = "menuDTO", value = "", required = true, dataType = "MenuDTO")
    @PostMapping("/getMenuById")
    public R<SysMenu> getMenuById(@RequestBody MenuDTO menuDTO) {
        return new R<>(sysMenuService.selectById(menuDTO.getMenuId()));
    }

    /**
     * 新增菜单
     *
     * @param sysMenu 菜单信息
     * @return success/false
     */
    @PostMapping
    public R<Boolean> menu(@RequestBody SysMenu sysMenu) {
        return new R<>(sysMenuService.insert(sysMenu));
    }

    /**
     * 新增菜单
     *
     * @param menuDTO 菜单信息
     * @return success/false
     */
    @ApiOperation(value = "新增菜单信息",notes = "返回true,false")
    @ApiImplicitParam(name = "menuDTO", value = "", required = true, dataType = "MenuDTO")
    @PostMapping("/addMenu")
    public R<Boolean> addMenu(@RequestBody MenuDTO menuDTO) {
        SysMenu menu = new SysMenu();
        BeanCopierUtils.copy(menuDTO,menu);
        return new R<>(sysMenuService.insert(menu));
    }


    /**
     * 删除菜单
     *
     * @param id 菜单ID
     * @return success/false
     * TODO  级联删除下级节点
     */
    @DeleteMapping("/{id}")
    public R<Boolean> menuDel(@PathVariable Integer id) {
        return new R<>(sysMenuService.deleteMenu(id));
    }

    /**
     * 删除菜单
     *
     * @param menuDTO
     * @return success/false
     * TODO  级联删除下级节点
     */
    @ApiOperation(value = "删除菜单信息",notes = "返回true,false")
    @ApiImplicitParam(name = "menuDTO", value = "", required = true, dataType = "MenuDTO")
    @PostMapping("/deleteMenuById")
    public R<Boolean> deleteMenuById(@RequestBody MenuDTO menuDTO) {
        return new R<>(sysMenuService.deleteMenu(menuDTO.getMenuId()));
    }


    @PutMapping
    public R<Boolean> menuUpdate(@RequestBody SysMenu sysMenu) {
        return new R<>(sysMenuService.updateMenuById(sysMenu));
    }

    /**
     * 修改菜单信息
     * @param menuDTO
     * @return
     */
    @ApiOperation(value = "修改菜单信息",notes = "返回true,false")
    @ApiImplicitParam(name = "menuDTO", value = "", required = true, dataType = "MenuDTO")
    @PostMapping("/updateMenu")
    public R<Boolean> updateMenu(@RequestBody MenuDTO menuDTO) {
        SysMenu menu = new SysMenu();
        BeanCopierUtils.copy(menuDTO,menu);
        return new R<>(sysMenuService.updateMenuById(menu));
    }

}
