package Main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public final class Obstacle extends Entity {


    // Injected objects
    Map map;
    int type; // this is to determine what type of obstacle is will be
    /*
    *   1 = barrel
    *   3 = crate
    *   5 = pot
    */
    Random r = new Random();

    public Obstacle(int width, int height ,Player player, int type, int x, Map map) {
        super(width, height ,player,64,64,6, 11);
        this.x = x;
        this.type = type;
        this.map = map;
        y = r.nextInt(30)+130;
        scale = 5;
        maxHealth = 200;
        health =  maxHealth;

        //spriteWidth = 64;
        //spriteHeight = 64;
        spriteFilePath = "/res/SpriteSheets/Destructible Objects Sprite Sheet.png";
        sprites = new BufferedImage[7][12];
        //This stuff is only for the deathAnimation()
        fpsSpriteCounter = 0;
        spriteCounter = 0;

        loadSpriteSheet();
        loadImages();

        initRectangles();

        switch(type) {

            case 1 -> {
                hitbox.width = 19*scale;
                hitbox.height = 22*scale;
                break;
            }

            case 3 -> {
                hitbox.width = 21*scale;
                hitbox.height = 22*scale;
            }

            case 5 -> {
                hitbox.width = 19*scale;
                hitbox.height = 18*scale;
            }

        }


    }




    @Override
    public void draw(Graphics2D g2) {
        g2.drawImage(activeImage,x,y,spriteWidth*scale,spriteHeight*scale,null);
    }


    @Override
    public void update() {
        // Update the hitboxes

        if(health < 1) {
            isDead = true;
            deathAnimation();
            hitbox.width = 0; // make sure the hitboxes don't interrupt anything else
            hitbox.y = 500;
        }

        hitbox.x = x+(24*scale); // will needed to be tweaked
        hitbox.y = y+(24*scale); // will needed to be tweaked
        imageBox.x = x;
        imageBox.y = y;

        healthBox_green.x = x+(13*scale); // will needed to be tweaked
        healthBox_green.y = y+(16*scale); // will needed to be tweaked
        healthBox_red.x = x+(13*scale); // will needed to be tweaked
        healthBox_red.y = y+(16*scale); // will needed to be tweaked
        healthBox_red.width = maxHealth - health;

        updateSpriteDamage();


    }

    @Override
    public void updateSpriteDamage() {
    // needs to be overridded because of the type system
        if(takingDamage) {
            if(takingDamageCounter < 8) {
                takingDamageCounter++;
                turnSpriteRed();
            } else {
                takingDamage = false;
                takingDamageCounter = 0;
                activeImage = sprites[0][type];
            }
        }

    }

    public void deathAnimation() {

        fpsSpriteCounter++;
        if(fpsSpriteCounter >=5) {
            spriteCounter++;

            if(spriteCounter == 2) {
                int itemNum = r.nextInt(3);
                switch(itemNum) {

                    case 0 -> {
                        map.addBanana(x+133,y+((spriteHeight*scale)/4));
                        break;
                    }
                    case 1 -> {
                        map.addGrapesoda(x+133,y+((spriteHeight*scale)/4));
                        break;
                    }

                    case 2 -> {
                        map.addWatermelon(x+133,y+((spriteHeight*scale)/4));
                    }
                }
            } else if(spriteCounter > 5) {
                activeImage = sprites[5][type];
            } else {
                activeImage = sprites[spriteCounter][type];
                y+=30;
            }
            fpsSpriteCounter = 0;
        }
    }

    @Override
    public void loadImages() {
        try {
            spriteSheet = ImageIO.read(getClass().getResourceAsStream(spriteFilePath));


            for(int i=0; i<6; i++) {
                for(int j=0; j<11; j++) {
                    sprites[i][j] = spriteSheet.getSubimage(i*spriteWidth, j*spriteHeight, spriteWidth, spriteHeight);
                }
            }
            activeImage = sprites[0][type];

        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Obstacle image loading failed");
        }
    }


}
