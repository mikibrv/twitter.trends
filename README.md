Query:

Find most used words;

db.words.find({count:{$gt:1000}},{_id:false,beginDate:true,word:true,count:true,"country.countryName":true,"country.count":true}).pretty()



COUNT SORT MOST USED TWEETS

db.words.aggregate([
{$group : {_id : "$word", countWords : {$sum : "$count"}}},
{$sort:{countWords:-1}},
{$limit:50}
]);