function init() {
    noConnection = false; // Cad, on parlera avec le serveur
    env = new Object();
    $("body").on("appear", function (event, $affected) {
        $.clear_append();
        completeMessage();
    });
    env.msgs = new Set();
    env.photo = "./web/default_profile.png";
    env.id_max = 0;
    env.id_min = 0;
    env.selected_twist = 0;
}

function msg(id, author, name, lastname, text, date, comments) {
    console.log('msg function');
    this.id = id;
    this.author = author;
    this.name = name;
    this.lastname = lastname;
    this.text = text;
    this.date = new Date(date.toLocaleString());
    if (comments == undefined) {
        comments = [];
    }
    this.comments = comments;
    msg.prototype.getHtml = function () {
        var d = new Date();
        d = d.getMinutes() - this.date.getMinutes();
        var msg_html =
            "<div class=\"twist\" id=\"" + this.id + "\">" +
            "   <div class=\"message-header\">" +
            "       <a class=\"username\" id=\"username-twist\" href=\"...\">" + this.name + " " + this.lastname + "</a>" +
            "       <small class=\"login\"> @" + this.author + "</small>" +
            "       <small class=\"time\"> " + d + "m</small>" +
            "   </div>" +
            "   <div class=\"message-content\">" +
            "       <p>" + this.text + "</p>" +
            "   </div>" +
            "   <input class=\"comments\" id=\"" + this.id + "\" value=\"View comments\" type=\"submit\" onclick=\"showComments(this.id, this.name, this.lastname, this.date)\"/>" +
            "</div>";
        return msg_html;
    }
}

function comment(id, author, text, date, msg_id) {
    console.log('comment function');
    this.id = id;
    this.author = author;
    this.text = text;
    this.date = date;
    this.msg_id = msg_id;
    comment.prototype.getHtml = function () {
        var comment_html = "Hello"; // TODO: Comment html
        return comment_html;
    }
}

// TODO: Revival function, look up how it works and how to do it properly

function makeRegisterPanel() {
    console.log('Creating register html');
    var html =
        "<div class=\"wrapper-register\">" +
        "   <div class=\"register\">" +
        "       <h1>Register a new account</h1>" +
        "       <form class=\"register_form\" name=\"register_form\" onSubmit=\"register(this)\" action=\"javascript:(function(){})()\">" +
        "           <input type=\"text\" name=\"user_name\" placeholder=\"Name\">" +
        "           <input type=\"text\" name=\"user_lastname\" placeholder=\"Last Name\">" +
        "           <input type=\"text\" name=\"mail_register\" placeholder=\"Email\">" +
        "           <input type=\"text\" name=\"user_register\" placeholder=\"Username\">" +
        "           <input type=\"password\" name=\"user_password\" placeholder=\"Password\">" +
        "           <input type=\"password\" name=\"user_re_password\" placeholder=\"Type your password again\">" +
        "           <input type=\"submit\" placeholder=\"Connection\" class=\"register_submit\">" +
        "       </form>" +
        "       <div class=\"register-help\">" +
        "           <a href=\"#\">Forgot Password</a>" +
        "       </div>" +
        "   </div>" +
        "</div>";
    $("body").html(html);
    console.log("Register panel ready in body");
}

function register(f) {
    console.log('Testing register values client side');
    var name = document.register_form.user_name.value;
    var lastname = document.register_form.user_lastname.value;
    var login = document.register_form.user_register.value;
    var email = document.register_form.mail_register.value;
    var passwd = document.register_form.user_password.value;
    var re_passwd = document.register_form.user_re_password.value;
    if (testRegisterForm(name, lastname, login, email, passwd, re_passwd)) {
        registerConnection(name, lastname, login, email, passwd, re_passwd);
    }
}

function testRegisterForm(name, lastname, login, email, passwd, re_passwd) {
    result = false;
    if ((name.length <= 0) || (lastname.length <= 0) || (login.length <= 0)
        || (email.length <= 0) || (passwd.length <= 0) || (re_passwd.length <= 0)) {
        console.log('Error, one of the inputs is empty'); // TODO: End all the tests client-side
        errorRegisterForm("Error, one of the inputs is empty");
    } else {
        console.log('Register service accepted, sending to server');
        result = true;
    }
    return result;
}

function errorRegisterForm(errorMessage, code) {
    console.log("[ERROR] = " + code);
    var html =
        "<div id=\"errorRegister\" style=\"color: red;\">" +
        "   <p>" + errorMessage + "</p>" +
        "</div>";
    var oldMessage = $("errorRegister");
    if (oldMessage.length == 0) {
        $("form").prepend(html);
    } else {
        oldMessage.replaceWith(html);
    }
}

