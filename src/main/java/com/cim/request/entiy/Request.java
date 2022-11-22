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

    private String createTime;

    private String background;

    private String description;

    private String requestValue;

    private String department;

    private String requestPerson;

    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    private String requestTime;

    private Integer urgent;

    private Integer progress;

    private String startTime;

    private String endTime;

    private Integer completeStatus;

    private Integer takeStatus;

    private Integer statusDelete;
}
