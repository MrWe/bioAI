
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

class JSSPUi {

    private JFrame guiFrame;

    JSSPUi() {
        guiFrame = new JFrame();
        guiFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        guiFrame.setTitle("JSSP");
        guiFrame.setSize(300, 250);
    }

    public void run(int numComuters, int totalNumOfTimes){
        System.out.println("heisann");
        //This will center the JFrame in the middle of the screen
        guiFrame.setLocationRelativeTo(null);

        GridLayout grid = new GridLayout(numComuters, totalNumOfTimes);

        //Options for the JComboBox
        String[] lanes = new String[numComuters];

        for (int i = 0; i < numComuters; i++) {
            lanes[i] = "M" + i;
        }

        //Options for the JList
        String[] vegOptions = new String[totalNumOfTimes];




            //The first JPanel contains a JLabel and JCombobox
        final JPanel comboPanel = new JPanel();
        JLabel comboLbl = new JLabel("Fruits:");
        JComboBox fruits = new JComboBox(lanes);

        comboPanel.add(comboLbl);
        comboPanel.add(fruits);

        //Create the second JPanel. Add a JLabel and JList and
        //make use the JPanel is not visible.
        final JPanel listPanel = new JPanel();
        listPanel.setVisible(false);
        JLabel listLbl = new JLabel("Vegetables:");
        JList vegs = new JList(vegOptions);
        vegs.setLayoutOrientation(JList.HORIZONTAL_WRAP);

        listPanel.add(listLbl);
        listPanel.add(vegs);

        JButton vegFruitBut = new JButton( "Fruit or Veg");

        //The ActionListener class is used to handle the
        //event that happens when the user clicks the button.
        //As there is not a lot that needs to happen we can
        //define an anonymous inner class to make the code simpler.
        vegFruitBut.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                //When the fruit of veg button is pressed
                //the setVisible value of the listPanel and
                //comboPanel is switched from true to
                //value or vice versa.
                listPanel.setVisible(!listPanel.isVisible());
                comboPanel.setVisible(!comboPanel.isVisible());

            }
        });

        //The JFrame uses the BorderLayout layout manager.
        //Put the two JPanels and JButton in different areas.
        guiFrame.add(comboPanel, BorderLayout.NORTH);
        guiFrame.add(listPanel, BorderLayout.CENTER);
        guiFrame.add(vegFruitBut,BorderLayout.SOUTH);

        //make sure the JFrame is visible
        guiFrame.setVisible(true);
    }

}
