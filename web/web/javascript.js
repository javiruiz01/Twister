function Msg (id, auteur, texte, date, comments) {
	console.log('msg');
	this.id = id;
	this.auteur = auteur;
	this.texte = texte;
	this.date = date;
	if (comments == undefined) {
		comments = [];
	}
	this.comments = comments;
	Msg.prototype.getHtml = function () {
		var s = "hello";
		return s;
	}
}


function setVirtualMessages() {
	console.log('setVirtualMessages');
	localdb = [];
	follows = [];
	var user1 = '{"id": 1, "login": "sly"}';
	var user2 = '{"id": 2, "login": "jo"}';
	var user3 = '{"id": 4, "login": "fab"}';

	follows[1] = new Set();
	follows[1].add(2);
	follows[1].add(4);
	follows[2] = new Set();
	follows[2].add(4);
	follows[4] = new Set();
	follows[4].add(1);

	var com1 = new Comment(1, user3, "hum", new Date());
	var com2 = new Comment(2, user2, "hey", new Date());
	localdb[2] = new Msg(2, user1, "message_aleatoire", new Date());
	localdb[4] = new Msg(4, user2, "message_aleatoire2", new Date(), [com1, com2]);
}

function Comment (id, auteur, texte, date) {
	console.log('comment');
	this.id = id;
	this.auteur = auteur;
	this.texte = texte;
	this.date = date;
	console.log('hello');
	Comment.prototype.getHtml = function () {
		var s = "hello";
		return s;
	}
}

function revival (key, value) {
	console.log('revival');
	if (value.comment != undefined) {
		var c = new Msg(value.id, value.auteur, value.texte, value.date);
		return c;
	} else if(value.texte != undefined) {
		var c = new Comment(value.id, value.auteur, value.texte, value.date);
		return c;
	} else if (key == 'date') {
		var d = new Date(value);
		return d;
	} else {
		return value;
	}
}

function init() {
	noConnection = false;
	env = new Object();
	setVirtualMessages();
	$("body").on("appear", function(event,$affected){
		$.clear_append();
		completeMessage();
	});
	env.msgs = new Set();
}

function completeMessage(){
	if(!noConnection){
		//TODO
	}else {
		var tab = getFromLocalDb(env.fromId, -1, env.idMin,10);
		completeMessagesReponse(JSON.stringify(tab));
	}
}

function getFromLocalDb(fromId, idMin, idMax, nbMax){
	var tab = [];
	var nb = 0; 
	var f = undefined; 
	if(fromId > 0){
		f = follows[fromId];
		if(f == undefined){
			f = new Set(); 
		}
	}
	for(var i = localdb.length -1; i>= 0; i--){
		if((nbMax >= 0) && (nb >= nbMax)){
			break;
		} 
		if (localdb[i] == undefined){
			continue;
		}
		if(((idMax < 0) ||(localdb[i].id < idMax)) && (localdb[i].id > idMin)){
			if(( fromId < 0) || (localdb[i].auteur.id == fromId) ||(f.has(localdb[i].auteur.id))){
				tab.push(localdb[i]);
				nb++;
			}
		}
	}
	return tab;
}



function makeMainPanel(id, login, key) {
	var html = "<header>" +
    "<div class=\"wrapper\">" +
    "<a class=\"logo\" href=\"...\">" +
       " <img src=\"logo.png\" alt=\"Twister_logo\" class=\"logo\">" +
   " </a>" +
    "<input id=\"search\" type=\"text\" name=\"Search\" placeholder=\"Search Twister...\"/>" +
   " <input id=\"logout\" type=\"submit\" value=\"logout\"/>" +
"</div>" +
"</header>" +
		"<div class=\"wrapper-content\" id=\"page-container\"> "+
    "<div class=\"dashboard-left\">"+
    "<div class=\"profile-card\">"+
       " <a class=\"dashboard-left-image\" href=\"...\">"+
          "  <img class=\"dashboard-left-image\" src=\"0ZfOasx5_bigger.jpg\">"+
       " </a>"+
       " <div class=\"dashboard-left-user-info\">"+
          "  <a class=\"user-name\" href=\"...\">" + env.name + " " + env.lastname + "</a>"+
          "  <span class=\"user-login\">@" + env.login + "</span>"+
      "  </div>"+
 "   </div>"+
"</div>"+
"<div class=\"main\">"+
   " <div class=\"tweet-box\">"+
      "  <h1 class=\"post-twist\">Post your Twist!</h1>"+
      "  <p>"+
       "     <form method=\"POST\" action=\"...\">"+
       "        <textarea name=\"text\" class=\"tweet-text\" placeholder=\"What's happening?\"></textarea>"+
       "         <input class=\"new_tweet\" type=\"submit\" value=\"Twist\">"+
       "    </form>"+
      "  </p>"+
   " </div>"+
  "  <div class=\"twists-stream\">"+
        
  "  </div>"+
"</div>"+
"<div class=\"dashboard-right\">"+
   " <div class=\"comments-stream\">"+
     "   <div class=\"comments-header\">"+
       "     <h3>Comments: </h3>"+
     "       <form class=\"add-comment\" id=\"message-for-comment-id\">"+
             "   <textarea name=\"text\" class=\"add-comment\" placeholder=\"Add your own!\" form=\"message-for-comment-id\"></textarea>"+
            "    <input class=\"new-comment\" type=\"submit\" value=\"Comment on this twist!\">"+
          "  </form>"+
      " </div>"+
     "</div>"+
"</div>"+
"</div>";
	$("body").append(html);
	
}


