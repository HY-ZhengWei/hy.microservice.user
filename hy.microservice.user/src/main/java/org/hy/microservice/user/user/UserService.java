package org.hy.microservice.user.user;

import java.util.HashMap;
import java.util.Map;

import org.hy.common.Help;
import org.hy.common.Return;
import org.hy.common.xml.XHttp;
import org.hy.common.xml.XJSON;
import org.hy.common.xml.annotation.Xjava;
import org.hy.common.xml.log.Logger;
import org.hy.microservice.common.BaseResponse;





/**
 * 用户业务
 *
 * @author      ZhengWei(HY)
 * @createDate  2021-02-04
 * @version     v1.0
 */
@Xjava
public class UserService
{
    private static Logger $Logger = Logger.getLogger(UserService.class);
    
    @Xjava(ref="XHTTP_MS_User_GetLoginUser")
    private XHttp xhGetLoginUser;
    
    
    
    /**
     * 获取已登录的用户信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-02-04
     * @version     v1.0
     *
     * @param i_Token
     * @return
     */
    public UserSSO getUser(String i_Token)
    {
        Map<String ,Object> v_ReqParams = new HashMap<String ,Object>();
        v_ReqParams.put("token" ,i_Token);
        
        try
        {
            XJSON v_XJson = new XJSON();
            v_XJson.setReturnNVL(false);
            
            Return<?> v_Ret = xhGetLoginUser.request(v_ReqParams);
            
            if ( v_Ret != null && v_Ret.booleanValue() && !Help.isNull(v_Ret.getParamStr()) )
            {
                GetLoginUserRequest v_Data = (GetLoginUserRequest)v_XJson.toJava(v_Ret.getParamStr() ,GetLoginUserRequest.class);
                
                if ( v_Data != null )
                {
                    if ( BaseResponse.$Succeed.equals(v_Data.getCode()) && v_Data.getData() != null )
                    {
                        return v_Data.getData().getData();
                    }
                }
            }
        }
        catch (Exception exce)
        {
            $Logger.error(exce);
        }
        
        return null;
    }
    
}
