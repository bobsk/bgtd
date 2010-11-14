/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bgtd_simple_gui;


import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.GridLayout;


import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//import java.awt.event.KeyEvent;
import java.awt.BorderLayout;

import java.io.*;

import java.util.HashMap;
import java.util.Vector;

import bgtd_gtd_core.*;
/**
 *
 * @author boris
 */
public class GtdMainUiImpl extends JFrame implements GtdMainUi {

    Life    life;

    ProjectPlannerImpl pplanner;
    LifePlannerImpl lplanner;


    public GtdMainUiImpl(){

        super("GTD");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //life = new Life( "let's get it started!" );

        //pplanner = new SimpleProjectPlanner();
        lplanner = new LifePlannerImpl();

        //pplanner.setVisible(false);
        //lplanner.setVisible(false);

        this.getContentPane().add(lplanner, BorderLayout.CENTER);
        //this.getContentPane().add(pplanner, BorderLayout.PAGE_END);


        //frame.getContentPane().add(pplanner,1);
        //frame.setLayout(new LayoutManager(LayoutManager.class));
        //frame.validate();

        //JPanel panel = new JPanel();
        //frame.getContentPane


        //project = new Project("test_project", 0);
        //pplanner.startPlanning( frame.getContentPane(), project );

        // ****** The GUI parts for managing the main GTD stuff.
        JPanel gtdMainPanel = new JPanel( new GridLayout(1, 4) );
        JButton b1 = new JButton("New Life");
        //JButton b2 = new JButton("Delete Life");
        JButton b3 = new JButton("Save Life");
        JButton b4 = new JButton("Load Life");

        b1.setActionCommand("new");
        //b2.setActionCommand("delete");
        b3.setActionCommand("save");
        b4.setActionCommand("load");

        class MainActionListener implements ActionListener {
            // stuff to do on keypress
            public void actionPerformed(ActionEvent e) {
                System.out.println("SimpleGtdMainGui.MainActionListener.actionPerformed( " + e.getActionCommand() );
                if ("new".equals(e.getActionCommand())) {
                    life = new Life("It's my new life!");
                } else if ("save".equals(e.getActionCommand())) {

                    if( life == null ){
                        System.out.println("SimpleGtdMainGui.MainActinoListener(): no Life to save.");
                        return;
                    }
                    saveLife("./gtd_test_modified.xml");

                } else if ("load".equals(e.getActionCommand())) {

                    // get rid of the previous Life
                    life = new Life();

                    loadLife("./gtd_test.xml");

                    startPlanningLife(life);
                }
            }
        }

        MainActionListener mainActionLister = new MainActionListener();

        b1.addActionListener(mainActionLister);
        //b2.addActionListener(mainActionLister);
        b3.addActionListener(mainActionLister);
        b4.addActionListener(mainActionLister);

        gtdMainPanel.add(b1);
        //gtdMainPanel.add(b2);
        gtdMainPanel.add(b3);
        gtdMainPanel.add(b4);

        this.getContentPane().add(gtdMainPanel,BorderLayout.PAGE_START);
        // ******





        this.pack();
        this.setVisible(true);
        this.setSize(800, 500);
        this.setResizable(true);

        //this.startPlanningLife( life );
    }


    public  void    createLife( String name ){

    }

    public  void    deleteLife(){

    }

