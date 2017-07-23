var mapSpell = new Object();
mapSpell[21] = "SummonerBarrier";
mapSpell[1]  = "SummonerBoost";
mapSpell[14] = "SummonerDot";
mapSpell[3]  = "SummonerExhaust";
mapSpell[4]  = "SummonerFlash";
mapSpell[6]  = "SummonerHaste";
mapSpell[7]  = "SummonerHeal";
mapSpell[13] = "SummonerMana";
mapSpell[11] = "SummonerSmite";
mapSpell[12] = "SummonerTeleport";

//i = 86
var main = function() {
	// var fileDisplayArea = document.getElementById("update");
 //    fileDisplayArea.innerText = "test"; 
    readTextFileStart("http://localhost:8000/champions/data/35/JUNGLEstartMax.txt");
    readTextFileEnd("http://localhost:8000/champions/data/35/JUNGLEendMax.txt");
    readTextFileSpells("http://localhost:8000/champions/data/35/JUNGLEspellMax.txt");
    readTextFileRunes("http://localhost:8000/champions/data/35/JUNGLErunesMax.txt");
    readTextFileMastery("http://localhost:8000/champions/data/35/JUNGLEmasteryMax.txt");
};

//For summoner spells
function readTextFileSpells(file)
{
    var fileDisplayArea = document.getElementById("updateSpells");
    var list = document.getElementById('startSpells');
    var rawFile = new XMLHttpRequest();
    rawFile.open("GET", file, false);
    rawFile.onreadystatechange = function ()
    {
        if(rawFile.readyState === 4)
        {
            if(rawFile.status === 200 || rawFile.status == 0)
            {
                //getting the file data for most used spells
                var allText = rawFile.responseText;
                //fileDisplayArea.innerHTML = allText; 
                var idArray = allText.split("\n");
                for(i = 0; i < idArray.length; i++)
                {
                    if(idArray[i] != "")
                    {
                        //editing the html data to display the info from the text files
                        var temp = parseInt(idArray[i] + "");
                        var x = document.createElement("IMG");
                        x.setAttribute("src", "http://ddragon.leagueoflegends.com/cdn/6.19.1/img/spell/" + mapSpell[temp] + ".png");

                        var spaceNode = document.createTextNode(" ");
                        list.appendChild(x);
                        list.appendChild(spaceNode);
                    }
                }
            }
        }
    }
    rawFile.send(null);
}

//For runes
function readTextFileRunes(file)
{
    var fileDisplayArea = document.getElementById("updateRunes");
    var list = document.getElementById("startRunes");
    var rawFile = new XMLHttpRequest();
    rawFile.open("GET", file, false);
    rawFile.onreadystatechange = function ()
    {
        if(rawFile.readyState === 4)
        {
            if(rawFile.status === 200 || rawFile.status == 0)
            {
                var allText = rawFile.responseText;
                //fileDisplayArea.innerHTML = allText; 
                var idArray = allText.split("\n");
                for(i = 0; i < idArray.length; i += 2)
                {
                    if(idArray[i] != "")
                    {
                        var x = document.createElement("IMG");
                        var node = document.createElement("ul");
                        var runeId = httpGet("http://127.0.0.1:5000/rune/" + idArray[i]);
                        var runeName = IDtoRuneName(runeId);

                        x.setAttribute("src", "http://ddragon.leagueoflegends.com/cdn/6.19.1/img/rune/" + IDtoRunePic(runeId));
                        var textnode = document.createTextNode("  " + runeName + " x" + idArray[i + 1]); 

                        node.appendChild(x);
                        node.appendChild(textnode);
                        list.appendChild(node);
                    }
                }
            }
        }
    }
    rawFile.send(null);
}

//for masteries
function readTextFileMastery(file)
{
    var rawFile = new XMLHttpRequest();
    rawFile.open("GET", file, false);
    rawFile.onreadystatechange = function ()
    {
        if(rawFile.readyState === 4)
        {
            if(rawFile.status === 200 || rawFile.status == 0)
            {
                var allText = rawFile.responseText;
                var idArray = allText.split("\n");
                for(i = 0; i < idArray.length; i++)
                {
                    if(idArray[i] != "")
                    {
                        var temp = parseInt(idArray[i] + "");
                        document.getElementById(temp + "").classList.remove("grey");
                    }
                }
            }
        }
    }
    rawFile.send(null);
}

//for starting items
function readTextFileStart(file)
{
	var fileDisplayArea = document.getElementById("updateItems");
    var list = document.getElementById('startItems');
    var rawFile = new XMLHttpRequest();
    rawFile.open("GET", file, false);
    rawFile.onreadystatechange = function ()
    {
        if(rawFile.readyState === 4)
        {
            if(rawFile.status === 200 || rawFile.status == 0)
            {
                var allText = rawFile.responseText;
                //fileDisplayArea.innerHTML = allText; 
                var idArray = allText.split("\n");
                for(i = 0; i < idArray.length; i++)
                {
                    if(idArray[i] != "")
                    {
                        var x = document.createElement("IMG");
                        x.setAttribute("src", "http://ddragon.leagueoflegends.com/cdn/6.18.1/img/item/" + idArray[i] + ".png");
                        list.appendChild(x);
                    }
                }
            }
        }
    }
    rawFile.send(null);
}

function readTextFileEnd(file)
{
    var fileDisplayArea = document.getElementById("updateItems");
    var list = document.getElementById("endItems");
    var rawFile = new XMLHttpRequest();
    rawFile.open("GET", file, false);
    rawFile.onreadystatechange = function ()
    {
        if(rawFile.readyState === 4)
        {
            if(rawFile.status === 200 || rawFile.status == 0)
            {
                var allText = rawFile.responseText;
                //fileDisplayArea.innerHTML = allText; 
                var idArray = allText.split("\n");
                for(i = 0; i < idArray.length; i++)
                {
                    if(idArray[i] != "")
                    {
                        var x = document.createElement("IMG");
                        x.setAttribute("src", "http://ddragon.leagueoflegends.com/cdn/6.18.1/img/item/" + idArray[i] + ".png");
                        list.appendChild(x);
                    }
                }
            }
        }
    }
    rawFile.send(null);
}

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

//takes in rune data and gets name
function IDtoRuneName(id)
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

//takes in rune data and gets png name
function IDtoRunePic(id)
{
    var index = id.indexOf("\"full\"") + 8;
    var champName = "";
    while(id.charAt(index) !== '\"')
    {
        champName += id.charAt(index);
        index++;
    }

    return champName;
}

$(document).ready(main);