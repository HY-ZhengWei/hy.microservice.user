package org.hy.microservice.user.userInfo;

import org.hy.common.xml.annotation.XType;
import org.hy.common.xml.annotation.Xjava;
import org.hy.common.xml.annotation.Xparam;
import org.hy.common.xml.annotation.Xsql;





/**
 * 用户中心：用户信息的DAO
 *
 * @author      ZhengWei(HY)
 * @createDate  2021-08-08
 * @version     v1.0
 */
@Xjava(id="UserInfoDAO" ,value=XType.XSQL)
public interface IUserInfoDAO
{
    
    /**
     * 按用户编号查询用户
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-08-08
     * @version     v1.0
     *
     * @param i_UserGID
     * @return
     */
    @Xsql(id="XSQL_User_UserInfo_Query_ByID" ,returnOne=true)
    public UserInfo queryByID(@Xparam(id="id" ,notNull=true) String i_UserGID);
    
    
    
    /**
     * 创建用户
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-08-12
     * @version     v1.0
     * 
     * @param i_User
     * @return
     */
    @Xsql("GXSQL_User_UserInfo_Insert_CreateUser")
    public boolean createUser(UserInfo i_User);
    
    
    
    /**
     * 绑定微信用户的OpenID
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-08-15
     * @version     v1.0
     * 
     * @param i_User
     * @return
     */
    @Xsql("XSQL_User_UserInfo_Update_OpenID")
    public int bindingOpenID(@Xparam(notNulls={"appKey" ,"id" ,"openID"}) UserInfo i_User);
    
}