    public  void    saveLife( String filename ){

        try{
            // Create file
            FileWriter fstream = new FileWriter(filename);
            BufferedWriter out = new BufferedWriter(fstream);

            out.write("<life>\n");
            out.write("\t<name>" +life.name + "</name>\n");


            out.write("\t<thoughts>\n");
            for( int i=0;i<life.getThoughts().size();i++ ){
                out.write("\t\t<thought>" +life.getThoughts().elementAt(i) +"</thought>\n");
            }
            out.write("\t</thoughts>\n");


            for( int i=0;i<life.getGoals().size();i++){
                out.write("\t<goal>\n");

                out.write("\t\t<id>" +life.getGoals().elementAt(i).getId() +"</id>\n");
                out.write("\t\t<name>" +life.getGoals().elementAt(i).name +"</name>\n");

                out.write("\t\t<projects>\n");
                for( int j=0;j<life.getProjects().size();j++ ){
                    // TODO implement in LifePlanner a way to manage goals and projects' belonging to goals.
                    //if(life.getProjects().elementAt(j).getGoalId()==life.getGoals().elementAt(i).getId()){
                        out.write("\t\t\t<project>\n");
                        out.write("\t\t\t\t<id>"+life.getProjects().elementAt(j).getId()+"</id>\n");
                        out.write("\t\t\t\t<name>"+life.getProjects().elementAt(j).name+"</name>\n");
                        out.write("\t\t\t\t<priority>"+life.getProjects().elementAt(j).priority+"</priority>\n");
                        out.write("\t\t\t\t<actions>\n");
                        for(int k=0;k<life.getProjects().elementAt(j).getActions().size();k++){
                            out.write("\t\t\t\t\t<action>\n");
                            out.write("\t\t\t\t\t\t<id>"+life.getProjects().elementAt(j).getActions().elementAt(k).getId()+"</id>\n");
                            out.write("\t\t\t\t\t\t<name>"+life.getProjects().elementAt(j).getActions().elementAt(k).name+"</name>\n");
                            out.write("\t\t\t\t\t\t<context>"+life.getProjects().elementAt(j).getActions().elementAt(k).context+"</context>\n");
                            out.write("\t\t\t\t\t\t<duration>"+life.getProjects().elementAt(j).getActions().elementAt(k).duration+"</duration>\n");
                            out.write("\t\t\t\t\t\t<startDate>"+life.getProjects().elementAt(j).getActions().elementAt(k).startDate+"</startDate>\n");
                            out.write("\t\t\t\t\t\t<dueDate>"+life.getProjects().elementAt(j).getActions().elementAt(k).dueDate+"</dueDate>\n");
                            out.write("\t\t\t\t\t\t<projectPlannerData projectPlanner=\"SimpleProjectPlanner\" colour=\"blue\"/>\n");
                            out.write("\t\t\t\t\t</action>\n");
                        }
                        out.write("\t\t\t\t</actions>\n");
                        out.write("\t\t\t</project>\n");
                    //}
                }
                out.write("\t\t</projects>\n");

                out.write("\t</goal>\n");
            }

            out.write("</life>\n");

            //Close the output stream
            out.close();
        }catch (Exception exceptional){//Catch exception if any
            System.err.println("Error: " + exceptional.getMessage());
        }

    }


    private class XmlFileParser{

        BufferedReader br;
        String str;         // the string to parse

        void parseFile( String filename ){

            try{
                //Open file, data stream, reader...
                FileInputStream fstream = new FileInputStream(filename);
                DataInputStream in = new DataInputStream(fstream);
                br = new BufferedReader(new InputStreamReader(in));

                String newline;
                while( (newline=br.readLine())!=null ){
                    str+=newline;
                    System.out.println(str);
                }
                parse();

                //Close the input stream
                in.close();
            }catch (Exception exceptional){//Catch exception if any
                  System.err.println("***** Error. Exception: " + exceptional.getMessage());
            }
        }

