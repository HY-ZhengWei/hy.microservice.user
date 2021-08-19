package org.hy.microservice.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hy.common.Help;
import org.hy.common.StringHelp;
import org.hy.common.app.Param;
import org.hy.common.xml.log.Logger;
import org.hy.microservice.common.BaseResponse;
import org.hy.microservice.user.role.RoleInfo;
import org.hy.microservice.user.role.RoleInfoService;
import org.hy.microservice.user.user.UserSSO;
import org.hy.microservice.user.user.UserService;
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
 * 用户中心：角色的控制层
 *
 * @author      ZhengWei(HY)
 * @createDate  2021-08-18
 * @version     v1.0
 */
@Controller
@RestController
@RequestMapping("role")
public class RoleController
{
    
    private static final Logger $Logger = new Logger(RoleController.class);
    
    @Autowired
    @Qualifier("MS_User_IsCheckToken")
    private Param isCheckToken;
    
    @Autowired
    @Qualifier("MS_User_CheckIllegalChar")
    private Param roleIllegalChar;
    
    @Autowired
    @Qualifier("UserService")
    private UserService userService;
    
    @Autowired
    @Qualifier("RoleInfoService")
    private RoleInfoService roleService;
    
    
    
    /**
     * 添加角色接口
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-08-19
     * @version     v1.0
     * 
     * @param i_Token        认证票据号
     * @param i_CreateRole   创建的角色信息
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
    @RequestMapping(value="addRole" ,produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public BaseResponse<RoleInfo> addRole(@RequestParam(value="token" ,required=false) String i_Token
                                         ,@RequestBody RoleInfo i_CreateRole
                                         ,HttpServletRequest    i_Request
                                         ,HttpServletResponse   i_Response)
    {
        BaseResponse<RoleInfo> v_RetResp = new BaseResponse<RoleInfo>();
        
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
            
            if ( !v_User.getAppKey().equals(i_CreateRole.getAppKey()) )
            {
                return v_RetResp.setCode("-901").setMessage("无权访问");
            }
        }
        
        if ( i_CreateRole == null || Help.isNull(i_CreateRole.getRoleName()) )
        {
            $Logger.info("创建角色：角色名称为空");
            return v_RetResp.setCode("902").setMessage("创建角色：角色名称为空");
        }
        
        if ( Help.isNull(i_CreateRole.getAppKey()) )
        {
            $Logger.info("创建角色：所属系统编号为空");
            return v_RetResp.setCode("903").setMessage("创建角色：所属系统编号为空");
        }
        
        String [] v_RoleIllegalChar = this.roleIllegalChar.getValue().split("-");
        if ( StringHelp.isContains(i_CreateRole.getRoleName() ,v_RoleIllegalChar) )
        {
            $Logger.info("创建角色：角色名称禁止非法字符" + i_CreateRole.getAppKey() + "：" + i_CreateRole.getRoleName());
            return v_RetResp.setCode("904").setMessage("创建角色：角色名称禁止非法字符");
        }
        
        if ( !Help.isNull(i_CreateRole.getRemarks()) )
        {
            if ( StringHelp.isContains(i_CreateRole.getRemarks() ,v_RoleIllegalChar) )
            {
                $Logger.info("创建角色：角色说明禁止非法字符" + i_CreateRole.getAppKey() + "：" + i_CreateRole.getRoleName());
                return v_RetResp.setCode("905").setMessage("创建角色：角色说明禁止非法字符");
            }
        }
        
        RoleInfo v_Role = this.roleService.addRole(i_CreateRole);
        if ( v_Role == null )
        {
            $Logger.info("创建角色失败：" + i_CreateRole.getAppKey() + "：" + i_CreateRole.getRoleName());
            return v_RetResp.setCode("-911").setMessage("创建角色失败");
        }
        else
        {
            $Logger.info("创建角色成功" + i_CreateRole.getAppKey() + "：" + i_CreateRole.getRoleName());
            return v_RetResp.setData(v_Role);
        }
    }
    
}
