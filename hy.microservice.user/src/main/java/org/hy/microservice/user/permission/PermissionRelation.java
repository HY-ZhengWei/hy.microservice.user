package org.hy.microservice.user.permission;

import org.hy.microservice.common.BaseViewMode;





/**
 * 权限项的关系
 *
 * @author      ZhengWei(HY)
 * @createDate  2021-08-24
 * @version     v1.0
 */
public class PermissionRelation extends BaseViewMode
{

    private static final long serialVersionUID = -151430567939898212L;

    /** 权限项关系的主键 */
    private String  uprID;
    
    /** 权限项的ID */
    private String  permissionID;
    
    /** 权限的所有者ID */
    private String  ownerID;
    
    /** 权限的所有者类型（1: 角色； 2：用户） */
    private Integer ownerType;

    
    
    /**
     * 获取：权限项关系的主键
     */
    public String getUprID()
    {
        return uprID;
    }

    
    /**
     * 设置：权限项关系的主键
     * 
     * @param uprID
     */
    public void setUprID(String uprID)
    {
        this.uprID = uprID;
    }


    /**
     * 获取：权限项的ID
     */
    public String getPermissionID()
    {
        return permissionID;
    }


    /**
     * 设置：权限项的ID
     * 
     * @param permissionID
     */
    public void setPermissionID(String permissionID)
    {
        this.permissionID = permissionID;
    }


    /**
     * 获取：权限的所有者ID
     */
    public String getOwnerID()
    {
        return ownerID;
    }


    /**
     * 设置：权限的所有者ID
     * 
     * @param ownerID
     */
    public void setOwnerID(String ownerID)
    {
        this.ownerID = ownerID;
    }


    /**
     * 获取：权限的所有者类型（1: 角色； 2：用户）
     */
    public Integer getOwnerType()
    {
        return ownerType;
    }


    /**
     * 设置：权限的所有者类型（1: 角色； 2：用户）
     * 
     * @param ownerType
     */
    public void setOwnerType(Integer ownerType)
    {
        this.ownerType = ownerType;
    }
    
}
