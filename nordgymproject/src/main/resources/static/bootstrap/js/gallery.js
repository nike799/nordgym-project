$(function() {                       //run when the DOM is ready
    $("img").click(function() {  //use a class, since your ID gets mangled
        $(".item").addClass("active");      //add the class to the clicked element
    });
});