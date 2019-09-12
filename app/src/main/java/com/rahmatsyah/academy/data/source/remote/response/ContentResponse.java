package com.rahmatsyah.academy.data.source.remote.response;

public class ContentResponse {
    private String moduleId;
    private String content;

    public ContentResponse(String moduleId, String content) {
        this.moduleId = moduleId;
        this.content = content;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getModuleId() {
        return moduleId;
    }

    public String getContent() {
        return content;
    }
}
