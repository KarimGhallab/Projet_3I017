/* Ajout de image de profil */
/*
function uploadFile() {
    var blobFile = $('#filechooser').files[0];
    var formData = new FormData();
    formData.append("fileToUpload", blobFile);

    $.ajax({
       url: "user/UploadImage",
       type: "POST",
       data: formData,
       processData: false,
       contentType: false,
       success: function(response) {
           // .. do something
       },
       error: function(jqXHR, textStatus, errorMessage) {
           console.log(errorMessage); // Optional
       }
    });
}

function reponseUploadFile(){

}*/
/* Ajout de message */

function mainAddMessage()
{
    var input = $("#new_msg").val();
    if (input != ""){
        addMessage(input);	
    }
    else{
    	alert("Vous ne pouvez pas publier un message vide")
    }
}

function addMessage(message)
{
    var editedMessage = encodeURIComponent(message);
    
    $.ajax({
        type: "POST",
        url: "message/addMessage",
        data: "key="+env.key+"&message="+editedMessage,
        dataType:"text",
        success: function(rep){
            reponseAddMessage(rep);
        },
        error: function(XHR , textStatus , errorThrown){
            alert(textStatus);
        } 
    })
    
}

function reponseAddMessage(rep)
{
    var repD = JSON.parse(rep, revival);   
    if (repD.status == "ko")
	{
    	alert(repD.message);
	}
    else
	{
    	$("#new_msg").val("");
    	var message = repD.added_message;
        env.messages[repD.added_message.id] = message;
        
        $("#messages").prepend(message.getHTML());
        $("#input_comment_"+message.id).keydown(enterHandlerAddComment);
	}
}


function addComment(id){
    var value = $("#input_comment_"+id).val();
    if (value != "")
	{
    	$.ajax({
            type: "POST",
            url: "comment/addComment",
            data: "key="+env.key+"&idMessage="+id+"&commentaire="+value,
            dataType:"text",
            success: function(rep){
                reponseAddComment(rep, id);
            },
            error: function(XHR , textStatus , errorThrown){
                alert(textStatus);
            } 
        })
	}
    else
	{
    	alert("Vous ne pouvez pas ajouter un commentaire vide");
	}
}

function reponseAddComment(rep, id)
{
	var repD = JSON.parse(rep, revival);   
    if (repD.status == "ko")
	{
    	alert(repD.message);
	}
    else
	{
    	$("#input_comment_"+id).val("");
    	var new_comment = repD.added_comment;
        env.messages[id].comments.push(new_comment)			// On ajoute le commentaire au message
        
        var el = $("#message_"+id+" .comments");
        el.prepend(new_comment.getHTML());
	}
}

function mainConnexion()
{
    var login = document.getElementById("login_co").value;
    var pwd = document.getElementById("pwd_co").value;
    
    if(document.getElementById("checkbox").checked){
        console.log("checked");
        connecte(login , pwd , 1);
    }
    else  {
        console.log("nope");
        connecte(login , pwd , 0);
    }
}


function connecte (login, pwd , save)
{
    if(!noConnection)
        $.ajax({
            type: "POST",
            url: "user/login",
            data: "login="+login+"&pwd="+pwd+"&root="+save,
            dataType:"text",
            success: function(rep){
                reponseConnexion(rep);
            },
            error: function(XHR , textStatus , errorThrown){
                alert(textStatus);
            } 
        })
    else {
        console.log("Erreur connexion : Poto ca a pa marché");
    }
}

function reponseConnexion(rep){
	var repD = JSON.parse(rep);
    if(repD.status == "ko"){
        $("#error_connexion").html("Connexion error : " + repD.message);
    }
    else{
    	$("#login_co").val("");
        $("#pwd_co").val("");
        
        env.key = repD.key;
        env.fromId = repD.id;
        env.login = repD.login;
        env.follows[repD.id] =  repD.friends;		// On ajoute les amis de l'utilisateur
        
        makeMainPanel();
    }
}

/* Inscription */
function verif_egalite_pwd(){
    if( document.getElementById("pwd").value===""||  document.getElementById("pwd2").value==="")
    {
        $("#mdp_error").html("Attention, tout les champs doivent etre remplis !!"); 
        return false;
    }
    else if (document.getElementById("pwd").value != document.getElementById("pwd2").value ){
        $("#mdp_error").html("Attention, les champs ne sont pas corrects !!"); 
        return false;
    }
    else {
        document.getElementById("mdp_error").innerHTML="";
        return true;
    }
}



