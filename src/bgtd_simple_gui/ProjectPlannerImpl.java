/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bgtd_simple_gui;



import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.Container;


import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.*;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.BorderLayout;


// for the buttons:
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import bgtd_gtd_core.*;


/**
 *
 * @author boris
 */
public class ProjectPlannerImpl extends JPanel implements ActionListener {

    //Life life;
    Project project;


    JButton b1, b2, b3, b4, b5;
    JTable  table;
    DefaultTableModel tableModel;

    ProjectPlannerImpl(){
        super();

        tableModel = new DefaultTableModel();
        //tableModel.setColumnIdentifiers( new Object[] {"Id","Name","Context","Duration","Start date","Due date"} );
        tableModel.setColumnIdentifiers( new Object[] {"Id","Dates","Action"} );
        table = new JTable(tableModel);
        table.getColumnModel().getColumn(2).setCellRenderer(new ActionPanel(project));
        table.getColumnModel().getColumn(2).setCellEditor(new ActionPanel(project));
        table.setPreferredScrollableViewportSize(new Dimension(700, 400));
        table.getColumnModel().getColumn(0).setPreferredWidth(20);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(500);
        table.setFillsViewportHeight(true);
        table.setVisible(true);
        table.setRowHeight(60);
        JScrollPane scrollPane = new JScrollPane(table);

        JPanel buttonsPanel = new JPanel(new GridLayout(1,3));

        b1 = new JButton("modify action");
        b2 = new JButton("create action");
        b3 = new JButton("delete selected actions");
        b4 = new JButton("move up");
        b5 = new JButton("move down");

        b1.setActionCommand("modify");
        b2.setActionCommand("create");
        b3.setActionCommand("delete");
        b4.setActionCommand("up");
        b5.setActionCommand("down");

        b1.addActionListener(this);
        b2.addActionListener(this);
        b3.addActionListener(this);
        b4.addActionListener(this);
        b5.addActionListener(this);

        buttonsPanel.add(b1);
        buttonsPanel.add(b2);
        buttonsPanel.add(b3);
        buttonsPanel.add(b4);
        buttonsPanel.add(b5);

        this.add(scrollPane, BorderLayout.PAGE_START);
        this.add(buttonsPanel, BorderLayout.PAGE_END);

        //ActionPanel ap = new ActionPanel();
        //ap.taActionName.setText("test name");
        //ap.taActionDuration.setText("dur test");
        //this.add(ap, BorderLayout.AFTER_LAST_LINE);
    }

    void    startPlanning( Project project ){

        //this.life = life;
        this.project = project;
        System.out.println("In SimpleProjectPlanner.startPlanning() " );// +container.toString() +", " +l.toString() +"  )" );

        tableModel.setRowCount(0);


        for( int i=0; i<project.getActions().size(); i++ ){
            System.out.println("about to add action with id " +project.getActions().elementAt(i).getId() +" and name " +project.getActions().elementAt(i).name );
            //tableModel.addRow( new Object[] {
            //                project.actions.elementAt(i).id,
            //                project.actions.elementAt(i).name,
            //                project.actions.elementAt(i).context,
            //                project.actions.elementAt(i).duration,
            //                project.actions.elementAt(i).startDate,
            //                project.actions.elementAt(i).dueDate
            //    } );
            tableModel.addRow( new Object[] {
                            project.getActions().elementAt(i).getId(),
                            project.getActions().elementAt(i).startDate,
                            project.getActions().elementAt(i) } );
            }


        // add empty line at the end for creating a new project
        tableModel.addRow( new Object[] {} );

        table.doLayout();
        this.repaint();
    }

