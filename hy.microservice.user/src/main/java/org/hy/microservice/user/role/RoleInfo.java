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
    private String id;
    
    /** 角色名称 */
    private String roleName;
    
    
    
    /**
     * 获取：角色主键
     */
    public String getId()
    {
        return id;
    }

    
    /**
     * 设置：角色主键
     * 
     * @param id
     */
    public void setId(String id)
    {
        this.id = id;
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
