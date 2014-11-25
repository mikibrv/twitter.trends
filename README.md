Query:

Find most used words;

db.words.find({count:{$gt:1000}},{_id:false,beginDate:true,word:true,count:true,"country.countryName":true,"country.count":true}).pretty()



COUNT SORT MOST USED TWEETS

db.words.aggregate([
{$group : {_id : "$word", countWords : {$sum : "$count"}}},
{$sort:{countWords:-1}},
{$limit:100}
]);



Query Count per word
db.words.aggregate([
   {$match : {word:"Bieber"}},
   {$group : {_id : "$word", countWords : {$sum : "$count"}}},
   {$sort:{countWords:-1}},
   {$limit:50}
   ]);


   db.allTweets.find({"ID":536070893240602624});


   http://10.185.40.98:8081/

   http://10.185.40.98:8081/charts


   https://github.com/mikibrv/twitter.trends