/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* global $this, $window */

$(function(){
   $(".custom-file-input").change(function(e){
       var fileName = e. target. files[0]. name;
       var label = $(this).parent().children(".custom-file-label");
       if (fileName !== null) {
            label.text(fileName);
       } else {
           label.text("Sube una fotografía");
       }
       /*if($(this).hasClass("file-image")) {
           if (!fileName.match(/(?:gif|jpg|png)$/)) {
                label.text("Sólo archivos .jpg, .png, .gif");
                $(this).val(null);
            }
       }*/
    });
});