package com.mecheniy.chat.utilities;

import net.luckperms.api.model.user.User;

public class PermissionUtils {
    public static boolean hasPermission(User user, String permission) {
        return user.getCachedData().getPermissionData().checkPermission(permission).asBoolean();
    }
}

