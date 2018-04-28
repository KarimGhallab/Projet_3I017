/* Ajout de image de profil */

function uploadImage(login, pwd, blobFile) {
    var formData = new FormData();
    formData.append("fileToUpload", blobFile);
    console.log("undefined :: "+login);
    formData.append("login", login);
    console.log(formData);
    $.ajax({
       url: "user/uploadImage",
       type: "POST",
       data: formData ,
       processData: false,
       contentType: false,
       success: function(response) {
           reponseUploadImage(response, login, pwd);
       },
       error: function(jqXHR, textStatus, errorMessage) {
           console.log(errorMessage); // Optional
       }
    });
}

function reponseUploadImage(rep, login, pwd){
	var repD = JSON.parse(rep);
	console.log("reponseUploadImage : ");
	console.log(repD);
	
	if(repD.status == "ko"){
		alert(repD.message)
	}
	else{
		connecte(login , pwd , 0);
	}
		
	
}
/* Suppression de message */
function removeMessage(id){
	var query = "message="+id+"&key="+env.key
	
	$.ajax({
	       url: "message/removeMessage",
	       type: "POST",
	       data: query ,
	       success: function(response) {
	           reponseRemoveMessage(response);
	       },
	       error: function(jqXHR, textStatus, errorMessage) {
	           console.log(textStatus); // Optional
	       }
	    });
}
function reponseRemoveMessage(response){
	var repD = JSON.parse(response);
	if(repD.status == "ko"){
		alert("erreur lors de la suppression du message: "+repD.message);
	}
	else{
		 refreshMessages();
		 setUpStats();
	}
}

/* Suppression de commentaire*/
function removeComment(id , idMessage){
	var query = "idCommentaire="+id+"&key="+env.key+"&idMessage="+idMessage;
	
	$.ajax({
	       url: "comment/removeComment",
	       type: "POST",
	       data: query ,
	       success: function(response) {
	           reponseRemoveComment(response , id);
	       },
	       error: function(jqXHR, textStatus, errorMessage) {
	           console.log(textStatus); // Optional
	       }
	    });
}
function reponseRemoveComment(response , id){
	var repD = JSON.parse(response);
	if(repD.status == "ko"){
		alert("erreur lors de la suppression du commentaire : "+repD.message);
		if (!isConnexion(repD))
		{
    		alert("Vous avez été inactif trop longtemps, vous allez être déconnecté")
    		mainDeconnexion();
		}
	}
	else{
		 refreshMessages();
	}
}

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
    	if (!isConnexion(repD))
		{
    		alert("Vous avez été inactif trop longtemps, vous allez être déconnecté")
    		mainDeconnexion();
		}
	}
    else
	{
    	$("#new_msg").val("");
    	var message = repD.added_message;
        env.messages[repD.added_message.id] = message;
        
        $("#messages").prepend(message.getHTML());
        $("#input_comment_"+message.id).keydown(enterHandlerAddComment);
        
        // Scroll jusqu'au top de la page
        $("html, body").animate({ scrollTop: 0 }, "slow");
        setUpStats();
	}
}


function addComment(id){
	
    var value = $("#input_comment_"+id).val();
    if (value != "")
	{
    	$.ajax({
            type: "POST",
            url: "comment/addComment",
            data: "key="+env.key+"&idMessage="+id+"&commentaire="+encodeInput(value),
            dataType:"text",
            success: function(rep){
                reponseAddComment(rep, id);
            },
            error: function(XHR , textStatus , errorThrown){
                alert(textStatus);
            } 
        });
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
    	if (!isConnexion(repD))
		{
    		alert("Vous avez été inactif trop longtemps, vous allez être déconnecté")
    		mainDeconnexion();
		}
	}
    else
	{
    	$("#input_comment_"+id).val("");
    	var new_comment = repD.added_comment;
        env.messages[id].comments.push(new_comment)			// On ajoute le commentaire au message
        
        var el = $("#message_"+id+" .comments");
        el.append(new_comment.getHTML());
        $("#message_"+id+" .nbr_comments" ).html(env.messages[id].comments.length+" Comments");
        refreshReact(env.fromId);
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
	login = encodeInput(login);
	pwd = encodeInput(pwd);
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
    	env.fromId = -1;
    }
    else{
    	$("#login_co").val("");
        $("#pwd_co").val("");
        
        env.key = repD.key;
        env.fromId = repD.id;
        env.login = repD.login;
        env.followsId[repD.id] =  repD.friendsId;		// On ajoute les amis de l'utilisateur
        env.followsLogin[repD.id] =  repD.friendsLogin;		// On ajoute les amis de l'utilisateur
        
        env.fromMessage = 0;
        makeMainPanel();
    }
}

