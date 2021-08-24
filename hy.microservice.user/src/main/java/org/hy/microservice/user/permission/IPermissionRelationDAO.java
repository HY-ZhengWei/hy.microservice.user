package org.hy.microservice.user.permission;

import java.util.List;

import org.hy.common.xml.annotation.XType;
import org.hy.common.xml.annotation.Xjava;
import org.hy.common.xml.annotation.Xparam;
import org.hy.common.xml.annotation.Xsql;





/**
 * 用户中心：权限项关系的DAO
 *
 * @author      ZhengWei(HY)
 * @createDate  2021-08-24
 * @version     v1.0
 */
@Xjava(id="PermissionRelationDAO" ,value=XType.XSQL)
public interface IPermissionRelationDAO
{
    
    /**
     * 按编号查询权限项关系
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-08-24
     * @version     v1.0
     *
     * @param i_AppKey
     * @param i_UrrID
     * @return
     */
    @Xsql(id="XSQL_User_UserPermissionRelaction_QueryByID" ,returnOne=true)
    public PermissionRelaction queryByID(@Xparam(id="appKey" ,notNull=true) String i_AppKey
                                        ,@Xparam(id="uprID"  ,notNull=true) String i_UrrID);
    
    
    
    /**
     * 按编号查询权限项关系
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-08-24
     * @version     v1.0
     *
     * @param i_PermissionRelaction
     * @return
     */
    @Xsql(id="XSQL_User_UserPermissionRelaction_QueryByID" ,returnOne=true)
    public PermissionRelaction queryByID(@Xparam(notNulls={"appKey" ,"uprID"} ,notNull=true) PermissionRelaction i_PermissionRelaction);
    
    
    
    /**
     * 1. 查询某一应用中，按某位所有属性编辑的所有关系
     * 2. 查询某一应用中，拥有某权限编号的所有者的关系
     * 3. 查询某一应用中，备注说明匹配的所有关系
     * 4. 查询某一应用中，有效的或无效的所有关系
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-08-24
     * @version     v1.0
     *
     * @param i_PermissionRelaction
     * @return
     */
    @Xsql("XSQL_User_UserPermissionRelaction_Query")
    public List<PermissionRelaction> query(@Xparam(notNulls={"appKey"}) PermissionRelaction i_PermissionRelaction);
    
    
    
    /**
     * 添加权限项关系
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-08-24
     * @version     v1.0
     * 
     * @param i_PermissionRelaction
     * @return
     */
    @Xsql("XSQL_User_UserPermissionRelaction_Insert")
    public int addRelation(@Xparam(notNulls= {"appKey" ,"uprID" ,"permissionID" ,"ownerID" ,"ownerType" ,"createrID"}) PermissionRelaction i_PermissionRelaction);
    
    
    
    /**
     * 删除权限项关系
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-08-24
     * @version     v1.0
     * 
     * @param i_PermissionRelaction
     * @return
     */
    @Xsql("XSQL_User_UserPermissionRelaction_Delete")
    public int delRelation(@Xparam(notNulls= {"appKey" ,"uprID"}) PermissionRelaction i_PermissionRelaction);
    
    
    
    /**
     * 更新权限项关系
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-08-24
     * @version     v1.0
     * 
     * @param i_PermissionRelaction
     * @return
     */
    @Xsql("XSQL_User_UserPermissionRelaction_Update")
    public int updateRelation(@Xparam(notNulls= {"appKey" ,"uprID" ,"updaterID"}) PermissionRelaction i_PermissionRelaction);
    
    
    
    /**
     * 更新权限项关系的有效标记
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-08-24
     * @version     v1.0
     * 
     * @param i_PermissionRelaction
     * @return
     */
    @Xsql("XSQL_User_UserPermissionRelaction_Update_IsValid")
    public int updateIsValid(@Xparam(notNulls= {"appKey" ,"uprID" ,"updaterID"}) PermissionRelaction i_PermissionRelaction);
    
}
