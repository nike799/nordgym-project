$(document).ready(function() {
    $("#myBtn").click(function () {
        $("#myModal-Pricelist").modal();
    });

    $("#flip").click(function () {
        $("#panel").slideToggle("slow");
    });


    /* Price list */
    $(".one").click(function () {
        $("#Limited").toggle();
        $(".two,.three").hide();
    });

    $(".title_2").click(function () {
        $(".one_2").toggle();
        $(".one_1,.one_3").hide();
    });

    $(".title_3").click(function () {
        $(".one_3").toggle();
        $(".one_1,.one_2").hide();
    });

});
