package org.hy.microservice.user.role;

import java.util.List;

import org.hy.common.StringHelp;
import org.hy.common.xml.annotation.Xjava;
import org.hy.common.xml.log.Logger;





/**
 * 用户中心的角色业务类
 *
 * @author      ZhengWei(HY)
 * @createDate  2021-08-18
 * @version     v1.0
 */
@Xjava
public class RoleInfoService
{
    
    private static final Logger $Logger = new Logger(RoleInfoService.class);
    
    @Xjava
    private IRoleInfoDAO roleDAO;
    
    
    
    /**
     * 查询某一应用中，按编号查询角色
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-08-26
     * @version     v1.0
     *
     * @param i_AppKey
     * @param i_UrrID
     * @return
     */
    public RoleInfo queryByID(String i_AppKey ,String i_RoleID)
    {
        return this.roleDAO.queryByID(i_AppKey ,i_RoleID);
    }
    
    
    /**
     * 查询某一应用中，按编号查询角色
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-08-18
     * @version     v1.0
     *
     * @param i_Role
     * @return
     */
    public RoleInfo queryByID(RoleInfo i_Role)
    {
        return this.roleDAO.queryByID(i_Role);
    }
    
    
    
    /**
     * 1. 查询某一应用中，所有角色
     * 2. 查询某一应用中，角色名称匹配的所有角色
     * 3. 查询某一应用中，按编号查询角色
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-08-18
     * @version     v1.0
     *
     * @param i_Role
     * @return
     */
    public List<RoleInfo> query(RoleInfo i_Role)
    {
        return this.roleDAO.query(i_Role);
    }
    
    
    
    /**
     * 添加角色
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-08-18
     * @version     v1.0
     * 
     * @param io_Role  生成角色ID，并给于返回
     * @return
     */
    public RoleInfo addRole(RoleInfo io_Role)
    {
        io_Role.setRoleID(StringHelp.getUUID());
        $Logger.debug(io_Role);
        
        if ( this.roleDAO.addRole(io_Role) >= 1 )
        {
            return this.queryByID(io_Role);
        }
        
        return null;
    }
    
    
    
    /**
     * 删除角色
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-08-18
     * @version     v1.0
     * 
     * @param i_Role
     * @return
     */
    public boolean delRole(RoleInfo i_Role)
    {
        $Logger.debug(i_Role);
        return this.roleDAO.delRole(i_Role) >= 1;
    }
    
    
    
    /**
     * 更新角色
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-08-18
     * @version     v1.0
     * 
     * @param i_Role
     * @return
     */
    public boolean updateRole(RoleInfo i_Role)
    {
        $Logger.debug(i_Role);
        return this.roleDAO.updateRole(i_Role) >= 1;
    }
    
}
