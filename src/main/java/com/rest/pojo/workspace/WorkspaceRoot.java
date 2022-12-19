package com.rest.pojo.workspace;

public class WorkspaceRoot {
    Workspace workspace;

    public WorkspaceRoot(Workspace workspace) {
        this.workspace = workspace;
    }

    public WorkspaceRoot() {
    }

    public Workspace getWorkspace() {
        return workspace;
    }

    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
    }

}
