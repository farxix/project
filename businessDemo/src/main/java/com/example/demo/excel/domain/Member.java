package com.example.demo.excel.domain;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.example.demo.excel.config.GenderConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 购物会员
 * Created by macro on 2021/10/12.
 * @ExcelProperty：核心注解，value属性可用来设置表头名称，converter属性可以用来设置类型转换器；
 * @ColumnWidth：用于设置表格列的宽度；
 * @DateTimeFormat：用于设置日期转换格式。
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Member {
    @ExcelProperty("ID")
    @ColumnWidth(10)
    private Long id;
    @ExcelProperty("用户名")
    @ColumnWidth(20)
    private String username;
    @ExcelIgnore
    private String password;
    @ExcelProperty("昵称")
    @ColumnWidth(20)
    private String nickname;
    @ExcelProperty("出生日期")
    @ColumnWidth(20)
    @DateTimeFormat("yyyy-MM-dd")
    private Date birthday;
    @ExcelProperty("手机号")
    @ColumnWidth(20)
    private String phone;
    @ExcelIgnore
    private String icon;
    @ExcelProperty(value = "性别", converter = GenderConverter.class)
    @ColumnWidth(10)
    private Integer gender;
}
