var main = function() {
    var ids = httpGet("http://127.0.0.1:5000/freeChamps").split(" ");
    var keys = [];

    var n = 0;
    //gets the names and keys for the champs that are free
    var test = "";
    for(i = 0; i < ids.length; i++)
    {
        if(ids[i] != 0)
        {
            var link = "http://127.0.0.1:5000/champion/" + ids[i];
            ids[i] = IDtoChamp(httpGet(link));
            keys[i] = IDtoKey(httpGet(link));
        }
    }

    //updating the carousel to have the right champs
    for(i = 0; i < ids.length; i++)
    {
        document.getElementById("champ" + i).href = "champions/" + keys[i] + ".html";
        document.getElementById("pic" + i).src = "http://ddragon.leagueoflegends.com/cdn/img/champion/splash/" + keys[i] + "_0.jpg";
        document.getElementById("name" + i).innerHTML = ids[i];
    }
    
};

function httpGet(theUrl)
{
    //taken from SO
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

//takes in ID, gets the name of the champion
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

//takes in ID, gets the key of the champion
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