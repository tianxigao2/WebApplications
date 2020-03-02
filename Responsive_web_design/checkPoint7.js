window.onload = function(){
    var p1 = document.getElementById("pic1");
    var p2 = document.getElementById("pic2");

    p1.onclick = () => {displayModal(p1.name);};
    p2.onclick = () => {displayModal(p2.name);};

    var close = document.getElementsByClassName("close")[0];
    close.onclick = () => {
        document.getElementById('myModal').style.display='none';
    };
}

function displayModal(pic_src){
    console.log(pic_src);
    // enable the displacement of modal
    var modal = document.getElementById('myModal');
    modal.style.display = "flex";

    var img = document.getElementById("modalImg");
    img.src = pic_src;
}
