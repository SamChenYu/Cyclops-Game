package Main;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public final class Player {
    
    //Injected / Driver Objects
    KeyHandler keyH;
    Map map;

    
    //Player Attributes
    private int x,y;
    private final int xLeftLimit = -32 ; // its meant to be -(spriteWidth /2 )
    private final int xRightLimit = 1280 + 32; // its meant to be width + (spriteWidth / 2)
    private final int maxHealth = 100;
    private int health = 100;
    private boolean isDead = false;
    private final int width, height;
    private final int speed = 2;
    private final int scale = 5;
    final int laserDamage = 3;
    final int stompDamage = 5;
    final int rockDamage = 7;
    Rock rock = new Rock(300,100, "left"); // for the rock attack
    private final Rectangle greenHealthBox = new Rectangle(x,y,maxHealth,10);
    private final Rectangle redHealthBox = new Rectangle(x,y,maxHealth,10);
    
    //Hitboxes
    private final Rectangle hitbox = new Rectangle(x,y,32*(scale-1),32*(scale-1)); // values here don't really matter as it is updated in update()
    private final Rectangle leftStompHitbox = new Rectangle(x,y,100,50);
    private final Rectangle leftLaserHitbox = new Rectangle(x,y,(64*scale)/2,50);
    private final Rectangle rightStompHitbox = new Rectangle(x,y,100,50);
    private final Rectangle rightLaserHitbox = new Rectangle(x,y,(64*scale)/2,50);

    
    // Sprite Variables
    final int spriteWidth = 64;
    final int spriteHeight = 64;
    final String filepath = "/res/SpriteSheets/Cyclops.png";
    private BufferedImage spriteSheet;
    private final BufferedImage sprites[][] = new BufferedImage[15][20];
    private BufferedImage activeImage;
    boolean takingDamage = false; // this is for keeping track of the red sprite
    int takingDamageCounter = 0;
    
    private String direction = "right";
    private String action = "idle";
    private String previousAction = "idle"; // if there are changes, you need to set the fpsCounter to 0
    
    private int fpsCounter = 0;
    private int spriteCounter = 0;
    private int spriteLimit = 14;
    
    //AttackHandler booleans
    private String attack = "idle";
    
    public Player(int width ,int height, KeyHandler keyH) {
        this.width = width;
        this.height = height;
        this.keyH = keyH;
        x =  -10;//width/5;
        y = height/8;
        loadImages();
    } // END constructor


    public void initMap(Map map) {
        // A bit awkward but map requires all the objects so I can't stick everything through the
        // constructor so I just have to init here instead
        this.map = map;
    }
    
    
    public void loadImages() {
        
        try {
            spriteSheet = ImageIO.read(getClass().getResourceAsStream(filepath));

            
            for(int i=0; i < 15; i++) {
                for(int j=0; j < 20; j++) {
                    sprites[i][j] = spriteSheet.getSubimage(i * spriteWidth, j * spriteHeight, spriteWidth, spriteHeight);
                }
            }
            
            activeImage = sprites[0][0];
            
            // Combine the laser images so that you don't have two draw two images
            
            // Right laser cyclops are from [8][5] to [8][5]
            // Right laser are from [9][7] to [9][7]
            
                for(int i=5; i<7; i++) {
                BufferedImage temp = sprites[5][8];
                sprites[i][8] = new BufferedImage(64,64,BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = sprites[i][8].createGraphics();
                g2d.drawImage(temp,0,0,null);
                g2d.drawImage(sprites[i][9], 0,0,null);
                g2d.dispose();
            }
            
            
            for(int i=0; i<5; i++) {
                BufferedImage temp = sprites[i][8];
                sprites[i][8] = new BufferedImage(64,64,BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = sprites[i][8].createGraphics();
                g2d.drawImage(temp,0,0,null);
                g2d.drawImage(sprites[i][9], 0,0,null);
                g2d.dispose();
            }   
            
 
            
            // Left laser cyclops are from [18][0] to [18][5]
            // Left laser from from [19][0] to [19][7]
            
                for(int i=5; i<7; i++) {
                BufferedImage temp = sprites[5][18];
                sprites[i][18] = new BufferedImage(64,64,BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = sprites[i][18].createGraphics();
                g2d.drawImage(temp,0,0,null);
                g2d.drawImage(sprites[i][19], 0,0,null);
                g2d.dispose();
            }
            
            
                for(int i=0; i<5; i++) {
                BufferedImage temp = sprites[i][18];
                sprites[i][18] = new BufferedImage(64,64,BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = sprites[i][18].createGraphics();
                g2d.drawImage(temp,0,0,null);
                g2d.drawImage(sprites[i][19], 0,0,null);
                g2d.dispose();
            }   
            
            
            
            
            
        } catch(IOException e) {
            System.out.println(e);
            System.out.println("Image Loading Failed");
        }
    } // END loadimages

    public void draw(Graphics2D g2) {
        g2.drawImage(activeImage, x , y, 64*scale, 64*scale, null);
        g2.setColor(Color.GREEN);
        g2.fill(greenHealthBox);
        g2.setColor(Color.RED);
        g2.fill(redHealthBox);
        if(rock.active) {
            rock.draw(g2);
        }
        
    }
    
    public void drawHitbox(Graphics2D g2) {
        g2.setColor(Color.GREEN);
        g2.draw(hitbox);
        g2.setColor(Color.RED);
        g2.drawRect(x,y,64*scale,64*scale); // this is the drawImage hitbox
        
        g2.setColor(Color.YELLOW);
        g2.draw(leftStompHitbox);
        g2.draw(leftLaserHitbox);
        
        g2.draw(rightStompHitbox);
        g2.draw(rightLaserHitbox);
        
        rock.drawHitbox(g2);
    }
    
    public void update() {
        
        if(rock.active) {
            rock.update();
        }
        

        // Update all the hitboxes
        greenHealthBox.x = x + 112;
        greenHealthBox.y = y + 100;
        redHealthBox.x = x+112;
        redHealthBox.width = (100 - health);
        redHealthBox.y = y + 100;
        
        hitbox.x = x+(24*(scale-1));
        hitbox.y = y+(40*(scale-1));
        leftStompHitbox.x = x +64+28;
        leftStompHitbox.y = y + 64*(scale-1)+10;
        leftLaserHitbox.x = x;
        leftLaserHitbox.y = y + 64*(scale-3)+20;

        rightStompHitbox.x = x + 64*(scale-3);
        rightStompHitbox.y = y + 64*(scale-1)+10;
        rightLaserHitbox.x = x + 64*(scale-3)+32;
        rightLaserHitbox.y = y + 64*(scale-3)+20;

        // Update the input from the KeyHandler if the player is not dead
        
        if(health < 1) {
            isDead = true;
            action = "dead";
            previousAction = "dead";
        } else {

            if(keyH.upPressed == true) {    
                previousAction = action;
                action = "walking";
                spriteLimit = 10;
                if(y>0) {
                    y-=speed;
                }
            }
            if (keyH.downPressed == true) {
                previousAction = action;
                action = "walking";
                spriteLimit = 10;
                if(y<400-64*scale) {
                    y+=speed;
                }

            }
            if (keyH.leftPressed == true) {
                direction = "left";
                previousAction = action;
                action = "walking";
                spriteLimit = 10;   // there are 10 different frames for the walking animation
                if(getX()-speed>xLeftLimit) {
                    x-=speed;

                    if(getX() < -10) {
                        map.changeRoom("left");
                        x = 1289;
                    }

                }
            }
            if (keyH.rightPressed == true) {
                direction = "right";
                previousAction = action;
                action = "walking";
                spriteLimit = 10;
                if(getX()+speed<xRightLimit) {
                    x+=speed;

                    if(getX() > 1290) {
                        map.changeRoom("right");
                        x = -9;
                    }

                }
            }

            if(keyH.idle) {
                previousAction = action;
                action = "idle";
                spriteLimit = 14;
            }


            if(keyH.rockThrow) {
                previousAction = action;
                action = "rockThrow";
                spriteLimit = 11;
            }

            if(keyH.stomp) {
                previousAction = action;
                action = "stomp";
                spriteLimit = 5;
            }


            if(keyH.laser) {
                previousAction = action;
                action = "laser";
                spriteLimit = 6;
            }

            if(keyH.dead) {
                previousAction = action;
                action = "dead";
                spriteLimit = 8;
            }
        
        }
        
        // Needs to listen to the state because there are different
        // amount of animation frames for each action
        
        
        // Total walking left / right frames = 10
        // Idle frames = 15;
        
        // CHANGE THE SPRITE EVERY 8 SECONDS
        /*
        
            FPS COUNTER TRACKS HOW MANY FRAMES HAVE PASSED
            SPRITE COUNTER IS POINTING TO WHICH SPRITE AT SPRITES[][]
            SPRITELIMIT IS THE LIMIT OF HOW MANY FRAMES ARE IN EACH ANIMATINON
        
        
        */
        // this ensures that each animation starts from the beginning frame
        if(!previousAction.equals(action)) {
            fpsCounter =0;
            spriteCounter = 0;
        }
        
        // Every 8 FPS change the sprite till the animation limit
        fpsCounter++;
        if(fpsCounter > 8) {
            if(spriteCounter < spriteLimit) {
                
                //This is to account for the fact that once you are dead the animation does not loop
                if(!(action.equals("dead") && spriteCounter == 6)) {
                    spriteCounter++;
                }
                
                
            } else {
                spriteCounter = 0;
            }

            fpsCounter = 0;
        }
        
        
//        System.out.println(direction + " " + action);


        // Update based on the action
        // Also updates whether an attack hit or not
        
        
        // set the default so the player is not attacking by default
        attack = "idle";
        
        
        switch(action) {
            
            
            case "idle" -> {
                if(direction.equals("right")) {
                    activeImage = sprites[spriteCounter][0];
                } else {
                    activeImage = sprites[spriteCounter][10];
                }

                break;
            }
            
            case "walking" -> {
                
                if(direction.equals("right")) {
                    activeImage = sprites[spriteCounter][1];
                } else {
                    activeImage = sprites[spriteCounter][11];
                }
                break;

            }
            
            case "rockThrow" -> {
                
                if(direction.equals("right")) {
                    activeImage = sprites[spriteCounter][3];
                    
                    if(spriteCounter == 11) {
                        attack = "rockRight";
                        rock.impact = false;
                        rock.active = true;
                        rock.x = getX();
                        rock.y = getY()-100;
                        rock.direction = "right";
                    }
                } else {
                    activeImage = sprites[spriteCounter][13];
                    if(spriteCounter == 11) {
                        attack = "rockLeft";
                        rock.active = true;
                        rock.x = getX()-64-32;
                        rock.y = getY()-100; 
                        rock.direction = "left";
                        rock.impact = false;
                    }
                }
                break;
            }   
            
            
            
            
            
            
            
            case "stomp" -> {
                if(direction.equals("right")) {
                    activeImage = sprites[spriteCounter][2];

                    // Checking if the stomp hit anything
                   if(spriteCounter == 4) {
                       attack = "stompRight"; // attackHandler's job
                   }

                } else {
                    activeImage = sprites[spriteCounter][12];
                    // Checking if the stomp hit anything
                   if(spriteCounter == 4) {
                       attack = "stompLeft"; // attackHandler's job
                   }
                    
                    
                }
                break;
            }
            
            
            case "laser" -> {
               if(direction.equals("right")) {
                   //Update the sprite
                    activeImage = sprites[spriteCounter][8];
                    // Checking if the right laser hitbox hit anything
                   if(spriteCounter == 4) {
                       attack = "laserRight"; // attackHandler's job
                       
                   }


                } else {
                    activeImage = sprites[spriteCounter][18];
                    //Checking if the left laser hitbox hit anything
                   if(spriteCounter == 4) {
                       attack = "laserLeft"; // attackHandler's job
                   }


                }



                break;
            }
            
            
            case "dead" -> {
                if(direction.equals("right")) {
                    activeImage = sprites[spriteCounter][6];
                } else {
                    activeImage = sprites[spriteCounter][16];
                }
                break;
            }
            

            
            
        }

        
       if(takingDamage) {
           if(takingDamageCounter < 8) {
               takingDamageCounter++;
               turnSpriteRed();
           } else {
               takingDamage = false;
               takingDamageCounter = 0;
           }
       }
        
       
    } // END Update

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
    
    
    
    public final class Rock {
        
        int x,y;
        String direction;
        BufferedImage spriteSheet;
        BufferedImage sprites[] = new BufferedImage[2];
        BufferedImage activeImage;
        String filepath = "/res/SpriteSheets/Cyclops_Rock.png";
        boolean active = false;
        
        boolean impact = false;
        int spriteCounter = 0;
        
        Rectangle hitbox = new Rectangle(x+(15*scale),y+(15*scale),10*scale,10*scale);
        Rectangle imageBox = new Rectangle(x,y,32*scale,32*scale);

        public Rock(int x, int y, String direction) {
            this.x = x;
            this.y = y;
            this.direction = direction;
            
            
            
            loadImages();
            update(); // this is such that the hitboxes disappear
        }
        
        public void loadImages() {
        
        try {
            spriteSheet = ImageIO.read(getClass().getResourceAsStream(filepath));
            int spriteWidth = 32;
            int spriteHeight = 32;
            sprites[0] = spriteSheet.getSubimage(0, 0, spriteWidth, spriteHeight);
            sprites[1] = spriteSheet.getSubimage(32, 0, spriteWidth, spriteHeight);
            activeImage = sprites[0];
        }
            catch(IOException e) {
                System.out.println("rock image loading failed");
            }
        }
        
        
        public void update() {
            
                
            
            
            if(impact) {
                spriteCounter++;
                activeImage = sprites[1];
                if(spriteCounter >=15) {
                    active = false;
                    impact = false;
                    spriteCounter = 0;
                }
            } else {
                activeImage = sprites[0];
            }
            
            
            if(active) {
                
                if(direction.equals("right")) {
                    x+=5;
                    y+=3;
                } else {
                    x-=5;
                    y+=3;
                }
                
                
                if(y > 450) {
                    active = false;
                }

            } else {
                y = -300;
            }
            hitbox.x = x+(12*scale);
            hitbox.y = y+(12*scale);
            imageBox.x = x;
            imageBox.y = y;
        }
        
        public void draw(Graphics2D g2) {
            g2.drawImage(activeImage,x,y,32*scale,32*scale,null);
        }
        
        
        public void drawHitbox(Graphics2D g2) {
            g2.setColor(Color.GREEN);
            g2.draw(hitbox);
            g2.setColor(Color.RED);
            g2.draw(imageBox);
        }
        
        public Rectangle getHitbox() { return hitbox; }
    } // END ROCK


    // Because Graphics draws the image from the top left, this x is the "true" centre x of the image
     public int getX() { return x +(64*scale)/2; }
     public int getY() { return y + (64*scale)/2; }
     
     public Rectangle getHitbox() { return hitbox; }
     public Rectangle getLeftStompHitbox() { return leftStompHitbox; }
     public Rectangle getRightStompHitbox() { return rightStompHitbox; }
     public Rectangle getLeftLaserHitbox() { return leftLaserHitbox; }
     public Rectangle getRightLaserHitbox() { return rightLaserHitbox; }
     
     public void reduceHealth(int dmg) {
        health -= dmg;
        takingDamage = true;
    }
     public void addHealth(int dmg) {
        health += dmg;
        if(health >= maxHealth) {
            health = maxHealth;
        }
    }
     public boolean isDead() { return isDead; }
     public String getAttack() {  return attack; }



    // Methods used to switch the rooms


    public void playerEntersNewRoom(String direction) {
        if(direction.equals("right")) {
            x = 0;
        } else {
            x = 1280;
        }
    }





} // END class
