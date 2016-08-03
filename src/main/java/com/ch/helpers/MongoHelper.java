package com.ch.helpers;

import com.ch.application.FormServiceConstants;
import com.ch.configuration.FormsServiceConfiguration;
import com.ch.conversion.config.ITransformConfig;
import com.ch.conversion.config.TransformConfig;
import com.ch.exception.DatabaseException;
import com.ch.model.FormStatus;
import com.ch.model.FormsPackage;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by elliott.jenkins on 30/06/2016.
 */
public final class MongoHelper {

  private static MongoHelper instance = new MongoHelper();
  private final ITransformConfig config = new TransformConfig();
  private FormsServiceConfiguration configuration;
  private MongoClient client;


  private MongoHelper() {
  }

  public static void init(FormsServiceConfiguration configuration) {
    instance.setConfiguration(configuration);
  }

  public static void init(FormsServiceConfiguration configuration, MongoClient client) {
    instance.setTestConfiguration(configuration, client);
  }

  public static MongoHelper getInstance() {
    return instance;
  }

  private void setConfiguration(FormsServiceConfiguration configuration) {
    this.configuration = configuration;
    this.client = setupMongoClient();
  }

  private void setTestConfiguration(FormsServiceConfiguration configuration, MongoClient client) {
    this.configuration = configuration;
    this.client = client;
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
  public Document getPackageByPackageId(long packageId) {
    MongoDatabase database = getDatabase();
    return database.getCollection(configuration.getMongoDbPackagesCollectionName())
      .find(new Document(config.getPackageIdentifierElementNameOut(), packageId))
      .first();
  }

  /**
   * Get the packages collection by status, limit number to parameter, sort oldest first. A count of 0, returns all packages.
   *
   * @return MongoCollection
   */
  public FindIterable<Document> getPackagesCollectionByStatus(String formStatus, int count) {
    MongoDatabase database = getDatabase();

    if (count == 0) {
      return database.getCollection(configuration.getMongoDbPackagesCollectionName())
        .find(new Document(config.getFormStatusPropertyNameOut(), formStatus)).sort(new Document("date", 1));
    } else {
      return database.getCollection(configuration.getMongoDbPackagesCollectionName())
        .find(new Document(config.getFormStatusPropertyNameOut(), formStatus))
        .limit(count).sort(new Document("date", 1));
    }
  }

  /**
   * update a package status by package Id.
   *
   * @return MongoCollection
   */
  public boolean updatePackageStatusByPackageId(long packageId, String formStatus) {
    MongoDatabase database = getDatabase();

    long modifiedCount = database.getCollection(configuration.getMongoDbPackagesCollectionName()).updateOne(new Document(
      config.getPackageIdentifierElementNameOut(), packageId), new Document("$set",
      new Document(config.getFormStatusPropertyNameOut(), formStatus))).getModifiedCount();

    if (modifiedCount == 1) {
      return true;
    }
    return false;
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
    return database.getCollection(configuration.getMongoDbFormsCollectionName()).find(new Document(config
      .getPackageIdentifierElementNameOut(), packageId));
  }

  /**
   * Get the forms collection by status.
   *
   * @return MongoCollection
   */
  public FindIterable<Document> getFormsCollectionByStatus(FormStatus status) {
    MongoDatabase database = getDatabase();
    return database.getCollection(configuration.getMongoDbFormsCollectionName())
      .find(new Document(config.getFormStatusPropertyNameOut(), status.toString().toLowerCase()));
  }

  /**
   * Get the forms collection by package id and status.
   *
   * @return MongoCollection
   */
  public FindIterable<Document> getFormsCollectionByPackageIdAndStatus(long packageId, String status) {
    MongoDatabase database = getDatabase();
    return database.getCollection(configuration.getMongoDbFormsCollectionName())
      .find(new Document(config.getFormStatusPropertyNameOut(), status.toUpperCase(Locale.ENGLISH))
        .append(config.getPackageIdentifierElementNameOut(), packageId));
  }

  /**
   * update matching forms status by package Id.
   *
   * @return MongoCollection
   */
  public boolean updateFormsStatusByPackageId(long packageId, String formStatus) {
    MongoDatabase database = getDatabase();

    long modifiedCount = database.getCollection(configuration.getMongoDbFormsCollectionName()).updateMany(new Document(
        config.getPackageIdentifierElementNameOut(), packageId),
      new Document("$set", new Document(config.getFormStatusPropertyNameOut(), formStatus))).getModifiedCount();

    if (modifiedCount == 1) {
      return true;
    }
    return false;
  }

  /**
   * update matching form status by package Id.
   *
   * @return MongoCollection
   */
  public boolean updateFormStatusByPackageId(ObjectId formId, String formStatus) {
    MongoDatabase database = getDatabase();

    long modifiedCount = database.getCollection(configuration.getMongoDbFormsCollectionName()).updateMany(new Document(
      FormServiceConstants.DATABASE_OBJECTID_KEY, formId), new Document("$set", new Document(config.getFormStatusPropertyNameOut(),
      formStatus))).getModifiedCount();

    if (modifiedCount == 1) {
      return true;
    }
    return false;
  }


  /**
   * Store a transformed package in the mongo database.
   * Package and forms stored separately but linked by the packageIdentifier.
   *
   * @param transformedPackage package to store
   */
  public boolean storeFormsPackage(FormsPackage transformedPackage) {

    List<String> forms = transformedPackage.getForms();

    // add package to db
    Document packageMetaDataDocument = Document.parse(transformedPackage.getPackageMetaData());
    getPackagesCollection().insertOne(packageMetaDataDocument);
    long packageId = packageMetaDataDocument.getInteger(config.getPackageIdentifierElementNameOut());

    if (getPackageByPackageId(packageId) != null) {
      // add each form to db
      for (String form : forms) {
        Document transformedForm = Document.parse(form);
        getFormsCollection().insertOne(transformedForm);
      }
      //check all forms are saved successfully, else throw exception
      if (getFormsCollectionByPackageId(packageId).into(new ArrayList<Document>()).size() != forms.size()) {
        throw new DatabaseException(config.getFormsPropertyNameOut());
      }
      return true;
    }
    //check package is saved successfully, else throw exception
    throw new DatabaseException(FormServiceConstants.PACKAGE_METADATA_NAME);
  }

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
