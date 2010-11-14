/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bgtd_gtd_core;

/**
 *
 * @author boris
 */
public class Action {
    int     id;
    private static  int    id_count = 1;

    public  String  name;
    public  String  context;
    public  int     duration;
    public  String  startDate;
    public  String  dueDate;

    public  int getId(){
        return id;
    }
    
    // for testing :
    Action( String name ){
        this.name = name;
        this.id = id_count++;
        context = "right here";
        duration = 5;
        startDate = "2nd March 2010";
        dueDate = "2nd March 2010";
    }

    // TODO this method to be deleted as soon as startDate is written/read to file by SimpleGtdMainGui
    Action( String name, String context, int duration, String dueDate ){
        this.name = name;
        this.id = id_count++;
        this.context = context;
        this.duration = duration;
        this.startDate = "";
        this.dueDate = dueDate;

        System.out.println("Action.Action()");
    }

    Action( String name, String context, int duration, String startDate, String dueDate ){
        this.name = name;
        this.id = id_count++;
        this.context = context;
        this.duration = duration;
        this.startDate = startDate;
        this.dueDate = dueDate;

        System.out.println("Action.Action()");
    }

    Action( int id, String name, String context, int duration, String startDate, String dueDate ){
        this.name = name;
        this.id = id;
        id_count = id+1;
        this.context = context;
        this.duration = duration;
        this.startDate = startDate;
        this.dueDate = dueDate;

        System.out.println("Action.Action()");
    }
}
