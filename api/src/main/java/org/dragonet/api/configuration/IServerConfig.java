/*
 * DragonProxy API
 * Copyright © 2016 Dragonet Foundation (https://github.com/DragonetMC/DragonProxy)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.dragonet.api.configuration;

/**
 *
 * @author Epic
 */
public interface IServerConfig {

    /**
     *
     * @return the lang
     */
    public String getLang();

    /**
     * @return the udp_bind_ip
     */
    public String getUdp_bind_ip();

    /**
     * @return the udp_bind_port
     */
    public int getUdp_bind_port();

    /**
     * @return the proxy_type
     */
    public String getProxy_type();

    /**
     * @return the proxy_ip
     */
    public String getProxy_ip();

    /**
     * @return the proxy_port
     */
    public int getProxy_port();

    /**
     * @return the motd
     */
    public String getMotd();

    /**
     * @return the remote_server_addr
     */
    public String getRemote_server_addr();

    /**
     * @return the remote_server_port
     */
    public int getRemote_server_port();

    /**
     * @return the mode
     */
    public String getMode();

    /**
     * @return the auto_login
     */
    public Boolean getAuto_login();

    /**
     * @return the online_username
     */
    public String getOnline_username();

    /**
     * @return the online_password
     */
    public String getOnline_password();

    /**
     * @return the cls_server
     */
    public String getCls_server();

    /**
     * @return the command_prefix
     */
    public String getCommand_prefix();

    /**
     * @return the max_players
     */
    public int getMax_players();

    /**
     * @return the log_console
     */
    public boolean isLog_console();

    /**
     * @return the log_colors
     */
    public boolean isLog_colors();

    /**
     * @return the authenticate_players
     */
    public boolean isAuthenticate_players();

    /**
     * @return the thread_pool_size
     */
    public int getThread_pool_size();

    /**
     * @return the disable_packet_events
     */
    public Boolean getDisable_packet_events();

    /**
     * @return the ping_passthrough
     */
    public boolean isPing_passthrough();
}
