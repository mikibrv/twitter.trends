Query:

Find most used words;

db.words.find({count:{$gt:1000}},{_id:false,beginDate:true,word:true,count:true,"country.countryName":true,"country.count":true}).pretty()

