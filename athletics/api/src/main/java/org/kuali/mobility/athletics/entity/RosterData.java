package org.kuali.mobility.athletics.entity;

import java.io.Serializable;
import java.util.List;

public class RosterData implements Serializable {

	private static final long serialVersionUID = 1758726180629674311L;

	private List<Player> players;

	private Sport sport;

	public Sport getSport() {
		return sport;
	}

	public void setSport(Sport sport) {
		this.sport = sport;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}
}
