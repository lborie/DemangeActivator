<%@ page import="fr.bodul.demange.Character" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%
    List<Character> characters = (List<Character>) request.getAttribute("characters");
    if (characters == null) {
        characters = new ArrayList<Character>();
    }
%>

<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Demange List activators</title>
    <link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">
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
        <table class="table table-hover table-condensed table-bordered">
            <thead>
            <th>Matricule</th>
            <th>Nom</th>
            <th>Expérience</th>
            <th>Heure probable d'activation</th>
            <th>Heure probable de jeu</th>
            </thead>
            <% for (Character character : characters) {%>
            <tr <% if(character.isPlayingInLessThan3Hours()) { %>class="danger"<% }%>>
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
                <td class=""><%=character.displayGamingText()%>
                </td>
            </tr>
            <% } %>
        </table>

    <div class="row"><div class="col-md-8"><span class="glyphicon glyphicon-info-sign"></span><span class="label label-danger">   Une ligne en route signifie que le personnage peut jouer dans les 3 prochaines heures</span></div></div>
</div>
<!-- Latest compiled and minified JavaScript -->
<script src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>
</body>
</html>
