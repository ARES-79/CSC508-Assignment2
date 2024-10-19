package Model;

import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Established data for threads to be managed
 */
public abstract class CustomThread extends Thread{

    private Logger log;
    private String threadName;
    private boolean running =  true;

    /**
     * try performing work and catch any exceptions.
     * Log as necessary.
     */
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

    /**
     * Perform duties of thread
     */
    public abstract void doYourWork() throws InterruptedException, IOException;

    /**
     * Clean up any outstanding resources as necessary
     */
    public abstract void cleanUpThread();

    /**
     * Stop the loop of the thread so it can terminate
     */
    public void stopThread() {
        running = false;
        //interrupt(); // Interrupt any blocking operations
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