// TODO: Implement server-side receiving email and re_passwd
function registerConnection(name, lastname, login, email, passwd, re_passwd) {
    console.log('Adding new user server-side');
    $.ajax({
        type: "GET",
        url: "create",
        dataType: "json",
        data: "name=" + name + "&lastName=" + lastname + "&email=" + email + "&login=" + login + "&passwd=" + passwd,
        error: function (jqXHR, textStatus, errorThrown) {
            alert(textStatus + "\n" + errorThrown);
        },
        success: function (rep) {
            console.log("Answer from server: " + rep);
            registerConnectionResponse(rep);
        }
    })
}

function registerConnectionResponse(rep) {
    console.log('Loading local variables from server response');
    console.log(rep);
    if (rep.error == undefined) {
        env.id = rep.id;
        env.key = rep.key;
        env.login = rep.login;
        env.name = rep.name;
        env.lastname = rep.lastname;
        env.follows = new Set();
        if (rep.f != undefined) {
            for (var i = 0; i < rep.f.length; i++) {
                env.follows.add(rep.f[i]);
            }
        }
        makeLoginPanel();
    } else {
        errorRegisterForm(rep.error, rep.code);
    }
}

function makeLoginPanel() {
    console.log('Creating login html');
    var html =
        "<div class=\"wrapper-login\">" +
        "   <div class=\"login\">" +
        "       <h1>Log into your account</h1>" +
        "       <form class=\"login-form\" name=\"login_form\" onSubmit=\"login(this)\" action=\"javascript:(function(){})()\">" +
        "           <input type=\"text\" name=\"user_login\" placeholder=\"Username\">" +
        "           <input type=\"password\" name=\"user_password\" placeholder=\"Password\">" +
        "           <input type=\"submit\" placeholder=\"Connection\" class=\"login-submit\">" +
        "       </form>" +
        "       <div class=\"login-help\">" +
        "           <a onclick=\"javascript:makeRegisterPanel()\">Register</a> - <a href=\"#\">Forgot Password</a>" +
        "       </div>" +
        "   </div>" +
        "</div>";
    $("body").html(html);
    console.log("Login panel ready in body");
}

function login(f) {
    var login = document.login_form.user_login.value;
    var passwd = document.login_form.user_password.value;
    if (testLoginForm(login, passwd)) {
        loginConnection(login, passwd);
    }
}

function testLoginForm(login, passwd) {
    result = false;
    if ((login.length == 0) || (passwd.length == 0)) {
        console.log('Error, one of the inputs is empty'); // TODO: End all the tests client-side
        errorLoginForm("Error, one of the inputs is empty");
    } else {
        console.log("Login accepted, sending to server");
        result = true;
    }
    return result;
}

function errorLoginForm(errorMessage, code) {
    console.log("[ERROR] = " + code);
    var html =
        "<div id=\"errorLogin\" style=\"color: red;\">" +
        "   <p>" + errorMessage + "</p>" +
        "</div>";
    var oldMessage = $("errorLogin");
    if (oldMessage.length == 0) {
        $("form").prepend(html);
    } else {
        oldMessage.replaceWith(html);
    }
}

function loginConnection(login, passwd) {
    $.ajax({
        type: "GET",
        url: "login",
        dataType: "json",
        data: "user=" + login + "&passwd=" + passwd,
        error: function (jqXHR, textStatus, errorThrown) {
            console.log("ERROR:\n" + JSON.stringify(jqXHR));
            console.log("AJAX Error: " + textStatus + ":" + errorThrown);
        },
        success: function (rep) {
            loginConnectionResponse(rep);
        }
    });
}

function loginConnectionResponse(rep) {
    console.log("Handling Login response");
    if (rep.error == undefined) {
        console.log("No error, setting environnement variables");
        env.id = rep.id;
        env.key = rep.key;
        env.login = rep.login;
        env.name = rep.name;
        env.lastname = rep.lastname;
        env.follows = new Set();
        if (rep.f != undefined) {
            for (var i = 0; i < rep.f.length; i++) {
                env.follows.add(rep.f[i]);
            }
        }
        makeMainPanel();
    } else {
        errorLoginForm(rep.error, rep.code);
    }
}

