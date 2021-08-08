package org.hy.microservice.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hy.common.Date;
import org.hy.common.Help;
import org.hy.common.StringHelp;
import org.hy.common.app.Param;
import org.hy.common.xml.log.Logger;
import org.hy.microservice.common.BaseResponse;
import org.hy.microservice.user.account.UserAccount;
import org.hy.microservice.user.common.DatasPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;





/**
 * 用户中心：登录的控制层
 *
 * @author      ZhengWei(HY)
 * @createDate  2021-08-08
 * @version     v1.0
 */
@Controller
@RestController
@RequestMapping("user")
public class LoginController
{
    private static final Logger $Logger          = new Logger(LoginController.class);
    
    /** 数据池标记：会话级 */
    public  static final String $DP_SessionID    = "DatasPool_SessionID_";
    
    /** 数据池标记：登录用户名称 */
    public  static final String $DP_LoginAccount = "DatasPool_LoginAccount_";
    
    /** 数据池标记：微信OpenID */
    public  static final String $DP_OpenID       = "DatasPool_OpenID_";
    
    
    
    @Autowired
    @Qualifier("DatasPool")
    private DatasPool datasPool;
    
    @Autowired
    @Qualifier("MS_User_AccountMaxLen")
    private Param accountMaxLen;
    
    @Autowired
    @Qualifier("MS_User_AccountIllegalChar")
    private Param accountIllegalChar;
    
    @Autowired
    @Qualifier("MS_User_LoginLockMaxCount")
    private Param loginLockMaxCount;
    
    @Autowired
    @Qualifier("UserInfoService")
    private UserInfoService userInfoService;
    
    
    
