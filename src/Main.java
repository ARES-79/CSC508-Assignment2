
public class Main {
   public static void main(String[] args) {
       DisplayArea displayArea = new DisplayArea();
       Thread displayThread = new Thread(displayArea);
       displayThread.start();
   }
}
