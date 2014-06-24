/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vipsclient.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import static javax.swing.SwingConstants.CENTER;
import javax.swing.table.DefaultTableModel;
import vipsclient.Controller;
import vipsclient.entity.Contact;

/**
 *
 * @author LucianDobre
 */
public class ContactListPanel extends JPanel implements ActionListener{

    Controller ctrl;
    
    JLabel jlHeaderLabel;
    
    JTable jtContactTable;
    DefaultTableModel contactTableModel;
    
    public ContactListPanel() {
        setPreferredSize(new Dimension(340,330));
        setLayout(new BorderLayout());
    }
    
    /* Call assemble only after all dependencies have been injected*/
    public void assemble() {
        //Add header label
        jlHeaderLabel = new JLabel();
        jlHeaderLabel.setText("Contacts");
        jlHeaderLabel.setHorizontalAlignment(CENTER);
        add(jlHeaderLabel, BorderLayout.NORTH);

        //Add actual contact table with non-editable cells
        jtContactTable = new JTable();
        contactTableModel = new DefaultTableModel(vipsclient.entity.Contact.header, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        jtContactTable.setModel(contactTableModel);
        add(jtContactTable, BorderLayout.CENTER);
        
        setContactList(getCtrl().getContacts());
    }

    public Controller getCtrl() {
        return ctrl;
    }

    public void setCtrl(Controller ctrl) {
        this.ctrl = ctrl;
    }

    public JTable getJtContactTable() {
        return jtContactTable;
    }

    public DefaultTableModel getContactTableModel() {
        return contactTableModel;
    }
    
    public void setContactList(Vector<Contact> contacts){
        Vector header = new Vector();
        for ( String s : Contact.header ) header.add(s);
        contactTableModel.setDataVector( Contact.toVector(contacts), header );
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("ContactListPanel");
    }
    
}
