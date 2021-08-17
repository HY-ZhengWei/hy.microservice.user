package org.hy.microservice.user;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hy.common.Date;
import org.hy.common.Help;
import org.hy.common.StringHelp;
import org.hy.common.app.Param;
import org.hy.common.license.AppKey;
import org.hy.common.xml.log.Logger;
import org.hy.microservice.common.BaseResponse;
import org.hy.microservice.user.account.UserAccount;
import org.hy.microservice.user.common.DatasPool;
import org.hy.microservice.user.user.TokenInfo;
import org.hy.microservice.user.user.UserService;
import org.hy.microservice.user.userInfo.UserInfo;
import org.hy.microservice.user.userInfo.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    @Qualifier("MS_User_AccountMaxLen")
    private Param accountMaxLen;
    
    @Autowired
    @Qualifier("MS_User_AccountIllegalChar")
    private Param accountIllegalChar;
    
    @Autowired
    @Qualifier("MS_User_LoginLockMaxCount")
    private Param loginLockMaxCount;
    
    @Autowired
    @Qualifier("MS_User_LoginLockTimeLen")
    private Param loginLockTimeLen;
    
    @Autowired
    @Qualifier("MS_User_AppKeys")
    private Map<String ,AppKey> appKeys;
    
    @Autowired
    @Qualifier("MS_User_IsCheckToken")
    private Param isCheckToken;
    
    @Autowired
    @Qualifier("DatasPool")
    private DatasPool datasPool;
    
    @Autowired
    @Qualifier("UserService")
    private UserService userService;
    
    @Autowired
    @Qualifier("UserInfoService")
    private UserInfoService userInfoService;
    
    
    
    /**
     * 获取登录临时Code
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-08-15
     * @version     v1.0
     * 
     * @param i_AppKey   应用编号
     * @param i_Request
     * @param i_Response
     * @return
     */
    @RequestMapping(value="code" ,produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public BaseResponse<String> code(@RequestParam(value="appKey" ,required=false) String i_AppKey
                                    ,HttpServletRequest  i_Request
                                    ,HttpServletResponse i_Response)
    {
        BaseResponse<String> v_Ret = new BaseResponse<String>();
        
        if ( Help.isNull(i_AppKey) )
        {
            $Logger.info("登录临时Code：应用编号为空");
            return v_Ret.setCode("902").setMessage("登录临时Code：应用编号为空");
        }
        
        AppKey v_AppKey = this.appKeys.get(i_AppKey);
        if ( v_AppKey == null )
        {
            $Logger.info("登录临时Code：应用编号无效");
            return v_Ret.setCode("903").setMessage("登录临时Code：应用编号无效");
        }
        
        TokenInfo v_Token = this.userService.getCode(v_AppKey);
        if ( v_Token == null )
        {
            $Logger.info("登录临时Code：服务异常");
            return v_Ret.setCode("911").setMessage("登录临时Code：服务异常");
        }
        
        return v_Ret.setData(v_Token.getCode());
    }
    
    
    
    /**
     * 用户登录接口
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-08-08
     * @version     v1.0
     * 
     * @param i_Code       临时Code，用完即失效（只能使用一次）
     * @param i_AppKey     应用编号
     * @param i_LoginUser  登录用户。必要信息（loginAccount用户名称、password用户密码、appID应用编号、openID）
     *                         password：前缀为@@E@@时，表示已Base64位过了
     * @param i_Request
     * @param i_Response
     * @return             登录成功时，
     *                         1. 不返回密码
     *                         2. 会返回loginAccount：当时用于登录的账号
     *                         3. 会返回会话级票据
     *                         4. 首次登录时，会绑定微信OpenID，防止账号被盗号
     *                      登录失败时，
     *                         1. 记录一定时间内的失败次数，防止暴力破解
     */
    @RequestMapping(value="loginWeiXin" ,produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public BaseResponse<UserInfo> loginWeiXin(@RequestParam(value="code"   ,required=false) String i_Code
                                             ,@RequestParam(value="appKey" ,required=false) String i_AppKey
                                             ,@RequestBody UserInfo i_LoginUser
                                             ,HttpServletRequest    i_Request
                                             ,HttpServletResponse   i_Response)
    {
        BaseResponse<UserInfo> v_Ret    = new BaseResponse<UserInfo>();
        AppKey                 v_AppKey = null;
        
        if ( Help.isNull(i_AppKey) )
        {
            $Logger.info("登录验证：非法访问");
            return v_Ret.setCode("901").setMessage("登录验证：非法访问");
        }
        
        v_AppKey = this.appKeys.get(i_AppKey);
        if ( v_AppKey == null )
        {
            $Logger.info("登录验证：应用编号无效");
            return v_Ret.setCode("901").setMessage("登录验证：应用编号无效");
        }
        
        if ( isCheckToken != null && Boolean.parseBoolean(isCheckToken.getValue()) )
        {
            if ( Help.isNull(i_Code) )
            {
                $Logger.info("登录验证：非法访问");
                return v_Ret.setCode("901").setMessage("登录验证：非法访问");
            }
        }
        
        if ( i_LoginUser == null || Help.isNull(i_LoginUser.getLoginAccount()) || Help.isNull(i_LoginUser.getPassword()) )
        {
            $Logger.info("登录验证：账号、密码为空");
            return v_Ret.setCode("902").setMessage("登录验证：账号、密码为空");
        }
        i_LoginUser.setAppKey(i_AppKey);
        
        if ( Help.isNull(i_LoginUser.getAppID()) )
        {
            $Logger.info("登录验证：微信AppID为空");
            return v_Ret.setCode("903").setMessage("登录验证：微信AppID为空");
        }
        
        if ( Help.isNull(i_LoginUser.getOpenID()) )
        {
            $Logger.info("登录验证：微信OpenID为空");
            return v_Ret.setCode("904").setMessage("登录验证：微信OpenID为空");
        }
        
        int v_AccountMaxLen = Integer.parseInt(this.accountMaxLen.getValue());
        if ( i_LoginUser.getLoginAccount().length() >= v_AccountMaxLen )
        {
            $Logger.info("登录验证：账号长度超出允许范围（最大" + v_AccountMaxLen + "）：" + i_LoginUser.getAppKey() + "：" + i_LoginUser.getLoginAccount() + "：" + i_LoginUser.getOpenID());
            return v_Ret.setCode("905").setMessage("登录验证：账号长度超出允许范围（最大" + v_AccountMaxLen + "）");
        }
        
        String [] v_AccountIllegalChar = this.accountIllegalChar.getValue().split("-");
        if ( StringHelp.isContains(i_LoginUser.getLoginAccount() ,v_AccountIllegalChar) )
        {
            $Logger.info("登录验证：账号禁止非法字符" + i_LoginUser.getAppKey() + "：" + i_LoginUser.getLoginAccount() + "：" + i_LoginUser.getOpenID());
            return v_Ret.setCode("906").setMessage("登录验证：账号禁止非法字符");
        }
        
        if ( StringHelp.isContains(i_LoginUser.getAppID() ,v_AccountIllegalChar) )
        {
            $Logger.info("登录验证：AppID禁止非法字符" + i_LoginUser.getAppKey() + "：" + i_LoginUser.getLoginAccount() + "：" + i_LoginUser.getOpenID());
            return v_Ret.setCode("907").setMessage("登录验证：AppID禁止非法字符");
        }
        
        if ( StringHelp.isContains(i_LoginUser.getOpenID() ,v_AccountIllegalChar) )
        {
            $Logger.info("登录验证：OpenID禁止非法字符" + i_LoginUser.getAppKey() + "：" + i_LoginUser.getLoginAccount() + "：" + i_LoginUser.getOpenID());
            return v_Ret.setCode("908").setMessage("登录验证：OpenID禁止非法字符");
        }
        
        boolean v_IsLock = this.loginIsLock(i_LoginUser ,i_Request);
        if ( v_IsLock )
        {
            $Logger.info("登录验证：账户被锁定：" + i_LoginUser.getAppKey() + "：" + i_LoginUser.getLoginAccount() + "：" + i_LoginUser.getOpenID());
            return v_Ret.setCode("911").setMessage("登录验证：账户被锁定");
        }
        
        UserAccount v_UAccount = this.userInfoService.queryAccountNo(i_LoginUser.getAppKey() ,i_LoginUser.getLoginAccount());
        if ( v_UAccount == null )
        {
            $Logger.info("登录验证：非法账户：" + i_LoginUser.getAppKey() + "：" + i_LoginUser.getLoginAccount() + "：" + i_LoginUser.getOpenID());
            this.loginLock(i_LoginUser ,i_Request);
            return v_Ret.setCode("912").setMessage("登录验证：非法账户");
        }
        i_LoginUser.setId(v_UAccount.getUserGID());
        
        UserInfo v_RetUser = this.userInfoService.checkLogin(i_LoginUser);
        if ( v_RetUser == null )
        {
            $Logger.info("登录验证：非法账户：" + i_LoginUser.getAppKey() + "：" + i_LoginUser.getLoginAccount() + "：" + i_LoginUser.getOpenID());
            this.loginLock(i_LoginUser ,i_Request);
            return v_Ret.setCode("913").setMessage("登录验证：非法账户");
        }
        
        if ( !Help.isNull(v_RetUser.getOpenID()) && !i_LoginUser.getOpenID().equals(v_RetUser.getOpenID()) )
        {
            $Logger.info("登录验证：绑定微信用户不一致：" + i_LoginUser.getAppKey() + "：" + i_LoginUser.getLoginAccount() + "：" + i_LoginUser.getOpenID());
            this.loginLock(i_LoginUser ,i_Request);
            return v_Ret.setCode("914").setMessage("登录验证：绑定微信用户不一致");
        }
        
        if ( isCheckToken != null && Boolean.parseBoolean(isCheckToken.getValue()) )
        {
            TokenInfo v_SessionToken = this.userService.loginUser(i_Code ,v_AppKey ,v_RetUser);
            if ( v_SessionToken == null )
            {
                $Logger.info("登录验证：临时登录Code无效或已过期：" + i_LoginUser.getAppKey() + "：" + i_LoginUser.getLoginAccount() + "：" + i_LoginUser.getOpenID());
                this.loginLock(i_LoginUser ,i_Request);
                return v_Ret.setCode("915").setMessage("登录验证：临时登录Code无效或已过期");
            }
            
            v_RetUser.setToken(v_SessionToken);
        }
        
        // 首次登录后，绑定OpenID
        if ( Help.isNull(v_RetUser.getOpenID()) )
        {
            if ( !this.userInfoService.bindingOpenID(i_LoginUser) )
            {
                $Logger.info("登录验证：绑定微信用户异常：" + i_LoginUser.getAppKey() + "：" + i_LoginUser.getLoginAccount() + "：" + i_LoginUser.getOpenID());
                this.loginLock(i_LoginUser ,i_Request);
                return v_Ret.setCode("916").setMessage("登录验证：绑定微信用户异常");
            }
        }
        
        v_RetUser.setLoginAccount(i_LoginUser.getLoginAccount());
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
            int v_LoginLockTimeLen  = Integer.parseInt(this.loginLockTimeLen .getValue());
            
            // 客户端尝试登录三次后，不允许再连续登录
            String v_SessionID = i_Request.getSession().getId();
            if ( !Help.isNull(v_SessionID) )
            {
                Integer v_LCount = (Integer)datasPool.get($DP_SessionID + v_SessionID);
                
                if ( v_LCount == null || v_LCount <= 0 )
                {
                    datasPool.put($DP_SessionID + v_SessionID ,1            ,60 * v_LoginLockTimeLen);
                }
                else if ( v_LCount < v_LoginLockMaxCount )
                {
                    datasPool.put($DP_SessionID + v_SessionID ,v_LCount + 1 ,60 * v_LoginLockTimeLen);
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
                    datasPool.put($DP_LoginAccount + i_LoginUser.getAppID() + "_" + i_LoginUser.getLoginAccount() ,1            ,60 * v_LoginLockTimeLen);
                }
                else if ( v_LCount < v_LoginLockMaxCount )
                {
                    datasPool.put($DP_LoginAccount + i_LoginUser.getAppID() + "_" + i_LoginUser.getLoginAccount() ,v_LCount + 1 ,60 * v_LoginLockTimeLen);
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
                    datasPool.put($DP_OpenID + i_LoginUser.getOpenID() ,1            ,60 * v_LoginLockTimeLen);
                }
                else if ( v_LCount < v_LoginLockMaxCount )
                {
                    datasPool.put($DP_OpenID + i_LoginUser.getOpenID() ,v_LCount + 1 ,60 * v_LoginLockTimeLen);
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
