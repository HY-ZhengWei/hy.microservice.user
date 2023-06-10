package org.hy.microservice.user.permission;

import java.util.List;

import org.hy.common.xml.annotation.XType;
import org.hy.common.xml.annotation.Xjava;
import org.hy.common.xml.annotation.Xparam;
import org.hy.common.xml.annotation.Xsql;





/**
 * 用户中心：权限项的DAO
 *
 * @author      ZhengWei(HY)
 * @createDate  2021-08-23
 * @version     v1.0
 */
@Xjava(id="PermissionDAO" ,value=XType.XSQL)
public interface IPermissionDAO
{
    
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
    @Xsql(id="XSQL_User_UserPermission_QueryByID" ,returnOne=true)
    public Permission queryByID(@Xparam(id="appKey"       ,notNull=true) String i_AppKey
                               ,@Xparam(id="permissionID" ,notNull=true) String i_PermissionID);
    
    
    
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
    @Xsql(id="XSQL_User_UserPermission_QueryByID" ,returnOne=true)
    public Permission queryByID(@Xparam(notNulls={"appKey" ,"permissionID"})  Permission i_Permission);
    
    
    
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
    @Xsql(id="XSQL_User_UserPermission_QueryByCode" ,returnOne=true)
    public Permission queryByCode(@Xparam(notNulls={"appKey" ,"permissionCode"})  Permission i_Permission);
    
    
    
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
    @Xsql("XSQL_User_UserPermission_Query")
    public List<Permission> query(@Xparam(notNulls={"appKey"}) Permission i_Permission);
    
    
    
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
    @Xsql("XSQL_User_UserPermission_Insert")
    public int addPermission(@Xparam(notNulls= {"appKey" ,"permissionID" ,"permissionCode" ,"permissionName" ,"createUserID"}) Permission i_Permission);
    
    
    
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
    @Xsql("XSQL_User_UserPermission_Delete")
    public int delPermission(@Xparam(notNulls= {"appKey" ,"permissionID"}) Permission i_Permission);
    
    
    
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
    @Xsql("XSQL_User_UserPermission_Update")
    public int updatePermission(@Xparam(notNulls= {"appKey" ,"permissionID" ,"permissionName" ,"updateUserID"}) Permission i_Permission);
    
}
