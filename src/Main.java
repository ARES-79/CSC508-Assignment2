import Model.Blackboard;
import Model.DataProcessor;
import Model.Observer;
import View.DisplayArea;
import javax.swing.SwingUtilities;

public class Main {
   public static void main(String[] args) {
       Blackboard blackboard = Blackboard.getInstance();
       DataProcessor dataProcessor = new DataProcessor();
       Observer observer = new Observer();
       DisplayArea displayArea = new DisplayArea();
       

       Thread dataThread = new Thread(dataProcessor);
       Thread observerThread = new Thread(observer);
       //Thread displayThread = new Thread(displayArea);

       //displayThread.start();
       dataThread.start();
       observerThread.start();

//       SwingUtilities.invokeLater(blackboard.getDisplayArea());

   }
}
