<%@ page import="fr.bodul.demange.Character" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="fr.bodul.demange.Faction" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%
    List<Character> characters = (List<Character>) request.getAttribute("characters");
    if (characters == null) {
        characters = new ArrayList<Character>();
    }

    List<Faction> factions = (List<Faction>) request.getAttribute("factions");
    if (factions == null) {
        factions = new ArrayList<Faction>();
    }

    Object factionId = request.getAttribute("factionId");
%>

<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Demange List activators</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.0/css/bootstrap.min.css">
    <link rel="stylesheet" href="stylesheets/main.css">
</head>

<body>
<div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="#">Demange activator</a>
        </div>
        <div class="collapse navbar-collapse">
            <ul class="nav navbar-nav">
                <li class="active"><a href="#">Home</a></li>
            </ul>
        </div>
        <!--/.nav-collapse -->
    </div>
</div>
<div class="container">

    <form class="form-signin" role="form" action="display" method="POST">
        <h2 class="form-signin-heading">Authentification</h2>
        <label for="login" class="sr-only">Login</label>
        <input type="text" id="login" name="login" class="form-control" placeholder="Login" required autofocus>
        <label for="password" class="sr-only">Password</label>
        <input type="password" id="password" name="password" class="form-control" placeholder="Password" required>
        <button class="btn btn-lg btn-primary btn-block" type="submit">Log in</button>
    </form>

</div> <!-- /container -->

<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
<!-- Latest compiled and minified JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.0/js/bootstrap.min.js"></script>
<script>
    $('#presentation a').click(function (e) {
        e.preventDefault()
        $(this).tab('show')
    })
</script>
</body>
</html>
