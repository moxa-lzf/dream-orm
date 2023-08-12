package com.dream.test.base.table;

import com.dream.system.annotation.Column;
import com.dream.system.annotation.Id;
import com.dream.system.annotation.Table;
import com.dream.template.annotation.validate.NotBlank;

import java.sql.Types;

@Table("sys_user")
public class SysUser {
    @Id
    @Column("id")
    private Long id;
    @Id
    @Column("id2")
    private Long id2;
    @NotBlank(msg = "用户名不能为空")
    @Column(value = "name", jdbcType = Types.VARCHAR)
    private String name;
    @Column("age")
    private Integer age;
    @Column("email")
    private String email;
    @Column("tenant_id")
    private Integer tenantId;
    @Column("dept_id")
    private Integer deptId;
    @Column("del_flag")
    private Integer delFlag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId2() {
        return id2;
    }

    public void setId2(Long id2) {
        this.id2 = id2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getTenantId() {
        return tenantId;
    }

    public void setTenantId(Integer tenantId) {
        this.tenantId = tenantId;
    }

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }
}
