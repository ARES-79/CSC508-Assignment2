package Model;

import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.Level;

public abstract class CustomThread extends Thread{

    private Logger log;
    private String threadName;
    private boolean running =  true;

    @Override
    public void run() {
        try{

            while(running){
                doYourWork();
            }

        } catch (InterruptedException e) {
            log.log(Level.SEVERE, threadName + " thread was interrupted", e);
            Thread.currentThread().interrupt();
        } catch(Exception e){
            log.warning(e.toString());
        } finally{
            cleanUpThread();
        }
    }

    public abstract void doYourWork() throws InterruptedException, IOException;

    public abstract void cleanUpThread();

    public void stopThread() {
        running = false;
        //interrupt(); // Interrupt any blocking operations
    }

    public boolean isRunning() {
        return running;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public void setLog(Logger log) {
        this.log = log;
    }

    public Logger getLog() {
        return log;
    }

    public String getThreadName() {
        return threadName;
    }
}
