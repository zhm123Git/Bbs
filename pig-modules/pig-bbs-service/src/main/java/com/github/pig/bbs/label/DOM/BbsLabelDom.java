package com.github.pig.bbs.label.DOM;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.github.pig.bbs.label.entity.BbsLabel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author lengleng
 * @since 2018-11-24
 */
@Data
public class BbsLabelDom extends BbsLabel{

    private Integer pageNum; //当前页
    private Integer pageSize;//每页多少

}