    /**
     * 用户登录接口
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-08-08
     * @version     v1.0
     * 
     * @param i_LoginAccount  用户名称
     * @param i_Password      用户密码
     * @param i_AppID         应用编号AppID（可以是微信的AppID）
     * @param i_OpenID        微信OpenID
     * @param i_Request
     * @param i_Response
     * @return
     */
    @RequestMapping(value="loginWeiXin" ,produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public BaseResponse<UserInfo> loginWeiXin(@RequestBody UserInfo i_LoginUser
                                             ,HttpServletRequest    i_Request
                                             ,HttpServletResponse   i_Response)
    {
        BaseResponse<UserInfo> v_Ret = new BaseResponse<UserInfo>();
        
        if ( i_LoginUser == null || Help.isNull(i_LoginUser.getLoginAccount()) || Help.isNull(i_LoginUser.getPassword()) )
        {
            $Logger.info("登录验证：账号、密码为空");
            return v_Ret.setCode("900").setMessage("登录验证：账号、密码为空");
        }
        
        if ( Help.isNull(i_LoginUser.getAppID()) )
        {
            $Logger.info("登录验证：微信AppID为空");
            return v_Ret.setCode("901").setMessage("登录验证：微信AppID为空");
        }
        
        if ( Help.isNull(i_LoginUser.getOpenID()) )
        {
            $Logger.info("登录验证：微信OpenID为空");
            return v_Ret.setCode("902").setMessage("登录验证：微信OpenID为空");
        }
        
        int v_AccountMaxLen = Integer.parseInt(this.accountMaxLen.getValue());
        if ( i_LoginUser.getLoginAccount().length() >= v_AccountMaxLen )
        {
            $Logger.info("登录验证：账号长度超出允许范围（最大" + v_AccountMaxLen + "）：" + i_LoginUser.getAppID() + "：" + i_LoginUser.getLoginAccount() + "：" + i_LoginUser.getOpenID());
            return v_Ret.setCode("903").setMessage("登录验证：账号长度超出允许范围（最大" + v_AccountMaxLen + "）");
        }
        
        String [] v_AccountIllegalChar = this.accountIllegalChar.getValue().split("-");
        if ( StringHelp.isContains(i_LoginUser.getLoginAccount() ,v_AccountIllegalChar) )
        {
            $Logger.info("登录验证：账号禁止非法字符" + i_LoginUser.getAppID() + "：" + i_LoginUser.getLoginAccount() + "：" + i_LoginUser.getOpenID());
            return v_Ret.setCode("904").setMessage("登录验证：账号禁止非法字符");
        }
        
        if ( StringHelp.isContains(i_LoginUser.getPassword() ,v_AccountIllegalChar) )
        {
            $Logger.info("登录验证：密码禁止非法字符" + i_LoginUser.getAppID() + "：" + i_LoginUser.getLoginAccount() + "：" + i_LoginUser.getOpenID());
            return v_Ret.setCode("905").setMessage("登录验证：密码禁止非法字符");
        }
        
        if ( StringHelp.isContains(i_LoginUser.getAppID() ,v_AccountIllegalChar) )
        {
            $Logger.info("登录验证：AppID禁止非法字符" + i_LoginUser.getAppID() + "：" + i_LoginUser.getLoginAccount() + "：" + i_LoginUser.getOpenID());
            return v_Ret.setCode("906").setMessage("登录验证：AppID禁止非法字符");
        }
        
        if ( StringHelp.isContains(i_LoginUser.getOpenID() ,v_AccountIllegalChar) )
        {
            $Logger.info("登录验证：OpenID禁止非法字符" + i_LoginUser.getAppID() + "：" + i_LoginUser.getLoginAccount() + "：" + i_LoginUser.getOpenID());
            return v_Ret.setCode("907").setMessage("登录验证：OpenID禁止非法字符");
        }
        
        boolean v_IsLock = this.loginIsLock(i_LoginUser ,i_Request);
        if ( v_IsLock )
        {
            $Logger.info("登录验证：账户被锁定：" + i_LoginUser.getAppID() + "：" + i_LoginUser.getLoginAccount() + "：" + i_LoginUser.getOpenID());
            return v_Ret.setCode("911").setMessage("登录验证：账户被锁定");
        }
        
        UserAccount v_UAccount = this.userInfoService.queryAccountNo(i_LoginUser.getAppID() ,i_LoginUser.getLoginAccount());
        if ( v_UAccount == null )
        {
            $Logger.info("登录验证：非法账户：" + i_LoginUser.getAppID() + "：" + i_LoginUser.getLoginAccount() + "：" + i_LoginUser.getOpenID());
            this.loginLock(i_LoginUser ,i_Request);
            return v_Ret.setCode("912").setMessage("登录验证：非法账户");
        }
        i_LoginUser.setId(v_UAccount.getUserGID());
        
        UserInfo v_RetUser = this.userInfoService.checkLogin(i_LoginUser);
        if ( v_RetUser == null )
        {
            $Logger.info("登录验证：非法账户：" + i_LoginUser.getAppID() + "：" + i_LoginUser.getLoginAccount() + "：" + i_LoginUser.getOpenID());
            this.loginLock(i_LoginUser ,i_Request);
            return v_Ret.setCode("913").setMessage("登录验证：非法账户");
        }
        
        return v_Ret.setData(v_RetUser);
    }
    
    
    
    /**
     * 判定用户是否被锁定
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-08-08
     * @version     v1.0
     * 
     * @param i_Request
     * @return
     */
    private boolean loginIsLock(UserInfo i_LoginUser ,HttpServletRequest i_Request)
    {
        try
        {
            int v_LoginLockMaxCount = Integer.parseInt(this.loginLockMaxCount.getValue());
            
            // 客户端尝试登录三次后，不允许再连续登录
            String v_SessionID = i_Request.getSession().getId();
            if ( !Help.isNull(v_SessionID) )
            {
                Integer v_LCount = (Integer)datasPool.get($DP_SessionID + v_SessionID);
                
                if ( v_LCount != null && v_LCount >= v_LoginLockMaxCount )
                {
                    return true;
                }
            }
            
            // 账号尝试登录N次后，不允许再连续登录
            if ( !Help.isNull(i_LoginUser.getLoginAccount()) )
            {
                Integer v_LCount = (Integer)datasPool.get($DP_LoginAccount + i_LoginUser.getAppID() + "_" + i_LoginUser.getLoginAccount());
                
                if ( v_LCount != null && v_LCount >= v_LoginLockMaxCount )
                {
                    return true;
                }
            }
            
            // OpenID尝试登录N次后，不允许再连续登录
            if ( !Help.isNull(i_LoginUser.getOpenID()) )
            {
                Integer v_LCount = (Integer)datasPool.get($DP_LoginAccount + i_LoginUser.getOpenID());
                
                if ( v_LCount != null && v_LCount >= v_LoginLockMaxCount )
                {
                    return true;
                }
            }
        }
        catch (Exception exce)
        {
            $Logger.error(exce);
            return true;
        }
        
        return false;
    }
    
    
    
    /**
     * 防止多次登录，转向锁定状态
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-08-08
     * @version     v1.0
     * 
     * @param i_LoginUser
     * @param i_Request
     * @return
     */
    private void loginLock(UserInfo i_LoginUser ,HttpServletRequest i_Request)
    {
        try
        {
            int v_LoginLockMaxCount = Integer.parseInt(this.loginLockMaxCount.getValue());
            
            // 客户端尝试登录三次后，不允许再连续登录
            String v_SessionID = i_Request.getSession().getId();
            if ( !Help.isNull(v_SessionID) )
            {
                Integer v_LCount = (Integer)datasPool.get($DP_SessionID + v_SessionID);
                
                if ( v_LCount == null || v_LCount <= 0 )
                {
                    datasPool.put($DP_SessionID + v_SessionID ,1            ,60 * 5);
                }
                else if ( v_LCount < v_LoginLockMaxCount )
                {
                    datasPool.put($DP_SessionID + v_SessionID ,v_LCount + 1 ,60 * 5);
                }
                else
                {
                    String v_Msg = i_LoginUser.getLoginAccount() + "(" + i_LoginUser.getOpenID() + ")" + "多次尝试登录。" + Date.getNowTime().getFull();
                }
            }
            
            // 账号尝试登录三次后，不允许再连续登录
            if ( !Help.isNull(i_LoginUser.getLoginAccount()) )
            {
                Integer v_LCount = (Integer)datasPool.get($DP_LoginAccount + i_LoginUser.getAppID() + "_" + i_LoginUser.getLoginAccount());
                
                if ( v_LCount == null || v_LCount <= 0 )
                {
                    datasPool.put($DP_LoginAccount + i_LoginUser.getAppID() + "_" + i_LoginUser.getLoginAccount() ,1            ,60 * 5);
                }
                else if ( v_LCount < v_LoginLockMaxCount )
                {
                    datasPool.put($DP_LoginAccount + i_LoginUser.getAppID() + "_" + i_LoginUser.getLoginAccount() ,v_LCount + 1 ,60 * 5);
                }
                else
                {
                    String v_Msg = i_LoginUser.getLoginAccount() + "(" + i_LoginUser.getOpenID() + ")" + "多次尝试登录。" + Date.getNowTime().getFull();
                }
            }
            
            // OpenID尝试登录N次后，不允许再连续登录
            if ( !Help.isNull(i_LoginUser.getOpenID()) )
            {
                Integer v_LCount = (Integer)datasPool.get($DP_OpenID + i_LoginUser.getOpenID());
                
                if ( v_LCount == null || v_LCount <= 0 )
                {
                    datasPool.put($DP_OpenID + i_LoginUser.getOpenID() ,1            ,60 * 5);
                }
                else if ( v_LCount < v_LoginLockMaxCount )
                {
                    datasPool.put($DP_OpenID + i_LoginUser.getOpenID() ,v_LCount + 1 ,60 * 5);
                }
                else
                {
                    String v_Msg = i_LoginUser.getLoginAccount() + "(" + i_LoginUser.getOpenID() + ")" + "多次尝试登录。" + Date.getNowTime().getFull();
                }
            }
        }
        catch (Exception exce)
        {
            $Logger.error(exce);
        }
    }
    
}