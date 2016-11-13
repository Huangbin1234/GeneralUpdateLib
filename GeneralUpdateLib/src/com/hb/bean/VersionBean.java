package com.hb.bean;

/**
 * 版本更新Bean
 * Created by Administrator on 2016/11/13.
 */
public class VersionBean {

    String verCode;// 版本号
    String verName;// 版本名
    String content;// 更新内容
    String downloadUrl;// 下载地址

    public String getVerCode() {
        return verCode;
    }

    public void setVerCode(String verCode) {
        this.verCode = verCode;
    }

    public String getVerName() {
        return verName;
    }

    public void setVerName(String verName) {
        this.verName = verName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
}
