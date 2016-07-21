var main = function() {
	var freeWeekChamps = httpGet("https://na.api.pvp.net/api/lol/na/v1.2/champion?freeToPlay=true&api_key=APIKEY");
	var ids = [];
    var keys = [];

    var n = 0;

    for(i = 0; i < freeWeekChamps.length; i++)
    {
    	        	var temp = "";
    	while(freeWeekChamps.charAt(i) >= "0" && freeWeekChamps.charAt(i) <= "9")
    	{
    		temp += freeWeekChamps.charAt(i);
    		i++;
    	}
    	if(temp !== "" && temp !== "0")
    	{

        	ids[n] = temp;
        	n++;
    	}
    }

    var test = "";
	for(i = 0; i < ids.length; i++)
	{
		if(ids[i] != 0)
		{
            var link = "https://global.api.pvp.net/api/lol/static-data/na/v1.2/champion/"+ ids[i] + "?api_key=APIKEY";
		    ids[i] = IDtoChamp(httpGet(link));
            keys[i] = IDtoKey(httpGet(link));
            test += ids[i] + " key: " + keys[i] + ", ";
        }
	}

    		    $('.first-point').text(test);
    document.getElementById("champ0").href = "champions/azir.html";
};

function httpGet(theUrl)
{
    if (window.XMLHttpRequest)
    {// code for IE7+, Firefox, Chrome, Opera, Safari
        xmlhttp=new XMLHttpRequest();
    }
    else
    {// code for IE6, IE5
        xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
    }
    xmlhttp.open("GET", theUrl, false );
    xmlhttp.send();    
    return xmlhttp.responseText;
}

function IDtoChamp(id)
{
    var index = id.indexOf("\"name\"") + 8;
    var champName = "";
    while(id.charAt(index) !== '\"')
    {
        champName += id.charAt(index);
        index++;
    }

    return champName;
}

function IDtoKey(id)
{
    var index = id.indexOf("\"key\"") + 7;
    var champName = "";
    while(id.charAt(index) !== '\"')
    {
        champName += id.charAt(index);
        index++;
    }

    return champName;
}

$(document).ready(main);