function mainChangePwd(){
	var ancien = $("#ancien_pwd").val();
	var pwd1 = $("#pwd").val();
	var pwd2 = $("#pwd2").val();
	
	ancien = encodeInput(ancien);
	pwd1 = encodeInput(pwd1);
	pwd2 = encodeInput(pwd2);
	
	 $.ajax({
         type: "POST",
         url: "user/changePwd",
         data: "ancien="+ancien+"&pwd1="+pwd1+"&pwd2="+pwd2+"&key="+env.key,
         dataType:"text",
         success: function(rep){
             reponseChangePwd(rep);
         },
         error: function(XHR , textStatus , errorThrown){
             alert(textStatus +" " + errorThrown);
         } 
     })
}

function reponseChangePwd(rep){
	
	var repD = JSON.parse(rep);
	if(repD.status == "ko"){
		$("#error_change_mdp").html(repD.message);
	}
	else{
		alert("Votre mot de passe à été modifié avec succès !");
		makeConnexionPanel();
	}
}

/* Inscription */
function verif_egalite_pwd_inscription(){
    if( document.getElementById("pwd").value===""||  document.getElementById("pwd2").value===""){
    	$("#error_inscription").html("Attention, tout les champs doivent etre remplis !!"); 
        return false;
    }
    else if (document.getElementById("pwd").value != document.getElementById("pwd2").value ){
    	$("#error_inscription").html("Attention, les deux mot de passe ne correspondent pas"); 
        return false;
    }
    else {
    	$("#error_inscription").html("");
        return true;
    }
}
/* Pour le changement de mot de passe */
function verif_egalite_pwd_change_mdp(){
    if( document.getElementById("pwd").value===""||  document.getElementById("pwd2").value==="")
    {
    	$("#error_change_mdp").html("Attention, tout les champs doivent etre remplis !!"); 
        return false;
    }
    else if (document.getElementById("pwd").value != document.getElementById("pwd2").value ){
    	$("#error_change_mdp").html("Attention, les deux mot de passe ne correspondent pas"); 
        return false;
    }
    else {
    	$("#error_change_mdp").html("");
        return true;
    }
}

function validateMail(mail){
	var re = /^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/
	return re.test(mail);
}


function mainInscription()
{
    if(verif_egalite_pwd_inscription())
    {
    	if (validateMail(document.getElementById("input_mail").value))
		{
    		$("#error_inscription").html("");
    		
    		var login = document.getElementById("login_ins").value;
            var pwd = document.getElementById("pwd").value;
            var nom = document.getElementById("first_input_inscription").value;
            var prenom = document.getElementById("prenom_ins").value;
            var mail = document.getElementById("input_mail").value;
            
            inscrire(nom , prenom ,login , pwd , mail);
		}
    	else{
    		$("#error_inscription").html("L'adresse mail n'est pas correcte");
    	}
    }
}


