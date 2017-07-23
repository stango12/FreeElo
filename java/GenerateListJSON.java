package summonerList;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.json.*;

public class GenerateListJSON 
{
	public static void main(String[] args)
	{
		final String API = "API HERE";
		//profile names here
		String plat = "xxcheckmate";
		String diamond = "5hardul";
		String master = "imaqtpie";
		String challenger = "aphromoo";
		
		String begin[] = {"biofrost"};
		//,  plat, diamond, master, challenger , "doublelift" ,"pobelter" "biofrost","xmithie" , "stixxay" , "huhi" , "kina", "fallenbandit", "dhn", "Junglehuni"
        PrintWriter writer = null;
        PrintWriter matchFile = null;
		try {
			writer = new PrintWriter("accountsListBiofrost.txt", "UTF-8");
			matchFile = new PrintWriter("matchListBiofrost.txt", "UTF-8");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<Integer> summonerIds = new ArrayList<>();
		for(int i = 0; i < begin.length; i++)
		{
			int id = getID("https://na.api.pvp.net/api/lol/na/v1.4/summoner/by-name/" + begin[i] + "?api_key=" + API, begin[i]);
			
			//getting total games
			URL url = null;
			try {
				url = new URL("https://na.api.pvp.net/api/lol/na/v2.2/matchlist/by-summoner/" + id + "?rankedQueues=TEAM_BUILDER_DRAFT_RANKED_5x5,RANKED_SOLO_5x5&seasons=SEASON2016&api_key=" + API);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try 
			{
				InputStream is = url.openStream();
				JsonReader rdr = Json.createReader(is);
				JsonObject obj = rdr.readObject();
				ArrayList<Long> matchIds = new ArrayList<Long>();
				int matchAmmount = obj.getInt("totalGames");
				JsonArray matches = obj.getJsonArray("matches");
				//grabs all the match IDs
				for(int j = 0; j < matchAmmount; j++)
				{
					JsonObject match = matches.getJsonObject(j);
					matchIds.add(match.getJsonNumber("matchId").longValueExact());
					matchFile.println(match.getJsonNumber("matchId").longValueExact());
				}
				//takes the match IDs and gets more summoner names
				for(int j = 0; j < matchIds.size(); j++)
				{
					System.out.println(matchIds.size() + ": " + j);
					Long matchIdNum = matchIds.get(j);
					URL url2 = null;
					try
					{
						url2 = new URL("https://na.api.pvp.net/api/lol/na/v2.2/match/" + matchIdNum + "?api_key=" + API);
					}
					catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					try 
					{
						InputStream is2 = url2.openStream();
						JsonReader rdr2 = Json.createReader(is2);
						JsonObject obj2 = rdr2.readObject();
						JsonArray participants = obj2.getJsonArray("participantIdentities");
						for(int k = 0; k < 10; k++)
						{
							JsonObject o = participants.getJsonObject(k);
							JsonObject player = o.getJsonObject("player");
							int playerId = player.getInt("summonerId");
							if(!summonerIds.contains(playerId))
							{
//								String rank = getRank("https://na.api.pvp.net/api/lol/na/v2.5/league/by-summoner/" + playerId + "/entry?api_key=" + API, playerId);
//								if(rank.equals("PLATINUM") || rank.equals("DIAMOND") || rank.equals("MASTER") || rank.equals("CHALLENGER"))
//								{
									writer.println(playerId);
									summonerIds.add(playerId);
//								}
							}
						}
					}
					catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					try {
						Thread.sleep(1300);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		writer.close();
		matchFile.close();
	}
	
	public static int getID(String u, String name)
	{
		URL url = null;
		try {
			url = new URL(u);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {InputStream is = url.openStream();
			JsonReader rdr = Json.createReader(is);
			JsonObject obj = rdr.readObject();
			JsonObject results = obj.getJsonObject(name);

			return results.getInt("id");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return 0;
	}
	
	public static String getRank(String u, int id)
	{
		URL url = null;
		try {
			url = new URL(u);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {InputStream is = url.openStream();
			JsonReader rdr = Json.createReader(is);
			JsonObject obj = rdr.readObject();
			JsonArray results = obj.getJsonArray(String.valueOf(id));
			return results.getJsonObject(0).getString("tier");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
}
