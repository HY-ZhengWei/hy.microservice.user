package org.hy.microservice.user.userInfo;

import org.hy.common.Help;
import org.hy.common.StringHelp;
import org.hy.common.license.Hash;
import org.hy.common.license.IHash;
import org.hy.common.license.base64.Base64Factory;
import org.hy.common.xml.annotation.Xjava;
import org.hy.common.xml.log.Logger;
import org.hy.microservice.user.UserController;
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
    private static final Logger $Logger = new Logger(UserController.class);
    
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
     * @param i_AppKey     应用编号
     * @param i_AccountNo  用户账号
     * @return
     */
    public UserAccount queryAccountNo(String i_AppKey ,String i_AccountNo)
    {
        return this.userAccountDAO.queryAccountNo(i_AppKey ,i_AccountNo);
    }
    
    
    
    /**
     * 创建密码加密方式
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-08-08
     * @version     v1.0
     * 
     * @param i_Key
     * @return
     */
    public IHash createPassowrdSecurity(String i_Key)
    {
        return new Hash(2 ,3 ,i_Key);
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
            IHash  v_IHash    = this.createPassowrdSecurity(v_User.getId());
            String v_Password = i_UserInfo.getPassword();
            
            if ( v_Password.startsWith("@@E@@") )   // 表示已被前端Base64过才传输此
            {
                v_Password = StringHelp.replaceAll(v_Password ,"@@E@@" ,"");
                v_Password = new String(Base64Factory.getIntance().decode(v_Password));
            }
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
    
    
    
    /**
     * 创建用户
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-08-12
     * @version     v1.0
     * 
     * @param io_User  当入参指定密码时，使用入参密码，否则内部自动创建密码。
     * @return         返回带明文密码的用户信息。
     */
    public UserInfo createUser(UserInfo io_User)
    {
        String v_ID       = StringHelp.getUUID();
        IHash  v_IHash    = this.createPassowrdSecurity(v_ID);
        String v_Password = "";
                
        if ( Help.isNull(io_User.getPassword()) )
        {
            v_Password = StringHelp.random(16 ,true);
        }
        else
        {
            v_Password = io_User.getPassword();
        }
                
        io_User.setPassword(v_IHash.encrypt(v_Password));
        io_User.setId(v_ID);
        
        if ( this.userInfoDAO.createUser(io_User) )
        {
            try
            {
                UserInfo v_User = this.userInfoDAO.queryByID(io_User.getId());
                
                v_User.setPassword(v_Password);
                
                return v_User;
            }
            catch (Exception exce)
            {
                $Logger.error(exce);
            }
        }
        
        return null;
    }
    
    
    
    /**
     * 绑定微信用户的OpenID
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-08-15
     * @version     v1.0
     * 
     * @param i_User  必要参数是 id 和 openID
     * @return
     */
    public boolean bindingOpenID(UserInfo i_User)
    {
        return this.userInfoDAO.bindingOpenID(i_User) >= 1;
    }
    
}
