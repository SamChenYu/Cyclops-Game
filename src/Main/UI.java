package Main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.File;
import javax.swing.JPanel;
import java.util.ArrayList;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public final class UI extends JPanel implements Runnable {
    
    //All the game objects
        
    final KeyHandler keyH = new KeyHandler();
    Player player;
    ArrayList<Crab> crabs = new ArrayList<>();
    ArrayList<Bat> bats = new ArrayList<>();
    ArrayList<MechaGoblin> mechaGoblin = new ArrayList<>();
    ArrayList<Obstacle> obstacles = new ArrayList<>();
    ArrayList<Item> items = new ArrayList<>();
    ArrayList<KingGoblin> kingG = new ArrayList<>();
    ArrayList<Demon> demons = new ArrayList<>();


    Map map;
    AttackHandler attackHandler;
    
    String musicFilepath = "src/res/Song.wav";
    AudioInputStream audioInputStream;
    Clip clip;

    
    final long fpsDelay = 1000/60;
    long currentTime = System.currentTimeMillis();
    long lastUpdateTime = System.currentTimeMillis();
    private Thread gameThread;
    
    int width, height;
    
    boolean deathMessage = false;
    
    public UI(int width, int height) {
        this.width = width;
        this.height = height;
        
        player = new Player(width, height, keyH);
        attackHandler = new AttackHandler(player, bats, crabs, mechaGoblin,
                obstacles, items, demons);
        map = new Map(width, height,player, bats, crabs, mechaGoblin,
                obstacles, items, kingG, demons);
        player.initMap(map);
        
        loadMusicFile();
        setBackground(Color.BLACK);
        addKeyListener(keyH);
        setFocusable(true);
        requestFocus();
        startThread();
 
    }
    
    public void loadMusicFile() {
        File audioFile = new File(musicFilepath);
        try {
        audioInputStream = AudioSystem.getAudioInputStream(audioFile);
        clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        System.out.println("music loading done");
        } catch(Exception e) {
            System.out.println(e);
            System.out.println("music loading failed");
        }
    }
    
    public void startThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }
    
    public void update() {
        player.update();
        for(int i=0 ;i<crabs.size(); i++) {
            crabs.get(i).update();
        }

        for(int i=0; i<bats.size(); i++) {
            bats.get(i).update();
        }
        
        for(int i=0; i<mechaGoblin.size(); i++) {
            mechaGoblin.get(i).update();
        }

        for(int i=0; i<obstacles.size(); i++) {
            obstacles.get(i).update();
        }

        for(int i=0; i<items.size(); i++) {
            items.get(i).update();
        }

        for(int i=0; i<kingG.size(); i++) {
            kingG.get(i).update();
        }

        for(int i=0; i<demons.size(); i++) {
            demons.get(i).update();
        }

        attackHandler.update();
        map.update();
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        map.draw(g2);

        for(int i=0; i<obstacles.size(); i++) {
            obstacles.get(i).draw(g2);
        }

        for(int i=0; i<items.size(); i++) {
            items.get(i).draw(g2);
        }
        
        for(int i=0; i<crabs.size(); i++) {
            crabs.get(i).draw(g2);
        }
        for(int i=0; i<bats.size(); i++) {
            bats.get(i).draw(g2);
        }
        
        for(int i=0; i<mechaGoblin.size(); i++) {
            mechaGoblin.get(i).draw(g2);
        }

        for(int i=0; i<kingG.size(); i++) {
            kingG.get(i).draw(g2);
        }

        for(int i=0; i<demons.size(); i++) {
            demons.get(i).draw(g2);
        }

        player.draw(g2);


        
        
        if(keyH.hitboxes) {
            player.drawHitbox(g2);
            for(int i=0 ;i<crabs.size(); i++) {
                crabs.get(i).drawHitbox(g2);
            }
            for(int i=0; i<bats.size(); i++) {
                bats.get(i).drawHitbox(g2);
            }
            for(int i=0; i<mechaGoblin.size(); i++) {
                mechaGoblin.get(i).drawHitbox(g2);
            }
            for(int i=0; i<obstacles.size(); i++) {
                obstacles.get(i).drawHitbox(g2);
            }

            for(int i=0; i<items.size(); i++) {
                items.get(i).drawHitbox(g2);
            }

            for(int i=0; i<kingG.size(); i++) {
                kingG.get(i).drawHitbox(g2);
            }

            for(int i=0; i<demons.size(); i++) {
                demons.get(i).drawHitbox(g2);
            }


        }
        g2.setFont(new Font("Arial", Font.BOLD, 50));
        if(deathMessage) {
            g2.setColor(Color.GREEN);
            g2.drawString("YOU ARE DEAD", width/2-200, height/2);
        }

        g2.dispose();
    }

    @Override
    public void run() {
        //clip.start(); to play the music
        while(!player.isDead()) {
 
            
            currentTime = System.currentTimeMillis();
            
            if(currentTime - lastUpdateTime >= fpsDelay) {
                update();
                repaint();
                lastUpdateTime = System.currentTimeMillis();
            }

        } // End game loop
        deathMessage = true;
        repaint();
    }
    
}