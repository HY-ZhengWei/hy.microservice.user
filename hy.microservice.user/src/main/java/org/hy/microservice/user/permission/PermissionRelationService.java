package org.hy.microservice.user.permission;

import java.util.List;

import org.hy.common.StringHelp;
import org.hy.common.xml.annotation.Xjava;
import org.hy.common.xml.log.Logger;





/**
 * 用户中心：权限项关系的的业务类
 *
 * @author      ZhengWei(HY)
 * @createDate  2021-08-26
 * @version     v1.0
 */
public class PermissionRelationService
{
    
    private static final Logger $Logger = new Logger(PermissionRelationService.class);
    
    @Xjava
    private IPermissionRelationDAO permissionRelationDAO;
    
    
    
    /**
     * 按编号查询权限项关系
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-08-26
     * @version     v1.0
     *
     * @param i_AppKey
     * @param i_UrrID
     * @return
     */
    public PermissionRelation queryByID(String i_AppKey ,String i_UrrID)
    {
        return this.permissionRelationDAO.queryByID(i_AppKey ,i_UrrID);
    }
    
    
    
    /**
     * 按编号查询权限项关系
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-08-26
     * @version     v1.0
     *
     * @param i_PermissionRelation
     * @return
     */
    public PermissionRelation queryByID(PermissionRelation i_PermissionRelation)
    {
        return this.permissionRelationDAO.queryByID(i_PermissionRelation);
    }
    
    
    
    /**
     * 1. 查询某一应用中，按某位所有属性编辑的所有关系
     * 2. 查询某一应用中，拥有某权限编号的所有者的关系
     * 3. 查询某一应用中，备注说明匹配的所有关系
     * 4. 查询某一应用中，有效的或无效的所有关系
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-08-26
     * @version     v1.0
     *
     * @param i_PermissionRelation
     * @return
     */
    public List<PermissionRelation> query(PermissionRelation i_PermissionRelation)
    {
        return this.permissionRelationDAO.query(i_PermissionRelation);
    }
    
    
    
    /**
     * 添加权限项关系
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-08-26
     * @version     v1.0
     * 
     * @param io_PermissionRelation
     * @return
     */
    public PermissionRelation addRelation(PermissionRelation io_PermissionRelation)
    {
        io_PermissionRelation.setUprID(StringHelp.getUUID());
        $Logger.debug(io_PermissionRelation);
        
        if ( this.permissionRelationDAO.addRelation(io_PermissionRelation) >= 1 )
        {
            return this.queryByID(io_PermissionRelation);
        }
        
        return null;
    }
    
    
    
    /**
     * 删除权限项关系
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-08-26
     * @version     v1.0
     * 
     * @param i_PermissionRelation
     * @return
     */
    public boolean delRelation(PermissionRelation i_PermissionRelation)
    {
        $Logger.debug(i_PermissionRelation);
        return this.permissionRelationDAO.delRelation(i_PermissionRelation) >= 1;
    }
    
    
    
    /**
     * 更新权限项关系
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-08-26
     * @version     v1.0
     * 
     * @param i_PermissionRelation
     * @return
     */
    public boolean updateRelation(PermissionRelation i_PermissionRelation)
    {
        $Logger.debug(i_PermissionRelation);
        return this.permissionRelationDAO.updateRelation(i_PermissionRelation) >= 1;
    }
    
    
    
    /**
     * 更新权限项关系的有效标记
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-08-26
     * @version     v1.0
     * 
     * @param i_PermissionRelation
     * @return
     */
    public boolean updateIsValid(PermissionRelation i_PermissionRelation)
    {
        $Logger.debug(i_PermissionRelation);
        return this.permissionRelationDAO.updateIsValid(i_PermissionRelation) >= 1;
    }
    
}
