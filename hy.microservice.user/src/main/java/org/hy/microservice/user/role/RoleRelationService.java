package org.hy.microservice.user.role;

import java.util.List;

import org.hy.common.StringHelp;
import org.hy.common.xml.annotation.Xjava;
import org.hy.common.xml.log.Logger;





/**
 * 用户中心的角色与用户关系的业务类
 *
 * @author      ZhengWei(HY)
 * @createDate  2021-08-22
 * @version     v1.0
 */
@Xjava
public class RoleRelationService
{
    
    private static final Logger $Logger = new Logger(RoleRelationService.class);
    
    @Xjava
    private IRoleRelationDAO relationDAO;
    
    
    
    /**
     * 按编号查询用户与角色关系
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-08-22
     * @version     v1.0
     *
     * @param i_AppKey
     * @param i_UrrID
     * @return
     */
    public RoleRelation queryByID(String i_AppKey ,String i_UrrID)
    {
        return this.relationDAO.queryByID(i_AppKey ,i_UrrID);
    }
    
    
    
    /**
     * 按编号查询用户与角色关系
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-08-22
     * @version     v1.0
     *
     * @param i_RoleRelation
     * @return
     */
    public RoleRelation queryByID(RoleRelation i_RoleRelation)
    {
        return this.relationDAO.queryByID(i_RoleRelation);
    }
    
    
    
    /**
     * 1. 查询某一应用中，按角色编号查询所有关系
     * 2. 查询某一应用中，按用户编号查询所有关系
     * 3. 查询某一应用中，备注说明匹配的所有关系
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-08-22
     * @version     v1.0
     *
     * @param i_RoleRelation
     * @return
     */
    public List<RoleRelation> query(RoleRelation i_RoleRelation)
    {
        return this.relationDAO.query(i_RoleRelation);
    }
    
    
    
    /**
     * 添加关系
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-08-22
     * @version     v1.0
     * 
     * @param io_RoleRelation
     * @return
     */
    public RoleRelation addRelation(RoleRelation io_RoleRelation)
    {
        io_RoleRelation.setUrrID(StringHelp.getUUID());
        $Logger.debug(io_RoleRelation);
        
        if ( this.relationDAO.addRelation(io_RoleRelation) >= 1 )
        {
            return this.queryByID(io_RoleRelation);
        }
        
        return null;
    }
    
    
    
    /**
     * 删除关系
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-08-22
     * @version     v1.0
     * 
     * @param i_RoleRelation
     * @return
     */
    public boolean delRelation(RoleRelation i_RoleRelation)
    {
        $Logger.debug(i_RoleRelation);
        return this.relationDAO.delRelation(i_RoleRelation) >= 1;
    }
    
    
    
    /**
     * 更新关系
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-08-22
     * @version     v1.0
     * 
     * @param i_RoleRelation
     * @return
     */
    public boolean updateRelation(RoleRelation i_RoleRelation)
    {
        $Logger.debug(i_RoleRelation);
        return this.relationDAO.updateRelation(i_RoleRelation) >= 1;
    }
    
}
