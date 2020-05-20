// Global Variables--------------------------------------------
var covered = [true, true, true, true, true, true, true, true, true, true, true, true];
var status = "first";
var lock = false;
var previous_click;
var assignedPicOrder;
var storageAvailability;
var counter = 0;
var click;
var score;
var startGame = true;
var secCount;





// timer-------------------------------------------------------
function startTimer(sec, min){
    secCount = 0;
    var time_register = setInterval(function() {
        secCount++;
        sec.innerHTML = secCount % 60;
        min.innerHTML = parseInt(secCount / 60) % 60;
    }, 1000);
}


// sound effect------------------------------------------------
function sound(src){
    var myAudio = document.createElement("AUDIO");
    myAudio.src = src;
    wait(1)
    .then(() => {
        lock = true;
    })
    .then(() => {
        myAudio.load();
    })
    .then(() => {
        myAudio.play();
    })
    .then(() => {
        lock = false;
    })
}


// Modal-------------------------------------------------------
    // close the modal when the "x" buttom clicked
    function closeModal(){
        document.getElementById('myModal').style.display='none';
    }
    // close the modal automatically with a countdown timer
    function countTimeAndCloseModal(){
        // wait for 10 sec
        setTimeout(function (){
            document.getElementById('myModal').style.display='none';
        }, 10000);
    }


// Random ordered picture matching to tiles---------------------
function randomOrder(){
    var arr =[1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12];
    var tmp;
    for(let i = 0;i < arr.length; i++){
        var rand = parseInt(Math.random()*arr.length);
        t = arr[rand];
        arr[rand] =arr[i];
        arr[i] = t;
    }

    for(let i in arr){
        if(arr[i] > 6){
            arr[i] = arr[i] - 6;
        }
    }

    return arr;
}


// check counter ability----------------------------------------
function storageAvailable(){
    if(typeof(Storage) !== "undefined")
        return true;
    else{
        // clear all the board designed for counter results
        var leftSide_parent = document.getElementById("records");
        var leftSide_children = leftSide_parent.children;
        for(let i = 0; i < leftSide_children.length; i ++){
            leftSide_parent.removeChild(leftSide_children[i]);
        }
        // append an error notification to the org position
        var notification = document.createElement("p");
        notification.innerHTML = "Sorry, the browser does not support storage!<br>You may choose to use another browser or play without the score board :)";
        leftSide_parent.appendChild(notification);
        
        return false;
    }
}


// session counter----------------------------------------------
    // The n-th trial
    function matchOutputCountNumber(trial_count){
        if(trial_count == 1)
            return trial_count + "st";
        else if(trial_count == 2)
            return trial_count + "nd";
        else
            return trial_count + "th";
    }
    function determineTrial(){
        if(!sessionStorage.trial_count)
            sessionStorage.trial_count = 1;
        sessionStorage.trial_count = Number(sessionStorage.trial_count) + 1;
        document.getElementById("trial_count").innerHTML = matchOutputCountNumber(sessionStorage.trial_count);
    }
    // Current best record: min click
    function determineMinClickRecord(click){
        if(!sessionStorage.min_click)
            sessionStorage.min_click = click;
        sessionStorage.min_click = Number(sessionStorage.min_click);
        sessionStorage.min_click = (sessionStorage.min_click<click?sessionStorage.min_click:click);
        document.getElementById("min_click").innerHTML = sessionStorage.min_click;
    }
    // Current best record: min time
    function determineMinTime(s, m){    // s and m are the record in this trial, sec, min are the display id
        if(!sessionStorage.min_time)
            sessionStorage.min_time = m + "\'" + s + "\'\'";
        else{
            var recordCount = string2SecCount(sessionStorage.min_time);
            if(s + m * 60 < recordCount)
                sessionStorage.min_time = m + "\'" + s + "\'\'";
        }
    }
    function string2SecCount(record){
        var minIndex = record.indexOf("'");
        var secIndex = record.indexOf("''");
        return (Number(record.substring(0, minIndex)) * 60 + Number(record.substring(minIndex+1, secIndex)));
    }

    // Score determination selection function
    function countScore(click){
        if(click < 10)
            return 5;
        else if(click < 15)
            return 4;
        else if(click < 20)
            return 3;
        else
            return 1;
    }
    // Accumulate Score:
    function accumulateScore(score){
        if(!sessionStorage.score)
            sessionStorage.score = 0;
        sessionStorage.score = Number(sessionStorage.score) + score;
        document.getElementById("score").innerHTML = sessionStorage.score;
    }


