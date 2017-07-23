package summonerList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonReaderFactory;
import javax.json.JsonValue;


public class mostUsedFile 
{
	public static void main(String args []) throws IOException
	{
		final String API = "API HERE";
		int champId[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 48, 50, 51, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 63, 67, 68, 69, 72, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 89, 90, 91, 92, 96, 98, 99, 101, 102, 103, 104, 105, 106, 107, 110, 111, 112, 113, 114, 115, 117, 119, 120, 121, 122, 126, 127, 131, 133, 134, 136, 143, 150, 154, 157, 161, 163, 201, 202, 203, 222, 223, 236, 238, 240, 245, 254, 266, 267, 268, 412, 420, 421, 427, 429, 432};
		String position[] = {"TOP", "JUNGLE", "MIDDLE", "BOTTOM"};
		for(int i = 0; i < champId.length; i++)
		{
			System.out.println("Champ ID: " + champId[i] + ":" + i);
		//int i = 1;
			for(int a = 0; a < position.length; a++)
			{
				File runeFile = new File("champions" + File.separator + champId[i] + File.separator + position[a] + "runes.txt");
	
				if(runeFile.exists())
				{
					JsonObject runeObject = readFile("champions" + File.separator + champId[i] + File.separator + position[a] + "runes.txt");
					JsonObject masteryObject = readFile("champions" + File.separator + champId[i] + File.separator + position[a] + "masteries.txt");
					JsonObject spellObject = readFile("champions" + File.separator + champId[i] + File.separator + position[a] + "spells.txt");
					JsonObject startObject = readFile("champions" + File.separator + champId[i] + File.separator + position[a] + "startingItems.txt");
					JsonObject endObject = readFile("champions" + File.separator + champId[i] + File.separator + position[a] + "endingItems.txt");
					
					//getting max counter for runes
					int yellowMax = 0, redMax = 0, blueMax = 0, quintMax = 0;
					int yellowId = 0, redId = 0, blueId = 0, quintId = 0;
					
					int yellowMax2 = 0, redMax2 = 0, blueMax2 = 0, quintMax2 = 0;
					int yellowId2 = 0, redId2 = 0, blueId2 = 0, quintId2 = 0;
					
					int blueCount = 0, yellowCount = 0, redCount = 0, quintCount = 0;
					for(Entry<String, JsonValue> entry : runeObject.entrySet())
					{
						if(!entry.getKey().equals("totalGames"))
						{
							int key = Integer.parseInt(entry.getKey());
							URL url = new URL("https://global.api.pvp.net/api/lol/static-data/na/v1.2/rune/" + key / 10 + "?includeTimeline=true&api_key=" + API);
							InputStream is = url.openStream();
							JsonReader rdr = Json.createReader(is);
							JsonObject obj = rdr.readObject();
							JsonObject rune = obj.getJsonObject("rune");
							String type = rune.getString("type");
							
							if(type.equals("blue"))
							{
								if(Integer.parseInt(entry.getValue().toString()) > blueMax)
								{
									blueMax = Integer.parseInt(entry.getValue().toString());
									blueId = key;
									blueCount = blueId % 10;
								}
								if(blueCount < 9)
								{
									if(Integer.parseInt(entry.getValue().toString()) > blueMax2 && key % 10 + blueCount == 9)
									{
										blueMax2 = Integer.parseInt(entry.getValue().toString());
										blueId2 = key;
									}
								}
							}
							
							if(type.equals("yellow"))
							{
								if(Integer.parseInt(entry.getValue().toString()) > yellowMax)
								{
									yellowMax = Integer.parseInt(entry.getValue().toString());
									yellowId = key;
									yellowCount = yellowId % 10;
								}
								if(yellowCount < 9)
								{
									if(Integer.parseInt(entry.getValue().toString()) > yellowMax2 && key % 10 + yellowCount == 9)
									{
										yellowMax2 = Integer.parseInt(entry.getValue().toString());
										yellowId2 = key;
									}
								}
							}
							
							if(type.equals("red"))
							{
								if(Integer.parseInt(entry.getValue().toString()) > redMax)
								{
									redMax = Integer.parseInt(entry.getValue().toString());
									redId = key;
									redCount = redId % 10;
								}
								if(redCount < 9)
								{
									if(Integer.parseInt(entry.getValue().toString()) > redMax2 && key % 10 + redCount == 9)
									{
										redMax2 = Integer.parseInt(entry.getValue().toString());
										redId2 = key;
									}
								}
							}
							
							if(type.equals("black"))
							{
								if(Integer.parseInt(entry.getValue().toString()) > quintMax)
								{
									quintMax = Integer.parseInt(entry.getValue().toString());
									quintId = key;
									quintCount = quintId % 10;
								}
								if(quintCount < 9)
								{
									if(Integer.parseInt(entry.getValue().toString()) > quintMax2 && key % 10 + quintCount == 9)
									{
										quintMax2 = Integer.parseInt(entry.getValue().toString());
										quintId2 = key;
									}
								}
							}
						}
					}
					
					File runeMaxFile = new File("champions" + File.separator + champId[i] + File.separator + position[a] + "runesMax.txt");
					PrintWriter runeWriter = new PrintWriter(runeMaxFile, "UTF-8");
					
					runeWriter.println(blueId / 10);
					runeWriter.println(blueId % 10);
					if(blueId % 10 + blueId2 % 10 == 9 && blueId2 % 10 != 0)
					{
						runeWriter.println(blueId2 / 10);
						runeWriter.println(blueId2 % 10);
					}
					runeWriter.println(yellowId / 10);
					runeWriter.println(yellowId % 10);
					if(yellowId % 10 + yellowId2 % 10 == 9 && yellowId2 % 10 != 0)
					{
						runeWriter.println(yellowId2 / 10);
						runeWriter.println(yellowId2 % 10);
					}
					runeWriter.println(redId / 10);
					runeWriter.println(redId % 10);
					if(redId % 10 + redId2 % 10 == 9 && redId2 % 10 != 0)
					{
						runeWriter.println(redId2 / 10);
						runeWriter.println(redId2 % 10);
					}
					runeWriter.println(quintId / 10);
					runeWriter.println(quintId % 10);
					if(quintId % 10 + quintId2 % 10 == 9 && quintId2 % 10 != 0)
					{
						runeWriter.println(quintId2 / 10);
						runeWriter.println(quintId2 % 10);
					}
					runeWriter.close();
					
					File masteryMaxFile = new File("champions" + File.separator + champId[i] + File.separator + position[a] + "masteryMax.txt");
					PrintWriter masteryWriter = new PrintWriter(masteryMaxFile, "UTF-8");
					
					String[] ferocity = {"", "", "", "", "", ""};
					String[] cunning = {"", "", "", "", "", ""};
					String[] resolve = {"", "", "", "", "", ""};
					
					//getting the mastery used the most for each tier in each tree
					//ferocity tree
					//level 1
					if(masteryObject.containsKey("6111") || masteryObject.containsKey("6114"))
					{
						ferocity[0] = maxTwoMasteries("6111", "6114", masteryObject);
						
						//level 2
						if(masteryObject.containsKey("6121") || masteryObject.containsKey("6122") || masteryObject.containsKey("6123"))
						{
							ferocity[1] = maxThreeMasteries("6121", "6122", "6123", masteryObject);
							
							
							//level 3
							if(masteryObject.containsKey("6131") || masteryObject.containsKey("6134"))
							{
								ferocity[2] = maxTwoMasteries("6131", "6134", masteryObject);
								
								//level 4
								if(masteryObject.containsKey("6141") || masteryObject.containsKey("6142"))
								{
									ferocity[3] = maxTwoMasteries("6141", "6142", masteryObject);
									
									//level 5
									if(masteryObject.containsKey("6151") || masteryObject.containsKey("6154"))
									{
										ferocity[4] = maxTwoMasteries("6151", "6154", masteryObject);
										
										//level 6
										if(masteryObject.containsKey("6161") || masteryObject.containsKey("6162") || masteryObject.containsKey("6164"))
										{
											ferocity[5] = maxThreeMasteries("6161", "6162", "6164", masteryObject);
										}
									}
								}
							}
						}
					}
					
					//cunning tree
					//level 1
					if(masteryObject.containsKey("6311") || masteryObject.containsKey("6312"))
					{
						cunning[0] = maxTwoMasteries("6311", "6312", masteryObject);
						
						//level 2
						if(masteryObject.containsKey("6321") || masteryObject.containsKey("6322") || masteryObject.containsKey("6323"))
						{
							cunning[1] = maxThreeMasteries("6321", "6322", "6323", masteryObject);
							
							
							//level 3
							if(masteryObject.containsKey("6331") || masteryObject.containsKey("6332"))
							{
								cunning[2] = maxTwoMasteries("6331", "6332", masteryObject);
								
								//level 4
								if(masteryObject.containsKey("6342") || masteryObject.containsKey("6343"))
								{
									cunning[3] = maxTwoMasteries("6342", "6343", masteryObject);
									
									//level 5
									if(masteryObject.containsKey("6351") || masteryObject.containsKey("6152"))
									{
										cunning[4] = maxTwoMasteries("6351", "6352", masteryObject);
										
										//level 6
										if(masteryObject.containsKey("6361") || masteryObject.containsKey("6362") || masteryObject.containsKey("6163"))
										{
											cunning[5] = maxThreeMasteries("6361", "6362", "6363", masteryObject);
										}
									}
								}
							}
						}
					}
					
					//resolve tree
					//level 1
					if(masteryObject.containsKey("6211") || masteryObject.containsKey("6212"))
					{
						resolve[0] = maxTwoMasteries("6211", "6212", masteryObject);
						
						//level 2
						if(masteryObject.containsKey("6221") || masteryObject.containsKey("6223"))
						{
							resolve[1] = maxTwoMasteries("6221", "6223", masteryObject);
							
							
							//level 3
							if(masteryObject.containsKey("6231") || masteryObject.containsKey("6232"))
							{
								resolve[2] = maxTwoMasteries("6231", "6232", masteryObject);
								
								//level 4
								if(masteryObject.containsKey("6241") || masteryObject.containsKey("6242"))
								{
									resolve[3] = maxTwoMasteries("6241", "6242", masteryObject);
									
									//level 5
									if(masteryObject.containsKey("6251") || masteryObject.containsKey("6252"))
									{
										resolve[4] = maxTwoMasteries("6251", "6252", masteryObject);
										
										//level 6
										if(masteryObject.containsKey("6261") || masteryObject.containsKey("6262") || masteryObject.containsKey("6263"))
										{
											resolve[5] = maxThreeMasteries("6261", "6262", "6263", masteryObject);
										}
									}
								}
							}
						}
					}
					
					//finding and writing to file which keystone is used the most
					String max = maxThreeMasteries(ferocity[5], cunning[5], resolve[5], masteryObject);
					if(max.equals(ferocity[5]))
					{
						for(int j = 0; j < ferocity.length; j++)
						{
							masteryWriter.println(ferocity[j]);
						}
						
						//finding which 12 point tree is used the most
						String subMax = maxTwoMasteries(cunning[3], resolve[3], masteryObject);
						if(subMax.equals(cunning[3]))
						{
							for(int j = 0; j < 4; j++)
							{
								masteryWriter.println(cunning[j]);
							}
						}
						else
						{
							for(int j = 0; j < 4; j++)
							{
								masteryWriter.println(resolve[j]);
							}
						}
					}
					else if(max.equals(cunning[5]))
					{
						for(int j = 0; j < cunning.length; j++)
						{
							masteryWriter.println(cunning[j]);
						}
						
						//finding which 12 point tree is used the most
						String subMax = maxTwoMasteries(ferocity[3], resolve[3], masteryObject);
						if(subMax.equals(ferocity[3]))
						{
							for(int j = 0; j < 4; j++)
							{
								masteryWriter.println(ferocity[j]);
							}
						}
						else
						{
							for(int j = 0; j < 4; j++)
							{
								masteryWriter.println(resolve[j]);
							}
						}
					}
					else
					{
						for(int j = 0; j < resolve.length; j++)
						{
							masteryWriter.println(resolve[j]);
						}
						
						//finding which 12 point tree is used the most
						String subMax = maxTwoMasteries(cunning[3], ferocity[3], masteryObject);
						if(subMax.equals(cunning[3]))
						{
							for(int j = 0; j < 4; j++)
							{
								masteryWriter.println(cunning[j]);
							}
						}
						else
						{
							for(int j = 0; j < 4; j++)
							{
								masteryWriter.println(ferocity[j]);
							}
						}
					}
					
					masteryWriter.close();
					
					//getting max summoner spells
					int mostSpell = 0, mostSpell2 = 0;
					String mostSpellId = "", mostSpell2Id = "";
					for(Entry<String, JsonValue> entry : spellObject.entrySet())
					{
						if(!entry.getKey().equals("totalGames"))
						{
							if(Integer.parseInt(entry.getValue().toString()) > mostSpell)
							{
								mostSpell2 = mostSpell;
								mostSpell2Id = mostSpellId;
								mostSpell = Integer.parseInt(entry.getValue().toString());
								mostSpellId = entry.getKey();
							}
							else if(Integer.parseInt(entry.getValue().toString()) > mostSpell2)
							{
								mostSpell2 = Integer.parseInt(entry.getValue().toString());
								mostSpell2Id = entry.getKey();
							}
						}
					}
					
					File spellMaxFile = new File("champions" + File.separator + champId[i] + File.separator + position[a] + "spellMax.txt");
					PrintWriter spellWriter = new PrintWriter(spellMaxFile, "UTF-8");
					spellWriter.println(mostSpellId);
					spellWriter.println(mostSpell2Id);
					
					spellWriter.close();
					
					//getting max starting items
					int gold = 500;
					File startMaxFile = new File("champions" + File.separator + champId[i] + File.separator + position[a] + "startMax.txt");
					PrintWriter startWriter = new PrintWriter(startMaxFile, "UTF-8");
					ArrayList<Entry<String, JsonValue>> itemSort = new ArrayList();
					for(Entry<String, JsonValue> entry : startObject.entrySet())
					{
						if(!entry.getKey().equals("totalGames"))
						{
							if(itemSort.size() == 0)
								itemSort.add(entry);
							boolean putFlag = false;
							for(int j = 0; j < itemSort.size(); j++)
							{
								if(Integer.parseInt(entry.getValue().toString()) > Integer.parseInt(itemSort.get(j).getValue().toString()))
								{
									itemSort.add(j, entry);
									putFlag = true;
									break;
								}
							}
							if(!putFlag)
							{
								itemSort.add(entry);
							}
						}
					}
					
					int j = 0;
					int potionValue = 0;
					int biscuitValue = 0;
					int greenTrinket = 0;
					int redTrinket = 0;
					boolean potionCheck = false;
					
					//getting the list of starting items and sorting from most used to least
					while(gold > 0)
					{
						if(j == itemSort.size())
							break;
						//ignoring trinkets for now
						if(Integer.parseInt(itemSort.get(j).getKey()) != 3340 && Integer.parseInt(itemSort.get(j).getKey()) != 3341)//put trinket last for format
						{
							//ignoring potions for now
							if(Integer.parseInt(itemSort.get(j).getKey()) == 2003 || Integer.parseInt(itemSort.get(j).getKey()) == 2010) //potion check
							{
								potionCheck = true;
								if(Integer.parseInt(itemSort.get(j).getKey()) == 2003)
								{
									potionValue = Integer.parseInt(itemSort.get(j).getValue().toString());
								}
								else
								{
									biscuitValue = Integer.parseInt(itemSort.get(j).getValue().toString());
								}
							}//removed jungle item? causes errors 
							else if(Integer.parseInt(itemSort.get(j).getKey()) == 2043)
							{
								
							}
							else
							{
								URL url = new URL("https://global.api.pvp.net/api/lol/static-data/na/v1.2/item/" + itemSort.get(j).getKey() + "?itemData=gold&api_key=" + API);
								InputStream is = url.openStream();
								JsonReader rdr = Json.createReader(is);
								JsonObject obj = rdr.readObject();
								JsonObject goldObj = obj.getJsonObject("gold");
								if(goldObj.getInt("total") <= gold)
								{
									gold -= goldObj.getInt("total");
									startWriter.println(itemSort.get(j).getKey());
									//if user bought a different type of potion, red pots and biscuits are invalid
									if(itemSort.get(j).getKey().equals("2031") || itemSort.get(j).getKey().equals("2032") || itemSort.get(j).getKey().equals("2033"))
									{
										potionCheck = false;
									}
								}
							}
						}
						
						if(Integer.parseInt(itemSort.get(j).getKey()) == 3340)
						{
							greenTrinket = Integer.parseInt(itemSort.get(j).getValue().toString());
						}
						
						if(Integer.parseInt(itemSort.get(j).getKey()) == 3341)
						{
							redTrinket = Integer.parseInt(itemSort.get(j).getValue().toString());
						}
						j++;
					}
					
					//putting the potions in
					if(potionCheck)
					{
						while(gold > 0)
						{
							if(potionValue > biscuitValue)
								startWriter.println("2003");
							else
								startWriter.println("2010");
							
							gold -= 50;
						}
					}
					
					if(greenTrinket > redTrinket)
						startWriter.println("3340");
					else
						startWriter.println("3341");
					
					startWriter.close();
					
					//ending items max
					File endMaxFile = new File("champions" + File.separator + champId[i] + File.separator + position[a] + "endMax.txt");
					PrintWriter endWriter = new PrintWriter(endMaxFile, "UTF-8");
					ArrayList<Entry<String, JsonValue>> endItemSort = new ArrayList();
					for(Entry<String, JsonValue> entry : endObject.entrySet())
					{
						if(!entry.getKey().equals("totalGames"))
						{
							if(endItemSort.size() == 0)
								endItemSort.add(entry);
							boolean putFlag = false;
							
							//sorting the items by highest frequency to lowest
							for(int k = 0; k < endItemSort.size(); k++)
							{
								if(Integer.parseInt(entry.getValue().toString()) > Integer.parseInt(endItemSort.get(k).getValue().toString()))
								{
									endItemSort.add(k, entry);
									putFlag = true;
									break;
								}
							}
							if(!putFlag)
							{
								endItemSort.add(entry);
							}
						}
					}
					
					int slots = 6;
					int temp = 0;
					boolean trinket = false;
					boolean boots = false;
					boolean jungleItem = false;
					
					//holding the boot and trinket id so they can always appear at the end
					String bootId = "", trinketId = "";
					
					while((slots > 0 || !trinket) && temp < endItemSort.size())
					{
						String id = endItemSort.get(temp).getKey();
						
						//to ensure only one jungle item
						if(id.equals("1412") || id.equals("1413") || id.equals("1414") || id.equals("1419") || id.equals("1400") || id.equals("1401") || id.equals("1402") || id.equals("1416") || id.equals("1408") || id.equals("1409") || id.equals("1410") || id.equals("1418"))
						{
							if(!jungleItem)
							{
								jungleItem = true;
								endWriter.println(id);
								slots--;
							}
						}
						
						//to ensure only one trinket in final build
						else if((id.equals("3363") || id.equals("3364") || id.equals("3340")))
						{
							if(!trinket)
							{
								trinket = true;
								trinketId = id;
							}
						}
						
						//to ensure only one boot in final build
						else if((id.equals("3006") || id.equals("3009") || id.equals("3020") || id.equals("3047") || id.equals("3111") || id.equals("3117") || id.equals("3158")) && slots > 0)
						{
							if(!boots)
							{
								boots = true;
								bootId = id;
								slots--;
							}
						}
						else if(slots > 0)
						{
							//some items to not include like doran's items
							if(!id.equals("1055") && !id.equals("1056") && !id.equals("1054"))
							{
								endWriter.println(id);
								slots--;
							}
						}
						
						temp++;
					}
					
					endWriter.println(bootId);
					endWriter.println(trinketId);
					endWriter.close();
				}
			}
		}
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
	
	public static String maxTwoMasteries(String id1, String id2, JsonObject mastery)
	{
		if(mastery.containsKey(id1) && mastery.containsKey(id2))
		{
			if(mastery.getInt(id1) > mastery.getInt(id2))
				return id1;
			else
				return id2;
		}
		else if(mastery.containsKey(id1))
			return id1;
		else
			return id2;
	}
	
	public static String maxThreeMasteries(String id1, String id2, String id3, JsonObject mastery)
	{
		if(mastery.containsKey(id1) && mastery.containsKey(id2) && mastery.containsKey(id3))
		{
			if(mastery.getInt(id1) > mastery.getInt(id2) && mastery.getInt(id1) > mastery.getInt(id3))
				return id1;
			else if(mastery.getInt(id2) > mastery.getInt(id1) && mastery.getInt(id2) > mastery.getInt(id3))
				return id2;
			else
				return id3;
		}
		else 
		{
			String m = maxTwoMasteries(id1, id2, mastery);
			return maxTwoMasteries(m, id3, mastery);
		}
	}
}
