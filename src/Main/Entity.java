
package Main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Entity {
    
    // Injected Objects
    Player player;
    int width, height;
    
    
    // Entity Attributes
    int x;
    int y;
    int maxHealth;
    int health;
    int scale;
    int speed;
    int damage;
    int movement[];
    int movementFlag = 0;
    boolean isDead = false;
    
    // Image Stuff
    int spriteWidth, spriteHeight;
    int spriteSheetWidth, spriteSheetHeight;
    
    // Attacking stuff
    int fpsDamageFlag = 0;
    String attack = "idle";
    
    Rectangle hitbox;
    Rectangle imageBox;
    Rectangle healthBox_green;
    Rectangle healthBox_red;
    
    String spriteFilePath;
    BufferedImage spriteSheet;
    BufferedImage[][] sprites; //= new BufferedImage[5][3];
    BufferedImage activeImage;
    int fpsSpriteCounter = 0;
    int spriteCounter = 0;
    boolean takingDamage = false;
    int takingDamageCounter = 0;
    
    int fpsDamageCounter = 0;
    
    
    public Entity(int width, int height, Player player, int spriteWidth, int spriteHeight,
                 int spriteSheetWidth, int spriteSheetHeight) {
        this.width = width;
        this.height = height;
        this.player = player;
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
        this.spriteSheetWidth = spriteSheetWidth;
        this.spriteSheetHeight = spriteSheetHeight;

    }

    public void initRectangles() {
        hitbox = new Rectangle(x*(32+scale),y+(32*scale),spriteWidth*scale, spriteHeight*scale);
        imageBox = new Rectangle(x,y,spriteWidth*scale, spriteHeight*scale);
        healthBox_green = new Rectangle(x,y,maxHealth,10);
        healthBox_red = new Rectangle(x,y,maxHealth - health, 10);
    }
    
    public void loadSpriteSheet() {
        try {
            spriteSheet = ImageIO.read(getClass().getResourceAsStream(spriteFilePath));
        } catch(IOException e) {
            System.out.println(e);
        }
    }
    
    public void loadImages() {
        //code to split the individual sprites
            for(int i=0; i<spriteSheetWidth; i++) {
                for(int j=0; j<spriteSheetHeight; j++) {
                    sprites[i][j] = spriteSheet.getSubimage(i*spriteWidth, j*spriteHeight, spriteWidth, spriteHeight);
                } 
            }
            activeImage = sprites[0][0];
        
        
    }
    
    
    public void update() {
        // Update the x / y coordinates
        
        
        // Update the hitboxes
        hitbox.x = x; // will needed to be tweaked
        hitbox.y = y; // will needed to be tweaked
        imageBox.x = x;
        imageBox.y = y;
        
        healthBox_green.x = x; // will needed to be tweaked
        healthBox_green.y = y; // will needed to be tweaked
        healthBox_red.x = x; // will needed to be tweaked
        healthBox_red.y = y; // will needed to be tweaked
        healthBox_red.width = maxHealth - health;
        
    }
    
    public void draw(Graphics2D g2) {
        g2.drawImage(activeImage,x,y,spriteWidth*scale,spriteHeight*scale,null);
        g2.setColor(Color.GREEN);
        g2.fill(healthBox_green);
        g2.setColor(Color.RED);
        g2.fill(healthBox_red);
    }
    
    public void drawHitbox(Graphics2D g2) {
        g2.setColor(Color.GREEN);
        g2.draw(hitbox);
        g2.setColor(Color.RED);
        g2.draw(imageBox);
    }


    public void updateSpriteDamage() {

        if(takingDamage) {
            if(takingDamageCounter < 8) {
                takingDamageCounter++;
                turnSpriteRed();
            } else {
                takingDamage = false;
                takingDamageCounter = 0;
            }
        }

    }
    public void turnSpriteRed() {
        // turn the player image red
        BufferedImage redImage = new BufferedImage(spriteWidth, spriteHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = redImage.createGraphics();

        g2d.drawImage(activeImage,0,0,null);
        g2d.dispose();

        int[] pixels = redImage.getRGB(0, 0, spriteWidth, spriteHeight, null, 0, spriteWidth);

        for (int i = 0; i < pixels.length; i++) {
            int alpha = (pixels[i] >> 24) & 0xFF;
            int red = (pixels[i] >> 16) & 0xFF;
            int green = (pixels[i] >> 8) & 0xFF;
            int blue = pixels[i] & 0xFF;

            // Apply red color filter only to non-transparent pixels
            if (alpha != 0) {
                red = Math.min(red + 255, 255); // You can adjust the intensity of red here
            }

            pixels[i] = (alpha << 24) | (red << 16) | (green << 8) | blue;
        }

        redImage.setRGB(0, 0, spriteWidth, spriteHeight, pixels, 0, spriteWidth);

        activeImage = redImage;

    }

    public int getX() { return x +(spriteWidth*scale)/2; }
    public void reduceHealth(int dmg) { health -= dmg; takingDamage = true; }
    public Rectangle getHitbox() { return hitbox; }
    public int getFPSDamageCounter() { return fpsDamageCounter; }
    public String getAttack() { return attack; }
    public int getDamage() { return damage; }
    public int getFPSDamageFlag() { return fpsDamageFlag; } // this is for the attackHandler
    public void incrementDamageFlag() { fpsDamageFlag++; }
    public void resetDamageFlag() { fpsDamageFlag = 0; }


}