/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bgtd_simple_gui;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import javax.swing.JButton;
import javax.swing.JTextArea;

import javax.swing.JTable;
import javax.swing.table.*;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.BorderLayout;


import java.awt.Container;

import java.util.Vector;

// for the buttons:
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

//import java.util.Map;
import java.util.HashMap;

import javax.swing.JFrame;

import bgtd_gtd_core.*;


/**
 *
 * @author boris
 */
public class LifePlannerImpl extends JPanel implements ActionListener{

    //Main main;  // we need to know whom to pass the control back to when we're done.
    Life life;

    JButton b1, b2, b3, b4;
    //JTextArea textArea;
    JTable  table;
    DefaultTableModel tableModel;

    HashMap projectGUIs;


    LifePlannerImpl( ){
        super();
        //super(new GridLayout(2,1));

        //this.projectGUIs = new HashMap();

        //table = new JTable(new LifePlannerTableModel());
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers( new Object[] {"ID","Name","Priority","Next action ID","Next action"} );
        table = new JTable(tableModel);
        table.setPreferredScrollableViewportSize(new Dimension(500, 400));
        table.setFillsViewportHeight(true);
        table.setVisible(true);
        JScrollPane scrollPane = new JScrollPane(table);

        JPanel buttonsPanel = new JPanel(new GridLayout(1,3));

        b1 = new JButton("modify project");
        b2 = new JButton("create project");
        b3 = new JButton("delete selected projects");
        b4 = new JButton("start planning");

        b1.setActionCommand("modify");
        b2.setActionCommand("create");
        b3.setActionCommand("delete");
        b4.setActionCommand("start");

        b1.addActionListener(this);
        b2.addActionListener(this);
        b3.addActionListener(this);
        b4.addActionListener(this);

        buttonsPanel.add(b1);
        buttonsPanel.add(b2);
        buttonsPanel.add(b3);
        buttonsPanel.add(b4);

        this.add(scrollPane, BorderLayout.PAGE_START);
        this.add(buttonsPanel, BorderLayout.PAGE_END);

    }

    // stuff to do on keypress
    public void actionPerformed(ActionEvent e) {
        if( life!=null ){

            // stop the presses! (stop editing the table)
            if( table.getCellEditor() != null ){
                table.getCellEditor().stopCellEditing();
            }

            if ("modify".equals(e.getActionCommand())) {
                //life.modifyProject("new_project");
                System.out.println("SimpleLifePlanner.actionPerformed( edit )");
                //main.startPlanningProject(life.projects.lastElement());
            } else if ("create".equals(e.getActionCommand())) {
                System.out.println("SimpleLifePlanner.actionPerformed( create )");
                //System.out.println("row count:" + tableModel.getRowCount() );
                String name = (String)table.getValueAt(tableModel.getRowCount()-1, 1);
                int priority = Integer.parseInt((String)table.getValueAt(tableModel.getRowCount()-1, 2));

                // Create the new project in the Life, add the new id to the table
                System.out.println("about to create with name=" +name + ", priority=" +priority +".");
                int latestProjectId = life.createProject(name, priority, 0);
                //table.setValueAt(Integer.toString(latestProjectId),tableModel.getRowCount()-1, 0);
                table.setValueAt( latestProjectId ,tableModel.getRowCount()-1, 0);

                // add empty line at the end for creating a new project
                tableModel.addRow( new Object[] {} );

            } else if ("delete".equals(e.getActionCommand())) {
                int selectedProjectRows[] = table.getSelectedRows();
                System.out.println("SimpleLifePlanner.actionPerformed( delete )");
                for( int i=selectedProjectRows.length-1; i>=0; i-- ){
                System.out.println("selected row:" + selectedProjectRows[i] );
                    if( life.deleteProject(((Integer)table.getValueAt(selectedProjectRows[i], 0)).intValue()) ){
                        tableModel.removeRow(selectedProjectRows[i]);
                    }
                }
            } else if ("start".equals(e.getActionCommand())) {
                System.out.println("SimpleLifePlanner.actionPerformed( start )");



                // stop the presses! (stop editing the table)
                if( table.getSelectedRowCount() != 1 ){
                    System.out.println("***** one and only one row should be selected for planning a project");
                    return;
                }

                int selected = table.getSelectedRow();
                if( table.getValueAt(selected, 0) == null ){
                    System.out.println("***** no projectId found for the selected row");
                    return;
                }

                ProjectPlannerImpl pplanner = new ProjectPlannerImpl();
                pplanner.startPlanning( life.getProject( ((Integer)table.getValueAt(selected,0)).intValue() ) );

                JFrame ppframe = new JFrame("GTD");
                ppframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                //pplanner.setVisible(true);

                ppframe.getContentPane().add(pplanner);

                ppframe.pack();
                ppframe.setVisible(true);
                ppframe.setSize(800, 500);
                ppframe.setResizable(true);
            }
        }
    }

    void    startPlanning( Life life ){
        this.life = life;
        System.out.println("In SimpleLifePlanner.startPlanning() " );// +container.toString() +", " +l.toString() +"  )" );

        tableModel.setRowCount(0);

        for( int i=0; i<life.getProjects().size(); i++ ){

            int projectId = life.getProjects().elementAt(i).getId();
            String[] nextAction = life.getProjectNextAction(projectId);
            int nextActionId = Integer.parseInt((String)nextAction[0]);
            String nextActionName = (String)nextAction[1];
            tableModel.addRow( new Object[] {
                            projectId,
                            life.getProjects().elementAt(i).name,
                            life.getProjects().elementAt(i).priority,
                            nextActionId,
                            nextActionName
                } );
        }

        // add empty line at the end for creating a new project
        tableModel.addRow( new Object[] {} );

        table.doLayout();
        this.repaint();
    }

}