function completeMessageReponse(rep){
	var tab = JSON.parse(rep, revival);
	var lastId = undefined; 
	for(var i = 0; i < tab.length; i++){
		$("#content").append(tab[i].getHtml());
		env.msgs[tab[i].id]== tab[i]; 
		if(tab[i].id > env.idMax){
			env.idMax = tab[i].id;
		}
		if((env.idMin < 0) ||( tab[i].id < env.idMin)){
			env.idMin = tab[i].id;
		}
		lastId = tab[i].id;
	}
	$("#message"+lastId).appear();
	$.force_appear();
}

function connexion (f){
	var login = document.login_form.user_login.value; 
	console.log(login);
	var pass = document.login_form.user_password.value;  
	console.log(pass);
	var ok = verifFormConnexion(login, pass);
	if(ok){
		connecte(login, pass);
	}
}

function connecte(login, pass){
	if(!noConnection){
		console.log("Making connection to server");
		$.ajax ({type :"GET",
			url:"Connexion",
			dataType: "JSON",
			data: "user="+login+ "&passwd="+pass,
			error: function(jqXHR, textStatus, errorThrown){
				alert(textStatus);},
			success:function(rep){
					connecteReponse(rep);}
		});
	}else { 
		connecteReponse({"key" : 3244,"id": 35, "login": "joe", "follows": [2]});
	}
}

function RefreshMessages(){
	if(!noConnection){
		$.ajax ({type :"GET",
			url:"GetMessages",
			dataType: "text/plain",
			data: "key = "+env.key+ "&query = "+acompleter+"&from = "+env.fromId+"&IdMin =  "+env.IdMax+" &IdMax = -1 & nbMax = -1",
			error: function(jqXHR, textStatus, errorThrown){
				alert(textStatus);}, 
			success: function(rep){
					refreshMessagesReponse(rep);}
		});
	}else{
		refreshMessagesReponse(JSON.stringify(getFromLocalDb(env.fromId, env.IdMax,-1, -1)));
	}
}

function refreshMessagesReponse(rep){
	var tab = JSON.parse(rep, revival);
	if(tab.error != undefined){
		alert(error);
	}else{
		for(var i = tab.length - 1; i <= 0; i--){
			$("#messages").prepend(tab[i].getHtml());
			env.msgs[tab[i].id] = tab [i];
			if(tab[i].id > env.IdMax){
				env.IdMax = tab[i].id;
			}
		}
	}
}

function newMessage(){
	var texte = $("#text_newMessage").val();
	if(! noConnection){
		$.ajax({type :"GET",
			url:"GetMessages",
			dataType: "JSON",
			error: function(jqXHR, textStatus, errorThrown){
				alert(textStatus);}, 
			success: function(rep){
					refreshMessagesReponse(rep);}
		});
	}else{
		var m = newMessage(localDb.length, {"id": env.id, "login": env.login}, texte,newDate());
		localDb[m.id] = m;
		newMessageReponse(rep);
	}
}

function newMessageReponse(rep){
	if(rep.error != undefined){
		alert(rep.error);
	}else{
		refreshMessages();
	}
}

function verifFormConnexion(login, pass) {
	if ((login.length <= 0) || (login.length > 20) || (pass.length <= 0) || (pass.length > 20)) {
		console.log("error");
		funcError('Login ou mot de passe invalide');
	} else {
		console.log("login reussi");
		return true;
	}
}

function funcError(msg){
	console.log(msg)
	var msgBox = "<div id='errorConnect' style=\"color: red;\"> " 
		+ msg +
		"</div>";
	console.log(msgBox)
	var oldMsg = $("#errorConnect");
	if(oldMsg.length == 0){
		$("form").prepend(msgBox);
	}else {
		oldMsg.replaceWith(msgBox);
	}
}

function register(f){
	var prenom = document.register_form.user_name.value; 
	console.log(prenom);
	var nom = document.register_form.user_lastname.value; 
	var login = document.register_form.user_register.value;
	var email = document.register_form.mail_register.value; 	
	var pass = document.register_form.user_password.value; 
	var passVerif = document.register_form.user_re_password.value; 
	var ok = verifFormRegister(prenom, nom, login, email, pass, passVerif)
	if(ok){
		connecteRegister(prenom, nom, login, email, pass);
	}
}

