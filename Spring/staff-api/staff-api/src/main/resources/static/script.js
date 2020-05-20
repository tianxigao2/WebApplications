window.onload = function(){
    this.document.getElementById("signin").addEventListener("click", verify);
    this.document.getElementById("signup").addEventListener("click",addAccount);
}

function verify(){
    var user={};
    user.account=document.getElementById("account").value;
    user.password=document.getElementById("password").value;
    
    var xhr = new XMLHttpRequest();
    xhr.open('POST', "http://localhost:8080/api/verify", true);
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send(JSON.stringify(user));
    xhr.onreadystatechange = function(){
        if(xhr.readyState == 4 && xhr.status == 200){
            var response = JSON.parse(xhr.responseText);
            if(response[0].msg === "Error"){
                document.getElementById("error-info").innerHTML="Account/ Password Not Correct";
            }else{
                vm.certification = true;
            }
        }
    }
}

function addAccount(){
    var user={};
    user.account=document.getElementById("account").value;
    user.password=document.getElementById("password").value;

    var xhr = new XMLHttpRequest();
    xhr.open('POST', "http://localhost:8080/api/register", true);
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send(JSON.stringify(user));
    xhr.onreadystatechange = function(){
        if(xhr.readyState == 4 && xhr.status == 200){
            var response = JSON.parse(xhr.responseText);
            if(response[0].msg === "Error"){
                document.getElementById("error-info").innerHTML="Account already exist";
            }
        }
    }
}

var vm = new Vue({
    el:"#app",
    data:{
        staffList:[],
        keyValue:"",
        certification:false,
    },
    mounted:function(){
        fetch("http://localhost:8080/api/staffs")
        .then((response) => response.json())
        .then((staffs) => {
            this.staffList = staffs;
        })
        .catch(error => console.log(error));
    },
    computed: {
        searchedStaff: function(){
            if(this.keyValue === ""){
                return this.staffList;
            }else{
                var filterKey = this.keyValue.toLowerCase();
                var container = this.staffList;
                return container.filter(function(staff){
                    return staff.name.toLowerCase().indexOf(filterKey) > -1;
                });
            }
        },
    },
    methods:{
        deleteStaff:function(staff){
            var xhr = new XMLHttpRequest();
            xhr.open('GET', "http://localhost:8080/api/delete/"+staff.name, true);
            xhr.setRequestHeader("Content-type", "application/json");
            xhr.send(JSON.stringify(staff));
            xhr.onreadystatechange = function(){
                if(xhr.readyState == 4 && xhr.status == 200){
                    var response = JSON.parse(xhr.responseText);
                    console.log(response);
                }
            }
        },
        toggleStatus:function(staff){
            var finished = false;
            for(let i = 0; i < this.staffList.length && (!finished); i ++){
                if(staff === this.staffList[i]){
                    this.staffList[i].status = (staff.status === "pending"? "completed": "pending");
                    console.log(this.staffList[i].status);
                    finished = true;
                }
            }
        },
        toggleStar:function(staff){
            var finished = false;
            for(let i = 0; i < this.staffList.length && (!finished); i ++){
                if(staff === this.staffList[i]){
                    this.staffList[i].star = (staff.star === "no"? "star": "no");

                    var xhr = new XMLHttpRequest();
                    xhr.open('GET', "http://localhost:8080/api/edit/"+this.staffList[i].name + "/" +this.staffList[i].star+"/"+this.staffList[i].status, true);
                    xhr.setRequestHeader("Content-type", "application/json");
                    xhr.send(JSON.stringify(staff));
                    xhr.onreadystatechange = function(){
                        if(xhr.readyState == 4 && xhr.status == 200){
                            var response = JSON.parse(xhr.responseText);
                            console.log(response);
                        }
                    }

                    finished = true;
                }
            }
        },
        refetch:function(){
            fetch("http://localhost:8080/api/staffs")
            .then((response) => response.json())
            .then((staffs) => {
                this.staffList = staffs;
            });
        },
    }
});

Vue.component("staff-blog", {
    props:["staff"],
    template:`
    <div class="staff-blog">
        <div class="left">
            <a :href="staff.profileURL" @click="preventJump">Click to View Profile</a>
            <div class="details">
            <div class="status editable" @click="$emit('toggle-status', staff)">{{staff.status}}</div>
            <div class="star editable" @click="$emit('toggle-star', staff)">{{staff.star}}</div>
            </div>
        </div>
        <div class="right">
            <div class="name">{{staff.name}}</div>
            <div class="membership">{{staff.membership}}</div>
            <div class="details">
                <div class="contact" :class="staff.email&&staff.phone?'':'invisible'">
                    CONTACT:</br>
                    <div class="email" :class="staff.email?'':'no-display'">Email: {{staff.email}}</div>
                    <div class="phone" :class="staff.phone?'':'no-display'">Phone: {{staff.phone}}</div>
                </div>
                <div class="address" :class="staff.address?'':'invisible'">Address:<br/> {{staff.address}}</div>
            </div>
        </div>
        <div class="close" @click="$emit('delete-staff', staff)">&times;</div>
    </div>
    `,
    methods:{
        preventJump:function(e){
            e.preventDefault();
            console.log("profile");
        },
    }
});
