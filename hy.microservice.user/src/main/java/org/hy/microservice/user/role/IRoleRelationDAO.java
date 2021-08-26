package org.hy.microservice.user.role;

import java.util.List;

import org.hy.common.xml.annotation.XType;
import org.hy.common.xml.annotation.Xjava;
import org.hy.common.xml.annotation.Xparam;
import org.hy.common.xml.annotation.Xsql;





/**
 * 用户中心：用户与角色关系的DAO
 *
 * @author      ZhengWei(HY)
 * @createDate  2021-08-22
 * @version     v1.0
 */
@Xjava(id="RoleRelationDAO" ,value=XType.XSQL)
public interface IRoleRelationDAO
{
    
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
    @Xsql(id="XSQL_User_UserRoleRelation_QueryByID" ,returnOne=true)
    public RoleRelation queryByID(@Xparam(id="appKey" ,notNull=true) String i_AppKey
                                 ,@Xparam(id="urrID"  ,notNull=true) String i_UrrID);
    
    
    
    /**
     * 按编号查询用户与角色关系
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-08-24
     * @version     v1.0
     *
     * @param i_RoleRelation
     * @return
     */
    @Xsql(id="XSQL_User_UserRoleRelation_QueryByID" ,returnOne=true)
    public RoleRelation queryByID(@Xparam(notNulls={"appKey" ,"urrID"} ,notNull=true) RoleRelation i_RoleRelation);
    
    
    
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
    @Xsql("XSQL_User_UserRoleRelation_Query")
    public List<RoleRelation> query(@Xparam(notNulls={"appKey"}) RoleRelation i_RoleRelation);
    
    
    
    /**
     * 添加关系
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-08-22
     * @version     v1.0
     * 
     * @param i_RoleRelation
     * @return
     */
    @Xsql("XSQL_User_UserRoleRelation_Insert")
    public int addRelation(@Xparam(notNulls= {"appKey" ,"urrID" ,"userGID" ,"roleID" ,"createrID"}) RoleRelation i_RoleRelation);
    
    
    
    /**
     * 删除角色
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-08-22
     * @version     v1.0
     * 
     * @param i_RoleRelation
     * @return
     */
    @Xsql("XSQL_User_UserRoleRelation_Delete")
    public int delRelation(@Xparam(notNulls= {"appKey" ,"urrID"}) RoleRelation i_RoleRelation);
    
    
    
    /**
     * 更新角色
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-08-22
     * @version     v1.0
     * 
     * @param i_RoleRelation
     * @return
     */
    @Xsql("XSQL_User_UserRoleRelation_Update")
    public int updateRelation(@Xparam(notNulls= {"appKey" ,"urrID" ,"updaterID"}) RoleRelation i_RoleRelation);
    
}
