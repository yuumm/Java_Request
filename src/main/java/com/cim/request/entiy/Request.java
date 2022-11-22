package com.cim.request.entiy;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Request implements Serializable {

    public static final long serialVersionUid = 1L;

    private Long id;

    private String factory;

    private String systemUse;

    private LocalDateTime createTime;

    private String background;

    private String description;

    private String requestValue;

    private String department;

    private String requestPerson;

    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    private LocalDateTime requestTime;

    private Integer urgent;

    private Integer progress;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer completeStatus;

    private Integer takeStatus;
}