function makeMainPanel() {
    console.log("Now, we should make the main panel :)");
    if (env.login == "jruiz") {
        env.photo = "./web/0ZfOasx5_bigger.jpg";
    }
    var html =
        "<header>" +
        "   <div class=\"wrapper\">" +
        "       <a class=\"logo\" action=\"javascript:(function(){})()\" >" +
        "           <img onclick=\"javascript:getInitialMessages()\" src=\"./web/logo.png\" alt=\"Twister_logo\" class=\"logo\">" +
        "       </a>" +
        "       <input id=\"search\" type=\"text\" name=\"Search\" placeholder=\"Search Twister...\"/>" +
        "       <input id=\"logout\" type=\"submit\" value=\"logout\" onclick=\"javascript:logout()\"/>" +
        "   </div>" +
        "</header>" +
        "   <div class=\"wrapper-content\" id=\"page-container\">" +
        "       <div class=\"dashboard-left\">" +
        "           <div class=\"profile-card\">" +
        "               <a class=\"dashboard-left-image\" action=\"javascript:(function(){})()\">" +
        "                   <img class=\"dashboard-left-image\" src=\"" + env.photo + "\" onclick=\"makeUserPanel(-1)\">" +
        "               </a>" +
        "               <div class=\"dashboard-left-user-info\">" +
        "                   <a class=\"user-name\" href=\"...\">" + env.name + " " + env.lastname + "</a>" +
        "                   <span class=\"user-login\">@" + env.login + "</span>" +
        "               </div>" +
        "           </div>" +
        "       </div>" +
        "       <div class=\"main\">" +
        "           <div class=\"tweet-box\">" +
        "               <h1 class=\"post-twist\">Post your Twist!</h1>" +
        "               <p>" +
        "                   <form name=\"new_twist_form\" action=\"javascript:(function(){})()\" onsubmit=\"newTwist()\">" +
        "                       <textarea name=\"new_twist_text\" class=\"tweet-text\" placeholder=\"What's happening?\"></textarea>" +
        "                       <input class=\"new_tweet\" type=\"submit\" value=\"Twist\">" +
        "                   </form>" +
        "               </p>" +
        "           </div>" +
        "           <div class=\"twists-stream\">" +

        "           </div>" +
        "       </div>" +
        "       <div class=\"dashboard-right\">" +
        "           <div class=\"comments-stream\">" +
        "               <div class=\"comments-header\">" +
        "                   <h3>Comments: </h3>" +
        "                   <form name=\"new_comment_form\" class=\"add-comment\" id=\"message-for-comment-id\" action=\"javascript:(function(){})()\" onsubmit=\"newComment()\">" +
        "                       <textarea name=\"new_comment_text\" class=\"add-comment\" placeholder=\"Add your own!\" form=\"message-for-comment-id\"></textarea>" +
        "                       <input class=\"new-comment\" type=\"submit\" value=\"Comment on this twist!\">" +
        "                   </form>" +
        "               </div>" +
        "           <div class=\"twist-for-comment\" id=\"comment-twist\">" +

        "           </div>" +
        "       </div>" +
        "   </div>" +
        "</div>";
    $("body").html(html);
    getInitialMessages();
    console.log("Body is now the main panel.")
}

function logout() {
    $.ajax({
        type: "GET",
        url: "logout",
        dataType: "json",
        data: "key=" + env.key,
        error: function (jqXHR, textStatus, errorThrown) {
            console.log("ERROR:\n" + JSON.stringify(jqXHR));
            console.log("AJAX Error: " + textStatus + ":" + errorThrown);
        },
        success: function (rep) {
            logoutResponse(rep);
        }
    });
}

function logoutResponse(rep) {
    console.log("Handling logout");
    if (rep.error == undefined) {
        console.log("No error, returning to login screen");
        makeLoginPanel();
    } else {
        errorLogout(rep.error, rep.code);
    }
}

function errorLogout(message, code) {
    console.log("[ERROR] = " + code + ", " + message);
}

function getInitialMessages() {
    $.ajax({
        type: "GET",
        url: "messages",
        dataType: "json",
        data: "key=" + env.key + "&from=-1&id_max=-1&id_min=-1&nb=10",
        error: function (jqXHR, textStatus, errorThrown) {
            console.log("ERROR:\n" + JSON.stringify(jqXHR));
            console.log("AJAX Error: " + textStatus + ":" + errorThrown);
        },
        success: function (rep) {
            getInitialMessagesResponse(rep);
        }
    });
}

