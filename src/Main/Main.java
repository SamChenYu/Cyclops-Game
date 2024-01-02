package Main;


import javax.swing.JFrame;


public class Main {
    
    
    
    
    public static void main(String[] args) {
        
        final int height = 400;
        final int width = 1280;
        
        
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Depth Perception: A Cycloptic Adventure");
        
        UI ui = new UI(width, height);
        

        window.add(ui);
        window.pack();
        
        window.setSize(width,height);
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }
    
}
