package me.veteran0.vpermission.misc;


import java.util.List;

public class GroupsAPI {

    private String prefixchat;
    private String prefixtab;
    private boolean changetag;
    private List<String> permissionsallow;
    private List<String> permissionsdeny;


    public GroupsAPI(String prefixchat, String prefixtab, boolean changetag, List<String> permissionsallow, List<String> permissionsdeny) {
        setPrefixchat(prefixchat);
        setPrefixtab(prefixtab);
        setChangetag(changetag);
        setPermissionsallow(permissionsallow);
        setPermissionsdeny(permissionsdeny);
    }

    public String getPrefixchat() {
        return prefixchat;
    }

    public void setPrefixchat(String prefixchat) {
        this.prefixchat = prefixchat;
    }

    public String getPrefixtab() {
        return prefixtab;
    }

    public void setPrefixtab(String prefixtab) {
        this.prefixtab = prefixtab;
    }

    public boolean isChangetag() {
        return changetag;
    }

    public void setChangetag(boolean changetag) {
        this.changetag = changetag;
    }

    public List<String> getPermissionsallow() {
        return permissionsallow;
    }

    public void setPermissionsallow(List<String> permissionsallow) {
        this.permissionsallow = permissionsallow;
    }

    public List<String> getPermissionsdeny() {
        return permissionsdeny;
    }

    public void setPermissionsdeny(List<String> permissionsdeny) {
        this.permissionsdeny = permissionsdeny;
    }


}