    public void actionPerformed(ActionEvent e) {
        if( project!=null ){

            // stop the presses! (stop editing the table)
            if( table.getCellEditor() != null ){
                table.getCellEditor().stopCellEditing();
            }

            if ("modify".equals(e.getActionCommand())) {
                //life.modifyProject("new_project");
                System.out.println("SimpleProjectPlanner.actionPerformed( edit )");
                //main.startPlanningProject(life.projects.lastElement());
            } else if ("create".equals(e.getActionCommand())) {
                System.out.println("SimpleProjectPlanner.actionPerformed( create )");
                //System.out.println("row count:" + tableModel.getRowCount() );
                //System.out.println(tableModel.getColumnCount());


                ActionPanel ap = (ActionPanel)table.getCellEditor(tableModel.getRowCount()-1,2);
                String name = ap.getActionName();
                String context = ap.getActionContext();
                int duration = Integer.parseInt(ap.getActionDuration());
                String startDate = ""; //(String)table.getValueAt(tableModel.getRowCount()-1, 4);
                String dueDate = "";  //(String)table.getValueAt(tableModel.getRowCount()-1, 5);


                // Create the new Action in the Project, add the new id to the table
                System.out.println("about to create action with name=" +name + ", context=" +context +"...");
                // TODO in SimpleProjectPlanner: replace any direct access to Project methods by access through Life interface, then make getProject() private.
                int latestActionId = project.createAction(name, context, duration, startDate, dueDate);
                //table.setValueAt(Integer.toString(latestActionId),tableModel.getRowCount()-1, 0);
                table.setValueAt(latestActionId,tableModel.getRowCount()-1, 0);
                table.setValueAt(project.getAction(latestActionId).dueDate,tableModel.getRowCount()-1, 1);
                table.setValueAt(project.getAction(latestActionId),tableModel.getRowCount()-1, 2);

                // add empty line at the end for creating a new project
                tableModel.addRow( new Object[] {} );

            } else if ("delete".equals(e.getActionCommand())) {
                int selectedProjectRows[] = table.getSelectedRows();
                System.out.println("SimpleProjectPlanner.actionPerformed( delete )");
                for( int i=selectedProjectRows.length-1; i>=0; i-- ){
                    System.out.println("selected row:" + selectedProjectRows[i] );
                    if( project.deleteAction(((Integer)table.getValueAt(selectedProjectRows[i], 0)).intValue()) ){
                        tableModel.removeRow(selectedProjectRows[i]);
                    }
                }
            } else if ("up".equals(e.getActionCommand())) {
                System.out.println("SimpleProjectPlanner.actionPerformed( up )");

                if( table.getSelectedRowCount() != 1 ){
                    System.out.println("***** one and only one row should be selected");
                    return;
                }

                int selected = table.getSelectedRow();

                if( selected <= 0 ){
                    System.out.println("***** not allowed to move above the first line");
                    return;
                }

                if( table.getValueAt(selected, 0) == null ){
                    System.out.println("***** no actionId found for the selected row");
                    return;
                }

                System.out.println("about to move row " +selected +" to position " +(selected-1) );
                int newPosition = project.moveActionToPosition( ((Integer)table.getValueAt(selected,0)).intValue(), selected-1 );

                System.out.println("moved to row " +newPosition );
                tableModel.moveRow( selected, selected, newPosition );

                if( selected > 0 ){
                    table.setRowSelectionInterval(selected-1, selected-1);
                }

            } else if ("down".equals(e.getActionCommand())) {
                System.out.println("SimpleLifePlanner.actionPerformed( down )");

                if( table.getSelectedRowCount() != 1 ){
                    System.out.println("***** one and only one row should be selected");
                    return;
                }

                int selected = table.getSelectedRow();

                if( selected >= tableModel.getRowCount()-2 ){
                    System.out.println("***** not allowed to move below the last blanc line");
                    return;
                }

                if( table.getValueAt(selected, 0) == null ){
                    System.out.println("***** no actionId found for the selected row");
                    return;
                }

                System.out.println("about to move row " +selected +" to position " +(selected+1) );
                int newPosition = project.moveActionToPosition( ((Integer)table.getValueAt(selected,0)).intValue(), selected+1 );

                System.out.println("moved to row " +newPosition );
                tableModel.moveRow( selected, selected, newPosition );

                table.setRowSelectionInterval(selected+1, selected+1);
            }
        }
    }




}