function mainInscription()
{
    if(verif_egalite_pwd())
    {
        var login = document.getElementById("login_ins").value;
        var pwd = document.getElementById("pwd").value;
        var nom = document.getElementById("first_input_inscription").value;
        var prenom = document.getElementById("prenom_ins").value;
        var mail = document.getElementById("input_mail").value;

        inscrire(nom , prenom ,login , pwd , mail);
    }
}


function  inscrire (nom , prenom ,login , pwd , mail)
{
    console.log("Nom : " + nom);
    console.log("Préom : " + prenom);
    console.log("Login : " + login);
    console.log("Pwd : " + pwd);
    console.log("Mail : " + mail);
	
    if(!noConnection){
        $.ajax({
            type: "POST",
            url: "user/createUser",
            data: "nom="+nom+"&prenom="+prenom+"&login="+login+"&pwd="+pwd+"&email="+mail,
            dataType:"text",
            success: function(rep){   
                reponseInscription(rep);
                connecte(login , pwd , 0);
            },
            error: function(XHR , textStatus , errorThrown){
                alert(textStatus);
            } 
        })
    }
    else {
       console.log("Erreur connexion : Poto ca a pa marché");
    }
}

function reponseInscription(rep){
    var repD = JSON.parse(rep);
    if(repD.status == "ko"){
        $("#error_Inscription").html("Inscription error : " + repD.message);
    }
    else{
       env.key = repD.key;
        env.fromId = repD.id;
        env.login = repD.login;
        env.follows[repD.id] =  repD.friends;		// On ajoute les amis de l'utilisateur
        
    }
}

/* Mot de passe oublié */
function verif_egalite_mail(){
	var valueMail1 = $("#mail_forgotten").val();
	var valueMail2 = $("#mail2_forgotten").val();
	
	console.log(valueMail1)
	console.log(valueMail2)
	
    if( valueMail1 ===  "" ||  valueMail2 === "")
    {
        $("#error_vide").html("Attention, tout les champs doivent etre remplis !!"); 
        return false;
    }
    else if (valueMail1 != valueMail2 )
    {
        $("#error_vide").html("Attention, les champs ne sont pas corrects !!"); 
        return false;
    }
    else {
        document.getElementById("error_vide").innerHTML="";
        
        $("#dialog").dialog( "open" );
        $.ajax({
            type: "POST",
            url: "user/forgottenPassword",
            data: "mail1="+valueMail1+"&mail2="+valueMail2,
            dataType:"text",
            success: function(rep){
                reponseForgottenPwd(rep, valueMail1);
            },
            error: function(XHR , textStatus , errorThrown){
                alert(textStatus);
                $("#dialog").dialog("close");
            } 
        })
        return true;
    }
}

function reponseForgottenPwd(rep, mail)
{
	var repD = JSON.parse(rep);
	console.log(rep);
	console.log(repD);
	if (repD.status == "ko")
	{
		$("#dialog").dialog("close");
		$("#error_vide").html(repD.message);
	}
	else
	{
		$("#dialog").dialog("close");
    	$("#mail_forgotten").val("");
    	$("#mail2_forgotten").val("");
		alert("Votre nouveau mot de passe à été envoyé avec succès à l'adresse mail \"" + mail +"\".");
	}
}

/* 			Init - tous 		*/
/* ############################ */
/* Classes JavaScript du projet */
/* ############################ */

///////////////////////////////
//// Les classes du projet ////
///////////////////////////////

// Classe Message
function Message(id , auteur , texte , comments , date){
    this.id = id;
    this.auteur=auteur;
    this.date=date;
    this.texte=texte;
    if(comments == undefined){
        comments=[];
    }
    this.comments=comments;
}

Message.prototype.getHTML = function(){
    s="<div id=\"message_"+this.id+"\" class=\"msg\" >"+
        "<div>"+
            "<div style=\"display:inline-block\">"+this.auteur.getHTML()+"</div>"+
            "<div style=\"float:right;\">"+dateToString(this.date)+"</div>"+
        "</div>"+
        "<div>"+escapeHTMLEncode(this.texte)+"</div>"+
        "<div class = \"new_comment\">";
        if (env.fromId != -1){
            s += "<input type =\"text\" id=\"input_comment_"+this.id+"\" placeholder=\"Un commentaire...\"/>"+
            "<input type=\"button\" value=\"Commenter\" onclick='addComment(\""+this.id+"\")'/>";
        }
        s += "</div>" +
        "<img class='expand' style='cursor:pointer;' src='image/plus_logo.png' onclick='developpeMessage(\""+this.id+"\")'/>"+
        "<div class = \"comments\">"+
        "</div>" +
    "</div>";
    return s;
}

