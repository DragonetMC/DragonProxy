package org.dragonet.dragonproxy.proxy.packet;

import com.sun.istack.internal.Nullable;

import java.io.Reader;
import java.util.ArrayList;

public class SendQueue extends Reader {
    private ArrayList<String> queue = new ArrayList<>();

    private static SendQueue instanceJ = null;
    private static SendQueue instanceB = null;

    private SendQueue(){

    }

    @Override
    public void close() {
        queue.clear();
    }

    @Override
    public synchronized int read(@Nullable char[] cbuf, @Nullable int off, @Nullable int len) {
        return read();
    }

    @Override
    public synchronized int read(){
        try {
            String s = queue.get(0);
            queue.remove(0);
            queue.add(0, s.substring(1));
            return (int) s.charAt(1);
        }
        catch (ArrayIndexOutOfBoundsException e){
            queue.remove(0);
            return 0;
        }
    }
    public static SendQueue getJavaInstance() {
        if(instanceJ == null) {
            instanceJ = new SendQueue();
        }
        return instanceJ;
    }

    public static SendQueue getBedrockInstance() {
        if(instanceB == null) {
            instanceB = new SendQueue();
        }
        return instanceB;
    }
}
