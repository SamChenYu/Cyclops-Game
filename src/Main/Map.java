package Main;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public final class Map {
    
    
    // Injected Objects
    int width, height;
    Player player;
    ArrayList<Bat> bats;
    ArrayList<Crab> crabs;
    ArrayList<MechaGoblin> mechaGoblins;
    MechaGoblin mechaG; // the actual object if I want to save it

    ArrayList<Obstacle> obstacles;
    ArrayList<Item> items;
    ArrayList<KingGoblin> kingG;
    ArrayList<Demon> demons;
    
    int roomNum = 0;
    final int roomLimit = 4;
    int tileScale = 5;
    int wave = 0; // waves are for spawning of entities
    boolean areEnemiesAlive = true;

    // Room zero assets
    BufferedImage burningHouse;


    // Room one
    BufferedImage burningPalace;
    
    // Room two assets
    BufferedImage forestBackground;
    BufferedImage grasstile1; // 14 x 12 pixels
    BufferedImage grasstile2; // 14 x 12 pixels
    
    // Room three assets
    BufferedImage caveEntrance;
    BufferedImage cavetiles;
    
    // Room four assets
    BufferedImage cave;
    BufferedImage stonetile1;
    BufferedImage stonetile2;
    BufferedImage dirttiles1;
    BufferedImage dirttiles2;

    
    
    public Map(int width, int height, Player player, ArrayList<Bat> bats,
               ArrayList<Crab> crabs, ArrayList<MechaGoblin> mechaGoblins,
               ArrayList<Obstacle> obstacles, ArrayList<Item> items,
               ArrayList<KingGoblin> kingG, ArrayList<Demon> demons) {

        this.width = width;
        this.height = height;
        this.player = player;
        this.bats = bats;
        this.crabs = crabs;
        this.mechaGoblins = mechaGoblins;
        this.obstacles = obstacles;
        this.items = items;
        this.kingG = kingG;
        this.demons = demons;
        
        loadImage();


        // because I haven't implemented a strict go to room # x, I have to set to
        // num = 1 then go back in order to properly start the room
        roomNum = 1;
        changeRoom("left");


    }
    
    public void loadImage() {
        try {

            burningHouse = ImageIO.read(getClass().getResourceAsStream("/res/mapFiles/burninghouse.png"));
            burningPalace = ImageIO.read(getClass().getResourceAsStream("/res/mapFiles/burningpalace.png"));

            forestBackground = ImageIO.read(getClass().getResourceAsStream("/res/mapFiles/background.png"));
            grasstile1 = ImageIO.read(getClass().getResourceAsStream("/res/mapFiles/Bush_2_16x16.png"));
            grasstile2 = ImageIO.read(getClass().getResourceAsStream("/res/mapFiles/Green_11_16x16.png"));
            
            caveEntrance = ImageIO.read(getClass().getResourceAsStream("/res/mapFiles/caveEntrance.jpg"));
            stonetile1 = ImageIO.read(getClass().getResourceAsStream("/res/mapFiles/Stone_1_16x16.png"));
            stonetile2 = ImageIO.read(getClass().getResourceAsStream("/res/mapFiles/Stone_2_16x16.png"));

            cave = ImageIO.read(getClass().getResourceAsStream("/res/mapFiles/cave.png"));
            dirttiles1 = ImageIO.read(getClass().getResourceAsStream("/res/mapFiles/Dirt_1_16x16.png"));
            dirttiles2 = ImageIO.read(getClass().getResourceAsStream("/res/mapFiles/Dirt_2_16x16.png"));

        } catch(IOException ex) {
            System.out.println(ex);
            System.out.println("map loading failed");
        }
            
    } // end loadimage

    public void update() {
        // the switch rooms will manage spawning of entities
        // this method manages the 'waves' after the initial entities have gone
        switch(roomNum) {

            case 3 -> {
                areEnemiesAlive = false;
                for(int i=0; i<bats.size(); i++ ) {
                    if (!bats.get(i).isDead) {
                        areEnemiesAlive = true;
                    }
                }

                for(int i=0; i<demons.size(); i++) {
                    if(!demons.get(i).isDead) {
                        areEnemiesAlive = true;
                    }
                }



                    if(!areEnemiesAlive) {
                        addDemon();
                        areEnemiesAlive = true;
                    }

            }








        }



    }
    
    public void draw(Graphics2D g2) {


        
        switch(roomNum) {

            case 0 -> {
                g2.drawImage(burningHouse, 0, -300,1254,716,null);


            }

            case 1 -> {
                g2.drawImage(burningPalace,0,0,null);
            }

            case 2 -> {
                g2.drawImage(forestBackground,0,0,null);
                for(int i=0; i<1280; i+=70) {
                    g2.drawImage(grasstile1,i,325,14*tileScale,12*tileScale,null);
                }
                g2.drawImage(grasstile2,70,325,14*tileScale,12*tileScale,null);
                g2.drawImage(grasstile2,140,325,14*tileScale,12*tileScale,null);
                g2.drawImage(grasstile2,210,325,14*tileScale,12*tileScale,null);
                g2.drawImage(grasstile2,280,325,14*tileScale,12*tileScale,null);

                g2.drawImage(grasstile2,490,325,14*tileScale,12*tileScale,null);
                g2.drawImage(grasstile2,560, 325, 14*tileScale, 12*tileScale, null);


                g2.drawImage(grasstile2, 840, 325, 14*tileScale, 12*tileScale,null);
                g2.drawImage(grasstile2, 910, 325, 14*tileScale, 12*tileScale, null);
                g2.drawImage(grasstile2, 980, 325, 14*tileScale, 12*tileScale, null);
                
            }
            
            case 3 -> {
                g2.drawImage(caveEntrance,0,-50,null);
                for(int i=0; i<1280; i+=70) {
                    g2.drawImage(stonetile1,i,325,16*tileScale,16*tileScale,null);
                }

                for(int i=480; i < 1000; i+= 70) {
                    g2.drawImage(stonetile2, i, 325, 16*tileScale, 16*tileScale, null);
                }
            }
            
            case 4 -> {
                g2.drawImage(cave,0,0,null);
                for(int i=0; i<1280; i+=70) {
                    g2.drawImage(dirttiles1,i,330,16*tileScale,16*tileScale,null);

                }

                    g2.drawImage(dirttiles2, 0,330,16*tileScale,16*tileScale,null);
                    g2.drawImage(dirttiles2, 70,330,16*tileScale,16*tileScale,null);
                    g2.drawImage(dirttiles2, 140,330,16*tileScale,16*tileScale,null);
                    g2.drawImage(dirttiles2, 210,330,16*tileScale,16*tileScale,null);
                    g2.drawImage(dirttiles2, 280,330,16*tileScale,16*tileScale,null);

                    for(int i=840; i<1280; i+=70) {
                        g2.drawImage(dirttiles2, i, 330, 16*tileScale, 16*tileScale, null);
                    }



            }
        }

    } // end Draw
    
    public void changeRoom(String direction) {

        if(direction.equals("right")) {

            if(roomNum != roomLimit) {
                roomNum++;
            }

        } else {
            if (roomNum != 0) {
                roomNum--;
            }
        }
        bats.clear();
        crabs.clear();
        mechaGoblins.clear();
        obstacles.clear();
        kingG.clear();

        switch(roomNum) {

            case 0 -> {
                break;
            }

            case 1 -> {
                addKingGoblin();
                break;
            }

            case 2 -> {
                addCrab();
                addCrab();
                addBarrel(800);
                addBarrel(900);
                break;
            }


            case 3 -> {
                addBat();
                addBat();
                addBat();
                addPot(300);
                addPot(900);
                break;
            }

            case 4 -> {
                addMechaGoblin();
                addCrate(200);
                break;
            }

        }
    } // END changeRoom


    
    public void addBat() {
        Bat bat1 = new Bat(width, height, player);
        bats.add(bat1);
    }
    
    public void addCrab() {
        Crab crab1 = new Crab(width,height, player);
        crabs.add(crab1);
    }
    
    public void addMechaGoblin() {
        mechaG = new MechaGoblin(width, height, player);
        mechaGoblins.add(mechaG);
    }

    public void addBarrel(int x) {
        Obstacle ob = new Obstacle(width, height ,player ,1,x, this);
        obstacles.add(ob);
    }

    public void addCrate(int x) {
        Obstacle ob = new Obstacle(width,height,player, 3, x, this);
        obstacles.add(ob);
    }

    public void addPot(int x) {
        Obstacle ob = new Obstacle(width, height, player, 5, x, this);
        obstacles.add(ob);
    }

    public void addBanana(int x, int y) {
        Item item = new Item(width, height, player, "banana", x, y);
        items.add(item);

    }

    public void addWatermelon(int x, int y) {
        Item item = new Item(width, height ,player, "watermelon", x, y);
        items.add(item);
    }

    public void addGrapesoda(int x, int y) {
        Item item = new Item(width, height, player, "grape soda", x, y);
        items.add(item);
    }

    public void addKingGoblin() {

        KingGoblin kingGoblin = new KingGoblin(width, height, player);
        kingG.add(kingGoblin);
    }

    public void addDemon() {
        Demon demon = new Demon(width, height, player, this);
        demons.add(demon);
    }

}