function connecteRegister(prenom, nom, login, email, pass){
	if(!noConnection){
		console.log("Making connection to server");
		console.log("name=" + prenom + "&lastName=" + nom + "&email="+ email +"&login="+login+ "&passwd="+pass);
		$.ajax ({type :"GET",
			url:"create",
			dataType: "JSON",
			data: "name=" + prenom + "&lastName=" + nom + "&email="+ email +"&login="+login+ "&passwd="+pass,
			error: function(jqXHR, textStatus, errorThrown){
				alert(textStatus);
				},
			success: //connecteReponse
				function(rep){
				   console.log("Success");
				   connecteReponse(rep);
			   }
		});
	}else { 
		connecteReponse({"key" : 3244,"id": 35, "login": "joe", "follows": [2]});
	}
}

function connecteReponse(rep){
	console.log("error?");
	if(rep.error == undefined){
		console.log("Pas de erreur");
		env.id = rep.id;
		env.key = rep.key;
		env.login = rep.login;
		env.name = rep.name;
		env.lastname = rep.lastName;
		env.follows = new Set();
		//for(var i = 0; i<rep.follows.length;i++){
			//env.follows.add(rep.f[i]);
		//}
		makeMainPanel(rep.id, rep.login, rep.key);
		/*if(noConnection){
			if(follows[rep.id] == undefined){
				follows[rep.id] = new Set();
				for(var i = 0; i<rep.follows.length;i++){
					follows[rep.id].add(rep.follows[i]);
				}
			}
		}*/
	}
}

function verifFormRegister(prenom, nom,login, email, pass,passVerif) {
	if ((login.length <= 0) || (login.length > 20) || (pass.length <= 0) || (pass.length > 20)) {
		console.log("error");
		funcError('Login  invalide');
	} else {
		console.log("login reussi");
	}
	return true;
}

function makeConnectionPanel(){
	var html = "<div class=\"wrapper-login\">" +
	"<div class=\"login\">"+
	"<h1>Log into your account</h1>" +
	"<form name=\"login_form\" class=\"login-form\"  onSubmit=\"connexion(this) \"action=\"javascript:(function(){})()\">" +
	" <input type=\"text\" name=\"user_login\" placeholder=\"username\">" +
	" <input type=\"password\" name=\"user_password\" placeholder=\"password\">" +
	" <input type=\"submit\" placeholder=\"Connection\" class=\"login-submit\">" +
	" </form>" +
	" <div class=\"login-help\">" +
	"  <a href=\"#\">Register</a> - <a href=\"#\">Forgot Password</a>" +
	" </div>" +
	"</div>" +	
	"</div>";
	$("body").append(html);
	console.log("Appended");
}

function makeRegisterPanel(){
	var html = "<div class=\"wrapper-register\"> " +
	"<div class=\"register\">" +
	"<h1>Register a new account</h1>" +
	"<form name=\"register_form\" class=\"register_form\" onSubmit=\"register(this)\" action=\"javascript:(function(){})()\">" +
	"<input type=\"text\" name=\"user_name\" placeholder=\"Name\">" +
	" <input type=\"text\" name=\"user_lastname\" placeholder=\"Last Name\">" +
	" <input type=\"text\" name=\"user_register\" placeholder=\"Username\">" +
	"<input type=\"text\" name=\"mail_register\" placeholder=\"Email\">" +
	" <input type=\"password\" name=\"user_password\" placeholder=\"Password\">" +
	"  <input type=\"password\" name=\"user_re_password\" placeholder=\"Type your password again\">" +
	" <input type=\"submit\" placeholder=\"Connection\" class=\"register_submit\">" +
	"</form>" +
	"<div class=\"register-help\">" +
	"   <a href=\"#\">Forgot Password</a>" +
	" </div>" +
	"</div>" +
	"</div>";
	$("body").append(html);
	console.log("Appended");
}


function makeMainPanelNpoConnection(fromId, fromLogin, query) {
	env.msg = [];
	env.idMax = -1;
	env.idMin = -1;
	if (fromId == undefined) {
		fromId = -1;
	}
	env.fromId = fromId;
	env.fromLogin = fromLogin;
	console.log(env.fromLogin);
	env.query = query;
	var s = "html";
	if (env.fromId < 0) {
		s += "div";
	} else {
		if (env.fromId == env.id) {
			s += "another div";
		} else if (!env.follows.has(env.fromId)) {
			s += "follow button";
		} else {
			s += "Ne plus suivre";
		}
	} 
	if (env.fromId > 0) {
		s += "Proposer d'aller sur le profil de l'utilisateur";
	}
	if (env.fromId < 0) {
		s+= "Ajout de message";
	}
	s+="Afficher les messages";
}



/*
x = '[{"id": 1,"auteur":197,"texte":"blabla","date":123456789,"comments": [1]},	{"id": 2,"auteur": 123,"texte":"humhum","date": 1}]';
 */
