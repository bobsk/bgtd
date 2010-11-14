/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bgtd_gtd_core;

/**
 *
 * @author boris
 */
public class Goal {
    int id;
    static int id_counter = 1;

    public  String name;

    public  int getId(){
        return id;
    }

    Goal( int id, String name ){
        System.out.println("Goal.Goal(id="+id+",name="+name+")");
        this.id = id;
        id_counter = id+1;
        this.name = name;
    }

    Goal(){
        id = id_counter++;
        this.name = "";
    }

    Goal(String name){
        id = id_counter++;
        this.name = name;
    }

}