function getInitialMessagesResponse(rep) {
    console.log("Testing for errors in response from server with the messages");
    if (rep.error == undefined) {
        console.log("No apparent errors, setting up messages");
        var html = '';
        for (var i = 0; i < rep.length; i++) {
            var obj = rep[i];
            console.log("OBJ.MESSAGE_ID = " + obj.message_id);
            if (obj.message_id == undefined) {
                continue; // TODO: Erase this when all the messages have ids
            }
            if (parseInt(obj.message_id) > env.id_max) {
                env.id_max = obj.message_id;
            }
            if (parseInt(obj.message_id) < env.id_min) {
                env.id_min = obj.message_id;
            }
            if (obj.comments != undefined) {
                env.msgs[obj.message_id] = new msg(obj.message_id, obj.author,
                    obj.name, obj.lastname, obj.text, obj.date, obj.comments);
                html += env.msgs[obj.message_id].getHtml();
            } else {
                env.msgs[obj.message_id] = new msg(obj.message_id, obj.author,
                    obj.name, obj.lastname, obj.text, obj.date);
                html += env.msgs[obj.message_id].getHtml();
            }
        }
        console.log("Printing messages");
        $(".twists-stream").html(html);
        console.log("Messages available");
    }
}

function newTwist() {
    console.log("Testing new message values");
    var text = document.new_twist_form.new_twist_text.value;
    if (text.length > 140) {
        newTwistFormError("Twist is too long.");
    }
    newTwistConnection(text);
}

function newTwistFormError(message) {
    console.log(message);
    console.log("[ERROR] = " + code);
    var html =
        "<div id=\"newTwistError\" style=\"color: red;\">" +
        "   <p>" + message + "</p>" +
        "</div>";
    var oldMessage = $("errorRegister");
    if (oldMessage.length == 0) {
        $(".twists_stream").prepend(html);
    } else {
        oldMessage.replaceWith(html);
    }
}

function newTwistConnection(text) {
    console.log("Attempting to post new message");
    $(".tweet-text").val('');
    $.ajax({
        type: "GET",
        url: "addmessage",
        dataType: "json",
        data: "key=" + env.key + "&text=" + text,
        error: function (jqXHR, textStatus, errorThrown) {
            alert(textStatus + "\n" + errorThrown);
        },
        success: function (rep) {
            console.log("Answer from server: " + rep);
            newTwistResponse(rep);
        }
    })
}

function newTwistResponse(rep) {
    console.log("Handling response from server");
    if (rep.error == undefined) {
        env.id_max = rep.message_id;
        refreshMessages()
    }
}

function refreshMessages() {
    $.ajax({
        type: "GET",
        url: "messages",
        dataType: "json",
        data: "key=" + env.key + "&from=-1" + "&id_max=" + env.id_max + "&id_min=" + env.id_min + "&nb=10",
        error: function (jqXHR, textStatus, errorThrown) {
            console.log("ERROR:\n" + JSON.stringify(jqXHR));
            console.log("AJAX Error: " + textStatus + ":" + errorThrown);
        },
        success: function (rep) {
            getInitialMessagesResponse(rep);
        }
    });
}

function showComments(twist_id, name, lastname, date) {
    env.selected_twist = twist_id;
    console.log("Printing comments.");
    var d = new Date();
    var minutes = d.getMinutes() - env.msgs[twist_id].date.toLocaleString();
    var html_twist =
        "<div class=\"message-header\">" +
        "   <a class=\"username\" href=\"...\">" + env.msgs[env.selected_twist].author + "</a>" +
        "   <small class=\"time\"> " + minutes + "m</small>" +
        "</div>" +
        "<div class=\"message-content\">" +
        "   <p>" + env.msgs[twist_id].text + "</p>" +
        "</div>";
    // $(".twist-for-comment").html(html_twist);
    console.log("Fetching comments from environment");
    // var html_comment = "";
    for (var i = 0; i < env.msgs[twist_id].comments.length; i++) {
        minutes = d.getMinutes() - env.msgs[twist_id].comments[i].date;
        html_twist +=
            "<div class=\"comment\" id=\"comment" + env.msgs[twist_id].comments[i].id + "\">" +
            "   <div class=\"message-header\">" +
            "       <a class=\"comment-username\" href=\"...\"> @" + env.msgs[twist_id].comments[i].author + "</a>" +
            "       <small class=\"time\"> " + minutes + "m</small>" +
            "   </div>" +
            "   <div class=\"comment-content\">" +
            "       <p>" + env.msgs[twist_id].comments[i].text + "</p>" +
            "   </div>" +
            "</div>";
    }

    $(".twist-for-comment").html(html_twist);
}

function newComment() {
    var text = document.new_comment_form.new_comment_text.value;
    if (text.length > 140) {
        newCommentError("Comment is too long");
    }
    newCommentConnection(text);
}

function newCommentError(message) {
    console.log(message);
    console.log("[ERROR] = " + code);
    var html =
        "<div id=\"newCommentError\" style=\"color: red;\">" +
        "   <p>" + message + "</p>" +
        "</div>";
    var oldMessage = $("errorComment");
    if (oldMessage.length == 0) {
        $(".comments_stream").prepend(html);
    } else {
        oldMessage.replaceWith(html);
    }
}

