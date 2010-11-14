/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bgtd_gtd_core;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.Vector;

/**
 *
 * @author boris
 */
public class Project {
    Vector  <Action>    actions;
    int    id;
    static  int    id_count = 0;

    public  String  name;
    public  int     priority;

    int    goalId;

    public  Vector<Action>  getActions(){
        return  actions;
    }

    public  int getId(){
        return id;
    }

    public  int getGoalId(){
        return goalId;
    }

    Project( String name, int priority, int goalId ){
        this.id = id_count++;
        this.name = name;
        this.priority = priority;
        this.actions = new Vector <Action>();
        this.goalId = goalId;

        System.out.println("Project.Project(" +name +"," +priority +" ) id=" +id +".");
    }

    Project( int id, String name, int priority, int goalId ){
        this.id = id;
        id_count = id+1;    // any new project will have to receive ids above the id of any existing project
        this.name = name;
        this.priority = priority;
        this.actions = new Vector <Action>();
        this.goalId = goalId;

        System.out.println( "Project.Project(" +id +"," +name +"," +priority +"," +goalId +")" );
    }

    void    associateToGoal( int goalId ){
        this.goalId = goalId;
    }


    // *************************************************************************
    // *** Action related stuff...
    // ***

    Action  getNextAction(){
        return actions.firstElement();
    }

    public  Action  getAction( int actionId ){
        boolean found = false;
        int i;

        for( i=0; i<actions.size() && !found; i++ ){
            if( actions.elementAt(i).id ==actionId ){
                System.out.println("\tGetting action with name " + actions.elementAt(i).name);
                found = true;
                i--;
            }
        }

        if( found ){
            return actions.elementAt(i);
        }
        else{
            return null;
        }
    }

    public  int createAction( String name, String context, int duration, String startDate, String dueDate ){
        Action action = new Action( name, context, duration, startDate, dueDate );
        actions.add(action);
        return actions.lastElement().id;
    }

    int addAction( int actionId, String name, String context, int duration, String startDate, String dueDate ){
        Action action = new Action( actionId, name, context, duration, startDate, dueDate );
        actions.add(action);
        return actions.lastElement().id;
    }

    int modifyAction( int actionId, String name, String context, int duration, String startDate, String dueDate ){
        Action action = getAction(actionId);

        if( action!=null ){
            action.name = name;
            action.context = context;
            action.duration = duration;
            action.startDate = startDate;
            action.dueDate = dueDate;
            return actionId;
        }
        else{
            return 0;
        }
    }

    public  boolean deleteAction( int actionId ){
        System.out.println("In project.deleteAction( " +actionId +" )");

        boolean deleted = false;

        // find action, remove it from queue
        for( int i=0; i<actions.size() && !deleted; i++ ){
            if( actions.elementAt(i).id ==actionId ){
                System.out.println("\tDeleting action with name " + actions.elementAt(i).name);
                actions.removeElementAt(i);
                deleted = true;
            }
        }
        if( !deleted ){
            System.out.println("\tCouldn't find action with that id ");
        }

        return deleted;
    }

    public  int moveActionToPosition( int actionId, int position ){
        System.out.println("In project.moveActionToPosition( " +actionId +", position=" +position +" )");

        boolean moved = false;
        boolean found = false;

        if( position<0 )    position = 0;   // don't allow to go below position 0
        if( position>actions.size() )   position = actions.size();  // don't allow to go beyond the edge

        // find action, move it
        for( int i=0; i<actions.size() && !found && !moved; i++ ){
            if( actions.elementAt(i).id == actionId ){
                if( i == position ){
                    found = true;
                }
                else{
                    actions.insertElementAt(actions.elementAt(i), position);
                    if( position<i ){
                        actions.remove(i+1);
                    }
                    else{
                        actions.remove(i);
                        //position--;         // the position of the moved object changed
                    }
                    moved = true;
                }
            }
        }

        return position;
    }

}
