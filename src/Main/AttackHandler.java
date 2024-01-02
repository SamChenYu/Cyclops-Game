
package Main;

import java.util.ArrayList;

public class AttackHandler {
    
    Player player;
    ArrayList<Bat> bats;
    ArrayList<Crab> crabs;
    ArrayList<MechaGoblin> mechaGoblin;
    ArrayList<Obstacle> obstacles;
    ArrayList<Item> items;
    ArrayList<Demon> demons;
    
    
    public AttackHandler(Player player, ArrayList<Bat> bats, ArrayList<Crab> crabs,
                         ArrayList<MechaGoblin> mechaGoblin, ArrayList<Obstacle> obstacles,
                         ArrayList<Item> items, ArrayList<Demon> demons) {
        
        this.player = player;
        this.bats = bats;
        this.crabs = crabs;
        this.mechaGoblin = mechaGoblin;
        this.obstacles = obstacles;
        this.items = items;
        this.demons = demons;
        
    }
    
    // UPDATE TO SEE IF ANY OF THE ENTITEIS ARE ATTACKING AND CHECKING IF THEIR HITBOXES ALIGN THEN SUBSEQUENTLY
    // UPDATE / DEAL DAMAGE TO THE OTHER ENTITIES

    // Within the FOR loops, there are break statements if the attack is NOT splash damage
    // Only the player's laser and rock should be splash damage
    public void update() {
        
        // #1 Checking if the player has attacked
        
        String playerAttack = player.getAttack();
        
        if(!playerAttack.equals("idle")) {
            
            switch(playerAttack) {
                
                case "stompRight" -> {
                    
                       for(int i=0 ;i<crabs.size(); i++) {
                           if(player.getRightStompHitbox().intersects(crabs.get(i).getHitbox())) {
                               crabs.get(i).reduceHealth(player.stompDamage);
                               break;
                           }
                       }
                       
                       for(int i=0 ;i<mechaGoblin.size(); i++) {
                           if(player.getRightStompHitbox().intersects(mechaGoblin.get(i).getHitbox())) {
                               mechaGoblin.get(i).reduceHealth(player.stompDamage);
                               break;
                           }
                       }

                       for(int i=0; i<obstacles.size(); i++) {
                           if(player.getRightStompHitbox().intersects(obstacles.get(i).getHitbox())) {
                               obstacles.get(i).reduceHealth(player.stompDamage);
                               break;
                           }
                       }


                       for(int i=0; i<demons.size(); i++) {
                           if(player.getRightStompHitbox().intersects(demons.get(i).getHitbox())) {
                               demons.get(i).reduceHealth(player.stompDamage);
                               break;
                           }
                       }

                    break;
                }
                
                case"stompLeft" -> {
                       for(int i=0 ;i<crabs.size(); i++) {
                           if(player.getLeftStompHitbox().intersects(crabs.get(i).getHitbox())) {
                               crabs.get(i).reduceHealth(player.stompDamage);
                               break;
                           }
                       }
                       
                       for(int i=0 ;i<mechaGoblin.size(); i++) {
                           if(player.getRightStompHitbox().intersects(mechaGoblin.get(i).getHitbox())) {
                               mechaGoblin.get(i).reduceHealth(player.stompDamage);
                               break;
                           }
                       }

                    for(int i=0; i<obstacles.size(); i++) {
                        if (player.getLeftStompHitbox().intersects(obstacles.get(i).getHitbox())) {
                            obstacles.get(i).reduceHealth(player.stompDamage);
                            break;
                        }
                    }

                    for(int i=0; i<demons.size(); i++) {
                        if(player.getLeftStompHitbox().intersects(demons.get(i).getHitbox())) {
                            demons.get(i).reduceHealth(player.stompDamage);
                            break;
                        }
                    }

                    break;
                }
                
                case "laserLeft" -> {
                       for(int i=0 ;i<bats.size(); i++) {
                           if(player.getLeftLaserHitbox().intersects(bats.get(i).getHitbox())) {
                               bats.get(i).reduceHealth(player.laserDamage);
                           }
                       }
                       
                       for(int i=0 ;i<mechaGoblin.size(); i++) {
                           if(player.getLeftLaserHitbox().intersects(mechaGoblin.get(i).getHitbox())) {
                               mechaGoblin.get(i).reduceHealth(player.laserDamage);
                           }
                       }

                    for(int i=0; i<obstacles.size(); i++) {
                        if (player.getLeftLaserHitbox().intersects(obstacles.get(i).getHitbox())) {
                            obstacles.get(i).reduceHealth(player.stompDamage);
                        }
                    }

                    for(int i=0; i<demons.size(); i++) {
                        if (player.getLeftLaserHitbox().intersects(demons.get(i).getHitbox())) {
                            demons.get(i).reduceHealth(player.stompDamage);
                        }
                    }
                    
                    break;
                }
                
                case "laserRight" -> {
                       for(int i=0 ;i<bats.size(); i++) {
                           if(player.getRightLaserHitbox().intersects(bats.get(i).getHitbox())) {
                               bats.get(i).reduceHealth(player.laserDamage);
                           }
                       }
                       
                       for(int i=0 ;i<mechaGoblin.size(); i++) {
                           if(player.getRightLaserHitbox().intersects(mechaGoblin.get(i).getHitbox())) {
                               mechaGoblin.get(i).reduceHealth(player.laserDamage);
                           }
                       }

                    for(int i=0; i<obstacles.size(); i++) {
                        if (player.getRightLaserHitbox().intersects(obstacles.get(i).getHitbox())) {
                            obstacles.get(i).reduceHealth(player.stompDamage);
                        }
                    }

                    for(int i=0; i<demons.size(); i++) {
                        if (player.getRightLaserHitbox().intersects(demons.get(i).getHitbox())) {
                            demons.get(i).reduceHealth(player.stompDamage);
                        }
                    }
                    
                    break;
                }
                
                
            }
        }


        // Also check if the cyclop's rock hits anything
        if(player.rock.active) {
            for(int i=0; i<bats.size(); i++) {
                if(player.rock.getHitbox().intersects(bats.get(i).getHitbox())) {
                    bats.get(i).reduceHealth(player.rockDamage);
                    player.rock.impact = true;
                }
            }
            
            for(int i=0; i<crabs.size(); i++) {
                if(player.rock.getHitbox().intersects(crabs.get(i).getHitbox())) {
                    crabs.get(i).reduceHealth(player.rockDamage);
                    player.rock.impact = true;
                }
            }
            
            for(int i=0; i<mechaGoblin.size(); i++) {
                if(player.rock.getHitbox().intersects(mechaGoblin.get(i).getHitbox())) {
                    mechaGoblin.get(i).reduceHealth(player.rockDamage);
                    player.rock.impact = true;
                }
            }

            for(int i=0; i<obstacles.size(); i++) {
                if(player.rock.getHitbox().intersects(obstacles.get(i).getHitbox())) {
                    obstacles.get(i).reduceHealth(player.rockDamage);
                    player.rock.impact = true;
                }
            }

            for(int i=0; i<demons.size(); i++) {
                if(player.rock.getHitbox().intersects(demons.get(i).getHitbox())) {
                    demons.get(i).reduceHealth(player.rockDamage);
                    player.rock.impact = true;
                }
            }
            
            
            
        }
        
        
        // # 2 check every single bat object
        
        for(int i=0; i<bats.size(); i++) {
                        // Check if the player gets hit

            if (bats.get(i).getHitbox().intersects(player.getHitbox())) {
                bats.get(i).incrementDamageFlag();
                // Every 60 seconds the crab is touching the player, deal damage
                if (bats.get(i).getFPSDamageFlag() == 60) {
                    //activeImage = sprites[14];
                    player.reduceHealth(bats.get(i).damage);
                    bats.get(i).resetDamageFlag();
                }
            } else {
                // reset the timer so that there isn't a precharged attack
                bats.get(i).resetDamageFlag();
            }
        }
        
        // #3 Check for the crabs
        for(int i=0; i<crabs.size(); i++) {
                        // Check if the player gets hit

            if (crabs.get(i).getHitbox().intersects(player.getHitbox())) {
                crabs.get(i).incrementDamageFlag();
                // Every 60 seconds the crab is touching the player, deal damage
                if (crabs.get(i).getFPSDamageFlag() == 60) {
                    //activeImage = sprites[14];
                    player.reduceHealth(crabs.get(i).damage);
                    crabs.get(i).resetDamageFlag();
                }
            } else {
                // reset the timer so that there isn't a precharged attack
                crabs.get(i).resetDamageFlag();
            }
        }    
        
        // # 4 Check for the mechaGoblin 
        for(int i=0; i<mechaGoblin.size(); i++) {
            String mechaGoblinAttack = mechaGoblin.get(i).getAttack();

            if(mechaGoblinAttack.equals("right")) {

                if(mechaGoblin.get(i).getAttackRight().intersects(player.getHitbox())) {
                        player.reduceHealth(mechaGoblin.get(i).damage);
                }



            } else if(mechaGoblinAttack.equals("left")) {

                if(mechaGoblin.get(i).getAttackLeft().intersects(player.getHitbox())) {
                        player.reduceHealth(mechaGoblin.get(i).damage);

                }
            }
        }

        // # 5 Check for the demons

        for(int i=0; i<demons.size(); i++) {
            // Check if the player gets hit

            //Demon is a little bit more advanced because there are directions for the attacks
            // The if statements are kinda cancer but basically it is checking:
            // IF dircection = right, then check for the right attacks
            // IF direction = left, then check for the left attacks

            if ( (demons.get(i).direction.equals("left") && demons.get(i).getAttackLeft().intersects(player.getHitbox())) ||
                    (demons.get(i).direction.equals("right")  && demons.get(i).getAttackRight().intersects(player.getHitbox()))) {
                demons.get(i).incrementDamageFlag();
                // Every 60 seconds the demon is touching the player, deal damage
                if (demons.get(i).getFPSDamageFlag() == 30) {

                    player.reduceHealth(demons.get(i).damage);
                    demons.get(i).resetDamageFlag();
                }
            } else {
                // reset the timer so that there isn't a precharged attack
                demons.get(i).resetDamageFlag();
            }
        }




        // Checking for player collision with items

        for(int i=0; i<items.size(); i++) {
            if(items.get(i).getHitbox().intersects(player.getHitbox())) {
                player.addHealth(items.get(i).health);
                items.get(i).used();
            }
        }







        
    } // END update
    
    
} // END class