function newCommentConnection(text) {
    console.log("Sending comment info to server");
    $(".new_comment_text").val('');
    $.ajax({
        type: "GET",
        url: "addcomment",
        dataType: "json",
        data: "key=" + env.key + "&text=" + text + "&message_id=" + parseInt(env.selected_twist),
        error: function (jqXHR, textStatus, errorThrown) {
            console.log("ERROR:\n" + JSON.stringify(jqXHR));
            console.log("AJAX Error: " + textStatus + ":" + errorThrown);
        },
        success: function(rep) {
            newCommentResponse(rep);
        }
    });
}

function newCommentResponse(rep) {
    console.log("Creating html for new comment");
    var d = new Date();
    var message_date = new Date(rep.date.toLocaleString());
    var minutes = d.getMinutes() - message_date;
    var html_comment =
        "<div class=\"comment\" id=\"comment" + env.selected_twist + "\">" +
        "   <div class=\"message-header\">" +
        "       <a class=\"comment-username\" href=\"...\"> @" + rep.author + "</a>" +
        "       <small class=\"time\"> " + minutes + "m</small>" +
        "   </div>" +
        "   <div class=\"comment-content\">" +
        "       <p>" + rep.text + "</p>" +
        "   </div>" +
        "</div>";
    env.msgs[env.selected_twist].comments.push(rep);
    $(".twist-for-comment").append(html_comment);
}

function getUserPanel() {
    console.log("Creating user panel");
    var html =
        "<header>" +
        "   <div class=\"wrapper\">" +
        "       <a class=\"logo\" action=\"javascript:(function(){})()\" >" +
        "           <img onclick=\"javascript:getInitialMessages()\" src=\"./web/logo.png\" alt=\"Twister_logo\" class=\"logo\">" +
        "       </a>" +
        "       <input id=\"search\" type=\"text\" name=\"Search\" placeholder=\"Search Twister...\"/>" +
        "       <input id=\"logout\" type=\"submit\" value=\"logout\" onclick=\"javascript:logout()\"/>" +
        "   </div>" +
        "</header>" +
        "   <div class=\"wrapper-content\" id=\"page-container\">" +
        "       <div class=\"dashboard-left\">" +
        "           <div class=\"profile-card\">" +
        "               <a class=\"dashboard-left-image\" action=\"javascript:(function(){})()\">" +
        "                   <img class=\"dashboard-left-image\" src=\"" + env.photo + "\" onclick=\"makeUserPanel(-1)\">" +
        "               </a>" +
        "               <div class=\"dashboard-left-user-info\">" +
        "                   <a class=\"user-name\" href=\"...\">" + env.name + " " + env.lastname + "</a>" +
        "                   <span class=\"user-login\">@" + env.login + "</span>" +
        "               </div>" +
        "           </div>" +
        "       </div>" +
        "       <div class=\"main\">" +
        "           <div class=\"tweet-box\">" +
        "               <h1 class=\"post-twist\">Post your Twist!</h1>" +
        "               <p>" +
        "                   <form name=\"new_twist_form\" action=\"javascript:(function(){})()\" onsubmit=\"newTwist()\">" +
        "                       <textarea name=\"new_twist_text\" class=\"tweet-text\" placeholder=\"What's happening?\"></textarea>" +
        "                       <input class=\"new_tweet\" type=\"submit\" value=\"Twist\">" +
        "                   </form>" +
        "               </p>" +
        "           </div>" +
        "           <div class=\"twists-stream\">" +

        "           </div>" +
        "       </div>" +
        "       <div class=\"dashboard-right\">" +
        "           <div class=\"comments-stream\">" +
        "               <div class=\"comments-header\">" +
        "                   <h3>Comments: </h3>" +
        "                   <form name=\"new_comment_form\" class=\"add-comment\" id=\"message-for-comment-id\" action=\"javascript:(function(){})()\" onsubmit=\"newComment()\">" +
        "                       <textarea name=\"new_comment_text\" class=\"add-comment\" placeholder=\"Add your own!\" form=\"message-for-comment-id\"></textarea>" +
        "                       <input class=\"new-comment\" type=\"submit\" value=\"Comment on this twist!\">" +
        "                   </form>" +
        "               </div>" +
        "           <div class=\"twist-for-comment\" id=\"comment-twist\">" +

        "           </div>" +
        "       </div>" +
        "   </div>" +
        "</div>";
    $("body").html(html);
}


