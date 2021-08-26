package org.hy.microservice.user.role;

import java.util.List;

import org.hy.common.xml.annotation.XType;
import org.hy.common.xml.annotation.Xjava;
import org.hy.common.xml.annotation.Xparam;
import org.hy.common.xml.annotation.Xsql;





/**
 * 用户中心：角色信息的DAO
 *
 * @author      ZhengWei(HY)
 * @createDate  2021-08-18
 * @version     v1.0
 */
@Xjava(id="RoleInfoDAO" ,value=XType.XSQL)
public interface IRoleInfoDAO
{
    
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
    @Xsql(id="XSQL_User_UserRoleInfo_Query" ,returnOne=true)
    public RoleInfo queryByID(@Xparam(id="appKey" ,notNull=true) String i_AppKey
                             ,@Xparam(id="roleID" ,notNull=true) String i_RoleID);
    
    
    
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
    @Xsql(id="XSQL_User_UserRoleInfo_Query" ,returnOne=true)
    public RoleInfo queryByID(@Xparam(notNulls={"appKey" ,"roleID"}) RoleInfo i_Role);
    
    
    
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
    @Xsql("XSQL_User_UserRoleInfo_Query")
    public List<RoleInfo> query(@Xparam(notNulls={"appKey"}) RoleInfo i_Role);
    
    
    
    /**
     * 添加角色
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-08-18
     * @version     v1.0
     * 
     * @param i_Role
     * @return
     */
    @Xsql("XSQL_User_UserRoleInfo_Insert")
    public int addRole(@Xparam(notNulls= {"appKey" ,"roleID" ,"roleName" ,"createrID"}) RoleInfo i_Role);
    
    
    
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
    @Xsql("XSQL_User_UserRoleInfo_Delete")
    public int delRole(@Xparam(notNulls= {"appKey" ,"roleID"}) RoleInfo i_Role);
    
    
    
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
    @Xsql("XSQL_User_UserRoleInfo_Update")
    public int updateRole(@Xparam(notNulls= {"appKey" ,"roleID" ,"roleName" ,"updaterID"}) RoleInfo i_Role);
    
}
