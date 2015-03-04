<%@ page import="fr.bodul.demange.dao.Character" %>
<%@ page import="fr.bodul.demange.dao.Faction" %>
<%@ page import="java.util.*" %>
<%@ page import="com.google.common.base.Joiner" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%
    List<Character> characters = (List<Character>) request.getAttribute("characters");
    List<Character> progressionsNames = (List<Character>) request.getAttribute("progressionsNames");
    List<Integer> progressionsExperiences = (List<Integer>) request.getAttribute("progressionsExperiences");
    Set<String> dates = new TreeSet<String>(characters.get(0).getExperience().keySet());
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
                <li><a href="display">Accueil</a></li>
                <li><a href="photo">Photo</a></li>
                <li class="active"><a href="#">Progression</a></li>
                <li><a href="admin">Administration</a></li>
            </ul>
        </div>
        <!--/.nav-collapse -->
    </div>
</div>
<div class="container">
    <div id="myChart" style="min-width: 310px; height: 400px; margin: 0 auto"></div>
    <br/><br/><br/>
    <div id="myProgression" style="min-width: 310px; height: 400px; margin: 0 auto"></div>
</div> <!-- /container -->

<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
<!-- Latest compiled and minified JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.0/js/bootstrap.min.js"></script>
<script src="http://code.highcharts.com/highcharts.js"></script>
<script>
    $('#presentation a').click(function (e) {
        e.preventDefault()
        $(this).tab('show')
    })

    $(function () {
        $('#myChart').highcharts({
            title: {
                text: 'Progression en expérience dans le temps',
                x: -20 //center
            },
            xAxis: {
                categories: [<% Iterator<String> iterator = dates.iterator();
        while(iterator.hasNext()){
            String libelle = "\"" + iterator.next() + "\"";
            if (iterator.hasNext()) libelle = libelle + ",";

        %><%=libelle %><%}%>]
            },
            yAxis: {
                title: {
                    text: 'Expérience'
                },
                plotLines: [{
                    value: 0,
                    width: 1,
                    color: '#808080'
                }]
            },
            tooltip: {
                valueSuffix: 'exp'
            },
            legend: {
                layout: 'vertical',
                align: 'right',
                verticalAlign: 'middle',
                borderWidth: 0
            },
            series: [
                <%
                    int i = 0;
                    for (Character personnage : characters) {
                        i++;
                        SortedMap<String, Integer> experience = new TreeMap<String, Integer>(personnage.getExperience());
                            Collection<Integer> experienceInteger = experience.values();
                            String data = Joiner.on(',').join(experienceInteger);
                %>
                {
                    name: "<%= personnage.getName() %>",
                    data: [<%=data%>]<% if(i > 10 && i != characters.size()){%>,
                    visible: false<%}%>
                },
                <% } %>
               ]
        });

        $('#myProgression').highcharts({
            chart: {
                type: 'column'
            },
            title: {
                text: 'Plus gros Tap Case sur 28 jours'
            },
            xAxis: {
                categories: [<% Iterator<Character> progressionName = progressionsNames.iterator();
        while(progressionName.hasNext()){
            String libelle = "\"" + progressionName.next().getName() + "\"";
            if (progressionName.hasNext()) libelle = libelle + ",";

        %><%=libelle %><%}%>
                ]
            },
            yAxis: {
                min: 0,
                title: {
                    text: 'Experience'
                }
            },
            tooltip: {
                headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                '<td style="padding:0"><b>{point.y}</b></td></tr>',
                footerFormat: '</table>',
                shared: true,
                useHTML: true
            },
            plotOptions: {
                column: {
                    pointPadding: 0.2,
                    borderWidth: 0
                }
            },
            series: [
                {
                    name: "Expérience",
                    data: [<% Iterator<Integer> progressionEx = progressionsExperiences.iterator();
        while(progressionEx.hasNext()){
            String libelle = "" + progressionEx.next() + "";
            if (progressionEx.hasNext()) libelle = libelle + ",";

        %><%=libelle %><%}%>]
                }
            ]
        });
    });

</script>
</body>
</html>
