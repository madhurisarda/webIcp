function upDate(previewPic) {
    //if(previewPic=)
    console.log(previewPic);
    $('#image').css('background-image', 'url(' + previewPic.src + ')');
    $('#image').html(previewPic.alt);
//     if(previewPic.alt=="Batter is ready")
// {
//     $('#image').src="https://cdn.sallysbakingaddiction.com/wp-content/uploads/2017/06/moist-chocolate-cupcakes-7.jpg";
//     $('#image').html("Batter is ready");
// }
//     else if (previewPic.alt=="Perfect Baking")
//     {
//         $('#image').src = "https://cdn.sallysbakingaddiction.com/wp-content/uploads/2017/06/moist-chocolate-cupcakes-6.jpg";
//         $('#image').html("Perfect Baking");
//     }
//     else if(previewPic.alt=="Yummy yummy cup cake"){
//         $('#image').src = "https://cdn.sallysbakingaddiction.com/wp-content/uploads/2017/06/moist-chocolate-cupcakes-5.jpg";
//         $('#image').html("Yummy yummy cup cake");
//     }
}
function unDo() {
    $('#image').css('background-image','none');
    $('#image').html("Hover over an image below to display here.");
}
