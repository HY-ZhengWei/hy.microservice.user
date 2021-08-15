package org.hy.microservice.user.user;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.hy.common.Date;
import org.hy.common.Help;
import org.hy.common.Return;
import org.hy.common.license.AppKey;
import org.hy.common.license.Signaturer;
import org.hy.common.license.sign.ISignaturer;
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
    
    public  static final String $Succeed = "200";
    
    
    
    @Xjava(ref="XHTTP_MS_User_GetLoginUser")
    private XHttp xhGetLoginUser;
    
    @Xjava(ref="XHTTP_MS_User_GetAccessToken")
    private XHttp xhGetAccessToken;
    
    @Xjava(ref="XHTTP_MS_User_SetLoginUser")
    private XHttp xhSetLoginUser;
    
    
    
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
    
    
    
    /**
     * 获取登录临时Code
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-08-15
     * @version     v1.0
     *
     * @param i_AppKey  应用编号
     *
     * @return
     */
    public TokenInfo getCode(AppKey i_AppKey)
    {
        try
        {
            long        v_Timestamp = Date.getNowTime().getTime();
            ISignaturer v_Sign      = new Signaturer(i_AppKey.getPrivateKey());
            String      v_Signature = v_Sign.sign("appKey" + i_AppKey.getAppKey() + "timestamp" + v_Timestamp);
            Map<String ,Object> v_ReqParams = new HashMap<String ,Object>();
            v_ReqParams.put("appKey"    ,i_AppKey.getAppKey());
            v_ReqParams.put("timestamp" ,v_Timestamp);
            v_ReqParams.put("signature" ,URLEncoder.encode(v_Signature ,"UTF-8"));
            
            // $Logger.error("集成认证登录：" + appKey.getAppKey() + " - " + v_Timestamp + " - " + v_Signature);
            
            Return<?> v_Ret = xhGetAccessToken.request(v_ReqParams);
            
            if ( v_Ret != null && v_Ret.booleanValue() && !Help.isNull(v_Ret.getParamStr()) )
            {
                XJSON v_XJson = new XJSON();
                
                TokenResponse v_Data = (TokenResponse)v_XJson.toJava(v_Ret.getParamStr() ,TokenResponse.class);
                
                if ( v_Data != null )
                {
                    if ( $Succeed.equals(v_Data.getCode()) && v_Data.getData() != null && v_Data.getData().getData() != null )
                    {
                        TokenInfo v_Token = v_Data.getData().getData();
                        $Logger.info("获取Token：" + v_Data.getCode() + " : " + v_Token.getAccessToken() + " ,过期时长：" + v_Token.getExpire());
                        return v_Token;
                    }
                    else
                    {
                        $Logger.error("获取Token异常：" + v_Data.getCode() + " - " + v_Data.getMessage());
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
    
    
    
    /**
     * 用户登录
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-08-15
     * @version     v1.0
     *
     * @param i_Code    临时Code
     * @param i_AppKey  应用编号
     * @param i_UserSSO
     */
    public TokenInfo loginUser(String i_Code ,AppKey i_AppKey ,UserSSO i_UserSSO)
    {
        i_UserSSO.setAppKey(i_AppKey.getAppKey());
        
        Map<String ,Object> v_ReqParams = new HashMap<String ,Object>();
        v_ReqParams.put("code" ,i_Code);
        
        try
        {
            XJSON v_XJson = new XJSON();
            v_XJson.setReturnNVL(false);
            
            Return<?> v_Ret = xhSetLoginUser.request(v_ReqParams ,v_XJson.toJson(i_UserSSO).toJSONString());
            
            if ( v_Ret != null && v_Ret.booleanValue() && !Help.isNull(v_Ret.getParamStr()) )
            {
                TokenResponse v_Data = (TokenResponse)v_XJson.toJava(v_Ret.getParamStr() ,TokenResponse.class);
                
                if ( v_Data != null )
                {
                    if ( $Succeed.equals(v_Data.getCode()) )
                    {
                        return v_Data.getData().getData();
                    }
                    else
                    {
                        return null;
                    }
                }
            }
        }
        catch (Exception exce)
        {
            exce.printStackTrace();
        }
        
        return null;
    }
    
}
