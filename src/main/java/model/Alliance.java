package model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table
public class Alliance {

	private String name;
	
	@Id
	//@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id = null;
	private Integer points;
	private Integer towns;
	private Integer rank;
	
	@OneToMany
	private List<Player> players;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	public Integer getTowns() {
		return towns;
	}

	public void setTowns(Integer towns) {
		this.towns = towns;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}

	@Override
	public String toString() {
		return "Alliance [name=" + name + ", id=" + id + ", points=" + points + ", towns=" + towns + ", rank=" + rank
				+ ", players=" + players.size() + "]";
	}


	public void addPlayer(Player player) {
		if(this.getPlayers() == null) {
			this.setPlayers(new ArrayList<Player>());
		}
		this.getPlayers().add(player);
	}

}
