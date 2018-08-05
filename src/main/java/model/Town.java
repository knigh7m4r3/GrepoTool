package model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table
public class Town {

	private String name;
	
	@Id
	//@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id = null;
	
	@ManyToOne
	private Player player;
	private Integer islandX;
	private Integer islandY; 
	private Integer numberOnIsland;
	private Integer points;
	private Integer ocean;
	
	public Town () {
		super();
	}
	
	public Town(String name, Integer id, Player player, Integer islandX, Integer islandY, Integer numberOnIsland,
			Integer points, Integer ocean) {
		super();
		this.name = name;
		this.id = id;
		this.player = player;
		this.islandX = islandX;
		this.islandY = islandY;
		this.numberOnIsland = numberOnIsland;
		this.points = points;
		this.ocean = ocean;
	}
	public Town(Integer islandX, Integer islandY) {
		super();
		this.islandX = islandX;
		this.islandY = islandY;
	}
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
	public Player getPlayer() {
		return player;
	}
	public void setPlayer(Player player) {
		this.player = player;
	}
	public Integer getIslandX() {
		return islandX;
	}
	public void setIslandX(Integer islandX) {
		this.islandX = islandX;
	}
	public Integer getIslandY() {
		return islandY;
	}
	public void setIslandY(Integer islandY) {
		this.islandY = islandY;
	}
	public Integer getNumberOnIsland() {
		return numberOnIsland;
	}
	public void setNumberOnIsland(Integer numberOnIsland) {
		this.numberOnIsland = numberOnIsland;
	}
	public Integer getPoints() {
		return points;
	}
	public void setPoints(Integer points) {
		this.points = points;
	}
	public Integer getOcean() {
		return ocean;
	}
	public void setOcean(Integer ocean) {
		this.ocean = ocean;
	}
	@Override
	public String toString() {
		return "Town [name=" + name + ", id=" + id + ", player=" + player + ", islandX=" + islandX + ", islandY="
				+ islandY + ", numberOnIsland=" + numberOnIsland + ", points=" + points + ", ocean=" + ocean + "]";
	}

	/**
	 * Calculates Distance between two Towns by using the Pythagorean theroem
	 * @param other Other Town
	 * @return Distance
	 */
	public int calcDist(Town other) {
		final int distX = Math.abs(this.getIslandX() - other.getIslandX());
		final int distY = Math.abs(this.getIslandY() - other.getIslandY());
		
		final int cSquare = distX * distX + distY * distY;
		final int c = (int) Math.round(Math.sqrt(cSquare));
		return c;
	}
	
}
