package com.yc.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_com")
public class Com   implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String content;
    private String username;
    private Integer userid;
    private Integer foreignid;
    private Integer pid;
    private String target;
    private String createtime;   //数据库中是datetime
}
