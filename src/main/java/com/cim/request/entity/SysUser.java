package com.cim.request.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

// 用户实体
@TableName(value = "sys_user")
@Data
public class SysUser extends BaseEntity implements Serializable {
    public static final long serialVersionUid = 1L;

    private String name;

    private String username;

    private String password;

    private String phone;

    private LocalDateTime loginDate;

    private String sex;

    private Integer status;
}
