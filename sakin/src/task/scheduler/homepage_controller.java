/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package task.scheduler;

import java.awt.event.MouseEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;


/**
 *
 * @author johnroel
 */
public class homepage_controller {
    
     @FXML
    private AnchorPane dashboard_tab;
 
     @FXML
    private AnchorPane notif_tab;
     
//     @FXML
//    private AnchorPane dashboard_tab;
//     
//     @FXML
//    private AnchorPane dashboard_tab;
//     
//     @FXML
//    private AnchorPane dashboard_tab;
     
     @FXML
     private Button dashboard_btn;
     
      @FXML
     private Button notif_btn;
      
       @FXML
     private Button manage_btn;
     
      @FXML
     private Button todo_btn;
        
        @FXML
     private Button user_btn;
     
      @FXML
     private Button setting_btn;
        
      public void switchScenes(MouseEvent events){
          if(events.getSource() == dashboard_btn){
              notif_tab.setVisible(false);
              
          } else if(events.getSource() == notif_btn){
              dashboard_tab.setVisible(false);
           
          }
          
         
      }
      
}
