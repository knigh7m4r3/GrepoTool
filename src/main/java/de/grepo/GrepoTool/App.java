package de.grepo.GrepoTool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.Query;

import model.Alliance;
import model.DBFactory;
import model.Player;
import model.Town;

/**
 * Hello world!
 *
 */
public class App {
	
	public static void mainTest(String [] args) {
		final Town blexen = new Town(409,516);
		
		final Town mimi = new Town(403,539);
		
		final Town chris = new Town(412,481);
		
		final Town holsten = new Town(394,536);
		
		System.out.println("me -> mimi:\t"+blexen.calcDist(mimi));
		System.out.println("me -> chris:\t"+blexen.calcDist(chris));
		System.out.println("me -> holsten:\t"+blexen.calcDist(holsten));
		
		
	}
	
	private static boolean cleanAndParse = true;
	
	@SuppressWarnings("unchecked")
	public static void main( String[] args ) throws IOException {
		DBFactory.initEM();
		
		if(cleanAndParse) {
			DBFactory.cleanDB();

			parseAlliance();
			parsePlayer();
			parseTown();
			System.out.println( "All Data inserted!" );

		}
			


		DBFactory.getEm().getTransaction().begin();
		System.out.println("Search for players who are not in an alliance ...");
		final List<Player> players = DBFactory.getEm().createQuery("select p from Player p where p.alliance = null and p.points > 1000").getResultList();
		
		System.out.println("Search for their towns ...");
		final Query query = DBFactory.getEm().createQuery("select t from Town t where t.player in ?1");
		query.setParameter(1, players);
		final List<Town> towns = query.getResultList();
		
		final Map<Player, List<Town>> townsNearToBlexen = new HashMap<>();
		
		System.out.println("Check the distance to their towns ...");
		final Town blexen = new Town(409,516);
		for(Town t : towns) {
			if(t.getPlayer() != null && blexen.calcDist(t) < 30) {
				if(townsNearToBlexen.containsKey(t.getPlayer())) {
					townsNearToBlexen.get(t.getPlayer()).add(t);
				}else {
					final List<Town> townList = new ArrayList<>();
					townList.add(t);
					townsNearToBlexen.put(t.getPlayer(), townList);
				}
			}
		}
		
		final Path out = Paths.get("./src/main/resources/txt/out.txt");
		final List<String> outLines = new ArrayList<>();
		
		for (Entry<Player, List<Town>> set : townsNearToBlexen.entrySet()) {
			//Check if the current Player has more than half of his towns in our area and is "reachable"
			if(set.getKey() != null && set.getKey().getTowns() != null && set.getValue().size() >= (set.getKey().getTowns().size() / 2.0)) {
				System.out.println("Player found!\t"+set.getKey().toSimpleString());
				outLines.add(set.getKey().toSimpleString());
			}
		}
		
		Files.write(out, outLines, Charset.forName("UTF-8"));
		
		
		DBFactory.getEm().getTransaction().commit();
	}



	private static void parseAlliance() throws IOException {
		System.out.print("Inserting Alliances\t [ ");
		final File file = new File("./src/main/resources/txt/alliances.txt");

		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;

		DBFactory.getEm().getTransaction().begin();
		int counter = 0;
		while((line = br.readLine()) != null) {
			//System.out.println(line);
			final Alliance all = new Alliance();
			final String [] parts = line.split(",");
			all.setId(Integer.parseInt(parts[0]));
			all.setName(parts[1]);
			all.setPoints(Integer.parseInt(parts[2]));
			all.setTowns(Integer.parseInt(parts[3]));
			all.setRank(Integer.parseInt(parts[5]));
			DBFactory.getEm().merge(all);
			++counter;
			if(counter % 5 == 0) {
				System.out.print(".");
			}
		}
		DBFactory.getEm().getTransaction().commit();
		br.close();
		System.out.println(" ] Done!");
	}

