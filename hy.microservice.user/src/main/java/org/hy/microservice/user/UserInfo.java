package org.hy.microservice.user;

import org.hy.common.Date;
import org.hy.microservice.user.user.UserSSO;

import com.fasterxml.jackson.annotation.JsonFormat;





/**
 * 用户信息
 *
 * @author      ZhengWei(HY)
 * @createDate  2021-08-08
 * @version     v1.0
 */
public class UserInfo extends UserSSO
{

    private static final long serialVersionUID = -3261003581839189606L;
    
    /** 密码 */
    private String  password;
    
    /** 密码过期时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date    pwdExpireTime;
    
    /** 身份证号 */
    private String  cardID;
    
    /** 入职日期 */
    @JsonFormat(pattern = "yyyy-MM-dd" ,timezone = "GMT+8")
    private Date    startDate;
    
    /** 转正日期 */
    @JsonFormat(pattern = "yyyy-MM-dd" ,timezone = "GMT+8")
    private Date    passDate;
    
    /** 离职日期 */
    @JsonFormat(pattern = "yyyy-MM-dd" ,timezone = "GMT+8")
    private Date    endDate;
    
    /** 是否入职（1：已入职） */
    private Integer isStart;
    
    /** 是否转正（1：已转正） */
    private Integer isPass;
    
    /** 是否离职（1：已离职） */
    private Integer isEnd;
    
    /** 删除标记（1：已删除） */
    private Integer isDel;
    
    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date    createTime;
    
    /** 修改时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date    updateTime;

    
    
    /**
     * 获取：应用appKey
     */
    public String getAppID()
    {
        return super.getAppKey();
    }

    
    /**
     * 设置：应用AppID
     * 
     * @param i_AppID
     */
    public void setAppID(String i_AppID)
    {
        super.setAppKey(i_AppID);
    }
    
    
    /**
     * 获取：身份证号
     */
    public String getCardID()
    {
        return cardID;
    }

    
    /**
     * 设置：身份证号
     * 
     * @param cardID
     */
    public void setCardID(String cardID)
    {
        this.cardID = cardID;
    }

    
    /**
     * 获取：创建时间
     */
    public Date getCreateTime()
    {
        return createTime;
    }

    
    /**
     * 设置：创建时间
     * 
     * @param createTime
     */
    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    
    /**
     * 获取：修改时间
     */
    public Date getUpdateTime()
    {
        return updateTime;
    }

    
    /**
     * 设置：修改时间
     * 
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime)
    {
        this.updateTime = updateTime;
    }


    /**
     * 获取：入职日期
     */
    public Date getStartDate()
    {
        return startDate;
    }


    /**
     * 设置：入职日期
     * 
     * @param
     */
    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }


    /**
     * 获取：转正日期
     */
    public Date getPassDate()
    {
        return passDate;
    }


    /**
     * 设置：转正日期
     * 
     * @param
     */
    public void setPassDate(Date passDate)
    {
        this.passDate = passDate;
    }


    /**
     * 获取：离职日期
     */
    public Date getEndDate()
    {
        return endDate;
    }


    /**
     * 设置：离职日期
     * 
     * @param
     */
    public void setEndDate(Date endDate)
    {
        this.endDate = endDate;
    }


    /**
     * 获取：是否入职（1：已入职）
     */
    public Integer getIsStart()
    {
        return isStart;
    }


    /**
     * 设置：是否入职（1：已入职）
     * 
     * @param
     */
    public void setIsStart(Integer isStart)
    {
        this.isStart = isStart;
    }


    /**
     * 获取：是否转正（1：已转正）
     */
    public Integer getIsPass()
    {
        return isPass;
    }


    /**
     * 设置：是否转正（1：已转正）
     * 
     * @param
     */
    public void setIsPass(Integer isPass)
    {
        this.isPass = isPass;
    }


    /**
     * 获取：是否离职（1：已离职）
     */
    public Integer getIsEnd()
    {
        return isEnd;
    }


    /**
     * 设置：是否离职（1：已离职）
     * 
     * @param
     */
    public void setIsEnd(Integer isEnd)
    {
        this.isEnd = isEnd;
    }


    /**
     * 获取：删除标记（1：已删除）
     */
    public Integer getIsDel()
    {
        return isDel;
    }


    /**
     * 设置：删除标记（1：已删除）
     * 
     * @param
     */
    public void setIsDel(Integer isDel)
    {
        this.isDel = isDel;
    }


    /**
     * 获取：密码
     */
    public String getPassword()
    {
        return password;
    }


    /**
     * 设置：密码
     * 
     * @param
     */
    public void setPassword(String password)
    {
        this.password = password;
    }


    /**
     * 获取：密码过期时间
     */
    public Date getPwdExpireTime()
    {
        return pwdExpireTime;
    }


    /**
     * 设置：密码过期时间
     * 
     * @param
     */
    public void setPwdExpireTime(Date pwdExpireTime)
    {
        this.pwdExpireTime = pwdExpireTime;
    }
    
}
