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
public class RoleRelactionService
{
    
    private static final Logger $Logger = new Logger(RoleRelactionService.class);
    
    @Xjava
    private IRoleRelationDAO relactionDAO;
    
    
    
    /**
     * 按编号查询用户与角色关系
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-08-22
     * @version     v1.0
     *
     * @param i_UrrID
     * @return
     */
    public RoleRelaction queryByID(String i_UrrID)
    {
        return this.relactionDAO.queryByID(i_UrrID);
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
     * @param i_RoleRelaction
     * @return
     */
    public List<RoleRelaction> query(RoleRelaction i_RoleRelaction)
    {
        return this.relactionDAO.query(i_RoleRelaction);
    }
    
    
    
    /**
     * 添加关系
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-08-22
     * @version     v1.0
     * 
     * @param io_RoleRelaction
     * @return
     */
    public RoleRelaction addRelation(RoleRelaction io_RoleRelaction)
    {
        io_RoleRelaction.setUrrID(StringHelp.getUUID());
        $Logger.debug(io_RoleRelaction);
        
        if ( this.relactionDAO.addRelation(io_RoleRelaction) >= 1 )
        {
            return this.queryByID(io_RoleRelaction.getUrrID());
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
     * @param i_RoleRelaction
     * @return
     */
    public boolean delRelation(RoleRelaction i_RoleRelaction)
    {
        $Logger.debug(i_RoleRelaction);
        return this.relactionDAO.delRelation(i_RoleRelaction) >= 1;
    }
    
    
    
    /**
     * 更新关系
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-08-22
     * @version     v1.0
     * 
     * @param i_RoleRelaction
     * @return
     */
    public boolean updateRelation(RoleRelaction i_RoleRelaction)
    {
        $Logger.debug(i_RoleRelaction);
        return this.relactionDAO.updateRelation(i_RoleRelaction) >= 1;
    }
    
}
