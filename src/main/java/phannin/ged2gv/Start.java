package phannin.ged2gv;

import javax.swing.*;
import java.awt.event.*;

public class Start extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField startPersonField;
    private JComboBox fileNameField;
    private JTextField targetPersonsField;
    private JCheckBox useCheckBox;

    public Start() {
        startPersonField.setText("@I0047@");
        targetPersonsField.setText("@I0424@");
        fileNameField.addItem("data/puujalka.ged");
        //.setText("data/puujalka.ged");
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        // add your code here
        System.out.println(startPersonField.getText());
        String person = startPersonField.getText();
        boolean hasTarget = !targetPersonsField.getText().isEmpty();
        String[] targets = new String[]{targetPersonsField.getText()};
        try {
            Pedigree sukupuu = new MongoPedigree();

            Long start = System.currentTimeMillis();

            sukupuu.load((String) fileNameField.getSelectedItem());

            Filter filter;
            if (hasTarget)
                filter = Filter.factory().forAncestors(person, targets, sukupuu);
            else
                filter = Filter.factory().allAncestors(person, sukupuu);

            System.out.println("filtertime=" + (System.currentTimeMillis() - start));
            start = System.currentTimeMillis();

            PedigreeWriter writer = new PedigreeWriter("results/pedigree.dot");
            writer.writePedigree(sukupuu, filter, person);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }


    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
