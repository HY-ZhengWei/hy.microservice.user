package org.hy.microservice.user;

import org.hy.microservice.common.BaseViewMode;





/**
 * 发帖子信息
 *
 * @author      ZhengWei(HY)
 * @createDate  2020-10-19
 * @version     v1.0
 */
public class PostInfo extends BaseViewMode
{
    
    private static final long serialVersionUID = -5018435706755947460L;

    /** 主键ID */
    private String  id;
    
    /** 帖子标题 */
    private String  title;
    
    /** 帖子分类编号 */
    private String  postTypeID;
    
    /** 帖子分类名称 */
    private String  postTypeName;
    
    /** 帖子视频地址 */
    private String  videoUrl;
    
    /** 帖子视频的文件ID */
    private String  videoUrlID;
    
    /** 帖子封面图片地址 */
    private String  coverUrl;
    
    /** 帖子封面图片的文件ID */
    private String  coverUrlID;
    
    /** 查看帖子的用户ID */
    private String  seePostUserID;
    
    /** 帖子内容 */
    private String  content;
    
    /** 阅读次数 */
    private Long    openCount;
    
    /** 我是否阅读过 */
    private Integer myIsOpen;
    
    /** 消息次数 */
    private Long    messageCount;
    
    /** 收藏次数 */
    private Long    favoritesCount;
    
    /** 我是否收藏过 */
    private Integer myIsFavorites;

    /** 我的评论数 */
    private Integer myIsComment;
    
    /** 点赞次数 */
    private Long    goodCount;
    
    /** 我是否点赞过 */
    private Integer myIsNice;
    
    /** 是否推荐。1推荐；0不推荐 */
    private Integer isRecommend;

    /** 我发帖的数量 */
    private Integer myIsPostCount;
    
    /** 关联对象的编号 */
    private String  relationID;
    
    /** 关联对象的名称 */
    private String  relationName;
    
    
    
    public String getMessageCountInfo()
    {
        return toCountInfo(this.messageCount);
    }
    
    
    public void setMessageCountInfo(String i_MessageCount)
    {
        // Nothing.
    }
    
    
    public String getFavoritesCountInfo()
    {
        return toCountInfo(this.favoritesCount);
    }
    
    
    public void setFavoritesCountInfo(String i_FavoritesCount)
    {
        // Nothing.
    }
    
    
    public String getGoodCountInfo()
    {
        return toCountInfo(this.goodCount);
    }
    
    
    public void setGoodCountInfo(String i_GoodCount)
    {
        // Nothing.
    }
    
    
    /**
     * 获取：主键ID
     */
    public String getId()
    {
        return id;
    }

    
    /**
     * 获取：帖子标题
     */
    public String getTitle()
    {
        return title;
    }

    
    /**
     * 获取：帖子封面图片地址
     */
    public String getCoverUrl()
    {
        return coverUrl;
    }

    
    /**
     * 获取：帖子内容
     */
    public String getContent()
    {
        return content;
    }

    
    /**
     * 获取：消息次数
     */
    public Long getMessageCount()
    {
        return messageCount;
    }

    
    /**
     * 获取：收藏次数
     */
    public Long getFavoritesCount()
    {
        return favoritesCount;
    }

    
    /**
     * 获取：点赞次数
     */
    public Long getGoodCount()
    {
        return goodCount;
    }

    
    /**
     * 获取：我是否点赞过
     */
    public Integer getMyIsNice()
    {
        return myIsNice;
    }

    
    /**
     * 设置：主键ID
     * 
     * @param id
     */
    public void setId(String id)
    {
        this.id = id;
    }

    
    /**
     * 设置：帖子标题
     * 
     * @param title
     */
    public void setTitle(String title)
    {
        this.title = title;
    }

    
    /**
     * 设置：帖子封面图片地址
     * 
     * @param coverUrl
     */
    public void setCoverUrl(String coverUrl)
    {
        this.coverUrl = coverUrl;
    }

    
    /**
     * 设置：帖子内容
     * 
     * @param content
     */
    public void setContent(String content)
    {
        this.content = content;
    }

    
    /**
     * 设置：消息次数
     * 
     * @param messageCount
     */
    public void setMessageCount(Long messageCount)
    {
        this.messageCount = messageCount;
    }

    
    /**
     * 设置：收藏次数
     * 
     * @param favoritesCount
     */
    public void setFavoritesCount(Long favoritesCount)
    {
        this.favoritesCount = favoritesCount;
    }

    
    /**
     * 设置：点赞次数
     * 
     * @param goodCount
     */
    public void setGoodCount(Long goodCount)
    {
        this.goodCount = goodCount;
    }

    
    /**
     * 设置：我是否点赞过
     * 
     * @param myIsNice
     */
    public void setMyIsNice(Integer myIsNice)
    {
        this.myIsNice = myIsNice;
    }

    
    /**
     * 获取：我是否收藏过
     */
    public Integer getMyIsFavorites()
    {
        return myIsFavorites;
    }

    
    /**
     * 设置：我是否收藏过
     * 
     * @param myIsFavorites
     */
    public void setMyIsFavorites(Integer myIsFavorites)
    {
        this.myIsFavorites = myIsFavorites;
    }

    
    /**
     * 获取：是否推荐。1推荐；0不推荐
     */
    public Integer getIsRecommend()
    {
        return isRecommend;
    }

    
    /**
     * 设置：是否推荐。1推荐；0不推荐
     * 
     * @param isRecommend
     */
    public void setIsRecommend(Integer isRecommend)
    {
        this.isRecommend = isRecommend;
    }


