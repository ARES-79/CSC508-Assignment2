package Controller;

import Model.Blackboard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainController implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case ("Start") -> {
                Blackboard.getInstance().startDataRetrieval();
                System.out.println("Start pressed.");
            }
            case ("Stop") -> {
                Blackboard.getInstance().stopDataRetrieval();
                System.out.println("Stop pressed.");
            }
        }
    }
}
