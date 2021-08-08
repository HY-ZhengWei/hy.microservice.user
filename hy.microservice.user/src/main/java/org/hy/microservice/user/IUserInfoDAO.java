package org.hy.microservice.user;

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
    
}
