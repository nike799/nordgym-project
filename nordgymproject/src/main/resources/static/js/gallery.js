window.onload = getAllImages;

function getImageName(e) {
   let imageName = e.target.src.substring(e.target.src.lastIndexOf('/') + 1);
   getAllCarouselImages(imageName);
}

function getAllImages() {
    let imgElements = document.querySelectorAll('#gallery-images img');
    imgElements.forEach((img) => img.addEventListener('click', getImageName))
}

function getAllCarouselImages(imageName) {
    let carouselInnerDivs = document.querySelectorAll('.carousel-inner .item');
    for (let i = 0; i < carouselInnerDivs.length; i++) {
        let img = carouselInnerDivs[i].getElementsByTagName('img')[0];
        if(img.src.indexOf(imageName) > 0 ){
            carouselInnerDivs[i].classList.add('active');
            break;
        }
    }
}