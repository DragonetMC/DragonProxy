package org.dragonet.proxy.utilities.pingpassthrough;

import com.github.steveice10.mc.protocol.MinecraftConstants;
import com.github.steveice10.mc.protocol.MinecraftProtocol;
import com.github.steveice10.mc.protocol.data.SubProtocol;
import com.github.steveice10.mc.protocol.data.status.ServerStatusInfo;
import com.github.steveice10.mc.protocol.data.status.handler.ServerInfoHandler;
import com.github.steveice10.packetlib.Client;
import com.github.steveice10.packetlib.tcp.TcpSessionFactory;
import org.dragonet.proxy.DragonProxy;

public class PingThread extends Thread{

    private static PingThread instance;
    private ServerStatusInfo info;
    private Client client;

    public PingThread(){
        super("PingThread");
        instance = this;
    }

    public static PingThread getInstance(){
        return instance;
    }

    public ServerStatusInfo getInfo() {
        return info;
    }

    private void setClient(){
        this.client = new Client(DragonProxy.getInstance().getConfig().getRemote_server_addr(), DragonProxy.getInstance().getConfig().getRemote_server_port(), new MinecraftProtocol(SubProtocol.STATUS), new TcpSessionFactory());
        this.client.getSession().setFlag(MinecraftConstants.SERVER_INFO_HANDLER_KEY, (ServerInfoHandler) (session, info) -> {
            this.info = info;
            this.client.getSession().disconnect(null);
        });
    }

    @Override
    public void run() {
        while(true){
            try {
                this.setClient();
                client.getSession().connect();
            } catch (Exception e) { e.printStackTrace(); }
        }

    }
}
