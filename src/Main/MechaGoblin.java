
package Main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;


public final class MechaGoblin extends Entity {
    
    Random r = new Random();
    
    String state = "idle";
    String direction = "right";
    int idleStateCounter = 0; // after attacking it will wait a few frames before reattacking
    int targetLockedPlayerX; // The mech will lock onto the player's location and go there
    
    
    final Rectangle attackRight = new Rectangle(x,y,180,460) ;
    //final Rectangle attackLeft = new Rectangle(x,y,440,460);
    final Rectangle attackLeft = new Rectangle(x,y,180,460);
    final Rectangle targetLock = new Rectangle(0,200,50,50);

    
    
    public MechaGoblin(int width, int height, Player player) {
        super(width, height, player,160,96, 8,5);
        x = r.nextInt(800)+50;    //tweak later
        y = -110;    //tweak later
        maxHealth = 400;
        health = maxHealth;
        speed = 3;
        scale = 5;
        damage = 3;
        
        //spriteWidth = 160;
        //spriteHeight = 96;
        sprites = new BufferedImage[8][5];
        spriteFilePath = "/res/SpriteSheets/MechaGoblin_Right.png";
        
        hitbox = new Rectangle(x,y,(spriteWidth*scale)-500,(spriteHeight*scale));
        imageBox = new Rectangle(x,y,spriteWidth*scale,spriteHeight*scale);
        healthBox_green = new Rectangle((width-maxHealth)/2,50,maxHealth,10);
        healthBox_red = new Rectangle((width-maxHealth)/2,50,maxHealth-health,10);
        healthBox_red.width = maxHealth - health;
        
        


        
        loadSpriteSheet();
        loadImages();
        
        
    }
    
    
    @Override
    public void loadImages() {
            //code to split the individual sprites
            
            //Load in the right facing subimages
        for(int i=0; i<8; i++) {
            for(int j=0; j<5; j++) {
                sprites[i][j] = spriteSheet.getSubimage(i*spriteWidth, j*spriteHeight, spriteWidth, spriteHeight);
            } 
        }
        
        activeImage = sprites[0][0];
    }
    
    @Override
    public void update() {
        if(health<1) {
            isDead = true;
            state = "dead";

            hitbox.y = 500;
            attackRight.y = 500;
            attackLeft.y = 500;
        }
        
        if(!isDead) {
            // Update Hitboxes
            hitbox.x = x+250; // will needed to be tweaked
            hitbox.y = y+200; // will needed to be tweaked
            imageBox.x = x;
            imageBox.y = y;
            attackRight.x = x + 435;
            attackRight.y = y;
            attackLeft.x = x+175;
            attackLeft.y = y;

            healthBox_red.width = maxHealth - health;





                attack = "idle";
                
                // Update the STATE: either walking ,attacking or idle
                if(state.equals("attack")) {
                    
                    if(spriteCounter == 4) {
                        if(direction.equals("right")) {
                            attack = "right";

                        } else {
                            attack = "left";
                        }
                    }
                    
                    if(spriteCounter == 6) {
                        state = "idle";
                        spriteCounter = 0;
                        idleStateCounter = 0;



                    }
                }

                if(state.equals("walking")) {
                    if(targetLockedPlayerX > getX()+10) {
                        direction = "right";
                        x+= speed;
                    } else if(targetLockedPlayerX < getX()-10) {
                        direction = "left";
                        x-=speed;
                    } else {
                        state = "attack";
                        spriteCounter = 0;
                    }

                } 

                if(state.equals("idle")) {
                    idleStateCounter++;
                }
                if(idleStateCounter >= 120) {
                    state = "walking";
                    spriteCounter = 0;
                    targetLockedPlayerX = player.getX();
                    targetLock.x = targetLockedPlayerX;
                    idleStateCounter = 0;
                }

        }
        
        
        
        
        
                int spriteRow = 0;
                int spriteAnimationLimit = 0;

                switch(state) {
                    case "attack" -> {
                        spriteRow = 2;
                        spriteAnimationLimit = 7;
                    }

                    case "walking" -> {
                        spriteRow = 1;
                        spriteAnimationLimit = 8;

                        break;
                    }

                    case "idle" -> {
                        spriteRow = 0;
                        spriteAnimationLimit = 2;
                        break;
                    }
                    
                    case "dead" -> {
                        spriteRow = 4;
                        //spriteAnimationLimit = 5; the real limit
                        spriteAnimationLimit = 20;
                    }
                }
                
            // Update sprites
            fpsSpriteCounter++;
            if (fpsSpriteCounter == 20) {
                    spriteCounter++;
                
                if (spriteCounter == spriteAnimationLimit) {
                    spriteCounter = 0;
                }
                fpsSpriteCounter = 0;
            }
            if(isDead && spriteCounter >=5) {
                spriteCounter = 5;
            }
            activeImage = sprites[spriteCounter][spriteRow];
            // 0 7 6 
            // [5][6] [6][6] [7][6]

        updateSpriteDamage();
        
    }          
    
    @Override
    public void draw(Graphics2D g2) {
        
        //This is special because there are no left sprites and we have to flip it it horizontally manaully
        if(direction.equals("left")) {
        BufferedImage flippedImage = new BufferedImage(
                activeImage.getWidth(),
                activeImage.getHeight(),
                activeImage.getType());

        // Get the graphics context of the new image
        Graphics2D g2d = flippedImage.createGraphics();

        // Apply a horizontal flip transformation
        g2d.drawImage(activeImage, activeImage.getWidth(), 0, 0, activeImage.getHeight(),
                0, 0, activeImage.getWidth(), activeImage.getHeight(), null);
        
        // Draw the flipped image
        g2.drawImage(flippedImage, x, y, spriteWidth * scale, spriteHeight * scale, null);

        } else {
            g2.drawImage(activeImage,x,y,spriteWidth*scale,spriteHeight*scale,null);
        }
        
        
        if(!isDead) {
        
            g2.setColor(Color.GREEN);
            g2.fill(healthBox_green);
            g2.setColor(Color.RED);
            g2.fill(healthBox_red);
            g2.setFont(new Font("Arial", Font.BOLD, 35));
            g2.setColor(Color.RED);
            int textWidth = g2.getFontMetrics().stringWidth("MechaGoblin");
            g2.drawString("MechaGoblin", (width-textWidth)/2, 40);
        }

        
    }
    
    @Override 
    public void drawHitbox(Graphics2D g2) {
        g2.setColor(Color.GREEN);
        g2.draw(hitbox);
        g2.setColor(Color.RED);
        g2.draw(imageBox);
        g2.setColor(Color.YELLOW);
        g2.draw(attackRight);
        g2.draw(attackLeft);
        g2.fill(targetLock);
    }
 
    // Because Graphics draws the image from the top left, this x is the "true" centre x of the image
     
    
    public Rectangle getAttackRight() { return attackRight; }
    public Rectangle getAttackLeft() { return attackLeft; }
}