    /**
     * 获取：帖子分类编号
     */
    public String getPostTypeID()
    {
        return postTypeID;
    }

    
    /**
     * 获取：帖子分类名称
     */
    public String getPostTypeName()
    {
        return postTypeName;
    }

    
    /**
     * 设置：帖子分类编号
     * 
     * @param postTypeID
     */
    public void setPostTypeID(String postTypeID)
    {
        this.postTypeID = postTypeID;
    }


    /**
     * 设置：帖子分类名称
     * 
     * @param postTypeName
     */
    public void setPostTypeName(String postTypeName)
    {
        this.postTypeName = postTypeName;
    }

    
    /**
     * 获取：阅读次数
     */
    public Long getOpenCount()
    {
        return openCount;
    }

    
    /**
     * 获取：我是否阅读过
     */
    public Integer getMyIsOpen()
    {
        return myIsOpen;
    }

    
    /**
     * 设置：阅读次数
     * 
     * @param openCount
     */
    public void setOpenCount(Long openCount)
    {
        this.openCount = openCount;
    }

    
    /**
     * 设置：我是否阅读过
     * 
     * @param myIsOpen
     */
    public void setMyIsOpen(Integer myIsOpen)
    {
        this.myIsOpen = myIsOpen;
    }

    public Integer getMyIsComment() {
        return myIsComment;
    }

    public void setMyIsComment(Integer myIsComment) {
        this.myIsComment = myIsComment;
    }

    
    /**
     * 获取：我发帖的数量
     */
    public Integer getMyIsPostCount()
    {
        return myIsPostCount;
    }

    
    /**
     * 设置：我发帖的数量
     * 
     * @param myIsPostCount
     */
    public void setMyIsPostCount(Integer myIsPostCount)
    {
        this.myIsPostCount = myIsPostCount;
    }


    /**
     * 获取：帖子视频地址
     */
    public String getVideoUrl()
    {
        return videoUrl;
    }

    
    /**
     * 获取：帖子视频的文件ID
     */
    public String getVideoUrlID()
    {
        return videoUrlID;
    }

    
    /**
     * 获取：帖子封面图片的文件ID
     */
    public String getCoverUrlID()
    {
        return coverUrlID;
    }

    
    /**
     * 设置：帖子视频地址
     * 
     * @param videoUrl
     */
    public void setVideoUrl(String videoUrl)
    {
        this.videoUrl = videoUrl;
    }


    /**
     * 设置：帖子视频的文件ID
     * 
     * @param videoUrlID
     */
    public void setVideoUrlID(String videoUrlID)
    {
        this.videoUrlID = videoUrlID;
    }

    
    /**
     * 设置：帖子封面图片的文件ID
     * 
     * @param coverUrlID
     */
    public void setCoverUrlID(String coverUrlID)
    {
        this.coverUrlID = coverUrlID;
    }

    
    /**
     * 获取：关联对象的编号
     */
    public String getRelationID()
    {
        return relationID;
    }

    
    /**
     * 获取：关联对象的名称
     */
    public String getRelationName()
    {
        return relationName;
    }

    
    /**
     * 设置：关联对象的编号
     * 
     * @param relationID
     */
    public void setRelationID(String relationID)
    {
        this.relationID = relationID;
    }


    /**
     * 设置：关联对象的名称
     * 
     * @param relationName
     */
    public void setRelationName(String relationName)
    {
        this.relationName = relationName;
    }

    
    /**
     * 获取：查看帖子的用户ID
     */
    public String getSeePostUserID()
    {
        return seePostUserID;
    }


    /**
     * 设置：查看帖子的用户ID
     * 
     * @param seePostUserID
     */
    public void setSeePostUserID(String seePostUserID)
    {
        this.seePostUserID = seePostUserID;
    }
    
}
