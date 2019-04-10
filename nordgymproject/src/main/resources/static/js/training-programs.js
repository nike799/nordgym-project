window.onload = function () {
    getAllTrainingPrograms();
};

function getAllTrainingPrograms() {
    //Clear result div
    let resultArea = $(".sidebar");
    let row = $("<div></div>").addClass('row');

    //Get JSON data by calling action method in controller
    fetch('http://localhost:8000/fetch/training-programs-all')
        .then(response => response.json())
        .then(data => {
            $.each(data, function (i, value) {

                //Create new row for each record
                $('<div class="col-sm-8">' +
                    '<a href="/training-programs/programId">'.replace('programId', value.id) + '<h5>imageHeader</h5></input>'.replace('imageHeader', value.header) +
                    '</div>').appendTo(row);
                $('<div class="col-sm-4">' +
                    '<img style="width: 100px; height: auto; margin-top: 5px" src="programImage">'.replace('programImage', value.programImagePath) +
                    '</div>').appendTo(row);
            })
        })
        .then(resultArea.append(row));
}
