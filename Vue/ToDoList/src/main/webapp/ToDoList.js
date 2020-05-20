var editingIdx;

window.onload = function(){
    this.document.getElementById("newEvent").addEventListener('click', enterNewEvent);
    this.document.getElementById("deleteAll").addEventListener('click', deleteAllEvents);
    this.document.getElementById("status").addEventListener('click', toggleStatusList);
    this.document.getElementById("confirm").addEventListener('click', confirmEdit);
    this.document.getElementById("pwdBtn").addEventListener('click', pwdVerification);
    this.loadPreviousMission();
}

function pwdVerification(e){
    var pwd = document.getElementById("pwd").value;

    var xhr = new XMLHttpRequest();
    xhr.open("POST", "http://localhost:8080/Lab3Assignment/authentication", true);
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send(JSON.stringify(pwd));
    console.log(JSON.stringify(pwd));
    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4 && xhr.status == 200) {
            var response = JSON.parse(xhr.responseText);
            console.log(response[0].certification);
            app.authentication = response[0].certification;
        }
    };
}

function toggleStatusList(e){
    var target = e.target;
    if(target.innerHTML === "Hide Completed Events"){
        target.innerHTML = "Display All (Completed Include)";
        app.statusKey = "hide";
    }
    else{
        target.innerHTML = "Hide Completed Events";
        app.statusKey = "display";
    }
}

window.onbeforeunload = function(){
    this.updateDB();
}

function deleteAllEvents(e){
    for(let i = 0; i < app.events.length; i ++){
        deleteOneFromDB(app.events[i]);
    }

    app.events.splice(0, app.events.length);
}

function loadPreviousMission(){
    this.fetch("http://localhost:8080/Lab3Assignment/missions")
    .then(response => response.json())
    .then(function(response){
        for(let i = 0; i < response.length; i ++){
            app.events.push(response[i]);
        }
    })
    .catch(error => this.console.log(error));
}

function deleteOneFromDB(mission){
    var xhr = new XMLHttpRequest();
    xhr.open("POST", "http://localhost:8080/Lab3Assignment/delete", true);
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send(JSON.stringify(mission));
    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4 && xhr.status == 200) {
                console.log("delete: request submitted");
        }
    };
}

function updateDB(){
    for(let i = 0; i < app.events.length; i ++){
        addMissionToDB(app.events[i]);
    }
}

function addMissionToDB(mission){
    var xhr = new XMLHttpRequest();
    xhr.open("POST", "http://localhost:8080/Lab3Assignment/newMission", true);
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send(JSON.stringify(mission));
    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4 && xhr.status == 200) {
                console.log("request submitted");
        }
    };
}

function enterNewEvent(e){
    var title = document.getElementById("EvtTitle").value;
    var deadline = document.getElementById("ddl").value;

    // console.log("title: ", title);
    // console.log("deadline: ", deadline);

    var event = {};
    event.title = title;
    event.deadline = deadline;
    event.star = "None";
    event.status = "Pending";

    console.log(event);

    app.events.push(event);
}

function displayModal(){
    var modal = document.getElementById("myModal");
    document.getElementById("ddl_modified").value=app.events[editingIdx].deadline;
    modal.style.display = 'flex';
}

function confirmEdit(){
    app.events[editingIdx].deadline = document.getElementById("ddl_modified").value;    
    document.getElementById("myModal").style.display = 'none';
}

