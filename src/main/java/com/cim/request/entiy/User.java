package com.cim.request.entiy;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import org.springframework.context.annotation.Bean;

import java.io.Serializable;
import java.time.LocalDateTime;

// 用户实体
@Data
public class User implements Serializable {
    public static final long serialVersionUid = 1L;

    private Long id;

    private String name;

    private String username;

    private String password;

    private String phone;

    private String sex;

    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
