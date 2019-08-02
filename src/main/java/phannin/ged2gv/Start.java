package phannin.ged2gv;

import phannin.ged2gv.domain.Person;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import java.awt.*;
import java.awt.event.*;


public class Start extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField startPersonField;
    private JComboBox fileNameField;
    private JTextField targetPersonsField;
    private JCheckBox useCheckBox;
    private JRadioButton ancestorsRadioButton;
    private final PedigreeService service = new PedigreeService();
    private JComboBox startPersonSelect;
    private JRadioButton bothRadioButton;
    private JComboBox targetPersonSelect;
    private JRadioButton descendantsRadioButton;


    public Start() {
        fileNameField.addItem("");
        fileNameField.addItem("data/tommiska.ged");
        fileNameField.addItem("data/puujalka.ged");
        fileNameField.addItem("data/Hänninen.ged");


        //.setText("data/puujalka.ged");
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        startPersonSelect.setRenderer(new ItemRenderer());
        targetPersonSelect.setRenderer(new ItemRenderer());

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

                startPersonSelect.removeAllItems();
                targetPersonSelect.removeAllItems();
                service.getPersons((String) combo.getSelectedItem()).forEach((v) -> {
                    startPersonSelect.addItem(v);
                    targetPersonSelect.addItem(v);
                });


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

        targetPersonSelect.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                JComboBox<String> combo = (JComboBox<String>) event.getSource();
                Person selectedPerson = (Person) combo.getSelectedItem();
                if (selectedPerson != null) {
                    if (targetPersonsField.getText().isEmpty())
                        targetPersonsField.setText(selectedPerson.getId());
                    else
                        targetPersonsField.setText(targetPersonsField.getText() + ";" + selectedPerson.getId());
                }

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

        String[] targets = useCheckBox.isSelected() ? targetPersonsField.getText().split(";") : null;

        if (ancestorsRadioButton.isSelected()) {
            service.createPedigree((String) fileNameField.getSelectedItem(), person, PedigreeService.Style.ANCESTORS, targets);
        } else if (descendantsRadioButton.isSelected()) {
            service.createPedigree((String) fileNameField.getSelectedItem(), person, PedigreeService.Style.DESCENDANTS, null);
        } else if (bothRadioButton.isSelected()) {
            service.createPedigree((String) fileNameField.getSelectedItem(), person, PedigreeService.Style.BOTH, targets);
        }


    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }


    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    static class ItemRenderer extends BasicComboBoxRenderer {
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
