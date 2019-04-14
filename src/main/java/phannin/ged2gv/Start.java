package phannin.ged2gv;

import phannin.ged2gv.domain.Person;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

public class Start extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField startPersonField;
    private JComboBox fileNameField;
    private JTextField targetPersonsField;
    private JCheckBox useCheckBox;
    private JRadioButton ancestorsRadioButton;
    private JRadioButton desendanrsRadioButton;
    private JComboBox startPersonSelect;

    public Start() {
        startPersonField.setText("@I0047@");
        targetPersonsField.setText("@I0424@");
        fileNameField.addItem("");
        fileNameField.addItem("data/tommiska.ged");
        fileNameField.addItem("data/puujalka.ged");
        fileNameField.addItem("data/Hänninen.ged");


        //.setText("data/puujalka.ged");
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        startPersonSelect.setRenderer(new ItemRenderer());

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

        fileNameField.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                JComboBox<String> combo = (JComboBox<String>) event.getSource();
                Pedigree sukupuu = new InMemoryPedigree();

                Long start = System.currentTimeMillis();

                try {
                    sukupuu.load((String) combo.getSelectedItem());
                    startPersonSelect.removeAllItems();
                    sukupuu.getPersons().values().stream()
                            .sorted()
                            .forEach((v) -> {
                                startPersonSelect.addItem(v);
                            });

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            }
        });

        startPersonSelect.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                JComboBox<String> combo = (JComboBox<String>) event.getSource();
                Person selectedPerson = (Person) combo.getSelectedItem();
                if (selectedPerson != null)
                    startPersonField.setText(selectedPerson.getId());

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

        fileNameField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println(fileNameField.getSelectedItem() + " selected");
            }
        });
    }

    private void onOK() {
        // add your code here
        System.out.println(startPersonField.getText());
        String person = startPersonField.getText();
        boolean hasTarget = !targetPersonsField.getText().isEmpty();
        String[] targets = new String[]{targetPersonsField.getText()};
        try {
            //          Pedigree sukupuu = new MongoPedigree();
            Pedigree sukupuu = new InMemoryPedigree();

            Long start = System.currentTimeMillis();

            sukupuu.load((String) fileNameField.getSelectedItem());
            sukupuu.dump();

            Filter filter;
            if (useCheckBox.isSelected())
                filter = Filter.factory().forAncestors(person, targets, sukupuu);
            else
                filter = Filter.factory().allAncestors(person, sukupuu);

            System.out.println("filtertime=" + (System.currentTimeMillis() - start));
            start = System.currentTimeMillis();

            if (ancestorsRadioButton.isSelected()) {
                PedigreeWriter writer = new PedigreeWriter("results/pedigree.dot");
                writer.writePedigree(sukupuu, filter, person);
            } else {
                DecendantsWriter writer = new DecendantsWriter("results/pedigree.dot");
                writer.writeDecendants(sukupuu, filter, person);
            }


        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        //       dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }


    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    class ItemRenderer extends BasicComboBoxRenderer {
        public Component getListCellRendererComponent(
                JList list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index,
                    isSelected, cellHasFocus);

            if (value != null) {
                Person item = (Person) value;
                setText(item.getFullName());
            }

            if (index == -1) {
                Person item = (Person) value;
                setText(item != null ? item.getFullName() : "");
            }


            return this;
        }
    }

}
