package Main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class KeyHandler implements KeyListener {
    // movement booleans
    public boolean idle, upPressed, downPressed, leftPressed, rightPressed;
    // action booleans
    public boolean rockThrow, stomp, laser, dead;
    boolean hitboxes = false;
    
    
    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        
        int code = e.getKeyCode();
        
        idle = true;
        
        if(code == KeyEvent.VK_W) {
            upPressed = true;
            idle = false;
        }
        
        if(code == KeyEvent.VK_A) {
            leftPressed = true;
            idle = false;
        }
        
        if(code == KeyEvent.VK_S) {
            downPressed = true;
            idle = false;
        }
        
        if(code == KeyEvent.VK_D) {
            rightPressed = true;
            idle = false;
        }
        
        if(code == KeyEvent.VK_ENTER) {
            idle = false;
            rockThrow = true;
        }
        
        if(code == KeyEvent.VK_F) {
            idle = false;
            stomp = true;
        }
        
               
        if(code == KeyEvent.VK_SPACE) {
            idle = false;
            laser = true;
        }
        
        if(code == KeyEvent.VK_J) { // for testing purposes
            idle = false;
            dead = true;
        }
        
        
        if(code == KeyEvent.VK_P) {
            hitboxes = true;
        }
 
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
        int code = e.getKeyCode();
        
        idle = false;
        
        if(code == KeyEvent.VK_W) {
            upPressed = false;
            if(!downPressed) {
                idle = true;
            }
            
        }
        
        if(code == KeyEvent.VK_A) {
            leftPressed = false;
            if(!rightPressed) {
                idle = true;
            }
        }
        
        if(code == KeyEvent.VK_S) {
            downPressed = false;
            if(!upPressed) {
                idle = true;
            }
        }
        
        if(code == KeyEvent.VK_D) {
            rightPressed = false;
            if(!leftPressed) {
                idle = true;
            }
        }
        
        if(code == KeyEvent.VK_ENTER) {
            rockThrow = false;
            idle = true;
            
        }
        
        if(code == KeyEvent.VK_F) {
            stomp = false;
            idle = true;
        }
        
        
        if(code == KeyEvent.VK_SPACE) {
            laser = false;
            idle = true;
        }
        
        if(code == KeyEvent.VK_J) { // for testing purposes
            //dead = false;
            //idle = true;
        }
        
        if(code == KeyEvent.VK_P) {
            hitboxes = false;
        }
    }
    

}