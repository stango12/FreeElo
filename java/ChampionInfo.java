package summonerList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonReaderFactory;
import javax.json.JsonValue;

//data of all the champions with their id: https://global.api.pvp.net/api/lol/static-data/na/v1.2/champion?api_key=API HERE
public class ChampionInfo 
{
	final static String API = "API HERE";
	public static void main(String [] args) throws IOException
	{
		final String PATH = "matchListBiofrost.txt";
		BufferedReader br = new BufferedReader(new FileReader(PATH));
		String match = br.readLine();
		while(match != null)
		{
			File log = new File("changeLog.txt");
			PrintWriter changeLog = new PrintWriter(new FileWriter(log, true));
			System.out.println("Analyzing match Id " + match + "...");
			changeLog.println("Analyzing match Id " + match + "...");
			URL url = new URL("https://na.api.pvp.net/api/lol/na/v2.2/match/" + match + "?includeTimeline=true&api_key=" + API);
			InputStream is = url.openStream();
			JsonReader rdr = Json.createReader(is);
			JsonObject obj = rdr.readObject();
			JsonObject timeline = obj.getJsonObject("timeline");
			JsonArray frames = timeline.getJsonArray("frames");
			JsonArray participants = obj.getJsonArray("participants");
			ArrayList<Integer>[] items = getStartingItems(frames);
			for(int i = 0; i < participants.size(); i++)
			{
				JsonObject info = participants.getJsonObject(i);
				JsonArray mastery = info.getJsonArray("masteries");
				JsonObject stats = info.getJsonObject("stats");
				JsonArray rune = info.getJsonArray("runes");
				JsonObject timelineLane = info.getJsonObject("timeline");
				String lane = timelineLane.getString("lane");
				
				ArrayList<Integer> masteries = new ArrayList<Integer>();
				if(mastery != null)
					masteries = getMasteries(mastery);
				Map<Integer, Integer> runes = getRunes(rune);
				ArrayList<Integer> fullBuild = getFullItems(stats);
				int spell1 = info.getInt("spell1Id");
				int spell2 = info.getInt("spell2Id");
				int championId = info.getInt("championId");
				int participantId = info.getInt("participantId");
				ArrayList<Integer> startItems = items[participantId];
				
				//checking if file exists
				File runeFile = new File("champions" + File.separator + championId + File.separator + lane + "runes.txt");
				File masteryFile = new File("champions" + File.separator + championId + File.separator + lane + "masteries.txt");
				File spellFile = new File("champions" + File.separator + championId + File.separator + lane + "spells.txt");
				File startFile = new File("champions" + File.separator + championId + File.separator + lane + "startingItems.txt");
				File endFile = new File("champions" + File.separator + championId + File.separator + lane + "endingItems.txt");
				
				if(!runeFile.exists())
				{
					//create a new file if they dont exist
					runeFile.createNewFile();
					masteryFile.createNewFile();
					spellFile.createNewFile();
					startFile.createNewFile();
					endFile.createNewFile();
					
					//creating JSON objects for each file
					JsonBuilderFactory factory = Json.createBuilderFactory(null);	
					
					//creating rune JSON object names are id + amount
					JsonObjectBuilder runeBuild = factory.createObjectBuilder();
					for(Map.Entry<Integer, Integer> entry : runes.entrySet())
					{
						runeBuild.add(entry.getKey().toString() + entry.getValue(), 1);
					}
					runeBuild.add("totalGames", 1);
					JsonObject runeObject = runeBuild.build();
					
					//creating mastery JSON object
					JsonObjectBuilder masteryBuild = factory.createObjectBuilder();
					for(Integer id : masteries)
					{
						masteryBuild.add(id.toString(), 1);
					}
					masteryBuild.add("totalGames", 1);
					JsonObject masteryObject = masteryBuild.build();
					
					//creating spell JSON object
					JsonObject spellObject = factory.createObjectBuilder()
							.add(spell1 + "", 1)
							.add(spell2 + "", 1)
							.add("totalGames", 1)
							.build();
					
					//creating starting items JSON object potion: 2003 biscuit: 2010
					JsonObjectBuilder startBuild = factory.createObjectBuilder();
					boolean potionFlag = false;
					for(Integer id : startItems)
					{
						if((id == 2003 || id == 2010) && !potionFlag) //potions/biscuits
						{
							potionFlag = true;
							startBuild.add(id.toString(), 1);
						}
						else
						{
							startBuild.add(id.toString(), 1);
						}
					}		
					startBuild.add("totalGames", 1);
					JsonObject startObject = startBuild.build();
					
					//creating end game items JSON object
					JsonObjectBuilder endBuild = factory.createObjectBuilder();
					for(Integer id : fullBuild)
					{
						if(checkFullItem(id))
							endBuild.add(id.toString(), 1);
					}
					endBuild.add("totalGames", 1);
					JsonObject endObject = endBuild.build();
					
					//writing JsonObjects to files just created
					PrintWriter runeWriter = new PrintWriter(runeFile, "UTF-8");
					PrintWriter masteryWriter = new PrintWriter(masteryFile, "UTF-8");
					PrintWriter spellWriter = new PrintWriter(spellFile, "UTF-8");
					PrintWriter startWriter = new PrintWriter(startFile, "UTF-8");
					PrintWriter endWriter = new PrintWriter(endFile, "UTF-8");
					
					runeWriter.print(runeObject.toString());
					masteryWriter.print(masteryObject.toString());
					spellWriter.print(spellObject.toString());
					startWriter.print(startObject.toString());
					endWriter.print(endObject.toString());
					
					runeWriter.close();
					masteryWriter.close();
					spellWriter.close();
					startWriter.close();
					endWriter.close();
				}
				else
				{
					//getting the JSON objects if the file exists
					JsonObject runeObject = readFile("champions" + File.separator + championId + File.separator + lane + "runes.txt");
					JsonObject masteryObject = readFile("champions" + File.separator + championId + File.separator + lane + "masteries.txt");
					JsonObject spellObject = readFile("champions" + File.separator + championId + File.separator + lane + "spells.txt");
					JsonObject startObject = readFile("champions" + File.separator + championId + File.separator + lane + "startingItems.txt");
					JsonObject endObject = readFile("champions" + File.separator + championId + File.separator + lane + "endingItems.txt");
				
					//creating JSON objects for each file
					JsonBuilderFactory factory = Json.createBuilderFactory(null);	
					
					//creating rune JSON object names are id + amount
					JsonObjectBuilder runeBuild = factory.createObjectBuilder();
					for(Map.Entry<Integer, Integer> entry : runes.entrySet())
					{
						String key = entry.getKey().toString() + entry.getValue();
						if(runeObject.containsKey(key))
						{
							runeBuild.add(key, runeObject.getInt(key) + 1);
						}
						else
						{
							runeBuild.add(key, 1);
						}
					}
					
					//adding the other values in the old file
					for(Entry<String, JsonValue> entry : runeObject.entrySet())
					{
						if(!entry.getKey().equals("totalGames"))
						{
							int key = Integer.parseInt(entry.getKey());
							if(!runes.containsKey(key / 10))
							{
								runeBuild.add(entry.getKey(), entry.getValue());
							}
							
							if(runes.containsKey(key / 10)  && key % 10 != runes.get(key / 10))
							{
								runeBuild.add(entry.getKey(), entry.getValue());
							}
						}
					}
					runeBuild.add("totalGames", runeObject.getInt("totalGames") + 1);
					JsonObject newRuneObject = runeBuild.build();

					//creating mastery JSON object
					JsonObjectBuilder masteryBuild = factory.createObjectBuilder();
					for(Integer id : masteries)
					{
						if(masteryObject.containsKey(id.toString()))
						{
							masteryBuild.add(id.toString(), masteryObject.getInt(id.toString()) + 1);
						}
						else
						{
							masteryBuild.add(id.toString(), 1);
						}
					}
					
					//adding the other values in the old file
					for(Entry<String, JsonValue> entry : masteryObject.entrySet())
					{
						if(!entry.getKey().equals("totalGames"))
						{
							int key = Integer.parseInt(entry.getKey());
							if(!masteries.contains(key))
							{
								masteryBuild.add(entry.getKey(), entry.getValue());
							}
						}
					}
					masteryBuild.add("totalGames", masteryObject.getInt("totalGames") + 1);
					JsonObject newMasteryObject = masteryBuild.build();

					//creating spell JSON object
					JsonObjectBuilder spellBuild = factory.createObjectBuilder();
					if(spellObject.containsKey(spell1 + ""))
					{
						spellBuild.add(spell1 + "", spellObject.getInt(spell1 + "") + 1);
					}
					else
						spellBuild.add(spell1 + "", 1);
					if(spellObject.containsKey(spell2 + ""))
					{
						spellBuild.add(spell2 + "", spellObject.getInt(spell2 + "") + 1);
					}
					else
						spellBuild.add(spell2 + "", 1);
					
					//adding old values in the old file
					for(Entry<String, JsonValue> entry : spellObject.entrySet())
					{
						if(!entry.getKey().equals("totalGames"))
						{
							int key = Integer.parseInt(entry.getKey());
							if(key != spell1 && key != spell2)
							{
								spellBuild.add(entry.getKey(), entry.getValue());
							}
						}
					}
					spellBuild.add("totalGames", spellObject.getInt("totalGames") + 1);
					JsonObject newSpellObject = spellBuild.build();
					
					//creating starting items Json Object
					JsonObjectBuilder startBuild = factory.createObjectBuilder();
					boolean potionFlag = false;
					for(Integer id : startItems)
					{
						if(startObject.containsKey(id.toString()))
						{
							if((id == 2003 || id == 2010) && !potionFlag) //potions/biscuits
							{
								potionFlag = true;
								startBuild.add(id.toString(), startObject.getInt(id.toString()) + 1);
							}
							else
								startBuild.add(id.toString(), startObject.getInt(id.toString()) + 1);
						}
						else
							startBuild.add(id.toString(), 1);
					}
					
					
					//adding old values to file
					for(Entry<String, JsonValue> entry : startObject.entrySet())
					{
						if(!entry.getKey().equals("totalGames"))
						{
							int key = Integer.parseInt(entry.getKey());
							if(!startItems.contains(key))
							{
								startBuild.add(entry.getKey(), entry.getValue());
							}
						}
					}
					startBuild.add("totalGames", startObject.getInt("totalGames") + 1);
					JsonObject newStartObject = startBuild.build();
					
					//creating full build JSON object
					JsonObjectBuilder endBuild = factory.createObjectBuilder();
					for(Integer id : fullBuild)
					{
						if(checkFullItem(id))
						{
							if(endObject.containsKey(id.toString()))
							{
								endBuild.add(id.toString(), endObject.getInt(id.toString()) + 1);
							}
							else
							{
								endBuild.add(id.toString(), 1);
							}
						}
					}
					
					//adding the other values in the old file
					for(Entry<String, JsonValue> entry : endObject.entrySet())
					{
						if(!entry.getKey().equals("totalGames"))
						{
							int key = Integer.parseInt(entry.getKey());
							if(!fullBuild.contains(key))
							{
								endBuild.add(entry.getKey(), entry.getValue());
							}
						}
					}
					endBuild.add("totalGames", endObject.getInt("totalGames") + 1);
					JsonObject newEndObject = endBuild.build();
					
//					//writing JsonObjects to files just created
					PrintWriter runeWriter = new PrintWriter(runeFile, "UTF-8");
					PrintWriter masteryWriter = new PrintWriter(masteryFile, "UTF-8");
					PrintWriter spellWriter = new PrintWriter(spellFile, "UTF-8");
					PrintWriter startWriter = new PrintWriter(startFile, "UTF-8");
					PrintWriter endWriter = new PrintWriter(endFile, "UTF-8");
					
					runeWriter.print(newRuneObject.toString());
					masteryWriter.print(newMasteryObject.toString());
					spellWriter.print(newSpellObject.toString());
					startWriter.print(newStartObject.toString());
					endWriter.print(newEndObject.toString());
					
					runeWriter.close();
					masteryWriter.close();
					spellWriter.close();
					startWriter.close();
					endWriter.close();
				}
				
				//printing everything
//				System.out.println("Participant: " + participantId + " Champion: " + championId);
//				System.out.println("Spells: " + spell1 + ", " + spell2);
//				System.out.println("Items:");
//				for(int j = 0; j < fullBuild.size(); j++)
//					System.out.print(fullBuild.get(j) + ", ");
//				System.out.println();
//				System.out.println("Masteries:");
//				for(int j = 0; j < masteries.size(); j++)
//					System.out.print(masteries.get(j) + ", ");
//				System.out.println();
//				System.out.println("Runes:");
//				for(Map.Entry<Integer, Integer> entry : runes.entrySet())
//				{
//					System.out.println(entry.getKey() + ": " + entry.getValue());
//				}
//				System.out.println();
				
				System.out.println("Champion ID: " + championId + " changed.");
				changeLog.println("Champion ID: " + championId + " changed.");
				
			}
			System.out.println();
			changeLog.println();
//			printItems(items);
			match = br.readLine();
			changeLog.close();
			
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		br.close();
	}
	
	//gets the starting items of a match
	public static ArrayList<Integer>[] getStartingItems(JsonArray frames)
	{
		//participantId starts at 1 so 0 spot is empty
		ArrayList<Integer>[] items = (ArrayList<Integer>[]) new ArrayList[11];
		for(int i = 0; i < items.length; i++)
			items[i] = new ArrayList<Integer>();
		JsonObject start = frames.getJsonObject(1);
		JsonArray events = start.getJsonArray("events");
		for(int i = 0; i < events.size(); i++)
		{
			JsonObject item = events.getJsonObject(i);
			if(item.getString("eventType").equals("ITEM_PURCHASED"))
			{
				items[item.getInt("participantId")].add(item.getInt("itemId"));
			}
		}
		return items;
	}
	
	//Gets the runes and puts them in a map with key runeId value amount
	public static Map<Integer, Integer> getRunes(JsonArray rune)
	{
		Map<Integer, Integer> runes = new HashMap<Integer, Integer>();
		for(int i = 0; i < rune.size(); i++)
		{
			JsonObject r = rune.getJsonObject(i);
			int amount = r.getInt("rank");
			int id = r.getInt("runeId");
			runes.put(id, amount);
		}
		return runes;
	}
	
	//gets the masteries and puts it in an arraylist
	public static ArrayList<Integer> getMasteries(JsonArray mastery)
	{
		ArrayList<Integer> masteries = new ArrayList<Integer>();
		for(int i = 0; i < mastery.size(); i++)
		{
			JsonObject m = mastery.getJsonObject(i);
			masteries.add(m.getInt("masteryId"));
		}
		return masteries;
	}

	//gets all items at the end of the game
	public static ArrayList<Integer> getFullItems(JsonObject stats)
	{
		ArrayList<Integer> items = new ArrayList<Integer>(); //item6 is the trinket slot
		for(int i = 0; i < 7; i++)
		{
			items.add(stats.getInt("item" + i));
		}
		return items;
	}
	
	//prints the items for debugging
	public static void printItems(ArrayList<Integer>[] items)
	{
		System.out.println("Starting Items:");
		for(int i = 0; i < items.length; i++)
		{
			System.out.print(i + ":");
			for(int j = 0; j < items[i].size(); j++)
			{
				System.out.print(items[i].get(j) + ", ");
			}
			System.out.println();
		}
	}
	
	//checks if the item ID is a full item or subcomponent of an item
	public static boolean checkFullItem(int id) throws IOException
	{
		if(id == 0)
			return false;
		//special cases ex. boots
		if(id == 3047 || id == 3006 || id == 3009 || id == 3020 || id == 3111 || id == 3117 || id == 3158)
			return true;
		
		//old boot enchantments
		if(id >= 1300 && id <= 1334)
			return true;
		
		//sated devourer
		if(id == 3931 || id == 1415 || id == 1411)
			return true;
		
		//sightstone
		if(id == 2049)
			return true;
		
		if(id == 3930)
			return true;
		
		//DNE anymore item?
		if(id == 2043 || id == 3932)
			return false;
		
		//System.out.println("ID: " + id);
		URL url = new URL("https://global.api.pvp.net/api/lol/static-data/na/v1.2/item/" + id + "?itemData=into&api_key=" + API);
		InputStream is = url.openStream();
		JsonReader rdr = Json.createReader(is);
		JsonObject obj = rdr.readObject();
		return !obj.containsKey("into");
	}
	
	//reads a JSON text file and returns it as a JSON object
	public static JsonObject readFile(String path) throws FileNotFoundException
	{
		JsonReaderFactory factory = Json.createReaderFactory(null);
		InputStream is = new FileInputStream(path);
		JsonReader rdr = factory.createReader(is, java.nio.charset.StandardCharsets.UTF_8);
		JsonObject obj = rdr.readObject();
		rdr.close();
		return obj;
	}
}
