/*
 * GNU LESSER GENERAL PUBLIC LICENSE
 *                       Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view LICENCE file for details. 
 *
 * @author The Dragonet Team
 */
package org.dragonet.proxy.configuration;

import java.io.IOException;

public class Lang extends PropertiesConfig {

    public static final String INIT_LOADING = "init_loading";
    public static final String INIT_MC_PC_SUPPORT = "init_mc_pc_support";
    public static final String INIT_MC_PE_SUPPORT = "init_mc_pe_support";
    public static final String INIT_CREATING_THREAD_POOL = "init_creating_thread_pool";
    public static final String INIT_BINDING = "init_binding";
    public static final String INIT_DONE = "init_done";
    public static final String BROADCAST_TITLE = "broadcast_title";
    public static final String MESSAGE_CLIENT_CONNECTED = "message_client_connected";
    public static final String MESSAGE_CLS_NOTICE = "message_cls_notice";
    public static final String MESSAGE_SERVER_ERROR = "message_server_error";
    public static final String ERROR_CLS_UNREACHABLE = "error_cls_unreachable";
    public static final String ERROR_CLS_ERROR = "error_cls_error";
    public static final String ERROR_CLS_LOGIN = "error_cls_login";
    //public static final String MESSAGE_ONLINE_NOTICE = "message_online_notice";
    //public static final String MESSAGE_ONLINE_EMAIL = "message_online_email";
    public static final String MESSAGE_LOGIN_PROMPT = "message_login_prompt";
    public static final String MESSAGE_LOGIN_PROGRESS = "message_login_progress";
    public static final String FORM_LOGIN_TITLE = "form_login_title";
    public static final String FORM_LOGIN_DESC = "form_login_desc";
    public static final String FORM_LOGIN_PROMPT = "form_login_prompt";
    public static final String FORM_LOGIN_USERNAME = "form_login_username";
    public static final String FORM_LOGIN_PASSWORD = "form_login_password";
    public static final String MESSAGE_ONLINE_ERROR = "message_online_error";
    public static final String MESSAGE_ONLINE_PASSWORD = "message_online_password";
    public static final String MESSAGE_ONLINE_LOGGING_IN = "message_online_logging_in";
    public static final String MESSAGE_ONLINE_LOGIN_SUCCESS = "message_online_login_success";
    public static final String MESSAGE_ONLINE_LOGIN_SUCCESS_CONSOLE = "message_online_login_success_console";
    public static final String MESSAGE_TELEPORT_TO_SPAWN = "message_teleport_to_spawn";
    public static final String MESSAGE_ONLINE_USERNAME = "message_online_username";
    public static final String MESSAGE_ONLINE_LOGIN_FAILD = "message_online_login_faild";
    public static final String MESSAGE_REMOTE_CONNECTED = "message_remote_connected";
    public static final String MESSAGE_REMOTE_CONNECT_FAILURE = "message_remote_connect_failure";
    public static final String MESSAGE_JOINED = "message_joined";
    public static final String MESSAGE_KICKED = "message_kicked";
    public static final String MESSAGE_CLIENT_DISCONNECT = "message_client_disconnect";
    public static final String MESSAGE_REMOTE_IS_ONLINE = "message_remote_is_online";
    public static final String MESSAGE_REMOTE_ERROR = "message_remote_error";
    public static final String MESSAGE_REMOTE_DISCONNECTED = "message_remote_disconnected";
    public static final String CLIENT_DISCONNECTED = "client_disconnected";
    public static final String COMMAND_NOT_FOUND = "command_not_found";
    public static final String SHUTTING_DOWN = "shutting_down";
    public static final String MESSAGE_UNSUPPORTED_CLIENT = "message_unsupported_client";
    public static final String QUERY_FAILED = "query_failed";

    public Lang(String langFileName) throws IOException {
        super("/en_US.properties", langFileName, false);
    }

    public String get(String key) {
        return getConfig().getProperty(key).replace("[PROJNAME]", getConfig().getProperty("project_name"));
    }

    public String get(String key, Object... repl) {
        return String.format(get(key), repl);
    }
}