// counting the clicks in the current trial---------------------
function thisTrialClickingCounter(){
    counter += 1;
    var click = counter>>1;
    document.getElementById("click_count").innerHTML = click;
    return click;
}



// reload the records stored in the left side bar---------------
function reloadRecords(){
    if(!sessionStorage.score){
        sessionStorage.trial_count = 1;
        document.getElementById("trial_count").innerHTML = matchOutputCountNumber(sessionStorage.trial_count);
        document.getElementById("min_click").innerHTML = "";
        document.getElementById("min_time").innerHTML = "";
        document.getElementById("score").innerHTML = "0";
    }
    else{
        document.getElementById("trial_count").innerHTML = matchOutputCountNumber(sessionStorage.trial_count);
        document.getElementById("min_click").innerHTML = sessionStorage.min_click;
        document.getElementById("min_time").innerHTML = sessionStorage.min_time;
        document.getElementById("score").innerHTML = sessionStorage.score;
    }
}

function recordWhenFinish(){
    // determine the min click
    click = thisTrialClickingCounter();
    determineMinClickRecord(click);
    // accumulate scores
    score = countScore(click);
    accumulateScore(score);
    // accumulate the trial count
    determineTrial();
    // determine the min time of accomplishing a trial
    determineMinTime(secCount % 60, parseInt(secCount / 60) % 60);
}


// check if the trial is finished
function checkForFinish(){
    var finished = true;
    for(let i = 0; i < 12 && finished == true; i ++)
        if(covered[i] === true)
            finished = false;
    
    if(!finished)
        return false;
    else{
        // the game finished
        sound("finish.mp3");
        wait(2000)
        .then(() => {recordWhenFinish();})
        .then(() => {
            alert("YOU FINISHED THE GAME IN " + click + " CLICKS,\nYOU USED "+Number(parseInt(secCount / 60) % 60)+"\'"+Number(secCount % 60)+"\'\',\nYOU EARNED " + score + " POINT IN THIS ROUND!");
        })
        .then(() => {
            if(confirm("PLAY AGAIN?"))
                location.reload();
        })
        return true;
    }
}


// functions about the displacement of the tiles---------------
function revealImage(index, ctx){
    var image = new Image();
    image.src = "pair" + assignedPicOrder[index] + ".jpg";
    // let the image load before it could be displayed!
    image.onload = () => {ctx.drawImage(image, 0, 0, 100, 100);};
}
function coverImage(ctx1, ctx2){
    // for the cases that two tiles revealed are different, and the two tiles are covered together
    wait(1000)
    .then(()=>{
        ctx1.clearRect(0, 0, 100, 100);
        return wait(10);
    })
    .then(() => {
        ctx2.clearRect(0, 0, 100, 100);
    })
}
function wait(ms) {
    lock = true;
    return new Promise(resolve => setTimeout(() => {
        resolve();
        lock = false;
    }, ms))
}

// when a tile is clicked---------------------------------------
function boxClicked(e){
    if(startGame){
        startGame = false;
        var sec = document.getElementById("sec");
        var min = document.getElementById("min");
        startTimer(sec, min);
    }

    if(lock === false){
        // trigger the local counter of clicks
        thisTrialClickingCounter();

        // pinpoint to the particular tile
        var canvas = e.target;
        var index = parseInt(canvas.dataset.index);
        var ctx = canvas.getContext("2d");

        if(covered[index] === true){
            // reveal the image
            revealImage(index, ctx);

            if(status === "first"){
                previous_click = index;
                status = "second";
            }
            else{
                var queryString = "canvas[data-index=\"" + previous_click + "\"]";
                var previousTile = document.querySelector(queryString);

                // determine if the two images are the same
                if(assignedPicOrder[index] === assignedPicOrder[previous_click]){
                    covered[index] = false;
                    covered[previous_click] = false;
                    var finished = checkForFinish();
                    if(!finished)
                        sound("click.mp3");
                }
                else{
                    coverImage(previousTile.getContext("2d"), ctx);
                }
                status = "first";
            }
        }
    }
}

// main-----------------------------------------------------------
window.onload = function(){
    countTimeAndCloseModal();

    assignedPicOrder = randomOrder();
    console.log(assignedPicOrder);

    storageAvailability = storageAvailable();

    reloadRecords();

    document.getElementById("box").addEventListener('click', boxClicked);
}
