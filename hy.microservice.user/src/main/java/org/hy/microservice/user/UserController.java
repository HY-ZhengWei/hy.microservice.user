package org.hy.microservice.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hy.common.Help;
import org.hy.common.StringHelp;
import org.hy.common.app.Param;
import org.hy.common.xml.log.Logger;
import org.hy.microservice.common.BaseResponse;
import org.hy.microservice.user.account.UserAccount;
import org.hy.microservice.user.user.UserSSO;
import org.hy.microservice.user.user.UserService;
import org.hy.microservice.user.userInfo.UserInfo;
import org.hy.microservice.user.userInfo.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;





/**
 * 用户中心的控制层
 *
 * @author      ZhengWei(HY)
 * @createDate  2021-08-06
 * @version     v1.0
 */
@Controller
@RestController
@RequestMapping("user")
public class UserController
{
    
    private static final Logger $Logger = new Logger(UserController.class);
    
    @Autowired
    @Qualifier("MS_User_IsCheckToken")
    private Param isCheckToken;
    
    @Autowired
    @Qualifier("MS_User_AccountMaxLen")
    private Param accountMaxLen;
    
    @Autowired
    @Qualifier("MS_User_CheckIllegalChar")
    private Param accountIllegalChar;
    
    @Autowired
    @Qualifier("UserService")
    private UserService userService;
    
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
     * @param i_Token        认证票据号
     * @param i_CreateUser   创建的用户信息
     * @param i_Request
     * @param i_Response
     * @return
     */
    @RequestMapping(value="createUser" ,produces=MediaType.APPLICATION_JSON_VALUE ,method= {RequestMethod.POST})
    @ResponseBody
    public BaseResponse<UserInfo> createUser(@RequestParam(value="token" ,required=false) String i_Token
                                            ,@RequestBody UserInfo i_CreateUser
                                            ,HttpServletRequest    i_Request
                                            ,HttpServletResponse   i_Response)
    {
        BaseResponse<UserInfo> v_RetResp = new BaseResponse<UserInfo>();
        
        UserSSO v_User = null;
        if ( isCheckToken != null && Boolean.parseBoolean(isCheckToken.getValue()) )
        {
            // 验证票据及用户登录状态
            if ( Help.isNull(i_Token) )
            {
                return v_RetResp.setCode("-901").setMessage("非法访问");
            }
            
            v_User = this.userService.getUser(i_Token);
            if ( v_User == null )
            {
                return v_RetResp.setCode("-901").setMessage("非法访问");
            }
            
            if ( !v_User.getAppKey().equals(i_CreateUser.getAppKey()) )
            {
                return v_RetResp.setCode("-901").setMessage("无权访问");
            }
        }
        
        if ( i_CreateUser == null || Help.isNull(i_CreateUser.getUserName()) )
        {
            $Logger.info("创建用户：用户名称为空");
            return v_RetResp.setCode("902").setMessage("创建用户：用户名称为空");
        }
        
        if ( Help.isNull(i_CreateUser.getAppKey()) )
        {
            $Logger.info("创建用户：所属系统编号为空");
            return v_RetResp.setCode("903").setMessage("创建用户：所属系统编号为空");
        }
        
        if ( Help.isNull(i_CreateUser.getCardID())
          && Help.isNull(i_CreateUser.getEmail())
          && Help.isNull(i_CreateUser.getPhone())
          && Help.isNull(i_CreateUser.getUserCode())
          && Help.isNull(i_CreateUser.getUserNo()) )
        {
            $Logger.info("创建用户：身份证、电子邮箱、手机号、用户编号和工号五者必须有其一");
            return v_RetResp.setCode("904").setMessage("创建用户：身份证、电子邮箱、手机号、用户编号和工号五者必须有其一");
        }
        
        int         v_AccountMaxLen      = Integer.parseInt(this.accountMaxLen.getValue());
        String []   v_AccountIllegalChar = this.accountIllegalChar.getValue().split("-");
        UserAccount v_IsHaveAccount      = null;
        if ( !Help.isNull(i_CreateUser.getCardID()) )
        {
            if ( i_CreateUser.getCardID().length() >= v_AccountMaxLen )
            {
                $Logger.info("创建用户：身份证长度超出允许范围（最大" + v_AccountMaxLen + "）：" + i_CreateUser.getAppKey() + "：" + i_CreateUser.getUserName());
                return v_RetResp.setCode("905").setMessage("创建用户：身份证长度超出允许范围（最大" + v_AccountMaxLen + "）");
            }
            
            if ( StringHelp.isContains(i_CreateUser.getCardID() ,v_AccountIllegalChar) )
            {
                $Logger.info("创建用户：身份证禁止非法字符" + i_CreateUser.getAppKey() + "：" + i_CreateUser.getLoginAccount() + "：" + i_CreateUser.getUserName());
                return v_RetResp.setCode("906").setMessage("创建用户：身份证禁止非法字符");
            }
            
            v_IsHaveAccount = this.userInfoService.queryAccountNo(i_CreateUser.getAppKey() ,i_CreateUser.getCardID());
            if ( v_IsHaveAccount != null )
            {
                $Logger.info("创建用户：身份证已存在" + i_CreateUser.getAppKey() + "：" + i_CreateUser.getLoginAccount() + "：" + i_CreateUser.getUserName());
                return v_RetResp.setCode("907").setMessage("创建用户：身份证已存在");
            }
        }
        
        if ( !Help.isNull(i_CreateUser.getEmail()) )
        {
            if ( i_CreateUser.getEmail().length() >= v_AccountMaxLen )
            {
                $Logger.info("创建用户：电子邮箱长度超出允许范围（最大" + v_AccountMaxLen + "）：" + i_CreateUser.getAppKey() + "：" + i_CreateUser.getUserName());
                return v_RetResp.setCode("905").setMessage("创建用户：电子邮箱长度超出允许范围（最大" + v_AccountMaxLen + "）");
            }
            
            if ( StringHelp.isContains(i_CreateUser.getEmail() ,v_AccountIllegalChar) )
            {
                $Logger.info("创建用户：电子邮箱禁止非法字符" + i_CreateUser.getAppKey() + "：" + i_CreateUser.getLoginAccount() + "：" + i_CreateUser.getUserName());
                return v_RetResp.setCode("906").setMessage("创建用户：电子邮箱禁止非法字符");
            }
            
            v_IsHaveAccount = this.userInfoService.queryAccountNo(i_CreateUser.getAppKey() ,i_CreateUser.getEmail());
            if ( v_IsHaveAccount != null )
            {
                $Logger.info("创建用户：电子邮箱已存在" + i_CreateUser.getAppKey() + "：" + i_CreateUser.getLoginAccount() + "：" + i_CreateUser.getUserName());
                return v_RetResp.setCode("907").setMessage("创建用户：电子邮箱已存在");
            }
        }
        
        if ( !Help.isNull(i_CreateUser.getPhone()) )
        {
            if ( i_CreateUser.getPhone().length() >= v_AccountMaxLen )
            {
                $Logger.info("创建用户：手机号长度超出允许范围（最大" + v_AccountMaxLen + "）：" + i_CreateUser.getAppKey() + "：" + i_CreateUser.getUserName());
                return v_RetResp.setCode("905").setMessage("创建用户：手机号长度超出允许范围（最大" + v_AccountMaxLen + "）");
            }
            
            if ( StringHelp.isContains(i_CreateUser.getPhone() ,v_AccountIllegalChar) )
            {
                $Logger.info("创建用户：手机号禁止非法字符" + i_CreateUser.getAppKey() + "：" + i_CreateUser.getLoginAccount() + "：" + i_CreateUser.getUserName());
                return v_RetResp.setCode("906").setMessage("创建用户：手机号禁止非法字符");
            }
            
            v_IsHaveAccount = this.userInfoService.queryAccountNo(i_CreateUser.getAppKey() ,i_CreateUser.getEmail());
            if ( v_IsHaveAccount != null )
            {
                $Logger.info("创建用户：手机号已存在" + i_CreateUser.getAppKey() + "：" + i_CreateUser.getLoginAccount() + "：" + i_CreateUser.getUserName());
                return v_RetResp.setCode("907").setMessage("创建用户：手机号已存在");
            }
        }
        
        if ( !Help.isNull(i_CreateUser.getUserCode()) )
        {
            if ( i_CreateUser.getUserCode().length() >= v_AccountMaxLen )
            {
                $Logger.info("创建用户：用户编号长度超出允许范围（最大" + v_AccountMaxLen + "）：" + i_CreateUser.getAppKey() + "：" + i_CreateUser.getUserName());
                return v_RetResp.setCode("905").setMessage("创建用户：用户编号长度超出允许范围（最大" + v_AccountMaxLen + "）");
            }
            
            if ( StringHelp.isContains(i_CreateUser.getUserCode() ,v_AccountIllegalChar) )
            {
                $Logger.info("创建用户：用户编号禁止非法字符" + i_CreateUser.getAppKey() + "：" + i_CreateUser.getLoginAccount() + "：" + i_CreateUser.getUserName());
                return v_RetResp.setCode("906").setMessage("创建用户：用户编号禁止非法字符");
            }
            
            v_IsHaveAccount = this.userInfoService.queryAccountNo(i_CreateUser.getAppKey() ,i_CreateUser.getUserCode());
            if ( v_IsHaveAccount != null )
            {
                $Logger.info("创建用户：用户编号已存在" + i_CreateUser.getAppKey() + "：" + i_CreateUser.getLoginAccount() + "：" + i_CreateUser.getUserName());
                return v_RetResp.setCode("907").setMessage("创建用户：用户编号已存在");
            }
        }
        
        if ( !Help.isNull(i_CreateUser.getUserNo()) )
        {
            if ( i_CreateUser.getUserNo().length() >= v_AccountMaxLen )
            {
                $Logger.info("创建用户：工号长度超出允许范围（最大" + v_AccountMaxLen + "）：" + i_CreateUser.getAppKey() + "：" + i_CreateUser.getUserName());
                return v_RetResp.setCode("905").setMessage("创建用户：工号长度超出允许范围（最大" + v_AccountMaxLen + "）");
            }
            
            if ( StringHelp.isContains(i_CreateUser.getUserNo() ,v_AccountIllegalChar) )
            {
                $Logger.info("创建用户：用工号禁止非法字符" + i_CreateUser.getAppKey() + "：" + i_CreateUser.getLoginAccount() + "：" + i_CreateUser.getUserName());
                return v_RetResp.setCode("906").setMessage("创建用户：工号禁止非法字符");
            }
            
            v_IsHaveAccount = this.userInfoService.queryAccountNo(i_CreateUser.getAppKey() ,i_CreateUser.getUserNo());
            if ( v_IsHaveAccount != null )
            {
                $Logger.info("创建用户：工号已存在" + i_CreateUser.getAppKey() + "：" + i_CreateUser.getLoginAccount() + "：" + i_CreateUser.getUserName());
                return v_RetResp.setCode("907").setMessage("创建用户：工号已存在");
            }
        }
        
        if ( !Help.isNull(i_CreateUser.getOpenID()) )
        {
            if ( i_CreateUser.getOpenID().length() >= v_AccountMaxLen )
            {
                $Logger.info("创建用户：OpenID长度超出允许范围（最大" + v_AccountMaxLen + "）：" + i_CreateUser.getAppKey() + "：" + i_CreateUser.getUserName());
                return v_RetResp.setCode("905").setMessage("创建用户：OpenID长度超出允许范围（最大" + v_AccountMaxLen + "）");
            }
            
            if ( StringHelp.isContains(i_CreateUser.getOpenID() ,v_AccountIllegalChar) )
            {
                $Logger.info("创建用户：OpenID禁止非法字符" + i_CreateUser.getAppKey() + "：" + i_CreateUser.getLoginAccount() + "：" + i_CreateUser.getUserName());
                return v_RetResp.setCode("906").setMessage("创建用户：OpenID禁止非法字符");
            }
        }
        
        
        if ( isCheckToken != null && Boolean.parseBoolean(isCheckToken.getValue()) )
        {
            if ( !i_CreateUser.getAppKey().equals(v_User.getAppKey()) )
            {
                $Logger.info("创建用户：创建用户的用户不能跨权系统创建用户"  + i_CreateUser.getAppKey() + "：" + i_CreateUser.getLoginAccount() + "：" + i_CreateUser.getUserName());
                return v_RetResp.setCode("907").setMessage("创建用户：创建用户的用户不能跨权系统创建用户");
            }
            
            // 临时用账号名称来判定是否有创建用户的权限。
            if ( !"administrator".equals(v_User.getUserCode()) )
            {
                $Logger.info("创建用户：创建用户的用户没有权限"  + i_CreateUser.getAppKey() + "：" + i_CreateUser.getLoginAccount() + "：" + i_CreateUser.getUserName());
                return v_RetResp.setCode("908").setMessage("创建用户：创建用户的用户没有权限");
            }
        }
        
        
        UserInfo v_CreateUser = this.userInfoService.createUser(i_CreateUser);
        if ( v_CreateUser == null )
        {
            $Logger.info("创建用户失败：" + i_CreateUser.getAppKey() + ":"+ i_CreateUser.getUserName());
            return v_RetResp.setCode("911").setMessage("创建用户失败");
        }
        else
        {
            $Logger.info("创建用户成功" + i_CreateUser.getAppKey() + ":"+ i_CreateUser.getUserName());
            return v_RetResp.setData(v_CreateUser);
        }
    }
    
}
