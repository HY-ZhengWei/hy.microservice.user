package org.hy.microservice.user.role;

import org.hy.microservice.common.BaseViewMode;





/**
 * 角色管理
 *
 * @author      ZhengWei(HY)
 * @createDate  2021-08-15
 * @version     v1.0
 */
public class RoleInfo extends BaseViewMode
{

    private static final long serialVersionUID = -4939813621990304510L;
    
    /** 角色主键 */
    private String roleID;
    
    /** 角色名称 */
    private String roleName;
    
    
    
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

    
    /**
     * 获取：角色名称
     */
    public String getRoleName()
    {
        return roleName;
    }

    
    /**
     * 设置：角色名称
     * 
     * @param roleName
     */
    public void setRoleName(String roleName)
    {
        this.roleName = roleName;
    }
    
}
