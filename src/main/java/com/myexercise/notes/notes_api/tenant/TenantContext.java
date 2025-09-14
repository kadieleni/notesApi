package com.myexercise.notes.notes_api.tenant;

public class TenantContext {

    private static final ThreadLocal<String> CURRENT_TENNANT_ID = new ThreadLocal<>();

    public static String getCurrentTenant() {
        return CURRENT_TENNANT_ID.get();
    }

    public static void setCurrentTenant(String tenantId) {
        CURRENT_TENNANT_ID.set(tenantId);
    }

    public static void clear() {
        CURRENT_TENNANT_ID.remove();
    }

}
