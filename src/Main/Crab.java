/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Main;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.Random;

public final class Crab extends Entity{
    
    // Injected Objects
    Player player;
    int width, height;

    Random r = new Random();

    public Crab( int width, int height, Player player) {
        super(width, height, player,32,32, 4, 4);
        this.width = width;
        this.height = height;
        this.player = player;



        x= r.nextInt(width-600)+350;
        y = 230;
        scale = 4;
        damage = 15;
        maxHealth = 50;
        health = 50;


        speed = r.nextInt(4)+1;
        movement = new int[] {speed, speed, speed, speed, speed, speed, speed, speed, speed, speed, speed, speed, speed, speed, speed, speed, speed, speed, speed, speed, speed,
                0, 0, 0, 0, 0, 0, 0, 0 , 0, 0, 0, 0, 0, 0, 0 ,0 ,0 ,0 ,0 , -speed, -speed, -speed, -speed, -speed,
                -speed, -speed, -speed, -speed, -speed, -speed, -speed, -speed, -speed, -speed,-speed,-speed,-speed,-speed,-speed,-speed, 0, 0, 0, 0, 0, 0, 0, 0 , 0, 0, 0, 0, 0,
                0, 0 ,0 ,0 ,0 ,0 ,0};
        movementFlag = r.nextInt(movement.length);
        hitbox = new Rectangle(x,y,40*(scale-2),40*(scale-2));
        imageBox = new Rectangle(x,y,spriteWidth*scale,spriteHeight*scale);
        healthBox_green = new Rectangle(x,y,50,10);
        healthBox_red = new Rectangle(x,y,50,10);

        spriteFilePath = "/res/SpriteSheets/Crabs.png";
        sprites = new BufferedImage[4][4];
        loadImages();
    }

    @Override
    public void loadImages() {
        try {
            spriteSheet = ImageIO.read(getClass().getResourceAsStream(spriteFilePath));


            for(int i=0; i<4; i++) {
                for(int j=0; j<4; j++) {
                    sprites[i][j] = spriteSheet.getSubimage(i*spriteWidth, j*spriteHeight, spriteWidth, spriteHeight);
                }
            }
            activeImage = sprites[0][0];

        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Crab image loading failed");
        }
    }

    @Override
    public void update() {
        
        if(health < 1) {
            isDead = true;
        }

        if(health < 0) {
            health = 0;
        }
        
        // Updating hitboxes
        
        healthBox_green.x = x+40;
        healthBox_green.y = y+70;
        
        healthBox_red.x = x+40;
        healthBox_red.y = y+70;

        healthBox_red.width = maxHealth-health;

        
        hitbox.x = x+25;
        hitbox.y = y+50;

        imageBox.x = x;
        imageBox.y = y;
        
        
        // Updating x y coordinates
        
        int xDistance = getX()-player.getX();
        
        if(xDistance < 200 && xDistance > 0 ) {
            //in the case that the player is on the right side of the crab
            x-=1;
        } else if(xDistance > -200 && xDistance < 0 ) {
            // in the case that the player is on the left side of the crab
            x+=1;
        } else {
            x += movement[movementFlag];
            movementFlag++;
            
            if(movementFlag == movement.length) {
                movementFlag = 0;
            }
        }
        
        // Update Image Sprites
        
//This old commented code both:
// Changes the sprites to death animation
// Corpse falls out of map. But I realised you don't need to do both.
// But I kept the code here in case I change my mind
//        if(isDead) {
//            healthBox_red.width = maxHealth;
//
//            fpsSpriteCounter++;
//            if (fpsSpriteCounter == 5) {
//                if (spriteCounter+8 < 11) {
//                    spriteCounter++;
//                }
//                fpsSpriteCounter = 0;
//            }
//            activeImage = sprites[spriteCounter+8];
//            if(y<400) {
//                y+=5;
//            }
            
            
            if(isDead && y<400) {
                y+=5;
            } else {
            
            
            fpsSpriteCounter++;
            if (fpsSpriteCounter == 5) {
                spriteCounter++;
                if (spriteCounter == 4) {
                    spriteCounter = 0;
                }
                fpsSpriteCounter = 0;
            }
            activeImage = sprites[spriteCounter][0];
            
            
            
        }

            updateSpriteDamage();
    }


}
