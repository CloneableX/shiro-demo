package com.clo.chapter3.permission;

import com.alibaba.druid.util.StringUtils;
import org.apache.shiro.authz.Permission;

public class BitPermission implements Permission {
    private String resourceId;
    private int permissionBit;
    private String instanceId;

    public BitPermission(String permissionStr) {
        String[] array = permissionStr.split("\\+");

        if(array.length > 1) {
            resourceId = array[1];
        }

        if(StringUtils.isEmpty(resourceId)) {
            resourceId = "*";
        }

        if(array.length > 2) {
            permissionBit = Integer.parseInt(array[2]);
        }

        if(array.length > 3) {
            instanceId = array[3];
        }

        if(StringUtils.isEmpty(instanceId)) {
            instanceId = "*";
        }
    }

    public boolean implies(Permission permission) {
        if(!(permission instanceof BitPermission)) {
            return false;
        }

        BitPermission other = (BitPermission) permission;
        if(!("*".equals(this.resourceId) || this.resourceId.equals(other.resourceId))) {
            return false;
        }

        if(!(this.permissionBit == 0 || (this.permissionBit & other.permissionBit) != 0)) {
            return false;
        }

        if(!("*".equals(this.instanceId) || this.instanceId.equals(other.instanceId))) {
            return false;
        }

        return true;
    }
}