// Classe Commentaire
function Commentaire(id , auteur , texte , date){
    this.id = id;
    this.auteur=auteur;
    this.date=date;
    this.texte=texte;
}
Commentaire.prototype.getHTML = function(){
    s="<div id=\"commentaire_"+this.id+"\">"+
        "<div>" +
        	"<div style=\"display:inline-block\">"+this.auteur.getHTML()+"</div>" +
			"<div style=\"float:right;\">"+dateToString(this.date)+"</div>" +
		"</div>"+
        "<div style=\"display:inline-block;\">"+this.texte+"</div>"+
        "</div>";
    return s;
	
}

// Classe Auteur
function Auteur(id , login){
    this.id = id;
    this.login=login;
}
Auteur.prototype.getHTML = function(){
    s="<div id=\"auteur_"+this.id+"\" style=\"float:left;\">"+
        "<div>"+this.login+"</div>"+
        "</div>";
    return s;
}

//fonction du parser
function revival(key, value) {
    if(value.comments != undefined) {
        return new Message(value.id , value.author, value.content , value.comments , value.date);
    }
    else if(value.content != undefined){
        return new Commentaire(value.id_comment , value.author ,value.content, value.date);
    }
    if(key == "author") {
        return new Auteur(value.idAuthor , value.login);
    }
    if(key=="date") {
        return value;
    }
    return value;
}


////////////////////////////////
////// Fonctions pour init /////
////////////////////////////////
function init()
{
	
    noConnection = false;
    ContainerEnum = {MAIN: 0, INSCRIPTION: 1, CONNEXION: 2, FORGOTTEN_PWD: 3, PROFIL: 4};
    
    env = new Object();
    env.login="";
    env.key = "abcd";
    env.idUser = 1;
    env.fromId = -1;
    env.follows = {};
    env.containers = {};
    env.messages = {};
    env.all_users = [];
    env.currentContainer = ContainerEnum.MAIN;
    
    // Dictionnaire pour la conversion de la date
    env.monthConversion = {"Jan": "Janvier", "Feb": "Fevrier", "Mar": "Mars", "Apr": "Avril", "May": "May", "Jun": "Juin", "Jul": "Juillet", "Aug": "Aout", "Sep": "Septembre", "Oct": "Octobre", "Nov": "Novembre", "Dec": "Decembre"};
    env.dayConversion = {"Mon": "Lundi", "Tue": "Mardi", "Wed": "Mercredi", "Thu": "Jeudi", "Fri": "Vendredi", "Sat": "Samedi", "Sun": "Dimanche"};
    
    initializeContainers();
    initializeAllLogins();
    setVirtualdb();
    setUpSearchBar();

    ////////////////////////////////////
    /* Charger liste des utilisateurs */
    ////////////////////////////////////
    makeMainPanel();
}

function setVirtualdb()
{
    localdb= []
    follows = []
    /*var u1 = {"id":1 , "login":"toto"}
    var u2 = {"id":2 , "login":"titi"}
    var u3 = {"id":3 , "login":"jordan"}*/

    var u1 = new Auteur(1, "toto");
    var u2 = new Auteur(2, "tata");
    var u3 = new Auteur(3, "jordan");

    follows[1] = new Set()
    follows[1].add(2)
    follows[2] = new Set()
    follows[2].add(3)
    follows[3] = new Set()
    follows[3].add(1)

    /*com1 = new Commentaire(1, u2, "Comment ca va", new Date());
    com2 =  new Commentaire(2, u3, "Ca va bien", new Date());

    msg = new Message( 1 , u1 , "yo !" , [com1 , com2] , new Date());

    env.messages[msg.id] = msg;*/
}

function initializeAllLogins()
{
    $.ajax({
            type: "POST",
            url: "user/getAllLogins",
            dataType:"text",
            async: false,
            success: function(rep)
            {
                reponseGetLogins(rep);
            },
            error: function(XHR , textStatus , errorThrown){
                alert(textStatus);
            } 
        })
}

