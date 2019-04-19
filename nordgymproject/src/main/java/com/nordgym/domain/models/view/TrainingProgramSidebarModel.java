package com.nordgym.domain.models.view;

public class TrainingProgramSidebarModel {
    private Long id;
    private String header;
    private String programImagePath;

    public TrainingProgramSidebarModel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getProgramImagePath() {
        return programImagePath;
    }

    public void setProgramImagePath(String programImagePath) {
        this.programImagePath = programImagePath;
    }
}
