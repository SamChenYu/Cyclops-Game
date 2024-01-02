package Main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

// One thing to note is that when it dies it drops an item

public class Demon extends Entity {

    //Injected objects
    Map map;

    Random r = new Random();
    final Rectangle targetLock = new Rectangle(0,200,50,50);


    Rectangle attackLeft;
    Rectangle attackRight;
    String direction = "right";
    String state = "idle";
    int idleStateCounter = 0; // after attacking it will wait a few frames before reattacking
    int targetLockedPlayerX; // The mech will lock onto the player's location and go there
    boolean droppedItemOnDeath = false;

    public Demon(int width, int height, Player player, Map map) {
        super(width, height ,player, 64, 64,8,4);

        this.map = map;

        x= 500;
        y = 50;
        speed = 4;
        scale = 5;
        maxHealth = 250;
        health = maxHealth;
        damage = 5;


        //Hitboxes
        hitbox = new Rectangle(x,y,spriteWidth*(scale)-80,spriteHeight*(scale-2)+40);
        imageBox = new Rectangle(x,y,spriteWidth*scale,spriteHeight*scale);
        healthBox_green = new Rectangle(x,y,maxHealth,10);
        healthBox_red = new Rectangle(x,y,0,10);
        attackLeft = new Rectangle(x,y+(spriteHeight*scale/2),100,50);
        attackRight = new Rectangle(x+(spriteWidth*scale/2), y+(spriteHeight*scale/2)+20,100,50);



        spriteFilePath = "/res/spriteSheets/Demon.png";
        sprites = new BufferedImage[spriteSheetWidth][spriteSheetHeight];

        loadSpriteSheet();
        loadImages();
    }


    @Override
    public void loadImages() {
        try {
            spriteSheet = ImageIO.read(getClass().getResourceAsStream(spriteFilePath));

            for(int i=0; i<spriteSheetWidth; i++) {
                for(int j=0; j<spriteSheetHeight; j++) {
                    sprites[i][j] = spriteSheet.getSubimage(i*spriteWidth, j*spriteHeight, spriteWidth, spriteHeight);
                }
            }



            activeImage = sprites[0][0];

        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Demon image loading failed");
        }
    }

    @Override
    public void update() {
        if(health < 1) {
            isDead = true;
            if(!droppedItemOnDeath) {
                map.addBanana(x+(spriteWidth*scale/2),y+(spriteHeight*scale/2));
                droppedItemOnDeath = true;
            }

        }
        //Updating the hitboxes
        hitbox.x = x+40;
        hitbox.y = y+60;

        imageBox.x = x;
        imageBox.y = y;

        healthBox_green.x = x+35;
        healthBox_green.y = y+40;
        healthBox_red.x = healthBox_green.x;
        healthBox_red.y = healthBox_green.y;

        if(health < 1) {
            healthBox_red.width = maxHealth;
        } else {
            healthBox_red.width = maxHealth-health;
        }

        attackLeft.x = x;
        attackLeft.y = y +(spriteHeight*scale/2)+20;

        attackRight.x = x+(spriteWidth*scale-100);
        attackRight.y = y + (spriteHeight*scale/2)+20;


        // Updating x y coordinates

        if(isDead && y<400) {
            y+=5;
        } else {


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

                if(spriteCounter == 3) {
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
                spriteAnimationLimit = 4;
            }

            case "walking" -> {
                spriteRow = 1;
                spriteAnimationLimit = 6;

                break;
            }

            case "idle" -> {
                spriteRow = 0;
                spriteAnimationLimit = 6;
                break;
            }

            case "dead" -> {
                spriteRow = 3;
                //spriteAnimationLimit = 5; the real limit
                spriteAnimationLimit = 8;
            }
        }

        // Update sprites
        fpsSpriteCounter++;
        if (fpsSpriteCounter == 8) {
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


        g2.setColor(Color.GREEN);
        g2.fill(healthBox_green);
        g2.setColor(Color.RED);
        g2.fill(healthBox_red);
    }

    @Override
    public void drawHitbox(Graphics2D g2) {
        g2.setColor(Color.GREEN);
        g2.draw(hitbox);
        g2.setColor(Color.RED);
        g2.draw(imageBox);
        g2.setColor(Color.YELLOW);
        g2.draw(attackLeft);
        g2.draw(attackRight);
        g2.fill(targetLock);
    }

    public Rectangle getAttackLeft() { return attackLeft; }
    public Rectangle getAttackRight() { return attackRight; }
    public String getDirection() { return direction; }


}