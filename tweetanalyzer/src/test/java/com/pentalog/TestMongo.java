package com.pentalog;

import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.pentalog.twitter.model.mongoObjects.Word;
import com.pentalog.twitter.mongo.MongoConnection;
import com.pentalog.twitter.mongo.MongoQueries;
import com.pentalog.twitter.mongo.MongoStatistics;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import twitter4j.GeoLocation;
import twitter4j.HashtagEntity;
import twitter4j.MediaEntity;
import twitter4j.Place;
import twitter4j.RateLimitStatus;
import twitter4j.Scopes;
import twitter4j.Status;
import twitter4j.SymbolEntity;
import twitter4j.URLEntity;
import twitter4j.User;
import twitter4j.UserMentionEntity;

import java.util.Date;
import java.util.List;

/**
 * Created by agherasim on 19/11/2014.
 */
public class TestMongo extends CamelTestSupport {

	@Test
    @Ignore
	public void Test01MongoConnection(){

		MongoConnection mongoConnection=MongoConnection.getInstance();
		MongoClient mongoClient = mongoConnection.getMongoClient();
		Assert.assertNotNull(mongoClient);
		MongoQueries.init(mongoClient);
		List<String> stopWords = MongoQueries.getStopWords();
		Assert.assertTrue(stopWords.size()>100);
		MongoStatistics.processWords(getFakeTweet());
	}
	@Test
	public void Test02TestMongoQueriesGetSpecificWord(){
		MongoConnection mongoConnection=MongoConnection.getInstance();
		MongoClient mongoClient = mongoConnection.getMongoClient();
		Assert.assertNotNull(mongoClient);
		MongoQueries.init(mongoClient);
		Word lslsls = MongoQueries.getSpecificWord("lslsls", 1);
		Assert.assertNull(lslsls);
		Word test = MongoQueries.getSpecificWord("test", 1);
		Assert.assertNull(test);
	}

	@Test
    @Ignore

    public void Test03TestMongoQueriesGetSpecificWord() {

		MongoConnection mongoConnection = MongoConnection.getInstance();
		MongoClient mongoClient = mongoConnection.getMongoClient();
		Assert.assertNotNull(mongoClient);
		MongoQueries.init(mongoClient);
		List<Word> words = MongoQueries.getWords(new Date().getTime());
		Assert.assertNotNull(words);
		System.out.println(words);
	}

	@Test
    @Ignore

    public void Test04TestMongoGetGraphData() {

		DBObject graphData = MongoStatistics.getGraphData(0, 10);
		Assert.assertNotNull(graphData);
	}

	private Status getFakeTweet(){
		return new Status() {

			@Override public Date getCreatedAt() {

				return new Date();
			}

			@Override public long getId() {

				return 10;
			}

			@Override public String getText() {

				return "I had a wonderful day at the picnic. I liked the barbecue and the forest.";
			}

			@Override public String getSource() {

				return null;
			}

			@Override public boolean isTruncated() {

				return false;
			}

			@Override public long getInReplyToStatusId() {

				return 0;
			}

			@Override public long getInReplyToUserId() {

				return 0;
			}

			@Override public String getInReplyToScreenName() {

				return null;
			}

			@Override public GeoLocation getGeoLocation() {

				return null;
			}

			@Override public Place getPlace() {
				Place place=new Place() {

					@Override public String getName() {

						return null;
					}

					@Override public String getStreetAddress() {

						return null;
					}

					@Override public String getCountryCode() {

						return null;
					}

					@Override public String getId() {

						return null;
					}

					@Override public String getCountry() {

						return "Romania";
					}

					@Override public String getPlaceType() {

						return null;
					}

					@Override public String getURL() {

						return null;
					}

					@Override public String getFullName() {

						return null;
					}

					@Override public String getBoundingBoxType() {

						return null;
					}

					@Override public GeoLocation[][] getBoundingBoxCoordinates() {

						return new GeoLocation[0][];
					}

					@Override public String getGeometryType() {

						return null;
					}

					@Override public GeoLocation[][] getGeometryCoordinates() {

						return new GeoLocation[0][];
					}

					@Override public Place[] getContainedWithIn() {

						return new Place[0];
					}

					@Override public int compareTo(Place o) {

						return 0;
					}

					@Override public RateLimitStatus getRateLimitStatus() {

						return null;
					}

					@Override public int getAccessLevel() {

						return 0;
					}
				};
				return place;
			}

			@Override public boolean isFavorited() {

				return false;
			}

			@Override public boolean isRetweeted() {

				return false;
			}

			@Override public int getFavoriteCount() {

				return 0;
			}

			@Override public User getUser() {

				return null;
			}

			@Override public boolean isRetweet() {

				return false;
			}

			@Override public Status getRetweetedStatus() {

				return null;
			}

			@Override public long[] getContributors() {

				return new long[0];
			}

			@Override public int getRetweetCount() {

				return 0;
			}

			@Override public boolean isRetweetedByMe() {

				return false;
			}

			@Override public long getCurrentUserRetweetId() {

				return 0;
			}

			@Override public boolean isPossiblySensitive() {

				return false;
			}

			@Override public String getLang() {

				return "en";
			}

			@Override public Scopes getScopes() {

				return null;
			}

			@Override public int compareTo(Status o) {

				return 0;
			}

			@Override public UserMentionEntity[] getUserMentionEntities() {

				return new UserMentionEntity[0];
			}

			@Override public URLEntity[] getURLEntities() {

				return new URLEntity[0];
			}

			@Override public HashtagEntity[] getHashtagEntities() {

				return new HashtagEntity[0];
			}

			@Override public MediaEntity[] getMediaEntities() {

				return new MediaEntity[0];
			}

			@Override public MediaEntity[] getExtendedMediaEntities() {

				return new MediaEntity[0];
			}

			@Override public SymbolEntity[] getSymbolEntities() {

				return new SymbolEntity[0];
			}

			@Override public RateLimitStatus getRateLimitStatus() {

				return null;
			}

			@Override public int getAccessLevel() {

				return 0;
			}
		};
	}






}
