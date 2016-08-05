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

    public final static String INIT_LOADING = "init_loading";

    public final static String INIT_MC_PC_SUPPORT = "init_mc_pc_support";

    public final static String INIT_MC_PE_SUPPORT = "init_mc_pe_support";

    public final static String INIT_CREATING_THREAD_POOL = "init_creating_thread_pool";

    public final static String INIT_BINDING = "init_binding";

    public final static String INIT_DONE = "init_done";

    public final static String BROADCAST_TITLE = "broadcast_title";

    public final static String MESSAGE_CLIENT_CONNECTED = "message_client_connected";
    
    public final static String MESSAGE_CLS_NOTICE = "message_cls_notice";
    
    public final static String MESSAGE_SERVER_ERROR = "message_server_error";
    
    public final static String ERROR_CLS_UNREACHABLE = "error_cls_unreachable";
    
    public final static String ERROR_CLS_ERROR = "error_cls_error";
    public final static String ERROR_CLS_LOGIN = "error_cls_login";

    public final static String MESSAGE_ONLINE_NOTICE = "message_online_notice";

    public final static String MESSAGE_ONLINE_EMAIL = "message_online_email";

    public final static String MESSAGE_ONLINE_ERROR = "message_online_error";

    public final static String MESSAGE_ONLINE_PASSWORD = "message_online_password";

    public final static String MESSAGE_ONLINE_LOGGIN_IN = "message_online_logging_in";

    public final static String MESSAGE_ONLINE_LOGIN_SUCCESS = "message_online_login_success";

    public final static String MESSAGE_ONLINE_LOGIN_SUCCESS_CONSOLE = "message_online_login_success_console";
    
    public final static String MESSAGE_TELEPORT_TO_SPAWN = "message_teleport_to_spawn";

    public final static String MESSAGE_ONLINE_USERNAME = "message_online_username";

    public final static String MESSAGE_ONLINE_LOGIN_FAILD = "message_online_login_faild";
    
    public final static String MESSAGE_REMOTE_CONNECTED = "message_remote_connected";

    public final static String MESSAGE_REMOTE_CONNECT_FAILURE = "message_remote_connect_failure";

    public final static String MESSAGE_JOINED = "message_joined";

    public final static String MESSAGE_KICKED = "message_kicked";

    public final static String MESSAGE_CLIENT_DISCONNECT = "message_client_disconnect";

    public final static String MESSAGE_REMOTE_IS_ONLINE = "message_remote_is_online";

    public final static String MESSAGE_REMOTE_ERROR = "message_remote_error";

    public final static String MESSAGE_REMOTE_DISCONNECTED = "message_remote_disconnected";

    public final static String CLIENT_DISCONNECTED = "client_disconnected";

    public final static String COMMAND_NOT_FOUND = "command_not_found";

    public final static String SHUTTING_DOWN = "shutting_down";

    public final static String MESSAGE_UNSUPPORTED_CLIENT = "message_unsupported_client";

    public final static String QUERY_FAILED = "query_failed";
    
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
