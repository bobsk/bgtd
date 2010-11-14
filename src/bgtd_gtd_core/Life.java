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
public class Life {

    public  String name;
    Vector<Goal> goals;
    Vector<Project> projects;
    Vector<String> thoughts;

    public  Vector<Goal>    getGoals(){
        return goals;
    }

    public  Vector<Project>    getProjects(){
        return projects;
    }

    public  Vector<String>    getThoughts(){
        return thoughts;
    }

    // *************************************************************************
    // *** Life stuff...

    public  Life(String name) {
        this.name = name;
        goals = new Vector<Goal>();
        projects = new Vector<Project>();
        thoughts = new Vector<String>();
    }

    public  Life() {
        this.name = "";
        goals = new Vector<Goal>();
        projects = new Vector<Project>();
        thoughts = new Vector<String>();
    }

    // *************************************************************************
    // *** Life stuff...
    public  void addThought(String thought) {
        thoughts.add(thought);
    }

    public  boolean deleteThought(String thought){
        return thoughts.remove(thought);
    }


    // *************************************************************************
    // *** Goal stuff...

    public  void addGoal(int goalId, String name) {
        //System.out.println("Life.addGoal() named >>>" +name +"<<<, currently are " +goals.size() );
        goals.add(new Goal(goalId, name));
        return;
    }

    public  boolean deleteGoal(int goalId){
        boolean done=false;
        for(int i=0;!done&&i<goals.size();i++){
            if( goals.elementAt(i).id==goalId ){
                goals.remove(i);
                done=true;
            }
        }
        return done;
    }


    // *************************************************************************
    // *** Project stuff...

    public  int createProject(String name, int priority, int goalId) {
        System.out.println("In Life.createProject( " +name +", " +priority +", " +goalId +" )");
        projects.add(new Project(name, priority,goalId));

        return projects.lastElement().id;
    }

    public  int addProject(int id, String name, int priority, int goalId ) {
        System.out.println("In Life.addProject( " + id + ", " + name + ", " + priority +", " +goalId + " )");
        projects.add(new Project(id, name, priority, goalId));

        return projects.lastElement().id;
    }

    public  boolean deleteProject(int projectId) {
        System.out.println("In Life.deleteProject( " + projectId + " )");

        boolean deleted = false;

        // find project, remove it from queue
        for (int i = 0; i < projects.size() && !deleted; i++) {
            if (projects.elementAt(i).id == projectId) {
                System.out.println("\tRemoving project with name " + projects.elementAt(i).name);
                projects.removeElementAt(i);
                deleted = true;
            }
        }
        if (!deleted) {
            System.out.println("\tCouldn't find project with that id ");
        }

        return deleted;
    }

    public  void modifyProject(int projectId, String name, int priority) {
        System.out.println("In Life.modifyProject( " + projectId + " )");

        // find project, modify it
        int i;
        for (i = 0; i < projects.size(); i++) {
            if (projects.elementAt(i).id == projectId) {
                System.out.println("\tModifying project with name " + projects.elementAt(i));
                projects.elementAt(i).name = name;
                projects.elementAt(i).priority = priority;
            }
        }
        if (i >= projects.size()) {
            System.out.println("\tCouldn't find project with id " + projectId);
        }

        return;
    }

    public  String[] getProjectNextAction(int projectId) {
        System.out.println("Life.getProjectNextAction( projectId=" + projectId + ")");
        for (int i = 0; i < projects.size(); i++) {
            if (projects.elementAt(i).id == projectId) {
                if (projects.elementAt(i).actions.isEmpty()) {
                    System.out.println("Life.getProjectNextAction() : project found, but has no action");
                    return new String[]{"0", ""};
                } else {
                    System.out.println("Life.getProjectNextAction() : project found, action found");
                    return new String[]{
                                Integer.toString(projects.elementAt(i).actions.elementAt(0).id),
                                projects.elementAt(i).actions.elementAt(0).name
                            };
                }
            }
        }
        System.out.println("Life.getProjectNextAction() : project not found");
        return new String[]{"0", ""};
    }

    public  boolean changeGoalOfProject(int projectId, int goalId){
        boolean changed = true;
        Project p = getProject(projectId);
        if( p!=null ){
            p.associateToGoal(goalId);
        }
        else{
            System.out.println("Life.changeGoalOfProject() : couldn't find project.");
            changed = false;
        }
        return changed;
    }

    // TODO make this private as soon as no one uses anymore.
    public  Project getProject(int projectId) {
        System.out.println("Life.getProject( projectId=" + projectId + ")");
        for (int i = 0; i < projects.size(); i++) {
            if (projects.elementAt(i).id == projectId) {
                return projects.elementAt(i);
            }
        }
        System.out.println("Life.getProjectNextAction() : project not found, returning null");
        return null;
    }


    // *************************************************************************
    // *** Action stuff...

    public  int createAction(int projectId, String name, String context, int duration, String startDate, String dueDate) {
        return getProject(projectId).createAction(name, context, duration, startDate, dueDate);
    }

    public  int addAction(int projectId, int actionId, String name, String context, int duration, String startDate, String dueDate) {
        return getProject(projectId).addAction(actionId, name, context, duration, startDate, dueDate);
    }

    public  boolean changeProjectOfAction( int oldProjectId, int newProjectId, int actionId ){
        Project oldProject = null;
        Action  action = null;
        boolean swapDone = true;

        // If oldProjectId is not given, find it, and get corresponding Project.
        // And find the Action while we're at it.
        if(oldProjectId<1){
            boolean found = false;
            for(int i=0;!found&&i<projects.size();i++){
                for(int j=0;!found&&j<projects.elementAt(i).actions.size();j++){
                    if( projects.elementAt(i).actions.elementAt(j).id == actionId){
                        found = true;
                        oldProject = projects.elementAt(i);
                        action = projects.elementAt(i).actions.elementAt(j);
                    }
                }
            }
        }
        else{
            oldProject = getProject(oldProjectId);
            if(oldProject!=null){
                action=getAction(oldProject.id,actionId);
            }
        }

        // If all found, make the change
        if(oldProject!=null&&action!=null ){
            Project newProject = getProject(newProjectId);
            if(newProject!=null){
                newProject.actions.add(action);
                oldProject.actions.remove(action);
            }
            else{
                System.out.println("Life.associateProjectToAction() : couldn't find new project");
                swapDone=false;
            }
        }
        else{
            System.out.println("Life.associateProjectToAction() : couldn't find old project or action");
            swapDone=false;
        }

        return swapDone;
    }

    public  Action getAction( int actionId ){
        for(int i=0;i<projects.size();i++){
            for(int j=0;j<projects.elementAt(i).actions.size();j++){
                if(projects.elementAt(i).actions.elementAt(j).id==actionId){
                    return projects.elementAt(i).actions.elementAt(j);
                }
            }
        }
        return null;
    }

    public  Action getAction( int projectId, int actionId ){
        Project p = getProject(projectId);
        if(p!=null){
            for(int i=0; i<p.actions.size();i++){
                if(p.actions.elementAt(i).id==actionId){
                    return p.actions.elementAt(i);
                }
            }
        }
        return null;
    }

}