function  inscrire (nom , prenom ,login , pwd , mail)
{
	login = encodeInput(login);
	nom = encodeInput(nom);
	prenom = encodeInput(prenom);
	pwd = encodeInput(pwd);
	mail = encodeInput(mail);
	
    if(!noConnection){
        $.ajax({
            type: "POST",
            url: "user/createUser",
            data: "nom="+nom+"&prenom="+prenom+"&login="+login+"&pwd="+pwd+"&email="+mail,
            dataType:"text",
            success: function(rep){
                reponseInscription(rep , login , pwd);
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

function reponseInscription(rep, login, pwd){
    var repD = JSON.parse(rep);
    if(repD.status == "ko"){
        $("#error_inscription").html("Inscription error : " + repD.message);
    }
    else{
    	env.key = repD.key;
        env.fromId = repD.id;
        env.login = repD.login;
        env.followsId[repD.id] =  repD.friends;		// On ajoute les amis de l'utilisateur
        
        var blobFile = $('#filechooser')[0].files[0]
        if (blobFile != undefined){
        	uploadImage(login, pwd, blobFile);
        }
        else {
        	connecte(login , pwd , 0);
    	}
    }
}

/* Mot de passe oublié */
function verif_egalite_mail(){
	var valueMail1 = $("#mail_forgotten").val();
	var valueMail2 = $("#mail2_forgotten").val();
	
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
        valueMail1 = encodeInput(valueMail1);
        valueMail2 = encodeInput(valueMail2);
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

Message.prototype.getHTML = function() {
    s="<div id=\"message_"+this.id+"\" class=\"msg\" >"+
        "<div>"+
            "<div onclick=\"makeProfilPanel('"+this.auteur.login+"')\" style=\"display:inline-block;\" class=\"auteur\">"+this.auteur.getHTML()+"</div>"+
            "<div style=\"float:right;\" class=\"date\">"+dateToString(this.date)+"</div>"+
        "</div>"+
        "<div class=\"text_msg\">"+escapeHTMLEncode(this.texte);
    	if (env.fromId == this.auteur.id){		// Je suis l'auteur du message
    		s += " <img class='delete_button' style='cursor:pointer;margin-left:10px;transform: translate(0 , 3px);' src='image/delete_logo.png' onclick='removeMessage(\""+this.id+"\")'/>";
    	}
    	s += "</div>"+
        "<div class = \"new_comment\">";

    	// On ne peux commenter que si on est connecter et que le message est le notre ou bien celui d'un amis
        if (env.fromId != -1)
        {
        	var res = $.inArray(this.auteur.id, env.followsId[env.fromId]);
        	
        	if ((env.fromId == this.auteur.id) || (env.followsId[env.fromId].includes(parseInt(this.auteur.id)))){
        		s += "<input id=\"input_comment_"+this.id+"\" placeholder=\"Un commentaire...\" class=\"input_comment\"/>"+
                "<input type=\"button\" value=\"Commenter\" onclick='addComment(\""+this.id+"\")'/>";
        	}
        }
        s += "</div>" +
        "<label  style=\"color: #0c6cd7;\" class=\"nbr_comments\">"+env.messages[this.id].comments.length+" Comments</label><img class='expand' style='cursor:pointer; position: relative ; left: 80%;' src='image/plus_logo.png' onclick='developpeMessage(\""+this.id+"\")'/>"+
        "<div class = \"comments\">"+
        "</div>" +
    "</div>";
    return s;
}

// Classe Commentaire
function Commentaire(id , auteur , texte , date , idMessage){
    this.id = id;
    this.auteur=auteur;
    this.date=date;
    this.texte=texte;
    this.idMessage = idMessage;
}
Commentaire.prototype.getHTML = function () {
    s="<div id=\"commentaire_"+this.id+"\" style=\"margin-top:5px;\">"+
        "<div>" +
        	"<div style=\"display:inline-block;\" class=\"auteur\" onclick=\"makeProfilPanel('"+this.auteur.login+"')\">"+this.auteur.getHTML()+"</div>" +
			"<div style=\"float:right;\" class=\"date\">"+dateToString(this.date)+"</div>" +
		"</div>"+
        "<div class=\"text_comment\">"+this.texte;
    if (env.fromId == this.auteur.id){		// Je suis l'auteur du commentaire
		s += " <img class='delete_button' style='cursor:pointer;margin-left:10px' src='image/delete_logo.png' onclick='removeComment(\""+this.id+"\" , \""+this.idMessage+"\")'/>";
	}
	s += "</div>"+
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
        return new Commentaire(value.id_comment , value.author ,value.content, value.date , value.idMessage);
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
    
    env = new Object();
    env.login="";
    env.key = "abcd";
    env.fromId = -1;
    env.followsId = {};
    env.followsLogin = {};
    env.containers = {};
    env.messages = {};
    env.all_users = [];
    env.users=[];
    env.fromMessage = 0;
    env.nbMessage = 8;
    env.keepListingMessage = true;
    env.refreshing = false;
    
    // Dictionnaire pour la conversion de la date
    env.monthConversion = {"Jan": "Janvier", "Feb": "Fevrier", "Mar": "Mars", "Apr": "Avril", "May": "May", "Jun": "Juin", "Jul": "Juillet", "Aug": "Aout", "Sep": "Septembre", "Oct": "Octobre", "Nov": "Novembre", "Dec": "Decembre"};
    env.dayConversion = {"Mon": "Lundi", "Tue": "Mardi", "Wed": "Mercredi", "Thu": "Jeudi", "Fri": "Vendredi", "Sat": "Samedi", "Sun": "Dimanche"};

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
}

////////////////////////////////////
// Fonctions de création de panel //
////////////////////////////////////
function makeChangePwdPanel(){
	 $("#container").load("Change_Pwd.html", function(){
	        $("#ancien_pwd").focus();
	    });
	    $("#changableLink").attr("href", "css/Connexion.css")    
}

function makeForgottenPwdPanel(){
    $("#container").load("Forgotten_Pwd.html", function(){
        $("#mail_forgotten").focus();
    });
    $("#changableLink").attr("href", "css/Connexion.css")    
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
}

function makeProfilPanel (login){
	if (login != "")
	{
		login = encodeInput(login);
		$.ajax({
	        type: "POST",
	        url: "user/getProfilFromLogin",
	        data: "loginUser="+login,
	        dataType:"text",
	        success: function(rep){
	            reponseProfil(rep, login);
	        },
	        error: function(XHR , textStatus , errorThrown){
	            alert(textStatus);
	        } 
	    })
	}
}


function addFriend(id){
    
    $.ajax({
        type: "POST",
        url: "friend/addFriend",
        data: "idFriend="+id+"&key="+env.key,
        dataType:"text",
        success: function(rep){
            reponseAddFriend(rep , id);
        },
        error: function(XHR , textStatus , errorThrown){
            alert(textStatus);
        } 
    })
}
function reponseAddFriend(rep , id){
    repD = JSON.parse(rep);
    if(repD.status == "ko"){
        console.log("erreur :" + rep.message );
    }
    else{
    	console.log("addFriend :");
    	console.log(repD);
    	env.followsId[env.fromId].push(id);
    	env.followsLogin[env.fromId].push(repD.login_friend.login);
        makeProfilPanel(env.all_users[id]);
    }
}
function removeFriend(id){
    

    $.ajax({
        type: "POST",
        url: "friend/removeFriend",
        data: "idFriend="+id+"&key="+env.key,
        dataType:"text",
        success: function(rep){
            reponseRemoveFriend(rep , id);
        },
        error: function(XHR , textStatus , errorThrown){
            alert(textStatus);
        } 
    })
}

function reponseRemoveFriend(rep , id){
    repD = JSON.parse(rep);
    if(repD.status == "ko"){
        console.log("erreur :" + rep.message );
    }
    else{
    	var i = env.followsId[env.fromId].indexOf(id);
        if(i != -1) {
            env.followsId[env.fromId].splice(i, 1);
            env.followsLogin[env.fromId].splice(i, 1);
        }
        makeProfilPanel(env.all_users[id]);
    }
}
function mainProfil(login, id, path){
	$("#login_profil").html(login);
	
	// Affiche des boutons dans le header
    var ajout = "";
    if (env.fromId == -1)
    {   
        ajout += '<input type="button" value="Connexion" onclick="javascript:(function (){makeConnexionPanel()})()"/>';
        ajout += '<input type="button" value="Inscription" onclick="javascript:(function (){makeInscriptionPanel()})()"/>';
    }
    else
    {   
        ajout += '<input type="button" value="Profil" onclick="javascript:(function (){makeProfilPanel(\''+env.login+'\')})()"/>';
        ajout += '<input type="button" value="Déconnexion" onclick="javascript:(function (){mainDeconnexion()})()"/>';
    }
    
    $("#input_search").focus();
    $("#input_search").keydown(enterHandlerSearch);
    $("#connexion_profil").html(ajout);
    
    // Gestion du bouton pour raffraichir les messages
    var toAddButton = '<input type="submit" value="Rafraîchir les messages"  onclick="refreshMessages('+id+')" class="refresh_button"/>'
    $("#principale").append(toAddButton);
    
    // gestion de la fonctionnalité follow/unfollow
    ajout="";
    if(env.fromId!=-1){
        if(id!=env.fromId){
            if(env.followsId[env.fromId] == [] ){
                ajout = "<input type=\"button\" id=\"friends\" value = \"S'abonner\" onclick=\"addFriend("+id+")\" >";
            }
            else if(env.followsId[env.fromId].includes(id)){
                ajout = "<input type=\"button\" id=\"friends\" value = \"Se désabonner\" onclick=\"removeFriend("+id+")\" >";
            }
            else{                
                ajout = "<input type=\"button\" id=\"friends\" value = \"S'abonner\" onclick=\"addFriend("+id+")\" >";
            }
        }
        // On affiche la liste des amis
        else{
        	if (env.followsLogin[env.fromId].length > 0){
        		$("#liste_follow").html("Personnes que vous suivez : ");
        		for(var index in env.followsLogin[env.fromId]){
            		$("#liste_follow").append(friendLoginToHTML(env.followsLogin[env.fromId][index]));
            		$("#liste_follow").append(", ");
            	}
        		$("#liste_follow").html( $("#liste_follow").html().slice(0, -1) );
    		}
        	else{
        		$("#liste_follow").html("Vous ne suivez actuellement personne");
        	}
        }
        document.getElementById("profil").innerHTML += ajout;
    }
    
    
    ///////////////////////////////////////
    ///////// Gestion de l'image //////////
    // ERREUR AFFICHAGE D'UN FICHIER LOCAL/
    ///////////////////////////////////////
    /*console.log("path : " + path)
    $("#profil_img").attr("src", "file:/"+path);*/
}

function reponseProfil(rep, login){
	var repD = JSON.parse(rep);
	if(repD.status == "ok")
	{
		$("#changableLink").attr("href", "css/Profil.css");
	    $("#container").load("Profil.html", function (){
	    	mainProfil(login, repD.idUser, repD.path);
	    	
	    	env.fromMessage = 0;
	    	setUpMessages(repD.idUser);
	    	setUpStats();
	    });
	}
	else
	{
		alert(repD.message);
	}
}

function friendLoginToHTML(login){
	return "<span onclick=\"makeProfilPanel('"+login+"')\" class=\"auteur\">"+login+"</span>";
}

function makeConnexionPanel(){
    $("#container").load("Connexion.html", function(){
        $("#login_co").focus();
        $("#login_co").keydown(enterHandlerConnexion);
        $("#pwd_co").keydown(enterHandlerConnexion);
    });
    $("#changableLink").attr("href", "css/Connexion.css");
}

function callbackMainPanel(){
    var ajout = "";
    if (env.fromId == -1)
    {
        ajout += '<input type="button" value="Connexion" onclick="javascript:(function (){makeConnexionPanel()})()"/>';
        ajout += '<input type="button" value="Inscription" onclick="javascript:(function (){makeInscriptionPanel()})()"/>';
        
        $("#nouveau_msg").hide();
        $("#input_search").focus();
    }
    else
    {   
        ajout += '<input type="button" value="Profil" onclick="javascript:(function(){makeProfilPanel(\''+env.login+'\')})()"/>'
        ajout += '<input type="button" value="Déconnexion" onclick="javascript:(function (){mainDeconnexion()})()"/>'
        ajout += '<input type="button" value="Changer password" onclick="javascript:(function (){makeChangePwdPanel()})()"/>';
        
        $("#new_msg").focus();
        $("#new_msg").keydown(enterHandlerAddMessage);
    }
    
    $("#input_search").keydown(enterHandlerSearch);
    $("#connexion").html(ajout);
    env.fromMessage = 0;
    setUpMessages();
    setUpStats();
    
}

function makeMainPanel()
{
    $("#changableLink").attr("href", "css/Principale.css");
    $("#container").load("Main.html", callbackMainPanel);
}

function setUpMessages(id){
	env.keepListingMessage = false;
	var query = "";
	if (id != undefined){		// On affiche les message de l'id
		query = "idUser="+id+"&from="+env.fromMessage+"&nbMessage="+env.nbMessage+"&amis="+[];
	}
	else if(env.fromId == -1) {	// On affiche tous les messages
        query = "idUser="+env.fromId+"&from="+env.fromMessage+"&nbMessage="+env.nbMessage;
    }
    else {		// On affiche les message de l'utilisateur et de ses amis
    	query = "idUser="+env.fromId+"&from="+env.fromMessage+"&nbMessage="+env.nbMessage+"&amis="+getFriendList(env.fromId);
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
    if (!isConnexion(repD))
	{
		alert("Vous avez été inactif trop longtemps, vous allez être déconnecté")
		mainDeconnexion();
	}
    if (repD.status != "ko"){
    	if (env.fromMessage == 0){
        	env.messages = {};		// On remet à zéro la liste des messages
        	$("#messages").html("");
        }
        env.fromMessage += env.nbMessage;
        var messages = repD.list_message;
        var i;
        for(i=0; i< messages.length-1; i++) {		// Le dernier élément indique s'il reste un ou des messages à afficher
    		env.messages[messages[i].id] = messages[i];
        }
        env.keepListingMessage = (messages[i].end == "yes");
        messages.pop();
        displayMessages(messages);
    }
    else{
    	alert(repD.message);
    }
}

function displayMessages(messages){
	
    for(var index in messages){
        $("#messages").append(messages[index].getHTML());
        $("#input_comment_"+messages[index].id).keydown(enterHandlerAddComment);
    }
}

function setUpStats(){
	$.ajax({
        type: "POST",
        url: "user/listStats",
        data: "idUser="+env.fromId,
        dataType:"text",
        success: function(rep){
            reponseSetUpStats(rep);
        },
        error: function(XHR , textStatus , errorThrown){
            alert(textStatus);
        } 
    })
	
}

function reponseSetUpStats(rep){
	var repD = JSON.parse(rep);
	if (repD.status == "ko"){
		console.log("error set up stats : " + repD.message);
	}
	else{
		console.log("Liste stats");
		console.log(repD);
		$("#stat").html("");
		
		for (var index in repD.stats){
			$("#stat").append(statToHTML(repD.stats[index].nomStat, repD.stats[index].valeurStat));
		}
	}
}

function statToHTML(name, value){
	return "<div> "+name+" : "+value+"</div>";
}

function getFriendList(id){
    res = ""
    for(var index in env.followsId[id]){
        res += env.followsId[id][index]+"-";
    }
    return res;
}

function mainDeconnexion()
{
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
                //alert("error logout : " + textStatus + errorThrown);
            } 
        })
    }
}

function reponseLogout(rep){
    var repD = JSON.parse(rep, revival);
    if(repD.status == "ko"){
        alert(repD.message);
    }
    else{
        env.fromId=-1;
        makeMainPanel();
    }
}

function isConnexion(repD){
	if(repD.status == "ko" && repD.code == 1005){
		return false;
	}
	else{
		return true;
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

    $("#message_"+id+" .expand" ).replaceWith("<img style=\"cursor:pointer; position: relative ; left: 80%;\" class=\"expand\" src=\"image/minus_logo.png\" onclick=\"javascript:replieMessage('"+id+"')\"/>")
    $("#message_"+id+" .nbr_comments" ).html(env.messages[id].comments.length+" Comments")
}

function replieMessage(id)
{
    var el = $("#message_"+id+" .comments");
    el.html("");
    $("#message_"+id+" .expand" ).replaceWith("<img style=\"cursor:pointer;position: relative ; left: 80%;\" class=\"expand\" src=\"image/plus_logo.png\" onclick=\"javascript:developpeMessage('"+id+"')\"/>")
     $("#message_"+id+" .nbr_comments" ).html(env.messages[id].comments.length+" Comments")
}

function refreshMessages(id){
	env.fromMessage = 0;
	env.refreshing = true
		$("html").animate({ scrollTop: 0 }, "fast", function(){
			refreshReact(id)
		});
}

function refreshReact(id){
	if (id == undefined){
		setUpMessages();
	}
	else{
		setUpMessages(id);
	}
	env.refreshing = false
}

//////////////////////////////////////
// Gestion de la barre de recherche //
//////////////////////////////////////
function doSearch(target)
{
    var input = document.getElementById("input_search").value;
    makeProfilPanel(input);
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

// Retourne une chaine de caractère pouvant être affichée dans des balises HTMLs
function escapeHTMLEncode(str) {
    var div = document.createElement("div");
    var text = document.createTextNode(str);
    div.appendChild(text);
    return div.innerHTML;
}

// Retourne la date au format Jour Mois Année hh:mm:ss
function dateToString(date){
	var splitedDate = date.split(" ");
	
	var s = "";
	s += env.dayConversion[splitedDate[0]]+" ";				// Le jour
	s += splitedDate[2]+" ";								// Le numéro
	s += env.monthConversion[splitedDate[1]]+" ";			// Le mois
	s += splitedDate[5]+" à ";								// L'année
	
	s += splitedDate[3];									// L'heure
	
	return s;
}

function encodeInput(string){
	var tmp = escapeHTMLEncode(string);
	var tmp2 =  encodeURIComponent(tmp);
	return tmp2;
}