package com.ch.helpers;

import com.ch.configuration.FormsServiceConfiguration;
import com.ch.model.FormsPackage;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

/**
 * Created by elliott.jenkins on 30/06/2016.
 */
public class MongoHelper {
    private final MongoClient client;
    private final FormsServiceConfiguration config;

    public MongoHelper(MongoClient client, FormsServiceConfiguration config) {
        this.client = client;
        this.config = config;
    }

    /**
     * Store a transformed package in the mongo database.
     * Package and forms stored separately but linked by the packageIdentifier.
     *
     * @param transformedPackage package to store
     */
    public void storeFormsPackage(FormsPackage transformedPackage) {
        // add package to db
        Document packageMetaDataDocument = Document.parse(transformedPackage.getPackageMetaData());
        getPackagesCollection().insertOne(packageMetaDataDocument);

        // add each form to db
        for (String form : transformedPackage.getForms()) {
            Document transformedForm = Document.parse(form);
            getFormsCollection().insertOne(transformedForm);
        }
    }

    private MongoDatabase getDatabase() {
        return client.getDatabase(config.getMongoDbName());
    }

    private MongoCollection<Document> getPackagesCollection() {
        MongoDatabase database = getDatabase();
        return database.getCollection(config.getMongoDbPackagesCollectionName());
    }

    private MongoCollection<Document> getFormsCollection() {
        MongoDatabase database = getDatabase();
        return database.getCollection(config.getMongoDbFormsCollectionName());
    }
}