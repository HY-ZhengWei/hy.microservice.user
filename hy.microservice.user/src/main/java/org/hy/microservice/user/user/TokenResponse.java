package org.hy.microservice.user.user;

import org.hy.microservice.common.BaseResp;





/**
 * 访问Token的数据结构
 *
 * @author      ZhengWei(HY)
 * @createDate  2020-12-22
 * @version     v1.0
 */
public class TokenResponse extends BaseResp
{
    
    private static final long serialVersionUID = 2652864387178948149L;
    
    /** 响应数据 */
    private TokenResponseData data;
    
    
    
    /**
     * 获取：响应数据
     */
    public TokenResponseData getData()
    {
        return this.data;
    }
    
    
    /**
     * 设置：响应数据
     * 
     * @param data
     */
    public TokenResponse setData(TokenResponseData data)
    {
        this.data = data;
        return this;
    }

}
