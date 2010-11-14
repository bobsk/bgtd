/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bgtd_simple_gui;

import bgtd_gtd_core.*;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author boris
 */
public interface GtdMainUi {


    void    createLife( String name );
    void    deleteLife();
    void    saveLife( String fileName );
    void    loadLife( String fileName );


    void    startPlanningProject( Project project );
    void    startPlanningLife( Life life );
}