function initializeContainers()
{
    env.containers[ContainerEnum.MAIN] = $("#container_main");
    env.containers[ContainerEnum.CONNEXION] = $("#container_connexion");
    env.containers[ContainerEnum.INSCRIPTION] = $("#container_inscription");
    env.containers[ContainerEnum.FORGOTTEN_PWD] = $("#container_forgotten_pwd");
    env.containers[ContainerEnum.PROFIL] =  $("#container_profil");
}

function reponseGetLogins(rep)
{
    var repD = JSON.parse(rep);
    env.all_users = repD.logins;
    
}

////////////////////////////////////
// Fonctions de création de panel //
////////////////////////////////////
function makeForgottenPwdPanel(){
    $("#container").load("Forgotten_Pwd.html", function(){
        $("#mail_forgotten").focus();
    });
    $("#changableLink").attr("href", "css/Connexion.css")    
    env.currentContainer = ContainerEnum.FORGOTTEN_PWD;
}

function makeInscriptionPanel()
{
    $("#container").load("Inscription.html", function(){
        $("#first_input_inscription").focus();
        
        $("#first_input_inscription").keydown(enterHandlerInscription);
        $("#prenom_ins").keydown(enterHandlerInscription);
        $("#login_ins").keydown(enterHandlerInscription);
        $("#input_mail").keydown(enterHandlerInscription);
        $("#prenom_ins").keydown(enterHandlerInscription);
        $("#pwd").keydown(enterHandlerInscription);
        $("#pwd2").keydown(enterHandlerInscription);
    });
    $("#changableLink").attr("href", "css/Inscription.css")
    env.currentContainer = ContainerEnum.INSCRIPTION;
}

function callbackProfilPanel(){
    document.getElementById("login_profil").innerHTML=env.login;
    env.currentContainer = ContainerEnum.PROFIL;
    console.log("Je suis connecté, mon login est : " + env.login);
}


function makeProfilPanel(id){
    id = 4
    $("#container").load("Profil.html", callbackProfilPanel);
    $("#changableLink").attr("href", "css/Profil.css");
    if(id!=null){
        ajout="";
        if(id!=env.fromId){
            if(id in env.follows.id){
                ajout = "<input type=\"button\" value = \"s'abonner\" onclick=\"addFriend("+id+")\" ";
            }
            else{
                ajout += "<input type=\"button\" value = \"se désabonner\" onclick=\"removeFriend("+id+")\" ";
            }
        }
        $("#friends").html(ajout);
    }
}

function makeConnexionPanel(){
    $("#container").load("Connexion.html", function(){
        $("#login_co").focus();
        $("#login_co").keydown(enterHandlerConnexion);
        $("#pwd_co").keydown(enterHandlerConnexion);
    });
    $("#changableLink").attr("href", "css/Connexion.css");
    env.currentContainer = ContainerEnum.CONNEXION;
}

function callbackMainPanel(){
    var ajout = "";
    if (env.fromId == -1)
    {
        console.log("Je ne suis pas connecté");
        
        ajout += '<input type="button" value="Connexion" onclick="javascript:(function (){makeConnexionPanel()})()"/>';
        ajout += '<input type="button" value="Inscription" onclick="javascript:(function (){makeInscriptionPanel()})()"/>';
        
        $("#nouveau_msg").hide();
        $("#input_search_main").focus();
    }
    else
    {
        console.log("Je suis connecté, mon login est : " + env.login);
        
        ajout += '<input type="button" value="Profil" onclick="javascript:(function (){makeProfilPanel()})()"/>';
        ajout += '<input type="button" value="Déconnexion" onclick="javascript:(function (){mainDeconnexion()})()"/>';
        
        $("#new_msg").focus();
        $("#new_msg").keydown(enterHandlerAddMessage);
    }
    
    $("#input_search_main").keydown(enterHandlerSearch);
    $("#connexion").html(ajout);
    setUpMessages();
    
}

function makeMainPanel()
{
    $("#changableLink").attr("href", "css/Principale.css");
    $("#container").load("Main.html", callbackMainPanel);
    env.currentContainer = ContainerEnum.MAIN;
}

function setUpMessages(){
    var query = "";
    if(env.fromId == -1) {
        query = "key="+env.key;
    }
    else {
        query = "key="+env.key+"&amis="+getFriendList(env.fromId);
    }
    $.ajax({
        type: "POST",
        url: "message/listMessage",
        data: query,
        dataType:"text",
        success: function(rep){
            reponseSetUpMessages(rep);
        },
        error: function(XHR , textStatus , errorThrown){
            alert(textStatus);
        } 
    })
}