        Element parse( /*String str, boolean greedy*/ ){

            System.out.println("parse() : >>>" + str +"<<<");
            str = str.trim();

            Element e = new Element();      // The element we'll return
            int pBeg, pEnd;                 // Begin and end pointers in the input string

            pBeg = str.indexOf('<');
            /*
            while( greedy && pBeg==-1 ){
                System.out.println("pBeg not found, str = " + str);
                try{
                    str += br.readLine();
                }catch( Exception exceptional ){
                  System.err.println("***** Error in parse at readLine(). Exception: " + exceptional.getMessage());
                }
                pBeg = str.indexOf('<');
            }
            */
            if( pBeg==-1 ){
                System.out.println("SimpleGtdMainGui.loadLife.XmlFileParser.parse() : couldn't find opening '<'");
                return e;
            }
            else{
                System.out.println("SimpleGtdMainGui.loadLife.XmlFileParser.parse() : opening '<' found at pos " +pBeg);
            }

            pEnd = str.indexOf('>',pBeg);
            System.out.println("SimpleGtdMainGui.loadLife.XmlFileParser.parse() : closing '>' found at pos " +pEnd);
            if( pEnd!=-1 ){

                if(str.charAt(pEnd-1)=='/' ){   // ----- standalone tag like <nameOfTag/>: no content
                    System.out.println("SimpleGtdMainGui.loadLife.XmlFileParser.parse() : standalone tag " +pEnd);
                    e.name = str.substring(pBeg+1,pEnd-1);
                    e.content = "";

                    str = str.substring(pEnd+1);
                }
                else{                           // --- yes content
                    System.out.println("SimpleGtdMainGui.loadLife.XmlFileParser.parse() : contentful tag " +pEnd);
                    e.name = str.substring(pBeg+1,pEnd);
                    pBeg = pEnd+1;
                    String endTag = "</" +e.name +">";
                    pEnd = str.indexOf(endTag);
                    /*
                    while( pEnd==-1 && greedy ){
                        System.out.println("pBeg not found for closing tag \"" +endTag +"\", str = " + str);
                        try{
                            str+=br.readLine();
                        }catch( Exception exceptional ){
                            System.err.println("***** Error. Exception: " + exceptional.getMessage());
                        }
                        pEnd = str.indexOf(endTag);
                    }
                    */
                    if( pEnd==-1 ){
                        System.out.println("SimpleGtdMainGui.loadLife.XmlFileParser.parse() : couldn't find closing tag " +endTag);
                        return e;
                    }

                    e.content = str.substring(pBeg, pEnd);
                    str = str.substring(pEnd+endTag.length());
                }
            }
            System.out.println("about to interpret element with name=>>>" +e.name +"<<< content=>>>" +e.content +"<<<");


            interpret( e );

            return e;
        }


        Element parseElementContent( Element parent ){

            //System.out.println("parseElementContent() : >>>" + parent.content +"<<<" );
            parent.content = parent.content.trim();

            Element e = new Element();      // The element we'll return
            int pBeg, pEnd;                 // Begin and end pointers in the input string

            pBeg = parent.content.indexOf('<');
            if( pBeg==-1 ){
                System.out.println("SimpleGtdMainGui.loadLife.XmlFileParser.parseElementContent() : couldn't find opening '<'");
                return e;
            }
            else{
                //System.out.println("SimpleGtdMainGui.loadLife.XmlFileParser.parseElementContent() : opening '<' found at pos " +pBeg);
            }

            pEnd = parent.content.indexOf('>',pBeg);
            //System.out.println("SimpleGtdMainGui.loadLife.XmlFileParser.parseElementContent() : closing '>' found at pos " +pEnd);
            if( pEnd!=-1 ){

                if(parent.content.charAt(pEnd-1)=='/' ){   // ----- standalone tag like <nameOfTag/>: no content
                    //System.out.println("SimpleGtdMainGui.loadLife.XmlFileParser.parseElementContent() : standalone tag " +pEnd);
                    e.name = parent.content.substring(pBeg+1,pEnd-1);
                    e.content = "";

                    parent.content = parent.content.substring(pEnd+1);
                }
                else{                           // --- yes content
                    //System.out.println("SimpleGtdMainGui.loadLife.XmlFileParser.parseElementContent() : contentful tag " +pEnd);
                    e.name = parent.content.substring(pBeg+1,pEnd);
                    pBeg = pEnd+1;
                    String endTag = "</" +e.name +">";
                    pEnd = parent.content.indexOf(endTag);

                    if( pEnd==-1 ){
                        System.out.println("SimpleGtdMainGui.loadLife.XmlFileParser.parseElementContent() : couldn't find closing tag " +endTag);
                        return e;
                    }

                    e.content = parent.content.substring(pBeg, pEnd);
                    parent.content = parent.content.substring(pEnd+endTag.length());
                }
            }
            return e;
        }


