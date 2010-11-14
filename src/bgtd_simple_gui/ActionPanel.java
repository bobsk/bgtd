/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bgtd_simple_gui;


import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import java.awt.Color;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.AbstractCellEditor;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.border.EtchedBorder;


import bgtd_gtd_core.*;
/**
 *6
 * @author boris
 */
public class ActionPanel extends AbstractCellEditor implements TableCellRenderer, TableCellEditor  {

    JPanel      panel;
    private JTextArea   taActionName;
    private JTextArea taActionContext;
    private JTextArea taActionDuration;
    JButton     bModify;

    Project project;
    Action a;

    public  ActionPanel ( Project project ) {
        this.project = project;

        panel = new JPanel();
        panel.setLayout(null);


        taActionName = new JTextArea();
        taActionContext = new JTextArea();
        taActionDuration = new JTextArea();
        bModify = new JButton("Mod");

        taActionName.setBackground(Color.orange);
        taActionContext.setBackground(Color.orange);
        taActionDuration.setBackground(Color.orange);

        bModify.setActionCommand("modify");
        bModify.addActionListener(new ActionActionListener());

        //taActionName.setPreferredSize(new Dimension(50,20));
        //taActionDuration.setPreferredSize(new Dimension(50,20));

        taActionName.setBounds(5, 5, 250, 45);
        taActionContext.setBounds(260, 5, 150, 20);
        taActionDuration.setBounds(260, 30, 50, 20);
        bModify.setBounds(320, 30, 20, 20);

        panel.add(taActionName);
        panel.add(taActionContext);
        panel.add(taActionDuration);
        panel.add(bModify);
        panel.repaint();

        panel.setBorder(new EtchedBorder());

        //this.setPreferredSize(new Dimension(110,50));
        //panel.setMinimumSize(new Dimension(110,55));


    }

    /**
     * @return the taActionName
     */
    public String getActionName() {
        return taActionName.getText();
    }

    /**
     * @param taActionName the taActionName to set
     */
    public void setActionName(String actionName) {
        this.taActionName.setText(actionName);
    }

    /**
     * @return the taActionContext
     */
    public String getActionContext() {
        return taActionContext.getText();
    }

    /**
     * @param actionContext the action context to set
     */
    public void setActionContext(String actionContext) {
        this.taActionContext.setText(actionContext);
    }

    /**
     * @return the taActionDuration
     */
    public String getActionDuration() {
        return taActionDuration.getText();
    }

    /**
     * @param taActionDuration the taActionDuration to set
     */
    public void setActionDuration(String actionDuration) {
        this.taActionDuration.setText(actionDuration);
    }

    private class ActionActionListener implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Action panel, button pressed! : " + e.getActionCommand() );
                a.name = getActionName();
                a.context = getActionContext();
                try{
                    a.duration = Integer.parseInt(getActionDuration());
                }
                catch( Exception exception ){
                    System.out.println("Invalid duration format. Exception = " +exception.toString() );
                }
            }
    }

    // ******************* Cell Editor stuff ***********************************
    // This method is called when a cell value is edited by the user.
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int rowIndex, int vColIndex)
    {
        // 'value' is value contained in the cell located at (rowIndex, vColIndex)
        if (isSelected) {
            // cell (and perhaps other cells) are selected
        }

        // Configure the component with the specified value
        if( value==null ){
            a = null;

            setActionName( "" );
            setActionContext( "" );
            setActionDuration( "" );
        }
        else{
            a = ((Action)value);

            System.out.println( "Value turned into action, name = " +a.name +", id = " +a.getId() );

            setActionName( a.name );
            setActionContext( a.context );
            setActionDuration( Integer.toString( a.duration ) );
        }

        // Return the configured component
        return panel;
    }

    // This method is called when editing is completed.
    // It must return the new value to be stored in the cell.
    public Object getCellEditorValue()
    {
        return a;
    }

    // ******************* Cell Renderer stuff *********************************

    // This method is called each time a cell in a column
    // using this renderer needs to be rendered.
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int rowIndex, int vColIndex)
    {

        // 'value' is value contained in the cell located at
        // (rowIndex, vColIndex)
        if (isSelected) {
            // cell (and perhaps other cells) are selected
            panel.setBackground(Color.yellow);
        }
        else{
            panel.setBackground(null);
        }

        if (hasFocus) {
            // this cell is the anchor and the table has the focus
        }

        // Configure the component with the specified value


        if( value== null ){
            System.out.println( "value is null! getting it for row " +rowIndex + " and col " + vColIndex );
            value = table.getValueAt(rowIndex, vColIndex);
        }

        if( value==null ){
            System.out.println( "value is still null!" );
            setActionName( "" );
            setActionContext( "" );
            setActionDuration( "" );
        }
        else{
            a = ((Action)value);

            System.out.println( "Value turned into action, name = " +a.name );

            setActionName( a.name );
            setActionContext( a.context );
            setActionDuration( Integer.toString( a.duration ) );
        }

        // Set tool tip if desired
        //setToolTipText((String)value);
//System.out.println( "in jpanelm name = " +taActionName.getText());
//this.layout();
        //this.repaint();
        // Since the renderer is a component, return itself
        return panel;
    }

    // The following methods override the defaults for performance reasons
    public void validate() {}
    public void revalidate() {}
    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {}
    public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {}


}