var app = new Vue({
    el:"#app",
    data:{
        events:[
            // title,
            // deadline,
            // star,
            // status
        ],
        statusKey: "display",
        authentication: "failed",
    },
    computed:{
        eventsSort: function(){
            if(this.authentication === "failed")
                return [];

            if(this.statusKey === "hide"){
                filteredEvents = [];
                for(let i = 0; i < this.events.length; i ++){
                    if(this.events[i].status === "Pending")
                        filteredEvents.push(this.events[i]);
                }
                filteredEvents.sort(sortByDDL);
                return filteredEvents;
            }
            else{
                this.events.sort(sortByDDL);
                return this.events;
            }
        },
    },
    methods:{
        starClass: function(event){
            if(event.star === "Star"){
                return {
                    fa:true,
                    'fa-star':true,
                    'fa-2x':true,
                    yellow:true,
                }
            }
            else{
                return {
                    fa:true,
                    'fa-star-o':true,
                    'fa-2x':true,
                    white:true,
                }
            }
        },
        statusClass: function(event){
            if(event.status === "Pending"){
                return {
                    fa:true,
                    'fa-circle-o':true, blue:true,  // Pending
                    'fa-5x':true,
                    center:true,
                }
            }
            else{
                return {
                    fa:true,
                    'fa-check-circle':true, green:true,   // Finished
                    'fa-5x':true,
                    center:true,
                }
            }
        },
        contentBoxClass: function(event){
            if(event.status === "Pending"){
                return {
                    center:true,
                    backgroundBlue:true,
                }
            }
            else{
                return {
                    center:true,
                    backgroundGreen:true,
                }
            }
        },
        shiftStar: function(event){
            if(event.star === "Star"){
                event.star = "None";
            }
            else{
                event.star = "Star";
            }
        },
        shiftStatus: function(event){
            if(event.status === "Pending"){
                event.status = "Finished";
            }
            else{
                event.status = "Pending";
            }
        },
        deleteEvent: function(event){
            var finish = false;
            var i = 0;
            while(!finish && i < this.events.length){
                if(this.events[i] === event){
                    this.events.splice(i, 1);
                    finish = true;
                }
                i = i + 1;
            }

            deleteOneFromDB(event);
        },
        toggleStar: function(event){
            var finish = false;
            var i = 0;
            while(!finish && i < this.events.length){
                if(this.events[i] === event){
                    if(event.star === "Star"){
                        this.events[i].star = "None";
                    }else{
                        this.events[i].star = "Star";
                    }
                    finish = true;
                }
                i = i + 1;
            }
        },
        toggleStatus: function(event){
            var finish = false;
            var i = 0;
            while(!finish && i < this.events.length){
                if(this.events[i] === event){
                    if(event.status === "Pending"){
                        this.events[i].status = "Finished";
                    }else{
                        this.events[i].status = "Pending";
                    }
                    finish = true;
                }
                i = i + 1;
            }
        },
        editDeadline: function(event){
            var finish = false;
            var i = 0;
            while(!finish && i < this.events.length){
                if(this.events[i] === event){
                    finish = true;
                    editingIdx = i;
                }
                i = i + 1;
            }

            displayModal();
        }
    }
})

Vue.component('eventContainer', {
    props:[
        "basicEvent",
        "starClass",
        "statusClass", 
        "contentBoxClass",
    ],
    template:`
    <div class="event-container">
        <div
            :class="starClass"
            class="editable"
            @click="$emit('toggle-star', basicEvent)"
        >
        </div>
        <i
            :class="statusClass"
            class="editable"
            @click="$emit('toggle-status', basicEvent)"
        >
        </i>
        <div
            :class="contentBoxClass"
            class="info-container"
        >
            <p>{{basicEvent.title}}</p>
            <p
                class="editable"
                @click="$emit('edit-deadline', basicEvent)"
            >
                {{basicEvent.deadline}}
            </p>
        </div>
        <div
            class="close center"
            @click="$emit('delete-event', basicEvent)"
        >
            &times;
        </div>
    </div>
    `
});

function sortByDDL(a, b){
    ddl_a = a.deadline;
    ddl_b = b.deadline;
    if(ddl_a > ddl_b)
        return 1;
    else if(ddl_a < ddl_b)
        return -1;
    else
        return 0;
}

function wait(ms) {
    return new Promise(resolve => setTimeout(() => {
        resolve();
    }, ms))
}

function closeModal(){
    document.getElementById("myModal").style.display = 'none';
}