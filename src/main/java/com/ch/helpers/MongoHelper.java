package com.ch.helpers;

import com.ch.configuration.FormsServiceConfiguration;
import com.ch.model.FormStatus;
import com.ch.model.FormsPackage;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

/**
 * Created by elliott.jenkins on 30/06/2016.
 */
public final class MongoHelper {

  private static final String PACKAGE_IDENTIFIER = "packageIdentifier";
  private static final String STATUS = "status";
  private static MongoHelper instance = new MongoHelper();
  private FormsServiceConfiguration configuration;
  private MongoClient client;


  private MongoHelper() {
  }

  public static void init(FormsServiceConfiguration configuration) {
    instance.setConfiguration(configuration);
  }

  public static MongoHelper getInstance() {
    return instance;
  }

  private void setConfiguration(FormsServiceConfiguration configuration) {
    this.configuration = configuration;
    this.client = setupMongoClient();
  }

  /**
   * Get the mongo client.
   *
   * @return MongoClient
   */
  public MongoClient getMongoClient() {
    return client;
  }

  /**
   * Get mongo database.
   *
   * @return MongoDatabase
   */
  public MongoDatabase getDatabase() {
    return client.getDatabase(configuration.getMongoDbName());
  }

  /**
   * Get the packages collection.
   *
   * @return MongoCollection
   */
  public MongoCollection<Document> getPackagesCollection() {
    MongoDatabase database = getDatabase();
    return database.getCollection(configuration.getMongoDbPackagesCollectionName());
  }

  /**
   * Get the packages collection by submission number.
   *
   * @return MongoCollection
   */
  public Document getPackagesCollectionByPackageId(long packageId) {
    MongoDatabase database = getDatabase();
    return database.getCollection(configuration.getMongoDbPackagesCollectionName())
      .find(new Document(PACKAGE_IDENTIFIER, packageId))
      .first();
  }

  /**
   * Get the packages collection by status, limit number to parameter, sort oldest first.
   *
   * @return MongoCollection
   */
  public FindIterable<Document> getPackagesCollectionByStatus(String formStatus, int count) {
    MongoDatabase database = getDatabase();
    return database.getCollection(configuration.getMongoDbPackagesCollectionName()).find(new Document(STATUS, formStatus))
      .limit(count).sort(new Document("date", 1));
  }

  /**
   * Get the forms collection.
   *
   * @return MongoCollection
   */
  public MongoCollection<Document> getFormsCollection() {
    MongoDatabase database = getDatabase();
    return database.getCollection(configuration.getMongoDbFormsCollectionName());
  }


  /**
   * Get the forms collection by packageId.
   *
   * @return MongoCollection
   */
  public FindIterable<Document> getFormsCollectionByPackageId(long packageId) {
    MongoDatabase database = getDatabase();
    return database.getCollection(configuration.getMongoDbFormsCollectionName()).find(new Document(PACKAGE_IDENTIFIER, packageId));
  }

  /**
   * Get the forms collection by status.
   *
   * @return MongoCollection
   */
  public FindIterable<Document> getFormsCollectionByStatus(FormStatus status) {
    MongoDatabase database = getDatabase();
    return database.getCollection(configuration.getMongoDbPackagesCollectionName()).find(new Document(STATUS, status.toString()
      .toLowerCase()));
  }

  /**
   * Store a transformed package in the mongo database.
   * Package and forms stored separately but linked by the packageIdentifier.
   *
   * @param transformedPackage package to store
   */
  public void storeFormsPackage(FormsPackage transformedPackage) {
    //TODO verify save was successful

    // add package to db
    Document packageMetaDataDocument = Document.parse(transformedPackage.getPackageMetaData());
    getPackagesCollection().insertOne(packageMetaDataDocument);

    // add each form to db
    for (String form : transformedPackage.getForms()) {
      Document transformedForm = Document.parse(form);
      getFormsCollection().insertOne(transformedForm);
    }
  }

//    private FormsPackage getPackageRequest(int count, String requestS//t//atus){
//
//        FindIterable<Document> documents = getPackagesCollectionByStatus(requestStatus, //c//o//u//n//t);
//
//
//
//
//        //get all packages matching c//r//iteria
//
//        //for each //p//ackage
//
//        //search the database fo//r// forms
//
//        //package the matching forms and send t//o// chips
//
//        //////r//e//p//eat
//
//
//
//
//    }

  private MongoClient setupMongoClient() {
    String uri = configuration.getMongoDbUri();
    MongoClientURI mongoUri = new MongoClientURI(uri);
    return new MongoClient(mongoUri);
  }

  public void dropCollection(String collection) {
    MongoDatabase database = getDatabase();
    database.getCollection(collection).drop();
  }
}
