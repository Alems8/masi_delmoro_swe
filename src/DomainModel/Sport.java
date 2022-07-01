/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DomainModel;

/**
 *
 * @author thomas
 */
public enum Sport {
    PADEL("padel",4), SOCCER("soccer",10);

    public final String name;
    public final int numPlayers;

    private Sport(String name, int numPlayers){
        this.name = name;
        this.numPlayers = numPlayers;
    }

}
