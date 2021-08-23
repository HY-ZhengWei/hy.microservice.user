package org.hy.microservice.user.permission;

import org.hy.microservice.common.BaseViewMode;





/**
 * 权限项
 *
 * @author      ZhengWei(HY)
 * @createDate  2021-08-23
 * @version     v1.0
 */
public class Permission extends BaseViewMode
{

    private static final long serialVersionUID = 5192046889794639313L;
    
    /** 权限项的主键 */
    private String  permissionID;
    
    /** 权限项的编码 */
    private String  permissionCode;
    
    /** 权限项的名称 */
    private String  permissionName;
    
    /** 是否为系统权限（1：是），系统权限只能通过DB脚本创建 */
    private Integer isSystem;
    
    /** 权限项的类型 */
    private String  permissionType;
    
    /** 权限项的级别 */
    private String  permissionLevel;

    
    
    /**
     * 获取：权限项的主键
     */
    public String getPermissionID()
    {
        return permissionID;
    }

    
    /**
     * 设置：权限项的主键
     * 
     * @param permissionID
     */
    public void setPermissionID(String permissionID)
    {
        this.permissionID = permissionID;
    }

    
    /**
     * 获取：权限项的编码
     */
    public String getPermissionCode()
    {
        return permissionCode;
    }

    
    /**
     * 设置：权限项的编码
     * 
     * @param permissionCode
     */
    public void setPermissionCode(String permissionCode)
    {
        this.permissionCode = permissionCode;
    }

    
    /**
     * 获取：权限项的名称
     */
    public String getPermissionName()
    {
        return permissionName;
    }

    
    /**
     * 设置：权限项的名称
     * 
     * @param permissionName
     */
    public void setPermissionName(String permissionName)
    {
        this.permissionName = permissionName;
    }

    
    /**
     * 获取：是否为系统权限（1：是），系统权限只能通过DB脚本创建
     */
    public Integer getIsSystem()
    {
        return isSystem;
    }

    
    /**
     * 设置：是否为系统权限（1：是），系统权限只能通过DB脚本创建
     * 
     * @param isSystem
     */
    public void setIsSystem(Integer isSystem)
    {
        this.isSystem = isSystem;
    }

    
    /**
     * 获取：权限项的类型
     */
    public String getPermissionType()
    {
        return permissionType;
    }

    
    /**
     * 设置：权限项的类型
     * 
     * @param permissionType
     */
    public void setPermissionType(String permissionType)
    {
        this.permissionType = permissionType;
    }

    
    /**
     * 获取：权限项的级别
     */
    public String getPermissionLevel()
    {
        return permissionLevel;
    }

    
    /**
     * 设置：权限项的级别
     * 
     * @param permissionLevel
     */
    public void setPermissionLevel(String permissionLevel)
    {
        this.permissionLevel = permissionLevel;
    }
    
}
