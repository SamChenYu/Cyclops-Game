package Main;

import javax.imageio.ImageIO;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.io.IOException;
import java.awt.Font;
import java.awt.Color;


public final class KingGoblin extends Entity{


    BufferedImage speechBubble;
    boolean drawingSpeech = true;
    String text = "";
    final String text1 = "AAAAAAAAH";
    final String text2 = "";
    String outputText = "";
    final Font font = new Font("Courier New", Font.BOLD, 40); // Font Name, Style, Size


    public KingGoblin( int width, int height, Player player) {
        super(width, height, player,64,64, 16, 11);

        x = 500;
        y = 50;
        scale = 5;
        maxHealth = 50;
        health = 50;
        speed = 2;

        hitbox = new Rectangle(x,y,spriteWidth*(scale-2)-25,spriteHeight*(scale/2)+85);
        imageBox = new Rectangle(x,y,spriteWidth*scale,spriteHeight*scale);
        healthBox_green = new Rectangle(x,y,50,10);
        healthBox_red = new Rectangle(x,y,50,10);

        spriteFilePath = "/res/SpriteSheets/Goblin King.png";
        sprites = new BufferedImage[16][11];
        loadSpriteSheet();
        loadImages();

    }

    @Override
    public void loadSpriteSheet() {
        try {
            spriteSheet = ImageIO.read(getClass().getResourceAsStream(spriteFilePath));
            speechBubble = ImageIO.read(getClass().getResourceAsStream("/res/SpriteSheets/speechbubble.png"));
        } catch(IOException e) {
            System.out.println(e);
            System.out.println("king goblin image loading failed");
        }
    }


    @Override
    public void update() {

        healthBox_green.x = x+40;
        healthBox_green.y = y+70;

        healthBox_red.x = x+40;
        healthBox_red.y = y+70;

        healthBox_red.width = maxHealth-health;


        hitbox.x = x+(spriteWidth*scale/4);
        hitbox.y = y+(spriteHeight*scale/3);

        imageBox.x = x;
        imageBox.y = y;



        fpsSpriteCounter++;
        if (fpsSpriteCounter == 7) {
            spriteCounter++;
            if (spriteCounter == 8) {
                spriteCounter = 0;
            }
            fpsSpriteCounter = 0;
        }
        activeImage = sprites[spriteCounter][2];
    }
    @Override
    public void draw(Graphics2D g2) {
        g2.drawImage(activeImage,x,y,spriteWidth*scale,spriteHeight*scale,null);
        g2.setColor(Color.GREEN);
        g2.setFont(font);
        g2.drawString(outputText, x+40,y+40);


    }


}
