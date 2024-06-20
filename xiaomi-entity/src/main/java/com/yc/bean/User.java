package com.yc.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user")
public class User  implements Serializable {
    private static final long serialVersionUID=-2905481818647723754L;


    @TableId(type = IdType.AUTO)
    private Integer id;
    private String username;
    private String tel;
    private String pwd;
    @TableField(value = "regDate")
    private String regDate;  //datetime类型
    private String email;
}
