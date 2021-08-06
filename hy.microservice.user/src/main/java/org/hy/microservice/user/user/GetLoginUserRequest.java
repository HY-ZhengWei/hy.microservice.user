package org.hy.microservice.user.user;

import org.hy.common.xml.SerializableDef;





/**
 * 获取在线登录的用户
 *
 * @author      ZhengWei(HY)
 * @createDate  2021-02-04
 * @version     v1.0
 */
public class GetLoginUserRequest extends SerializableDef
{

    private static final long serialVersionUID = -1081709582648653311L;
    
    /** 响应代码 */
    private String code;
    
    /** 响应消息 */
    private String message;
    
    /** 响应数据 */
    private GetLoginUserRequestData data;

    
    
    /**
     * 获取：响应代码
     */
    public String getCode()
    {
        return code;
    }

    
    /**
     * 获取：响应消息
     */
    public String getMessage()
    {
        return message;
    }

    
    /**
     * 获取：响应数据
     */
    public GetLoginUserRequestData getData()
    {
        return data;
    }

    
    /**
     * 设置：响应代码
     * 
     * @param code
     */
    public void setCode(String code)
    {
        this.code = code;
    }

    
    /**
     * 设置：响应消息
     * 
     * @param message
     */
    public void setMessage(String message)
    {
        this.message = message;
    }

    
    /**
     * 设置：响应数据
     * 
     * @param data
     */
    public void setData(GetLoginUserRequestData data)
    {
        this.data = data;
    }
    
}
