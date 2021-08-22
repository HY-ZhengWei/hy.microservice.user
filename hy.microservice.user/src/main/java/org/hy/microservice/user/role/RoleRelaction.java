package org.hy.microservice.user.role;

import org.hy.microservice.common.BaseViewMode;





/**
 * 用户与角色的关系
 *
 * @author      ZhengWei(HY)
 * @createDate  2021-08-22
 * @version     v1.0
 */
public class RoleRelaction extends BaseViewMode
{

    private static final long serialVersionUID = 7266284764559989836L;
    
    /** 用户与角色关系的主键 */
    private String urrID;
    
    /** 用户全域编号 */
    private String userGID;
    
    /** 角色主键 */
    private String roleID;

    
    
    /**
     * 获取：用户与角色关系的主键
     */
    public String getUrrID()
    {
        return urrID;
    }

    
    /**
     * 设置：用户与角色关系的主键
     * 
     * @param urrID
     */
    public void setUrrID(String urrID)
    {
        this.urrID = urrID;
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
     * 获取：角色主键
     */
    public String getRoleID()
    {
        return roleID;
    }

    
    /**
     * 设置：角色主键
     * 
     * @param roleID
     */
    public void setRoleID(String roleID)
    {
        this.roleID = roleID;
    }
    
}
