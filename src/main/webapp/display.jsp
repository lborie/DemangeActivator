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
                <li class="active"><a href="#">Accueil</a></li>
                <li><a href="admin">Administration</a></li>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <li><a href="logout">Déconnexion</a></li>
            </ul>
        </div>
        <!--/.nav-collapse -->
    </div>
</div>
<div class="container">
    <ul class="nav nav-tabs" role="tablist">
        <li role="presentation" <% if("ALL".equals(factionId)) { %>class="active"<% }%>><a href="/">Tous</a></li>
        <% for (Faction faction : factions) {%>
            <li role="presentation" <% if(faction.getFactionId().equals(factionId)) { %>class="active"<% }%>><a href="?factionId=<%=faction.getFactionId()%>"><%=faction.getName()%></a></li>
        <% } %>
    </ul>
        <table class="table table-hover table-condensed table-bordered">
            <thead>
            <th>Matricule</th>
            <th>Nom</th>
            <th>Expérience</th>
            <th>Augmentation de l'expérience de 1 point détecté le</th>
            </thead>
            <% for (Character character : characters) {%>
            <tr <% if(character.isPlayingInLessThan2Hours()) { %>class="danger"<% }%>>
                <td class=""><%=character.getMatricule()%>
                </td>
                <td class="">
                    <a href="http://www.demange-le-jeu.com/demange-v2/perso_events_view.php?id_perso=<%=character.getMatricule()%>">
                            <%=character.getName()%>
                    </a>
                </td>
                <td class=""><%=character.getCurrentExperience()%>
                </td>
                <td class=""><%=character.displayActivationText()%>
                </td>
            </tr>
            <% } %>
        </table>

    <div class="row"><div class="col-md-8"><span class="glyphicon glyphicon-info-sign"></span><span class="label label-warning">   Une ligne en rouge signifie que le personnage peut jouer dans les 2 prochaines heures</span></div></div>
</div>
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
