package org.hy.microservice.user.account;

import org.hy.common.Date;
import org.hy.common.xml.SerializableDef;

import com.fasterxml.jackson.annotation.JsonFormat;





/**
 * 用户账号信息（支持用户多账号的功能）
 *
 * @author      ZhengWei(HY)
 * @createDate  2021-08-08
 * @version     v1.0
 */
public class UserAccount extends SerializableDef
{

    private static final long serialVersionUID = 6711793222536927222L;
    
    
    /** 应用编号 */
    private String  appID;
    
    /** 用户账号编号 */
    private String  accountNo;
    
    /** 用户账号类型 */
    private String  accountType;
    
    /** 用户全域编号 */
    private String  userGID;
    
    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date    createTime;

    
    
    /**
     * 获取：用户账号编号
     */
    public String getAccountNo()
    {
        return accountNo;
    }

    
    /**
     * 设置：用户账号编号
     * 
     * @param accountNo
     */
    public void setAccountNo(String accountNo)
    {
        this.accountNo = accountNo;
    }

    
    /**
     * 获取：用户账号类型
     */
    public String getAccountType()
    {
        return accountType;
    }

    
    /**
     * 设置：用户账号类型
     * 
     * @param accountType
     */
    public void setAccountType(String accountType)
    {
        this.accountType = accountType;
    }

    
    /**
     * 获取：用户全域编号
     */
    public String getUserGID()
    {
        return userGID;
    }

    
    /**
     * 设置：用户全域编号
     * 
     * @param userGID
     */
    public void setUserGID(String userGID)
    {
        this.userGID = userGID;
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
     * 获取：应用编号
     */
    public String getAppID()
    {
        return appID;
    }


    /**
     * 设置：应用编号
     * 
     * @param appID
     */
    public void setAppID(String appID)
    {
        this.appID = appID;
    }
    
}
