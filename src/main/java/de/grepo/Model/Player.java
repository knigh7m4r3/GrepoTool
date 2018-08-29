package de.grepo.Model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table
public class Player {

	@Id
	//@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id = null;
	
	private String name;
	
	private Integer points;
	private Integer rank;
	
	@OneToMany
	private List<Town> towns;
	
	@ManyToOne
	private Alliance alliance;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name.replace("+", " ");
	}

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public List<Town> getTowns() {
		return towns;
	}

	public void setTowns(List<Town> towns) {
		this.towns = towns;
	}

	public Alliance getAlliance() {
		return alliance;
	}

	public void setAlliance(Alliance alliance) {
		this.alliance = alliance;
	}

	@Override
	public String toString() {
		return "Player [id=" + id + ", name=" + name + ", points=" + points + ", rank=" + rank + ", towns=" + towns.size()
				+ ", alliance=" + alliance + "]";
	}
	
	public String toSimpleString() {
		return name + " [points=" + points + ", rank=" + rank + ", towns=" + towns.size()
		+ ", alliance=" + alliance + "]";
	}
	
	public void addTown(Town town) {
		if(this.getTowns() == null) {
			this.setTowns(new ArrayList<Town>());
		}
		this.getTowns().add(town);
	}
	
}
