package org.hy.microservice.user.account;

import org.hy.common.xml.annotation.XType;
import org.hy.common.xml.annotation.Xjava;
import org.hy.common.xml.annotation.Xparam;
import org.hy.common.xml.annotation.Xsql;





/**
 * 用户中心：用户账号的DAO
 *
 * @author      ZhengWei(HY)
 * @createDate  2021-08-08
 * @version     v1.0
 */
@Xjava(id="UserAccountDAO" ,value=XType.XSQL)
public interface IUserAccountDAO
{
    
    /**
     * 查询用户账号，用账号查编号（UserID）
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-08-08
     * @version     v1.0
     *
     * @param i_AppID      应用编码
     * @param i_AccountNo  用户账号
     * @return
     */
    @Xsql(id="XSQL_User_UserAccount_Query_AccountNo" ,returnOne=true)
    public UserAccount queryAccountNo(@Xparam(id="appID"     ,notNull=true) String i_AppID
                                     ,@Xparam(id="accountNo" ,notNull=true) String i_AccountNo);
    
}
