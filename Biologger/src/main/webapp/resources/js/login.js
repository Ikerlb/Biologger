/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* global $this, $window */

$(function(){
    updateContainer();
    $(window).resize(function() {
        updateContainer();
    });
    $("#registerForm input:checkbox").change(function(){
        if($(this).is(":checked")) {
            $(".ntrabajador-wrapper").removeClass("d-none");
        } else {
           $(".ntrabajador-wrapper").addClass("d-none"); 
        }
    });
    $("#subirFoto .custom-file-input").change(function(e){
        var fileName = e. target. files[0]. name;
        if (fileName != null) {
            $(this).parent(".custom-file").children("label").text(fileName);
        }
    });
});

function updateContainer() {
    $(window).height() < $("body").height() ? 
    $("html").removeClass("h-100") : $("html").addClass("h-100");
}

