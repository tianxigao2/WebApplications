var table = new Vue({
    el:'table',
    data:{
        sortKey:"none",
        table_header:{
            id:"ID",
            name:"Movie Name",
            year:"Year",
            rank:"Ranking",
        },
        movies:[],
    },
    computed:{
        id_class: function(){
            if(this.sortKey==="id")
                return "arrow-up";
            else if(this.sortKey==="id_rev")
                return "arrow-down";
            else
                return "";
        },
        name_class: function(){
            if(this.sortKey==="name")
                return "arrow-up";
            else if(this.sortKey==="name_rev")
                return "arrow-down";
            else
                return "";
        },
        year_class: function(){
            if(this.sortKey==="year")
                return "arrow-up";
            else if(this.sortKey==="year_rev")
                return "arrow-down";
            else
                return "";
        },
        rank_class: function(){
            if(this.sortKey==="rank")
                return "arrow-up";
            else if(this.sortKey==="rank_rev")
                return "arrow-down";
            else
                return "";
        },
    },
    methods:{
        triggerId: function(){
            if(this.sortKey==="id"){
                this.sortKey="id_rev";
                this.movies.sort(cmp_Id_reverse);
            }
            else{
                this.sortKey = "id";
                this.movies.sort(cmp_Id);
            }

            console.log("sort key = ", this.sortKey);
        },
        triggerName: function(){
            if(this.sortKey==="name"){
                this.sortKey="name_rev";
                this.movies.sort(cmp_Name_reverse);
            }
            else{
                this.sortKey="name";
                this.movies.sort(cmp_Name);
            }
            console.log("sort key = ", this.sortKey);
        },
        triggerYear: function(){
            if(this.sortKey === "year"){
                this.sortKey = "year_rev";
                this.movies.sort(cmp_Year_reverse);
            }
            else{
                this.sortKey = "year";
                this.movies.sort(cmp_Year);
            }
            console.log("sort key = ", this.sortKey);
        },
        triggerRank: function(){
            if(this.sortKey === "rank"){
                this.sortKey = "rank_rev";
                this.movies.sort(cmp_Rank_reverse);
            }
            else{
                this.sortKey = "rank";
                this.movies.sort(cmp_Rank);
            }
            console.log("sort key = ", this.sortKey);
        },
    }
});

window.onload = function(){
    this.fetch("http://localhost/api/movies")
    .then(response => response.json())
    .then(function(response){
        for(let i = 0; i < response.length; i ++){
            table.movies.push(response[i]);
        }
    })
    .catch(error => this.console.log(error));
}

function cmp_Id(a, b){
    var result = 0;
    if (a.id > b.id)
        result = 1;
    else if (a.id < b.id)
        result = -1;
    return result;
}

function cmp_Name(a, b){
    var result = 0;
    if(a.name > b.name)
        result = 1;
    else if(a.name < b.name)
        result = -1;
    return result;
}

function cmp_Year(a, b){
    var result = 0;
    if(a.year > b.year)
        result = 1;
    else if(a.year < b.year)
        result = -1;
    return result;
}

function cmp_Rank(a, b){
    var result = 0;
    if(a.rank > b.rank)
        result = 1;
    else if(a.rank < b.rank)
        result = -1;
    return result;
}

function cmp_Id_reverse(a, b){
    return 0-cmp_Id(a, b);
}

function cmp_Name_reverse(a, b){
    return 0-cmp_Name(a, b);
}

function cmp_Year_reverse(a, b){
    return 0-cmp_Year(a, b);
}

function cmp_Rank_reverse(a, b){
    return 0-cmp_Rank(a, b);
}