function reponseSetUpMessages(rep){
    var repD = JSON.parse(rep, revival);
    env.messages = {};		// On remet à zéro la liste des messages
    var messages = repD.list_message;
    for(var index in messages) {
        env.messages[messages[index].id] = messages[index];
    }
    displayMessages();
}

function displayMessages(){
    for(index in env.messages){
        $("#messages").append(env.messages[index].getHTML());
        $("#input_comment_"+env.messages[index].id).keydown(enterHandlerAddComment);
    }
}

function getFriendList(id){
    res = ""
    for(var index in env.follows[id]){
        res += env.follows[id][index]+"-";
    }
    return res;
}

function mainDeconnexion()
{
	alert("Bonjour !");
    if(env.fromId==-1){
        console.log("erreur deconnexion")
    }
    else{
        $.ajax({
            type: "POST",
            url: "user/logout",
            data: "key="+env.key,
            dataType:"text",
            success: function(rep){
                reponseLogout(rep);
            },
            error: function(XHR , textStatus , errorThrown){
                alert(textStatus);
            } 
        })
    }
}

function reponseLogout(rep){
    var repD = JSON.parse(rep, revival);
    if(repD.status == "ko"){
        console.log("erreur deconnexion : " + repD.message);
    }
    else{
        env.fromId=-1;
        makeMainPanel();
    }
}


////////////////////////////////////////
// Gestion affichage des commentaires //
////////////////////////////////////////

function developpeMessage(id)
{
    var m = env.messages[id];
    var el = $("#message_"+id+" .comments");
    
    el.html("");						// Au cas ou un nouveau commentaire aurait été écrit et affiché
    
    for(var index in m.comments)
    {
        var c = m.comments[index];
        el.append(c.getHTML());
    }

    $("#message_"+id+" img" ).replaceWith("<img style=\"cursor:pointer;\" src=\"image/minus_logo.png\" onclick=\"javascript:replieMessage('"+id+"')\"/>")
}

function replieMessage(id)
{
    var el = $("#message_"+id+" .comments");
    el.html("");
    $("#message_"+id+" img" ).replaceWith("<img style=\"cursor:pointer;\" src=\"image/plus_logo.png\" onclick=\"javascript:developpeMessage('"+id+"')\"/>")
}

//////////////////////////////////////
// Gestion de la barre de recherche //
//////////////////////////////////////
function setUpSearchBar()
{
    $("#input_search_main").autocomplete({
        source: env.all_users
    });
}

function doSearch()
{
    console.log("Search");
    var input = $("#input_search_main").val();
    console.log("Input : " + input);
}

////////////////////////////////////
////////// enterHandler ////////////
////////////////////////////////////

function enterHandlerConnexion(event)
{
    if(event.keyCode == 13){
        mainConnexion()
    }	
}

function enterHandlerSearch(event)
{
    if(event.keyCode == 13){
        doSearch();
    }	
}

function enterHandlerAddMessage(event)
{
    if(event.keyCode == 13){
        mainAddMessage();
    }	
}

function enterHandlerAddComment(event)
{
	// Ici il faut obtenir l'id du message sur lequel on souhaite ajouter le commentaire
	// L'id est présent sur le noeud source de l'évenement
	var idMessage = event.currentTarget.id.split("_")[2];
	
	if(event.keyCode == 13)
	{
		if(idMessage != undefined)
		{
	        addComment(idMessage);
		}
		else
		{
			alert("Vous ne pouvez pas publier un message vide")
		}
        
	}
}

function enterHandlerInscription(event)
{
     if(event.keyCode == 13){
         mainInscription()
     }	
}

// Retourne une chaine de caaractère pouvant être affichée dans des balises HTMLs
function escapeHTMLEncode(str) {
    var div = document.createElement("div");
    var text = document.createTextNode(str);
    div.appendChild(text);
    return div.innerHTML;
}

function dateToString(date){
	var splitedDate = date.split(" ");
	console.log(splitedDate);
	
	var s = "";
	s += env.dayConversion[splitedDate[0]]+" ";				// Le jour
	s += splitedDate[2]+" ";								// Le numéro
	s += env.monthConversion[splitedDate[1]]+" ";			// Le mois
	s += splitedDate[5]+" à ";								// L'année
	
	s += splitedDate[3];									// L'heure
	
	return s;
}