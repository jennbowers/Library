package com.jennbowers.library.classes;

//Set up information and code came from Google documentation and sample code at:
//https://github.com/google/google-api-java-client-samples/blob/master/books-cmdline-sample/src/main/java/com/google/api/services/samples/books/cmdline/BooksSample.java

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.books.Books;
import com.google.api.services.books.BooksRequestInitializer;
import com.google.api.services.books.model.Volumes;

public class GoogleBookRequestBuilder {

    public static final String APPLICATION_NAME = "JennBowers-LibraryApp";

        public static Volumes queryGoogleBooks(JsonFactory jsonFactory, String query) throws Exception {

            ClientCredentials.errorIfNotSpecified();

//            Set up Books Client
            final Books books = new Books.Builder(GoogleNetHttpTransport.newTrustedTransport(), jsonFactory, null)
                    .setApplicationName(APPLICATION_NAME)
                    .setGoogleClientRequestInitializer(new BooksRequestInitializer(ClientCredentials.API_KEY))
                    .build();

//            Set the query string
            System.out.println("Query: [" + query + "]");
            Books.Volumes.List volumesList = books.volumes().list(query);
            volumesList.setStartIndex((long)0);
            volumesList.setMaxResults((long)40);
            System.out.println("volumeslist" + volumesList);
//            Execute the query
            Volumes volumes = volumesList.execute();
            if (volumes.getTotalItems() == 0 || volumes.getItems() == null) {
                System.out.println("No matches found.");

            }
            return volumes;
        }

//        public static void main(String args, String prefixParam) {
//            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
//            try {
////                Query format: "[<inauthor|intitle>:]<query>"
//                String query = args;
//
//                if (prefixParam != null) {
//                    query = prefixParam + query;
//                }
//
//                try {
//                    queryGoogleBooks(jsonFactory, query);
//                    // Success!
//                    return;
//                } catch (IOException e) {
//                    System.err.println(e.getMessage());
//                }
//            } catch (Throwable t) {
//                t.printStackTrace();
//            }
//
//            System.exit(0);
//        }

}
