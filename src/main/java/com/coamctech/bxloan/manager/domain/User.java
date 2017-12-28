package com.coamctech.bxloan.manager.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2017/11/11.
 */
@Entity
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String userName;
    private String nickName;
    private String password;
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date birthday;
    private String email;
    private String officePhone;
    private String telephone;
    private Long creator;
    private Integer status;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;
    private String sign;
    private Long departmentId;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date startTime;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date endTime;
    private Integer ifStoreViewHitory;//是否存储浏览历史，1：是，0：否
    @Transient
    private String token;

    public Integer getIfStoreViewHitory() {
        return ifStoreViewHitory;
    }

    public void setIfStoreViewHitory(Integer ifStoreViewHitory) {
        this.ifStoreViewHitory = ifStoreViewHitory;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOfficePhone() {
        return officePhone;
    }

    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Long getCreator() {
        return creator;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", nickName='" + nickName + '\'' +
                ", password='" + password + '\'' +
                ", birthday=" + birthday +
                ", email='" + email + '\'' +
                ", officePhone='" + officePhone + '\'' +
                ", telephone='" + telephone + '\'' +
                ", creator=" + creator +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", sign='" + sign + '\'' +
                ", departmentId=" + departmentId +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", ifStoreViewHitory=" + ifStoreViewHitory +
                ", token='" + token + '\'' +
                '}';
    }
}