<!-- Fonctions d'Affichage de la vue -->
// Copyright 721e - toute reproduction interdite.
var timeoutpopup = null;

function addslashes(ch) {


 ch = ch.replace(/\\/g,"\\\\") 


 ch = ch.replace(/\'/g,"\\'") 


 ch = ch.replace(/\"/g,"\\\"")


 return ch


}

function stripslashes(str) {
str=str.replace(/\\'/g,'\'');
str=str.replace(/\\"/g,'"');
str=str.replace(/\\0/g,'\0');
str=str.replace(/\\\\/g,'\\');
return str;
}





<!-- Fonction permettant l'affichage des infos sur les �nemis -->


function Show_perso (pid_perso, ppseudo, plib_race, pmsg_jour, x, y) {

  if (case_bloqued) { return (false); }

    x = x - 18;
    y = y - 165;

    var actions = '' ;

    if(pid_perso != jQuery('#id_perso').val()) {
        actions += "<a  id='action_mail' name='action_mail' value='Baler' href='http://www.demange-le-jeu.com/demange-v2/bal_send.php?cible="+jQuery('#id_perso').val()+"&id_concerne="+ pid_perso +" ' target='_blank' ><input type='button' value='Contacter'></a>";
    }

    if( pid_perso != jQuery('#id_perso').val() && jQuery('#entrainement_level').length > 0){
        actions += " <a  id='action_entrainement' name='action_entrainement' onclick='proposer_entrainement("+pid_perso+");return false;' onclick='proposer_entrainement("+pid_perso+");' ><input type='button' value='Entrainer("+jQuery('#entrainement_level').val()+")'></a>  ";
    }

    var popup =
        "<div class='popup_perso' style='z-index:31; height:auto;position:absolute; left:" + x + "px; top:" + y + "px;'>" +
            "<div class='informations'> <h2>Informations</h2><div class='underline'></div> " +
            "<div class='matricule line'><span class='item'>Matricule</span>&nbsp;:<span class='value'> " + pid_perso + "</span></div>" +
            "<div class='nom line'><span class='item'>Nom</span>&nbsp;:<span class='value'> " + ppseudo + "</span></div>" +
            "<div class='race line'><span class='item'>Race</span>&nbsp;:<span class='value'> " + plib_race + "</span></div>" +
            "<div class='mdj line'><span class='item'>Message du jour</span>&nbsp;:<span class='value'> " + pmsg_jour + "</span></div>" +
            "</div>" +
            "<div class='informations'> <h2>Actions</h2><div class='underline'></div>" +
            "<div class='line'> " +
            " <a  id='action_evenement' name='action_evenement' value='Evénements' href='http://www.demange-le-jeu.com/demange-v2/perso_events_view.php?id_perso="+ pid_perso +" ' target='_blank' ><input type='button' value='Evénements'></></a>  " +
                actions +
            "</div>" +
            "</div>" +
            "<div class='triangle'></div>" +
            "</div>";

    jQuery('.popup_perso').remove();
    window.clearTimeout(timeoutpopup);
    jQuery('#tab_conteneur_jeu').append(popup);

    jQuery('.popup_perso').mouseenter(function() {
        window.clearTimeout(timeoutpopup);
    });
    jQuery('.popup_perso').mouseleave(function() {
        jQuery('.popup_perso').remove();
    });
}





function Remove_perso(){
    timeoutpopup = setTimeout(function(){
        jQuery('.popup_perso').remove();
    },350)

}

<!-- Ouvre la popup d'info sur les persos -->


function info_perso () {
  if (document.view_perso.id_perso.value) window.open ('perso_events_view.php?id_perso='+ document.view_perso.id_perso.value, '_blank');
}






<!-- Ouvre la popup de mail d'un perso -->


function bal () {
  if (document.view_perso.id_perso.value) window.open ('bal_form.php?id_perso='+ document.view_perso.id_perso.value +'&index_perso='+ index_perso, '_blank');
}


<!-- Bloque l'affichage des infos des persos sur un perso particulier, ou debloque -->


function case_block() {
  case_bloqued = (case_bloqued == 0) ? 1 : 0;
}


function afficher_infos(lib,x,y,desc ){

	if(desc=="") desc="<i>Pas d'information sur cet objet.</i>";

    x = x - 18;
    y = y - 45;

    var popup =
        "<div class='popup_objet' style='z-index:31; height:auto;position:absolute; left:" + x + "px; top:" + y + "px;'>" +
            "<div class='informations'> " +
                    "<div class='lib'><span class='item'>Description</span>&nbsp;:<span class='value'> " + lib + "</span></div>" +
                  //  "<div class='desc'><span class='item'>Secondedescription?:</span><span class='value'> " + desc + "</span></div>" +
            "</div>" +
            "<div class='triangle'></div>" +
        "</div>";

    jQuery('#tab_conteneur_jeu').append(popup);
}

function masquer_infos(){
	jQuery('.popup_objet').remove();
}

function afficher_infos_p(lib,x,y,desc ){

        posx=parseInt(x)-160;
        posy=parseInt(y)+40;

        if(desc=="") desc="<i>Pas d'information sur ce projectile.</i>";

        document.getElementById('infos_projectile').style.top=posy+"px";
        document.getElementById('infos_projectile').style.left=posx+"px";
        document.getElementById('infos_projectile').innerHTML="<table width='200' border='1' bgcolor='#545b65' style='font-size:12px; color:#d7c999;'><tr><th><b><center>"+lib       +"</center></b></th></tr><tr><td>"+desc+"</td></tr></table>";
        document.getElementById('infos_projectile').style.visibility='visible';
        //alert(document.getElementById('infos_projectile').style.innerHTML);
}

function masquer_infos_p(){

        document.getElementById('infos_projectile').style.visibility='hidden';
}

function afficher_infos_c(lib,x,y,desc ){
        if(desc=="") desc="<i>Pas d'information sur cette case.</i>";
        jQuery('#infos_case').html('<span class="lib">' + lib + '</span><span class="desc">' + desc + '</span>');

}
function masquer_infos_c(lib,x,y,desc ){
}

function displayInfosCase (){
    jQuery('.table_infos_case').css('display','table');
}
function hideInfosCase (){
    jQuery('.table_infos_case').css('display','none');
}

