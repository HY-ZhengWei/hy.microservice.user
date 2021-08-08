package org.hy.microservice.user;

import org.hy.common.license.Hash;
import org.hy.common.license.IHash;
import org.hy.common.license.base64.Base64Factory;
import org.hy.common.xml.annotation.Xjava;
import org.hy.microservice.user.account.IUserAccountDAO;
import org.hy.microservice.user.account.UserAccount;





/**
 * 用户中心的用户业务类
 *
 * @author      ZhengWei(HY)
 * @createDate  2021-08-08
 * @version     v1.0
 */
@Xjava
public class UserInfoService
{
    
    @Xjava
    private IUserInfoDAO    userInfoDAO;
    
    @Xjava
    private IUserAccountDAO userAccountDAO;
    
    
    
    /**
     * 查询用户账号，用账号查编号（UserID）
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-08-08
     * @version     v1.0
     *
     * @param i_AccountNo  用户账号
     * @param i_AccountNo  用户账号
     * @return
     */
    public UserAccount queryAccountNo(String i_AppID ,String i_AccountNo)
    {
        return this.userAccountDAO.queryAccountNo(i_AppID ,i_AccountNo);
    }
    
    
    
    /**
     * 验证用户登录
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-08-08
     * @version     v1.0
     *
     * @param i_UserInfo
     * @return            验证成功时，返回用户信息
     *                    验证失败时，返回NULL
     */
    public UserInfo checkLogin(UserInfo i_UserInfo)
    {
        UserInfo v_User = this.userInfoDAO.queryByID(i_UserInfo.getId());
        
        if ( v_User != null )
        {
            String v_Password = new String(Base64Factory.getIntance().decode(i_UserInfo.getPassword()));
            IHash  v_IHash    = new Hash(2 ,3 ,i_UserInfo.getId());
            
            v_Password = v_IHash.encrypt(v_Password);
            
            if ( v_Password.equals(v_User.getPassword()) )
            {
                v_User.setPassword(null);
            }
            else
            {
                v_User = null;
            }
        }
        
        return v_User;
    }
    
}