	@SuppressWarnings("unchecked")
	private static void parsePlayer() throws NumberFormatException, IOException {
		final long start = System.currentTimeMillis();
		System.out.print("Inserting Players\t [ ");
		
		final List<Alliance> allianceList = DBFactory.getEm().createQuery("select a from Alliance a").getResultList();
		final Map<Integer, Alliance> allianceMap = new HashMap<Integer, Alliance>();
		for(Alliance a : allianceList) {
			allianceMap.put(a.getId(), a);
		}
		
		final File file = new File("./src/main/resources/txt/players.txt");

		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;

		int counter = 0;
		DBFactory.getEm().getTransaction().begin();
		while((line = br.readLine()) != null) {
			final String [] parts = line.split(",");

			final Player player = new Player();
			player.setId(Integer.parseInt(parts[0]));
			player.setName(parts[1]);
			player.setPoints(Integer.parseInt(parts[3]));
			player.setRank(Integer.parseInt(parts[4]));

			if(!parts[2].isEmpty()) {
				final Integer allianceID = Integer.parseInt(parts[2]);
				//Each time Database-Statement is 4 times slower than preparing a map...
//				final Query query = DBFactory.getEm().createQuery("select a from Alliance a where a.id = ?1");
//				query.setParameter(1, allianceID);
//				final List<Alliance> result = query.getResultList();
//				if(result.size() != 1) {
//					//System.out.println("Error detected, aborted!\nNot exactly one Alliance found!\t"+result.size()+"\nFor Player:\t"+line);
//					continue;
//				}else {
//					player.setAlliance(result.get(0));
//				}

				
				if(allianceMap.containsKey(allianceID)) {
					final Alliance alliance = allianceMap.get(allianceID);
					player.setAlliance(alliance);
					alliance.addPlayer(player);
					allianceMap.replace(alliance.getId(), alliance);
				}
			}else {
				player.setAlliance(null);
			}

			DBFactory.getEm().merge(player);

			++counter;
			if(counter % 125 == 0) {
				System.out.print(".");
			}
		}
		DBFactory.getEm().getTransaction().commit();
		br.close();
		final long end = System.currentTimeMillis();
		System.out.println(" ] Done! in \t"+ (end-start)+" ms");
	}

	private static void parseTown() throws NumberFormatException, IOException {
		final long start = System.currentTimeMillis();
		System.out.print("Inserting Towns\t\t [ ");
		
		final List<Player> playerList = DBFactory.getEm().createQuery("select p from Player p").getResultList();
		final Map<Integer, Player> playerMap = new HashMap<Integer, Player>();	
		for(Player p : playerList) {
			playerMap.put(p.getId(), p);
		}
		
		final File file = new File("./src/main/resources/txt/towns.txt");

		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;

		int counter = 0;
		DBFactory.getEm().getTransaction().begin();
		while((line = br.readLine()) != null) {
			final String [] parts = line.split(",");

			final Town town = new Town();
			town.setId(Integer.parseInt(parts[0]));
			town.setName(parts[2]);
			town.setIslandX(Integer.parseInt(parts[3]));
			town.setIslandY(Integer.parseInt(parts[4]));
			town.setNumberOnIsland(Integer.parseInt(parts[5]));
			town.setPoints(Integer.parseInt(parts[6]));

			//TODO Search for Player
			if(!parts[1].isEmpty()) {
				final Integer playerID = Integer.parseInt(parts[1]);

				//Each time Database-Statement is 4 times slower than preparing a map...
//				final Query query = DBFactory.getEm().createQuery("select p from Player p where p.id = ?1");
//				query.setParameter(1, playerID);
//				final List<Player> result = query.getResultList();
//				if(result.size() != 1) {
//					//System.out.println("Error detected, aborted!\nNot exactly one Player found!\t"+result.size()+"\nFor Town:\t"+line);
//					continue;
//				}else {
//					town.setPlayer(result.get(0));
//				}
				
				if(playerMap.containsKey(playerID)) {
					final Player player = playerMap.get(playerID);
					town.setPlayer(player);
					player.addTown(town);
					playerMap.replace(player.getId(), player);
				}
				
			}else {
				town.setPlayer(null);
			}

			DBFactory.getEm().merge(town);
			++counter;
			if(counter % 250 == 0) {
				System.out.print(".");
			}
		}
		DBFactory.getEm().getTransaction().commit();
		br.close();
		final long end = System.currentTimeMillis();
		System.out.println(" ] Done! in \t"+ (end-start)+" ms");
	}

}
