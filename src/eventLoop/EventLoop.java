package eventLoop;
import java.util.ArrayList;

public class EventLoop {
    private ArrayList<EventLoopListener> listeners = new ArrayList<EventLoopListener>();
    private boolean active = false;
    private long lastNow = 0;

    public boolean start() {
        if (active) return false;
        active = true;
        lastNow = System.nanoTime();
        loop();
        return true;
    }

    public boolean stop() {
        if (!active) return false;
        active = false;

        return true;
    } 


    private void loop() {
        while(active) {
            long now = System.nanoTime();
            emit(this.lastNow - now);
            this.lastNow = now;
        }
    }

    public boolean addListener(EventLoopListener toAdd) {
        return listeners.add(toAdd);
    }

    public void removeAllListener() {
        listeners.clear();
    }

    public boolean removeEventListener(EventLoopListener toRemove) {
        return listeners.remove(toRemove);
        
    }

    private void emit(long delta) {
        for (EventLoopListener eventLoopListener : listeners) {
            eventLoopListener.onFrame(delta);
        }
    }
}
