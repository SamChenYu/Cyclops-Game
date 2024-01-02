package Main;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public final class Bat extends Entity {
    Random r = new Random();

    public Bat(int width, int height, Player player) {
        super(width, height ,player, 16, 24,5,3);



        x= r.nextInt(width-300)+150;
        y = 175;
        scale = 5;
        maxHealth = 40;
        health = 40;
        damage = 10;

        movement = new int[] {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                0, 0, 0, 0, 0, 0, 0, 0 , 0, 0, 0, 0, 0, 0, 0 ,0 ,0 ,0 ,0 , -2, -2, -2, -2, -2,
                -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2,
                0, 0, 0, 0, 0, 0, 0, 0 , 0, 0, 0, 0, 0, 0, 0 ,0 ,0 ,0 ,0 ,0};

        movementFlag = r.nextInt(movement.length);


        spriteFilePath = "/res/spriteSheets/Bats.png";
        sprites = new BufferedImage[5][3];
        loadImages();

        //Hitboxes
        hitbox = new Rectangle(x,y,spriteWidth*(scale),spriteHeight*(scale-2));
        imageBox = new Rectangle(x,y,spriteWidth*scale,spriteHeight*scale);
        healthBox_green = new Rectangle(x,y,maxHealth,10);
        healthBox_red = new Rectangle(x,y,0,10);


    }
    

    @Override
    public void loadImages() {
        try {
            spriteSheet = ImageIO.read(getClass().getResourceAsStream(spriteFilePath));


//            for(int i=0; i<2; i++) {
//                for(int j=0; j<4; j++) {
//                    sprites[i][j] = spriteSheet.getSubimage(j*spriteWidth, i*spriteHeight, spriteWidth, spriteHeight);
//                }
//            }
            for(int i=0; i<5; i++) {
                for(int j=0; j<3; j++) {
                    sprites[i][j] = spriteSheet.getSubimage(i*spriteWidth, j*spriteHeight, spriteWidth, spriteHeight);
                }
            }



            activeImage = sprites[0][0];

        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Bat image loading failed");
        }
    }

    @Override
    public void update() {
        if(health < 1) {
            isDead = true;
        }
        //Updating the hitboxes
        hitbox.x = x;
        hitbox.y = y;

        imageBox.x = x;
        imageBox.y = y;

        healthBox_green.x = x+20;
        healthBox_green.y = y;
        healthBox_red.x = x+20;
        healthBox_red.y = y;
        healthBox_red.width = 40-health;


        // Updating x y coordinates

        int xDistance = getX()-player.getX();

        if(xDistance < 300 && xDistance > 0 ) {
            //in the case that the player is on the right side of the crab
            x-=2;
        } else if(xDistance > -300 && xDistance < 0 ) {
            // in the case that the player is on the left side of the crab
            x+=2;
        } else {
            x += movement[movementFlag];
            movementFlag++;

            if(movementFlag == movement.length) {
                movementFlag = 0;
            }
        }
        
// This old code implmenets the death sprites too but I don't actually need it
        
//        if(isDead) {
//            healthBox_red.width = maxHealth;
//
//            fpsCounter++;
//            if (fpsCounter == 5) {
//                if (spriteCounter+5 < 14) {
//                    spriteCounter++;
//                }
//                fpsCounter = 0;
//            }
//            activeImage = sprites[spriteCounter+5];
//            if(y<400) {
//                y+=5;
//            }
//        } 
        
        if(isDead && y<400) {
            y+=5;
        } else {
            //Update the image sprites
            fpsSpriteCounter++;
            if (fpsSpriteCounter == 5) {
                spriteCounter++;
                if (spriteCounter == 4) {
                    spriteCounter = 0;
                }
                fpsSpriteCounter = 0;
            }
            activeImage = sprites[spriteCounter][0];


            //Update x and y coords

            movementFlag++;
            if (movementFlag == movement.length) {
                movementFlag = 0;
            } else {
                x += movement[movementFlag];
            }



        }

        updateSpriteDamage();
    }
 
}
