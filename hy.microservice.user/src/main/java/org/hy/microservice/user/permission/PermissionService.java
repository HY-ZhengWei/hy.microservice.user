package org.hy.microservice.user.permission;

import java.util.List;

import org.hy.common.StringHelp;
import org.hy.common.xml.annotation.Xjava;
import org.hy.common.xml.log.Logger;





/**
 * 用户中心的权限项的业务类
 *
 * @author      ZhengWei(HY)
 * @createDate  2021-08-23
 * @version     v1.0
 */
@Xjava
public class PermissionService
{
    
    private static final Logger $Logger = new Logger(PermissionService.class);
    
    @Xjava
    private IPermissionDAO permissionDAO;
    
    
    
    /**
     * 按主键查询权限项
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-08-23
     * @version     v1.0
     *
     * @param i_AppKey
     * @param i_PermissionID
     * @return
     */
    public Permission queryByID(String i_AppKey ,String i_UrrID)
    {
        return this.permissionDAO.queryByID(i_AppKey ,i_UrrID);
    }
    
    
    
    /**
     * 按主键查询权限项
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-08-24
     * @version     v1.0
     *
     * @param i_Permission
     * @return
     */
    public Permission queryByID(Permission i_Permission)
    {
        return this.permissionDAO.queryByID(i_Permission);
    }
    
    
    
    /**
     * 按权限编号查询权限项
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-08-23
     * @version     v1.0
     *
     * @param i_Permission
     * @return
     */
    public Permission queryByCode(Permission i_Permission)
    {
        return this.permissionDAO.queryByCode(i_Permission);
    }
    
    
    
    /**
     * 1. 查询某一应用中，按权限编号查询
     * 2. 查询某一应用中，按是否为系统权限查询所有权限项
     * 3. 查询某一应用中，按权限类型的所有权限项
     * 4. 查询某一应用中，按权限级别的所有权限项
     * 5. 查询某一应用中，权限名称匹配的所有权限项
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-08-23
     * @version     v1.0
     *
     * @param i_Permission
     * @return
     */
    public List<Permission> query(Permission i_Permission)
    {
        return this.permissionDAO.query(i_Permission);
    }
    
    
    
    /**
     * 添加权限项(应用级)。
     * 
     * 注意：不能添加系统级的权限项
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-08-23
     * @version     v1.0
     * 
     * @param i_Permission
     * @return
     */
    public Permission addPermission(Permission io_Permission)
    {
        io_Permission.setPermissionID(StringHelp.getUUID());
        $Logger.debug(io_Permission);
        
        if ( this.permissionDAO.addPermission(io_Permission) >= 1 )
        {
            return this.queryByID(io_Permission);
        }
        
        return null;
    }
    
    
    
    /**
     * 删除权限项(应用级)。
     * 
     * 注意：不能删除系统级的权限项
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-08-23
     * @version     v1.0
     * 
     * @param i_Permission
     * @return
     */
    public boolean delPermission(Permission i_Permission)
    {
        $Logger.debug(i_Permission);
        return this.permissionDAO.delPermission(i_Permission) >= 1;
    }
    
    
    
    /**
     * 更新权限项(应用级)。
     * 
     * 注意：不能更新系统级的权限项
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-08-23
     * @version     v1.0
     * 
     * @param i_Permission
     * @return
     */
    public boolean updatePermission(Permission i_Permission)
    {
        $Logger.debug(i_Permission);
        return this.permissionDAO.updatePermission(i_Permission) >= 1;
    }
    
}
