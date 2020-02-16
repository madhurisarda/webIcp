function getGithubInfo(user) {
    //1. Create an instance of XMLHttpRequest class and send a GET request using it.
    // The function should finally return the object(it now contains the response!)
    var username='https://api.github.com/users/'+user;
    console.log(username);
$.ajax({
    type: "GET",
    url: username,
    dataType: 'json',

}).done(function(data){
    showUser(data);

    }).fail(function(){
   console.log("Some error Happened");
   noSuchUser(user);
});

}

function showUser(user) {
    //2. set the contents of the h2 and the two div elements in the div '#profile' with the user content
    console.log(user);
    document.getElementById('imgavg').src=user.avatar_url==null ? "NA" : user.avatar_url;
    document.getElementById('txtname').innerText=user.login==null ? "NA" : user.login;
    document.getElementById('txtid').innerText=user.id==null ? "NA" : user.id;
    document.getElementById('txturl').innerText=user.html_url==null ? "NA" : user.html_url;
    document.getElementById('txturl').href=user.html_url==null ? "NA" : user.html_url;
    document.getElementById('txtrepository').innerText=user.public_repos==null ? "NA" : user.public_repos;
    document.getElementById('txtcompany').innerText=user.company==null ? "NA" : user.company;
    document.getElementById('txtlocation').innerText=user.location==null ? "NA" : user.location;

}
function noSuchUser(username) {
    //3. set the elements such that a suitable message is displayed
    if(data.message == "Not Found" || username == '') {
     alert("User not found");
    }
}
$(document).ready(function () {
    $(document).on('keypress', '#username', function (e) {
        //check if the enter(i.e return) key is pressed
        if (e.which == 13) {
            //get what the user enters
            username = $(this).val();
            //reset the text typed in the input
            $(this).val("");
            //get the user's information and store the respsonse
            getGithubInfo(username);
            //if the response is successful show the user's details

        }
    })
});
