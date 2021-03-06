package edu.ycp.cs320.fokemon_webApp.shared.Player;


import java.io.Serializable;
import java.util.ArrayList;

import edu.ycp.cs320.fokemon_webApp.shared.Battle.TurnChoice;
import edu.ycp.cs320.fokemon_webApp.shared.ItemClasses.Item;
import edu.ycp.cs320.fokemon_webApp.shared.PokemonClasses.Pokemon;
import edu.ycp.cs320.fokemon_webApp.shared.PokemonClasses.Status;

public class Player implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private boolean gender;
	private Location PlayerLocation;
	private int PlayerID;
	private ArrayList<Pokemon> team;
	private ArrayList<Pokemon> PC;
	private ArrayList<Item> items;
	private int currentPokemonIndex;
	private TurnChoice choice;
	private int moveIndex;
	private int turnOrder;

	public Player(int PlayerID, String name, boolean gender,
			Location playerLocation) {
		this.setPlayerID(PlayerID);
		this.setName(name);
		this.setGender(gender); // True = Boy; False = Girl
		this.currentPokemonIndex = 0;
		this.moveIndex = 0;
		this.choice = TurnChoice.MOVE;
		this.turnOrder = 0;

		team = new ArrayList<Pokemon>();
		PC = new ArrayList<Pokemon>();
		items = new ArrayList<Item>();
		setPlayerLocation(new Location(playerLocation.getAreaArrayIndex(),
				playerLocation.getX(), playerLocation.getY()));
		// team.add(new Pokemon())

	}
	
	public Player() {
		
	}

	public int getTeamSize() {
		return team.size();
	}
	
	public void setTeam(ArrayList<Pokemon> _team) {
		this.team = _team;
	}
	
	public Pokemon getTeam(int i) {
		return team.get(i);
	}

	public ArrayList<Pokemon> getTeam() {
		return team;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean getGender() {
		return gender;
	}

	public void setGender(boolean gender) {
		this.gender = gender;
	}

	public Location getPlayerLocation() {
		return PlayerLocation;
	}

	public void setPlayerLocation(Location playerLocation) {
		PlayerLocation = playerLocation;
	}

	public int getPlayerID() {
		return PlayerID;
	}

	public void setPlayerID(int playerID) {
		PlayerID = playerID;
	}

	public int getCurrentPokemonIndex() {
		return currentPokemonIndex;
	}

	public void setCurrentPokemonIndex(int currentPokemonIndex) {
		if (currentPokemonIndex < getTeamSize()) {
			if (team.get(currentPokemonIndex).getStats().getStatus() != Status.FNT) {
				this.currentPokemonIndex = currentPokemonIndex;
			}
		}
	}

	public TurnChoice getChoice() {
		return choice;
	}

	public void setChoice(TurnChoice choice) {
		this.choice = choice;
	}

	public int getMoveIndex() {
		return moveIndex;
	}

	public void setMoveIndex(int moveIndex) {
		this.moveIndex = moveIndex;
	}

	public void addPokemonToTeam(Pokemon pokemon) {
		team.add(pokemon);
	}

	public int getTurnOrder() {
		return turnOrder;
	}

	public void setTurnOrder(int TurnOrder) {
		turnOrder = TurnOrder;
	}

	public ArrayList<Item> getItems() {
		return items;
	}

	public Item getItems(int index) {
		return items.get(index);
	}

	public void setItems(ArrayList<Item> items) {
		this.items = items;
	}

	public ArrayList<Pokemon> getPC() {
		return PC;
	}

	public void setPC(ArrayList<Pokemon> pC) {
		PC = pC;
	}

}
