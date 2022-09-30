package com.testsigma.api_bed_test.security;

public interface URLConstants {
    String LOGIN_URL = "/login";
    String ASSETS_URL = "/assets";
    String SESSIONS_URL = "/sessions";
    String LOGOUT_URL = "/logout";

    String JARVIS_BASE_URL = "/api/jarvis";
    String API_TENANTS = "/api/tenants";
    String API_TENANT_ID = "/api/tenants/{id}";
    String API_ON_GOING_TEST_PLAN = "/api/on_going_test_plan";
    String API_PLATFORMS = "/api/platforms";
    String API_MOBILE_INSPECTIONS_ERRORS = API_TENANT_ID + "/mobile_inspection_errors";
    String API_TENANT_USERS = API_TENANT_ID + "/users";
    String API_LAB_MODELS = API_TENANT_ID + "/labmodels";
    String API_SUBSCRIPTIONS = API_TENANT_ID + "/subscriptions";
    String API_PHONE_NUMBERS = API_TENANT_ID + "/phone_numbers";
    String API_MAIL_BOXES = API_TENANT_ID + "/mail_boxes";
    String API_IDENTITY_USERS = API_TENANTS + "/users";
    String API_USER_ACCOUNTS = API_TENANTS + "/user_accounts";
    String API_FEATURES = API_TENANT_ID + "/features";
    String API_DRYEXECUTIONS = API_TENANT_ID + "/dry_executions";
    String API_EXECUTIONS = API_TENANT_ID + "/executions";
    String API_PLUGIN = API_TENANT_ID + "/plugins";
    String RESTRICTED_DOMAINS = "/api/restricted_domains";
}
