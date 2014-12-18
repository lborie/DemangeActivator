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
                <li><a href="display">Home</a></li>
                <li class="active"><a href="admin">Administration</a></li>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <li><a href="logout">Déconnexion</a></li>
            </ul>
        </div>
        <!--/.nav-collapse -->
    </div>
</div>
<div class="container">
    <div class="row">
        <div class="col-md-6">
            <form class="admin" role="form" action="admin" method="POST">
                <h2 class="admin-heading">Ajouter ou activer un matricule</h2>
                <label for="addMatricule" class="sr-only">Matricule</label>
                <input type="text" id="addMatricule" name="addMatricule" class="form-control" placeholder="Matricule" required autofocus>
                <button class="btn btn-lg btn-primary btn-block" id="addMatriculeAction" name="addMatriculeAction" type="submit">Ajouter</button>
            </form>
        </div>
        <div class="col-md-6">
            <h2>Matricules Actifs</h2>
            <div class="row">
                <% for (Character character : characters) {
                    if (character.isActive() == null || character.isActive()) {%>
                <div class="col-md-1" style="margin-top: 20px; margin-right: 20px">
                    &nbsp;<span class="label label-primary" style="font-size: large"><%= character.getMatricule()%></span>
                </div>
                <% }} %>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-md-6">
            <form class="admin" role="form" action="admin" method="POST">
                <h2 class="admin-heading">Désactiver un matricule</h2>
                <label for="minusMatricule" class="sr-only">Matricule</label>
                <input type="text" id="minusMatricule" name="minusMatricule" class="form-control" placeholder="Matricule" required autofocus>
                <button class="btn btn-lg btn-primary btn-block" id="minusMatriculeAction" name="minusMatriculeAction" type="submit">Désactiver</button>
            </form>
        </div>
        <div class="col-md-6">
            <h2>Matricules Inactifs</h2>
            <div class="row">
            <% for (Character character : characters) {
                if (character.isActive() != null && !character.isActive()) {%>
                <div class="col-md-1" style="margin-top: 20px; margin-right: 20px">
                    &nbsp;<span class="label label-warning" style="font-size: large"><%= character.getMatricule()%></span>
                </div>
            <% }} %>
            </div>
        </div>
    </div>

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
