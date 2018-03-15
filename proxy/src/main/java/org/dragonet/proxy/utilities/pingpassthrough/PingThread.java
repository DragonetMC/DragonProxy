package org.dragonet.proxy.utilities.pingpassthrough;

import com.github.steveice10.mc.protocol.MinecraftConstants;
import com.github.steveice10.mc.protocol.MinecraftProtocol;
import com.github.steveice10.mc.protocol.data.SubProtocol;
import com.github.steveice10.mc.protocol.data.status.ServerStatusInfo;
import com.github.steveice10.mc.protocol.data.status.handler.ServerInfoHandler;
import com.github.steveice10.packetlib.Client;
import com.github.steveice10.packetlib.Session;
import com.github.steveice10.packetlib.tcp.TcpSessionFactory;
import org.dragonet.proxy.DragonProxy;

import java.util.concurrent.Executors;

public class PingThread implements Runnable{

    private static PingThread instance;
    private ServerStatusInfo info;
    private Client client;

    public PingThread(){
        instance = this;
        Executors.newSingleThreadExecutor().execute(this);
    }

    public static PingThread getInstance(){
        return instance;
    }

    public ServerStatusInfo getInfo() {
        return info;
    }

    private void setClient(){
        this.client = new Client(DragonProxy.getInstance().getConfig().remote_server_addr, DragonProxy.getInstance().getConfig().remote_server_port, new MinecraftProtocol(SubProtocol.STATUS), new TcpSessionFactory());
        this.client.getSession().setFlag(MinecraftConstants.SERVER_INFO_HANDLER_KEY, (ServerInfoHandler) (session, info) -> {
            this.info = info;
        });
    }

    @Override
    public void run() {
        while(true){
            try {
                this.setClient();
                client.getSession().connect();
                Thread.sleep(1000);
            } catch (Exception e) { e.printStackTrace(); }
        }

    }
}