        void interpret( Element e ){
            System.out.println("interpreter: received element, name = >>>" +e.name +"<<<");

            if( e.name.compareTo("thought")==0 ){
                System.out.println("interpreter: found thought, content = >>>" +e.content +"<<<");
                life.addThought(e.content);
            }


            else if( e.name.compareTo("goal")==0 ){
                System.out.println("interpreter: found project, content = >>>" +e.content +"<<<");

                while( e.content.length()!=0 ){
                    Element child = parseElementContent(e);
                    System.out.println("Goal's child: >>>" +child.name +"<<<");
                    e.children.add(child);
                }
                System.out.println("Goal should have 3 children: (id,name,projects), actually has >>>" +e.children.size() +"<<<");

                System.out.println( "SimpleGtdMainGui.XMLParser.interpret(), about to add goal"
                                    +e.getChildNamed("id").content
                                    +e.getChildNamed("name").content );
                life.addGoal(   Integer.parseInt(e.getChildNamed("id").content),
                                e.getChildNamed("name").content );


                System.out.println("about to extract children from Element 'projects', content length = " +e.getChildNamed("projects").content.length() );


                while(e.getChildNamed("projects").content.length()>0){
                    Element projectElement = parseElementContent(e.getChildNamed("projects"));

                    System.out.println("interpreter: found project, content = >>>" +projectElement.content +"<<<");

                    //str = projectElement.content;
                    while(projectElement.content.length()!=0){
                        Element child = parseElementContent(projectElement);
                        System.out.println("project's child:" +child.name );
                        projectElement.children.add(child);
                    }

                    System.out.println("about to add project " +projectElement.getChildNamed("id").content +", "
                            + projectElement.getChildNamed("name").content +", "
                            + projectElement.getChildNamed("priority").content +", "
                            + e.getChildNamed("id").content );
                    life.addProject(
                            Integer.parseInt(projectElement.getChildNamed("id").content),
                            projectElement.getChildNamed("name").content,
                            Integer.parseInt(projectElement.getChildNamed("priority").content),
                            Integer.parseInt(e.getChildNamed("id").content)
                            );

                    System.out.println("about to extract children from Element 'actions', content length = " +projectElement.getChildNamed("actions").content.length() );
                    while(projectElement.getChildNamed("actions").content.length()>0){
                        Element actionElement = parseElementContent(projectElement.getChildNamed("actions"));
                        while( actionElement.content.length()>0 ){
                            actionElement.children.add( parseElementContent(actionElement) );
                        }


                        life.addAction(
                                Integer.parseInt(projectElement.getChildNamed("id").content),
                                Integer.parseInt(actionElement.getChildNamed("id").content),
                                actionElement.getChildNamed("name").content,
                                actionElement.getChildNamed("context").content,
                                Integer.parseInt(actionElement.getChildNamed("duration").content),
                                actionElement.getChildNamed("startDate").content,
                                actionElement.getChildNamed("dueDate").content);
                    }
                }

            }




            else if( e.name.compareTo("life")==0 ){
                System.out.println("interpreter: found life, content = >>>" +e.content +"<<<");
                str = e.content;
                while(str.length()!=0){
                    Element child = parse(/*e.content, false*/);
                    System.out.println("life's child:" +child.name );
                    life.name = child.content;
                }
            }

        }

        private class Element{

            String name;
            String content;
            HashMap <String,String> attributes;
            Vector <Element> children;

            Element(){
                name = "";
                content = "";
                attributes = new HashMap<String,String>();
                children = new Vector<Element>();
            }

            Element getChildNamed( String childName ){
                boolean found = false;
                for( int i=0; i<children.size(); i++ ){
                    if( children.elementAt(i).name.compareTo(childName) == 0 ){
                        found = true;
                        //System.out.println("SimpleGtdMainGui.XmlFileParser.Element.getChildNamed(), found " +children.elementAt(i).name );
                        return children.elementAt(i);
                    }
                }
                System.out.println("SimpleGtdMainGui.XmlFileParser.Element.getChildNamed(), nothing found " );
                return new Element();
            }

        }

    }



    public  void    loadLife( String filename ){

        XmlFileParser xfp = new XmlFileParser();
        xfp.parseFile(filename);

    }



    public void    startPlanningProject( Project project ){
        System.out.println("SimpleGtdMainGui.startPlanningProject( p )");
     /*   lplanner.setVisible(false);
        //frame.getContentPane().removeAll();
        pplanner.startPlanning( project );
        //frame.getContentPane().add(pplanner);
        pplanner.setVisible(true);*/
    }

    public void    startPlanningLife( Life life ){
        //pplanner.setVisible(false);
        ////frame.getContentPane().removeAll();
        lplanner.startPlanning( life );
        ////frame.getContentPane().add(lplanner);
        //lplanner.setVisible(true);
    }

}
