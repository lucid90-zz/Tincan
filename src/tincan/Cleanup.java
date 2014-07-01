/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tincan;

/**
 *
 * @author LucianDobre
 */
public class Cleanup extends Thread{
    Controller ctrl;

    public Controller getCtrl() {
        return ctrl;
    }

    public void setCtrl(Controller ctrl) {
        this.ctrl = ctrl;
    }
    
    @Override
    public void run(){
        System.out.println("TRALALLALALA");
        getCtrl().getListener().stopListening();
        getCtrl().setListener(null);
    }
    
}
