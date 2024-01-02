package Main;

import java.awt.*;

public final class Item extends Entity {

    // The items bob up and down

    boolean isBobUp = false;

    public Item(int width, int height ,Player player, String item, int x, int y) {
        super(width, height ,player,32,32,0,0);

        spriteWidth = 32;
        spriteHeight = 32;
        scale = 2;

        this.x = x;
        this.y = y;

        fpsSpriteCounter = 0;

        switch(item) {

            case "watermelon" -> {
                spriteFilePath = "/res/SpriteSheets/watermelon2.png";
                health = 20;
                break;
            }

            case "banana" -> {
                spriteFilePath = "/res/SpriteSheets/banana.png";
                health = 40;
                break;
            }

            case "grape soda" -> {
                spriteFilePath = "/res/SpriteSheets/grape_soda.png";
                health = 50;
                break;
            }
        } // END SWITCH


        loadSpriteSheet();
        // because it is only one png then the entire image is stored in spriteSheet
        activeImage = spriteSheet;
        initRectangles();



    } //END CONSTRUCTOR


    @Override
    public void initRectangles() {
        hitbox = new Rectangle(x+4,y+4,spriteWidth*scale-8, spriteHeight*scale-8);
        imageBox = new Rectangle(x,y,spriteWidth*scale, spriteHeight*scale);
    }
    @Override
    public void update() {
        hitbox.x = x+4;
        hitbox.y = y+4;
        imageBox.x = x;
        imageBox.y = y;

        fpsSpriteCounter++;
        if(fpsSpriteCounter >= 30) {
            fpsSpriteCounter = 0;
            if(isBobUp) {
                y +=5;
            } else {
                y-=5;
            }
            isBobUp = !isBobUp;

        }


    }



    @Override
    public void draw(Graphics2D g2) {
        g2.drawImage(activeImage, x,y,spriteWidth*scale, spriteHeight*scale, null);
    }

    public void used() {
        y = 500; // teleports it up after use. will get rid of in the next room
    }

}
