window.onload = function () {
    getAllTrainingPrograms();
};

function getAllTrainingPrograms() {
    //Clear result div
    let resultArea = $(".sidebar");
    //Get JSON data by calling action method in controller
    fetch('http://localhost:8080/fetch/training-programs-all')
        .then(response => response.json())
        .then(data => {
            $.each(data, function (i, value) {
                //Create new row for each record
                $(   '<div class="row">' +
                       '<div class="col-sm-6">' +
                         '<img style="width: 150px; height: auto; margin-top: 5px" src="programImage">'.replace('programImage', value.programImagePath) +
                       '</div>' +
                       '<div class="col-sm-6">' +
                         '<a href="/training-programs/programId">'.replace('programId', value.id) + '<h5>imageHeader</h5></input>'.replace('imageHeader', value.header) +
                       '</div>' +
                     '</div>'
                ).appendTo(resultArea);
            })
        })
}
