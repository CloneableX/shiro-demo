package com.clo.session.mgt;

import org.apache.shiro.session.mgt.SimpleSession;

public class OnlineSession extends SimpleSession {
    public static enum OnlineStatus {
        on_line("在线"),hidden("隐身"),force_logout("强制退出");
        private final String info;

        private OnlineStatus(String info) {
            this.info = info;
        }

        public String getInfo() {
            return this.info;
        }
    }
    private String userAgent;
    private OnlineStatus status = OnlineStatus.on_line;
    private String systemHost;

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public OnlineStatus getStatus() {
        return status;
    }

    public void setStatus(OnlineStatus status) {
        this.status = status;
    }

    public String getSystemHost() {
        return systemHost;
    }

    public void setSystemHost(String systemHost) {
        this.systemHost = systemHost;
    }
}
