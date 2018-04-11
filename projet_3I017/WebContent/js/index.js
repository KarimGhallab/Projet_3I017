/* Ajout de message */
$("#new_msg").keydown(enterHandler);

function enterHandler(event)
{
        if(event.keyCode == 13){		// Touche "entrée"
            mainAddMessage();
        }	
}

function mainAddMessage()
{
    var input = $("#new_msg").val();
    if (input != ""){
        addMessage(input);	
    }
    else
        alert("Vous ne pouvez pas publier un message vide")
}

function addMessage(message)
{
    //var editedMessage = escapeHTMLEncode(message);
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
    repD = JSON.parse(rep, revival);
    var message = repD.added_message;
    env.messages[repD.added_message.id] = message;
    
    $("#messages").prepend(message.getHTML());
}


function addComment(id){
    var value = $("#input_comment").val();
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

function reponseAddComment(rep, id){
    repD = JSON.parse(rep, revival);
    env.messages[id].push(repD.addComment);
    console.log(rep);
    console.log(repD);
}

/* Connexion */
$("#login_co").keydown(enterHandler);
$("#pwd_co").keydown(enterHandler);

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

function enterHandler(event)
{
        if(event.keyCode == 13){		// Touche "entrée"
            mainConnexion()
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
        alert(repD.status);
        console.log("status : " + repD.status);
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
        $("#mdp_error").html("attention, tout les champs doivent etre remplis !!"); 
        return false;
    }
    else if (document.getElementById("pwd").value != document.getElementById("pwd2").value ){
        $("#mdp_error").html("attention, les champs ne sont pas corrects !!"); 
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

function enterHandler(event)
{
     if(event.keyCode == 13){		// Touche "entrée"
         mainInscription()
     }	
}


function  inscrire (nom , prenom ,login , pwd , mail)
{
    console.log(pwd);
    console.log(prenom);
    console.log(nom);
    console.log(login);
    console.log(mail);
    
    if(!noConnection){
        console.log("inscrire")
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
    if( document.getElementById("mail_forgotten").value===  ""||  document.getElementById("mail2_forgotten").value==="")
    {
        $("#error_vide").html("attention, tout les champs doivent etre remplis !!"); 
        return false;
    }
    else if (document.getElementById("mail_forgotten").value != document.getElementById("mail2_forgotten").value )
    {
        $("#error_vide").html("attention, les champs ne sont pas corrects !!"); 
        return false;
    }
    else {
        document.getElementById("error_vide").innerHTML="";
        return true;
    }
}

/* Init - tous */
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
        "<img class='expand' style='cursor:pointer;' src='image/plus_logo.png' onclick='developpeMessage(\""+this.id+"\")'/>"+
        "<div>"+
            "<div>"+this.auteur.getHTML()+"</div>"+
            "<div>"+this.date+"</div>"+
        "</div>"+
        "<div>"+escapeHTMLEncode(this.texte)+"</div>"+
        "<div class = \"new_comment\">";
        if (env.fromId != -1){
            s += "<input type =\"text\" id=\"input_comment\" placeholder=\"Un commentaire...\"/>"+
            "<input type=\"button\" value=\"Commenter\" onclick='addComment(\""+this.id+"\")'/>";
        }
        s += "</div>" +
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
        "<div><div>"+this.auteur.getHTML()+"</div><div>"+this.date+"</div></div>"+
        "<div>"+this.texte+"</div>"+
        "</div>";
    return s;
}

// Classe Auteur
function Auteur(id , login){
    this.id = id;
    this.login=login;
}
Auteur.prototype.getHTML = function(){
    s="<div id=\"auteur_"+this.id+"\">"+
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
    
    initializeContainers();
    initializeAllLogins();
    setVirtualdb();
    setUpSearchBar();
    ////////////////////////////////////
    /* Charger liste des utilisateurs */
    ////////////////////////////////////
    hideAll();
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


function hideCurrent()
{
    if(env.currentContainer == ContainerEnum.MAIN)
    {
        $("#messages").html("");        // On enlève les messages ajoutés au main
        $("#nouveau_msg").show();
    }
    else if (env.currentContainer == ContainerEnum.CONNEXION)
    {
        $("#error_connexion").html("");
        $("#login_co").val("");
        $("#pwd_co").val("");
        
    }
    console.log("Ancienne page : " + env.currentContainer);
    env.containers[env.currentContainer].hide();
}

function hideAll()
{
    for(var key in env.containers) {
        env.containers[key].hide();
    }
}

////////////////////////////////////
// Fonctions de création de panel //
////////////////////////////////////
function makeForgottenPwdPanel(){
    $("#container").load("Forgotten_pwd.html", function(){
        $("#mail_forgotten").focus();
    });
    $("#changableLink").attr("href", "css/Connexion.css")    
    env.currentContainer = ContainerEnum.FORGOTTEN_PWD;
}

function makeInscriptionPanel()
{
    $("#container").load("Inscription.html", function(){
        $("#first_input_inscription").focus();
    });
    $("#changableLink").attr("href", "css/Inscription.css")
    env.currentContainer = ContainerEnum.INSCRIPTION;
}

function callbackProfilPanel(){
    document.getElementById("login_profil").innerHTML=env.login;
    env.currentContainer = ContainerEnum.PROFIL;
    console.log("Je suis connecté, mon login est : " + env.login);
}

function makeProfilPanel()
{
    $("#container").load("Profil.html", callbackProfilPanel);
    $("#changableLink").attr("href", "css/Profil.css");
}

function makeConnexionPanel(){
    $("#container").load("Connexion.html", function(){
        $("#login_co").focus();
    });
    $("#changableLink").attr("href", "css/Connexion.css");
    env.currentContainer = ContainerEnum.CONNEXION;
}

function callbackMainPanel(){
    console.log("Bonjourssssssssssss");
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
    }
    
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
    console.log(repD);
    var messages = repD.list_message;
    for(var index in messages) {
        env.messages[messages[index].id] = messages[index];
    }
    
    displayMessages();
}

function displayMessages(){
    for(index in env.messages){
        $("#messages").append(env.messages[index].getHTML());
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
    console.log("ID : " + id);
    var m = env.messages[id];
    var el = $("#message_"+id+" .comments");
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

$("#input_search_main").keydown(enterHandler);

function enterHandler(event)
{
        if(event.keyCode == 13){		// Touche "entrée"
            doSearch();
        }	
}

function escapeHTMLEncode(str) {
    var div = document.createElement("div");
    var text = document.createTextNode(str);
    div.appendChild(text);
    return div.innerHTML;
}