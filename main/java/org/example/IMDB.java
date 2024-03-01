package org.example;

import org.json.simple.parser.JSONParser;
import org.json.simple.*;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class IMDB {

    private List<User> usersList = null;

    private List<Actor> actorsList = null;

    private List<Request> requestsList = null;

    private List<Production> productionsList = null;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");


    //stuff for Singleton design pattern
    public static IMDB app = null;

    private IMDB() {
        usersList = new ArrayList<User>();
        actorsList = new ArrayList<Actor>();
        requestsList = new ArrayList<Request>();
        productionsList = new ArrayList<Production>();
    }

    public static IMDB getInstance() {
        if (app == null) app = new IMDB();
        return app;
    }

    //getters

    public List<Actor> getActorsList() {
        return actorsList;
    }

    public List<Production> getProductionsList() {
        return productionsList;
    }

    public List<Request> getRequestsList() {
        return requestsList;
    }

    public List<User> getUsersList() {
        return usersList;
    }


    //readind data from json

    public void readDataFromJson() {
        JSONParser parser = new JSONParser();

        //readind the actors from json

        try (FileReader reader = new FileReader("src/main/resources/input/actors.json")) {

            JSONArray arr = (JSONArray) parser.parse(reader);

            for (Object obj : arr) {
                JSONObject object = (JSONObject) obj;

                Actor auxActor = new Actor();

                auxActor.setName((String) object.get("name"));
                auxActor.setBiography((String) object.get("biography"));

                JSONArray perf = new JSONArray();
                perf = (JSONArray) object.get("performances");

                for (Object obj2 : perf) {
                    JSONObject auxPerf = (JSONObject) obj2;
                    auxActor.addPerformances((String) auxPerf.get("title"), (String) auxPerf.get("type"));
                }
                IMDB.getInstance().getActorsList().add(auxActor);
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        //reading productions from json

        try (FileReader reader = new FileReader("src/main/resources/input/production.json")) {

            JSONArray arr = (JSONArray) parser.parse(reader);

            for (Object obj : arr) {
                JSONObject object = (JSONObject) obj;
                if (object.get("type").equals("Movie")) {
                    Movie auxMovie = new Movie();
                    auxMovie.setTitle((String) object.get("title"));

                    JSONArray directorsArray = (JSONArray) object.get("directors");
                    for (Object directorObj : directorsArray) {
                        auxMovie.addDirector((String) directorObj);
                    }

                    JSONArray actorsArray = (JSONArray) object.get("actors");
                    for (Object actorObj : actorsArray) {
                        auxMovie.addActor((String) actorObj);
                    }

                    JSONArray genresArray = (JSONArray) object.get("genres");
                    for (Object genreObj : genresArray) {
                        auxMovie.addGenre(FindInEnums.findGenre((String) genreObj));
                    }

                    auxMovie.setDescription((String) object.get("plot"));

                    auxMovie.setGrade((Double) object.get("averageRating"));

                    String textDuration = (String) object.get("duration");
                    String numericDuration = textDuration.replaceAll("[^0-9]", "");
                    auxMovie.setDuration(Integer.parseInt(numericDuration));

                    if (((Number) object.get("releaseYear")) == null) {
                        auxMovie.setReleaseYear(-1);
                    } else {
                        auxMovie.setReleaseYear(((Number) object.get("releaseYear")).intValue());
                    }

                    JSONArray ratingsArray = (JSONArray) object.get("ratings");
                    for (Object ratingObj : ratingsArray) {
                        auxMovie.addRating(new Rating((String) ((JSONObject) ratingObj).get("username"),
                                ((Number) ((JSONObject) ratingObj).get("rating")).intValue(),
                                (String) ((JSONObject) ratingObj).get("comment")));
                    }


                    productionsList.add(auxMovie);

                } else {
                    Series auxSeries = new Series();
                    auxSeries.setTitle((String) object.get("title"));

                    JSONArray directorsArray = (JSONArray) object.get("directors");
                    for (Object directorObj : directorsArray) {
                        auxSeries.addDirector((String) directorObj);
                    }

                    JSONArray actorsArray = (JSONArray) object.get("actors");
                    for (Object actorObj : actorsArray) {
                        auxSeries.addActor((String) actorObj);
                    }

                    JSONArray genresArray = (JSONArray) object.get("genres");
                    for (Object genreObj : genresArray) {
                        auxSeries.addGenre(FindInEnums.findGenre((String) genreObj));
                    }

                    auxSeries.setDescription((String) object.get("plot"));

                    auxSeries.setGrade((Double) object.get("averageRating"));

                    if (((Number) object.get("releaseYear")) == null) {
                        auxSeries.setReleaseYear(-1);
                    } else {
                        auxSeries.setReleaseYear(((Number) object.get("releaseYear")).intValue());
                    }

                    JSONArray ratingsArray = (JSONArray) object.get("ratings");
                    for (Object ratingObj : ratingsArray) {
                        auxSeries.addRating(new Rating((String) ((JSONObject) ratingObj).get("username"),
                                ((Number) ((JSONObject) ratingObj).get("rating")).intValue(),
                                (String) ((JSONObject) ratingObj).get("comment")));
                    }

                    auxSeries.setNumOfSeasons(((Number) object.get("numSeasons")).intValue());

                    JSONObject seasonsObject = (JSONObject) object.get("seasons");
                    for (int i = 1; i <= auxSeries.getNumOfSeasons(); i++) {
                        String auxSeasonName = "Season " + i;

                        JSONArray episodesArray = (JSONArray) seasonsObject.get(auxSeasonName);
                        ArrayList<Episode> auxEpisodeList = new ArrayList<Episode>();

                        for (Object episodeObj : episodesArray) {
                            JSONObject episodeObject = (JSONObject) episodeObj;
                            auxEpisodeList.add(new Episode(((String) episodeObject.get("episodeName")),
                                    Integer.parseInt(((String) episodeObject.get("duration")).replaceAll("\\D+", ""))));
                        }
                        auxSeries.addSeason(auxSeasonName, auxEpisodeList);
                    }
                    productionsList.add(auxSeries);
                }


            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        //reading the requests from json


        try (FileReader reader = new FileReader("src/main/resources/input/requests.json")) {

            JSONArray arr = (JSONArray) parser.parse(reader);

            for (Object obj : arr) {
                Request auxRequest = new Request();
                JSONObject object = (JSONObject) obj;

                auxRequest.setRequestType(FindInEnums.findReqest((String) object.get("type")));

                auxRequest.setCreatedDate(LocalDateTime.parse(((String) object.get("createdDate")), formatter));

                auxRequest.setUsername((String) object.get("username"));

                auxRequest.setTo((String) object.get("to"));

                auxRequest.setDesctiption((String) object.get("description"));

                if (auxRequest.getRequestType().equals(RequestType.ACTOR_ISSUE)) {
                    auxRequest.setProblemName((String) object.get("actorName"));
                }

                if (auxRequest.getRequestType().equals(RequestType.MOVIE_ISSUE)) {
                    auxRequest.setProblemName((String) object.get("movieTitle"));
                }

                IMDB.getInstance().getRequestsList().add(auxRequest);
            }


        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        //reading the users from json

        try (FileReader reader = new FileReader("src/main/resources/input/accounts.json")) {

            JSONArray arr = (JSONArray) parser.parse(reader);

            for (Object obj : arr) {
                JSONObject object = (JSONObject) obj;

                User auxUser = UserFactory.createUser((String) object.get("username"), (String) object.get("userType"));

                auxUser.setUserType(FindInEnums.findAccountType((String) object.get("userType")));

                if (object.get("username") == "")
                    throw new InformationIncompleteException("Users can not have empty username");
                auxUser.setUsername((String) object.get("username"));
                if (object.get("experience") != null)
                    auxUser.setExperience(Integer.parseInt((String) object.get("experience")));
                else auxUser.setExperience(0);

                auxUser.setInformation(new User.Information.InformationBuilder((String) ((JSONObject) object.get("information")).get("name"), (String) ((JSONObject) ((JSONObject) object.get("information")).get("credentials")).get("email"), (String) ((JSONObject) ((JSONObject) object.get("information")).get("credentials")).get("password"))
                        .age(((Number) ((JSONObject) object.get("information")).get("age")).intValue())
                        .country((String) ((JSONObject) object.get("information")).get("country"))
                        .gender((String) ((JSONObject) object.get("information")).get("gender"))
                        .birthDate(LocalDateTime.parse((String) ((JSONObject) object.get("information")).get("birthDate") + "T00:00:00", formatter))
                        .build());


                JSONArray favProdArr = (JSONArray) object.get("favoriteProductions");
                if (favProdArr != null) {
                    for (Object favProdObj : favProdArr) {
                        for (Production prodAux : IMDB.getInstance().getProductionsList()) {
                            if (prodAux.getTitle().equals(favProdObj)) auxUser.addLikedItem(prodAux);
                        }
                    }
                }

                JSONArray favActorsArr = (JSONArray) object.get("favoriteActors");
                if (favActorsArr != null) {
                    for (Object favActorObj : favActorsArr) {
                        for (Actor actorAux : IMDB.getInstance().getActorsList()) {
                            if (actorAux.getName().equals(favActorObj)) auxUser.addLikedItem(actorAux);
                        }
                    }
                }

                if (auxUser.getUserType() == AccountType.Contributor || auxUser.getUserType() == AccountType.Admin) {
                    JSONArray addedProdArr = (JSONArray) object.get("productionsContribution");
                    if (addedProdArr != null) {
                        for (Object addedProdObj : addedProdArr) {
                            for (Production prodAux : IMDB.getInstance().getProductionsList()) {
                                if (prodAux.getTitle().equals(addedProdObj)) {
                                    ((Staff) auxUser).addListUserAded(prodAux);
                                    //prodAux.attach(auxUser);
                                }
                            }
                        }
                    }

                    JSONArray addedActorsArr = (JSONArray) object.get("actorsContribution");
                    if (addedActorsArr != null) {
                        for (Object addedActorObj : addedActorsArr) {
                            for (Actor actorAux : IMDB.getInstance().getActorsList()) {
                                if (actorAux.getName().equals(addedActorObj))
                                    ((Staff) auxUser).addListUserAded(actorAux);
                            }
                        }
                    }
                }

                JSONArray notificationsArr = (JSONArray) object.get("notifications");
                if (notificationsArr != null) {
                    for (Object notificationObj : notificationsArr) {
                        auxUser.getNotifications().add((String) notificationObj);
                    }
                }


                IMDB.getInstance().getUsersList().add(auxUser);
            }

        } catch (
                FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } catch (InformationIncompleteException e) {
            throw new RuntimeException(e);
        }

    }

    public void sortRequestsFronJson() {
        for (Request auxReq : requestsList) {
            if (auxReq.getTo().equals("ADMIN")) Admin.RequestHolder.addRequest(auxReq);
            else {
                for (User auxUser : usersList) {
                    if (auxUser.getUsername().equals(auxReq.getTo()))
                        ((Staff) auxUser).addListRequestForUser(auxReq);
                }
            }
        }
    }

    public void runInCLI() throws InterruptedException, InformationIncompleteException, InvalidCommandException {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        String input, input2, input3;
        User currentUser = null;
        boolean found, show;
        int counter, search;

        while (running) {
            if (currentUser == null) {
                System.out.println("Welcome to IMDB! Enter your credentials\n");
                System.out.println("Let's log in!");
                System.out.print("Email: ");
                input = scanner.nextLine();
                System.out.print("Password: ");
                input2 = scanner.nextLine();

                for (User auxUser : IMDB.getInstance().getUsersList()) {
                    if (auxUser.getInformation().getCredentials().getEmail().equals(input) && auxUser.getInformation().getCredentials().getPassword().equals(input2)) {
                        currentUser = auxUser;
                        System.out.println("Welcome back, " + currentUser.getUsername());
                    }
                }

                if (currentUser == null) {
                    System.out.println("Incorrect email or password. Try again!");
                    Thread.sleep(2000);
                    for (int i = 0; i < 30; i++) System.out.println();
                }
            } else if (currentUser != null) {
                if (currentUser.getUserType() == AccountType.Regular) {
                    System.out.println("Username: " + currentUser.getUsername());
                    System.out.println("Experience: " + currentUser.getExperience());
                    System.out.println("Choose action: ");
                    System.out.println("    1)  View all productions details");
                    System.out.println("    2)  Search for a production");
                    System.out.println("    3)  View all actors details");
                    System.out.println("    4)  Search for an actor");
                    System.out.println("    5)  Show favorite list");
                    System.out.println("    6)  Add something to the favorites list");
                    System.out.println("    7)  Remove something from the favorites list");
                    System.out.println("    8)  Create a request");
                    System.out.println("    9)  Delete a request");
                    System.out.println("    10) Write a review for a production");
                    System.out.println("    11) Remove your review from a production");
                    System.out.println("    12) Display notifications");
                    System.out.println("    13) Clear notifications");
                    System.out.println("    14) Logout");
                    System.out.println("    15) Exit");
                    System.out.print("Your choice: ");
                    input = scanner.nextLine();
                    switch (input) {
                        case "1":
                            System.out.println("Do you want to display productions in alphabetical order?\n1) Yes\n2) No");
                            input = scanner.nextLine();
                            if (input.equals("1")) {
                                Collections.sort(IMDB.getInstance().getProductionsList());
                            }
                            System.out.println("Are you looking for specific genres?");
                            input = scanner.nextLine();
                            String genresToSearch[] = input.split(", ");

                            System.out.print("Minimum number of reviews for productions: ");
                            input2 = scanner.nextLine();
                            search = Integer.parseInt(input2);
                            for (Production auxProd : IMDB.getInstance().getProductionsList()) {
                                show = false;
                                if (input == "") show = true;
                                for (String auxString : genresToSearch) {
                                    if (auxProd.getGenres().contains(FindInEnums.findGenre(input))) show = true;
                                }
                                if (auxProd.getRatings().size() < search) show = false;
                                if (show) System.out.println(auxProd);
                            }
                            break;
                        case "2":
                            System.out.println("What are you looking for?");
                            input = scanner.nextLine();
                            found = false;
                            for (Production auxProd : IMDB.getInstance().getProductionsList()) {
                                if (auxProd.getTitle().toLowerCase().equals(input.toLowerCase())) {
                                    System.out.println(auxProd);
                                    found = true;
                                }
                            }
                            if (found == false)
                                System.out.println("No production found. Check spelling or submit a request for adding the actor in the system.");
                            break;
                        case "3":
                            System.out.println("What order do you want?\n1) Random\n2) Alphabetical");
                            input = scanner.nextLine();
                            if (input.equals("1")) {
                                for (Actor auxActor : IMDB.getInstance().getActorsList()) {
                                    System.out.println(auxActor);
                                }
                            } else {
                                Collections.sort(IMDB.getInstance().getActorsList());
                                for (Actor auxActor : IMDB.getInstance().getActorsList()) {
                                    System.out.println(auxActor);
                                }
                            }
                            break;
                        case "4":
                            System.out.println("Who are you looking for?");
                            input = scanner.nextLine();
                            found = false;
                            for (Actor auxActor : IMDB.getInstance().getActorsList()) {
                                if (auxActor.getName().toLowerCase().equals(input.toLowerCase())) {
                                    System.out.println(auxActor);
                                    found = true;
                                }
                            }
                            if (found == false)
                                System.out.println("No actor found. Check spelling or submit a request for adding the actor in the system.");
                            break;
                        case "5":
                            int actorCounter = 1, productionCounter = 1;
                            System.out.println("\nFavorite actors: ");
                            for (LikableItems item : currentUser.getLikedItems()) {
                                if (item.getClass() == Actor.class) {
                                    System.out.println(actorCounter + ") " + item.giveName());
                                    actorCounter += 1;
                                }
                            }
                            System.out.println("\nFavorite productions: ");
                            for (LikableItems item : currentUser.getLikedItems()) {
                                if (item.getClass() == Movie.class || item.getClass() == Series.class) {
                                    System.out.println(productionCounter + ") " + item.giveName());
                                    productionCounter += 1;
                                }
                            }
                            break;
                        case "6":
                            System.out.println("Give the name of your new favorite production or actor");
                            input = scanner.nextLine();
                            found = false;
                            for (Production auxProd : IMDB.getInstance().getProductionsList()) {
                                if (auxProd.getTitle().toLowerCase().equals(input.toLowerCase())) {
                                    currentUser.addLikedItem(auxProd);
                                    found = true;
                                    System.out.println("Production added to your favorite list");
                                }
                            }
                            for (Actor auxActor : IMDB.getInstance().getActorsList()) {
                                if (auxActor.getName().toLowerCase().equals(input.toLowerCase())) {
                                    currentUser.addLikedItem(auxActor);
                                    found = true;
                                    System.out.println("Actor added to your favorite list");
                                }
                            }

                            if (found == false) {
                                System.out.println("No actor or production with this name foud in the system");
                                System.out.println("Check your spelling or make a request for the actor or production to be added to the system");
                            }
                            break;
                        case "7":
                            System.out.println("Give the name of the favorite production or actor you want to remove");
                            input = scanner.nextLine();
                            found = false;
                            LikableItems itemToRemove = null;
                            for (LikableItems auxLikableItem : currentUser.getLikedItems()) {
                                if (auxLikableItem.giveName().toLowerCase().equals(input.toLowerCase())) {
                                    itemToRemove = auxLikableItem;
                                    found = true;
                                    System.out.println("Item removed from liked list");
                                }
                            }
                            if (found == false) {
                                System.out.println("No item with this name found. Check the spelling.");
                            } else currentUser.removeLikedItem(itemToRemove);
                            break;
                        case "8":
                            Request auxReqest = new Request();
                            System.out.println("Type of request:\n1)Delete account\n2)Actor Issue\n3)Movie Issue\n4)Other");
                            input = scanner.nextLine();
                            if (input.equals("1")) {
                                auxReqest.setTo("ADMIN");
                                auxReqest.setCreatedDate(LocalDateTime.now());
                                auxReqest.setUsername(currentUser.getUsername());
                                auxReqest.setRequestType(RequestType.DELETE_ACCOUNT);
                                auxReqest.setProblemName(null);
                                System.out.println("Describe the problem:");
                                input = scanner.nextLine();
                                auxReqest.setDesctiption(input);
                            }
                            if (input.equals("2")) {
                                auxReqest.setRequestType(RequestType.ACTOR_ISSUE);
                                auxReqest.setCreatedDate(LocalDateTime.now());
                                auxReqest.setUsername(currentUser.getUsername());
                                System.out.println("Who are you looking for?");
                                input = scanner.nextLine();
                                found = false;
                                for (User auxUser : getUsersList()) {
                                    if (auxUser.getUserType() == AccountType.Contributor || auxUser.getUserType() == AccountType.Admin) {
                                        for (LikableItems auxItem : ((Staff) auxUser).getListUserAdded()) {
                                            if (auxItem.giveName().toLowerCase().equals(input.toLowerCase())) {
                                                auxReqest.setTo(auxUser.getUsername());
                                                auxReqest.setProblemName(auxItem.giveName());
                                                found = true;
                                            }
                                        }
                                    }
                                }
                                if (found == false) {
                                    System.out.println("No actor with this name found. Check your spelling.");
                                    break;
                                }
                                System.out.println("What is the problem?");
                                input = scanner.nextLine();
                                auxReqest.setDesctiption(input);
                            }
                            if (input.equals("3")) {
                                auxReqest.setRequestType(RequestType.MOVIE_ISSUE);
                                auxReqest.setCreatedDate(LocalDateTime.now());
                                auxReqest.setUsername(currentUser.getUsername());
                                System.out.println("What production are you looking for?");
                                input = scanner.nextLine();
                                found = false;
                                for (User auxUser : getUsersList()) {
                                    if (auxUser.getUserType() == AccountType.Contributor || auxUser.getUserType() == AccountType.Admin) {
                                        for (LikableItems auxItem : ((Staff) auxUser).getListUserAdded()) {
                                            if (auxItem.giveName().toLowerCase().equals(input.toLowerCase())) {
                                                auxReqest.setTo(auxUser.getUsername());
                                                auxReqest.setProblemName(auxItem.giveName());
                                                found = true;
                                            }
                                        }
                                    }
                                }
                                if (found == false) {
                                    System.out.println("No production with this name found. Check your spelling.");
                                    break;
                                }
                                System.out.println("What is the problem?");
                                input = scanner.nextLine();
                                auxReqest.setDesctiption(input);
                            }
                            if (input.equals("4")) {
                                auxReqest.setRequestType(RequestType.OTHERS);
                                auxReqest.setCreatedDate(LocalDateTime.now());
                                auxReqest.setUsername(currentUser.getUsername());
                                auxReqest.setTo("ADMIN");
                                System.out.println("Describe your problem:");
                                input = scanner.nextLine();
                                auxReqest.setDesctiption(input);
                            }
                            if (auxReqest.getRequestType() != null) {
                                auxReqest.attach(currentUser);
                                if (auxReqest.getRequestType() == RequestType.OTHERS || auxReqest.getRequestType() == RequestType.DELETE_ACCOUNT) {
                                    for (User auxUser : IMDB.getInstance().getUsersList()) {
                                        if (auxUser.getUserType() == AccountType.Admin) auxReqest.attach(auxUser);
                                    }
                                    auxReqest.notifyObservers("You have a request that you can solve from " + currentUser.getUsername(), "receiver");
                                } else {
                                    for (User auxUser : IMDB.getInstance().getUsersList()) {
                                        if (auxUser.getUsername() == auxReqest.getTo()) auxReqest.attach(auxUser);
                                    }
                                    auxReqest.notifyObservers("You have a request that you can solve from " + currentUser.getUsername(), "receiver");
                                }
                                ((Regular) currentUser).createRequest(auxReqest);
                                System.out.println("Request added to the system");
                            } else System.out.println("Not a valid input. Try again.");
                            break;
                        case "9":
                            Request reqToBeDeleted = null;
                            System.out.println("What kind of request do you want to delete?");
                            System.out.println("1)Delete account\n2)Actor issue\n3)Production Issue\n4)Other");
                            input = scanner.nextLine();
                            if (input.equals("1")) {
                                for (Request req : Admin.RequestHolder.getRequestsList()) {
                                    if (req.getUsername().equals(currentUser.getUsername()) && req.getRequestType() == RequestType.DELETE_ACCOUNT) {
                                        reqToBeDeleted = req;
                                    }
                                }
                                if (reqToBeDeleted == null) {
                                    System.out.println("You have no request like that.");
                                    break;
                                }
                            }
                            if (input.equals("2")) {
                                counter = 0;
                                System.out.println("What request for an actor do you want to delete?");
                                for (User auxUser : IMDB.getInstance().getUsersList()) {
                                    if (auxUser.getUserType() != AccountType.Regular) {
                                        for (Request auxRequest : ((Staff) auxUser).getListRequestForUser()) {
                                            if (auxRequest.getUsername().equals(currentUser.getUsername()) && auxRequest.getRequestType() == RequestType.ACTOR_ISSUE) {
                                                counter += 1;
                                                System.out.println(counter + ") " + auxRequest.getProblemName());
                                            }
                                        }
                                    }
                                }
                                if (counter == 0) {
                                    System.out.println("You have no actor requests");
                                } else {
                                    counter = 0;
                                    System.out.print("Your choice: ");
                                    input = scanner.nextLine();
                                    search = Integer.parseInt(input);
                                    for (User auxUser : IMDB.getInstance().getUsersList()) {
                                        if (auxUser.getUserType() != AccountType.Regular) {
                                            for (Request auxRequest : ((Staff) auxUser).getListRequestForUser()) {
                                                if (auxRequest.getUsername().equals(currentUser.getUsername()) && auxRequest.getRequestType() == RequestType.ACTOR_ISSUE) {
                                                    counter += 1;
                                                    if (counter == search) reqToBeDeleted = auxRequest;
                                                }
                                            }
                                        }
                                    }

                                }
                            }
                            if (input.equals("3")) {
                                counter = 0;
                                System.out.println("What request for a production do you want to delete?");
                                for (User auxUser : IMDB.getInstance().getUsersList()) {
                                    if (auxUser.getUserType() != AccountType.Regular) {
                                        for (Request auxRequest : ((Staff) auxUser).getListRequestForUser()) {
                                            if (auxRequest.getUsername().equals(currentUser.getUsername()) && auxRequest.getRequestType() == RequestType.MOVIE_ISSUE) {
                                                counter += 1;
                                                System.out.println(counter + ") " + auxRequest.getProblemName());
                                            }
                                        }
                                    }
                                }
                                if (counter == 0) {
                                    System.out.println("You have no production requests");
                                    break;
                                } else {
                                    counter = 0;
                                    System.out.print("Your choice: ");
                                    input = scanner.nextLine();
                                    search = Integer.parseInt(input);
                                    for (User auxUser : IMDB.getInstance().getUsersList()) {
                                        if (auxUser.getUserType() != AccountType.Regular) {
                                            for (Request auxRequest : ((Staff) auxUser).getListRequestForUser()) {
                                                if (auxRequest.getUsername().equals(currentUser.getUsername()) && auxRequest.getRequestType() == RequestType.MOVIE_ISSUE) {
                                                    counter += 1;
                                                    if (counter == search) reqToBeDeleted = auxRequest;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            if (input.equals("4")) {
                                counter = 0;
                                System.out.println("What request do you want to delete?");
                                for (Request auxRequest : Admin.RequestHolder.getRequestsList()) {
                                    if (auxRequest.getRequestType() == RequestType.OTHERS && auxRequest.getUsername().equals(currentUser.getUsername())) {
                                        counter += 1;
                                        System.out.println(counter + ") " + auxRequest.getDesctiption());
                                    }
                                }
                                if (counter == 0) {
                                    System.out.println("You have no request");
                                    break;
                                } else {
                                    counter = 0;
                                    System.out.println("What request do you want to delete?");
                                    input = scanner.nextLine();
                                    search = Integer.parseInt(input);
                                    for (Request auxRequest : Admin.RequestHolder.getRequestsList()) {
                                        if (auxRequest.getRequestType() == RequestType.OTHERS && auxRequest.getUsername().equals(currentUser.getUsername())) {
                                            counter += 1;
                                            if (counter == search) reqToBeDeleted = auxRequest;
                                        }
                                    }
                                }
                            }
                            if (reqToBeDeleted != null) {
                                reqToBeDeleted.detach(currentUser);
                                ((Regular) currentUser).removeRequest(reqToBeDeleted);
                                System.out.println("Request removed successfully");
                            } else System.out.println("Not a valid input. Try again");
                            break;
                        case "10":
                            System.out.println("What production do you want to rate?");
                            input = scanner.nextLine();
                            found = false;
                            for (Production auxProd : IMDB.getInstance().getProductionsList()) {
                                if (auxProd.getTitle().toLowerCase().equals(input.toLowerCase())) {
                                    found = true;
                                    System.out.println("What grade do you give to the production(from 1 to 10)?");
                                    input = scanner.nextLine();
                                    System.out.println("Do you want to leave a comment?");
                                    input2 = scanner.nextLine();
                                    Rating auxRating = new Rating(currentUser.getUsername(), Integer.parseInt(input), input2);
                                    auxProd.addRating(auxRating);
                                    auxProd.attach(currentUser);
                                    auxProd.notifyObservers("User " + currentUser.getUsername() + " added a review for " + auxProd.getTitle() + " and it gave it a " + auxRating.getGrade(), "emitter");
                                    auxProd.notifyObservers("User " + currentUser.getUsername() + " added a review for the production you added in the system(" + auxProd.getTitle() + ").", "receiver");
                                }
                            }

                            if (found == false)
                                System.out.println("No production with this name found. Check spelling and try again.");
                            else {
                                System.out.println("Review added.");
                                currentUser.changeExperience(new ExperienceFromRating());
                            }
                            break;
                        case "11":
                            System.out.println("What production do you want to delete your review from?");
                            found = false;
                            input = scanner.nextLine();
                            int i;
                            for (Production auxProd : IMDB.getInstance().getProductionsList()) {
                                if (auxProd.getTitle().toLowerCase().equals(input.toLowerCase())) {
                                    for (i = 0; i < auxProd.getRatings().size(); i++) {
                                        if (auxProd.getRatings().get(i).getUserName().equals(currentUser.getUsername())) {
                                            found = true;
                                            break;
                                        }
                                    }
                                    if (i < auxProd.getRatings().size()) {
                                        auxProd.getRatings().remove(i);
                                        auxProd.setGrade();
                                        auxProd.detach(currentUser);
                                        System.out.println("Review removed successfully");
                                    }
                                }
                            }
                            if (found == false)
                                System.out.println("You have no review for this production or the production does not exist");
                            break;
                        case "12":
                            if (currentUser.getNotifications().size() == 0)
                                System.out.println("You have no notifications");
                            else {
                                for (String auxNotif : currentUser.getNotifications()) {
                                    System.out.println(auxNotif);
                                }
                            }
                            break;
                        case "13":
                            currentUser.getNotifications().clear();
                            System.out.println("Notifications cleared.");
                            break;
                        case "14":
                            System.out.println("Logging out...");
                            currentUser = null;
                            break;
                        case "15":
                            System.out.println("App closing. Bye!!!");
                            running = false;
                            break;
                        default:
                            System.out.println("That is not a valid command. Try again.");
                            throw new InvalidCommandException("Invalid command");

                    }
                    input = scanner.nextLine();
                }
                if ((currentUser != null) && (currentUser.getUserType() == AccountType.Contributor)) {
                    System.out.println("Username: " + currentUser.getUsername());
                    System.out.println("Experience: " + currentUser.getExperience());
                    System.out.println("Choose action: ");
                    System.out.println("    1)  View all productions details");
                    System.out.println("    2)  Search for a production");
                    System.out.println("    3)  View all actors details");
                    System.out.println("    4)  Search for an actor");
                    System.out.println("    5)  Show favorite list");
                    System.out.println("    6)  Add something to the favorites list");
                    System.out.println("    7)  Remove something from the favorites list");
                    System.out.println("    8)  Create a request");
                    System.out.println("    9)  Delete a request");
                    System.out.println("    10) Add a production to the system");
                    System.out.println("    11) Remove a production from the system");
                    System.out.println("    12) Add an actor in the system");
                    System.out.println("    13) Remove an actor from the system");
                    System.out.println("    14) View your requests");
                    System.out.println("    15) Solve requests");
                    System.out.println("    16) Update production");
                    System.out.println("    17) Update actors");
                    System.out.println("    18) Display notifications");
                    System.out.println("    19) Clear notifications");
                    System.out.println("    20) Logout");
                    System.out.println("    21) Exit");
                    System.out.print("Your choice: ");
                    input = scanner.nextLine();
                    switch (input) {
                        case "1":
                           /* for (Production auxProd : IMDB.getInstance().getProductionsList()) {
                                System.out.println(auxProd + "\n\n");
                            }
                            break;*/
                            System.out.println("Do you want to display productions in alphabetical order?\n1) Yes\n2) No");
                            input = scanner.nextLine();
                            if (input.equals("1")) {
                                Collections.sort(IMDB.getInstance().getProductionsList());
                            }
                            System.out.println("Are you looking for specific genres?");
                            input = scanner.nextLine();
                            String genresToSearch[] = input.split(", ");

                            System.out.print("Minimum number of reviews for productions: ");
                            input2 = scanner.nextLine();
                            search = Integer.parseInt(input2);
                            for (Production auxProd : IMDB.getInstance().getProductionsList()) {
                                show = false;
                                if (input == "") show = true;
                                for (String auxString : genresToSearch) {
                                    if (auxProd.getGenres().contains(FindInEnums.findGenre(input))) show = true;
                                }
                                if (auxProd.getRatings().size() < search) show = false;
                                if (show) System.out.println(auxProd);
                            }
                            break;
                        case "2":
                            System.out.println("What are you looking for?");
                            input = scanner.nextLine();
                            found = false;
                            for (Production auxProd : IMDB.getInstance().getProductionsList()) {
                                if (auxProd.getTitle().toLowerCase().equals(input.toLowerCase())) {
                                    System.out.println(auxProd);
                                    found = true;
                                }
                            }
                            if (found == false)
                                System.out.println("No production found. Check spelling or submit a request for adding the actor in the system.");
                            break;
                        case "3":
                            System.out.println("What order do you want?\n1) Random\n2) Alphabetical");
                            input = scanner.nextLine();
                            if (input.equals("1")) {
                                for (Actor auxActor : IMDB.getInstance().getActorsList()) {
                                    System.out.println(auxActor);
                                }
                            } else {
                                Collections.sort(IMDB.getInstance().getActorsList());
                                for (Actor auxActor : IMDB.getInstance().getActorsList()) {
                                    System.out.println(auxActor);
                                }
                            }
                            break;
                        case "4":
                            System.out.println("Who are you looking for?");
                            input = scanner.nextLine();
                            found = false;
                            for (Actor auxActor : IMDB.getInstance().getActorsList()) {
                                if (auxActor.getName().toLowerCase().equals(input.toLowerCase())) {
                                    System.out.println(auxActor);
                                    found = true;
                                }
                            }
                            if (found == false)
                                System.out.println("No actor found. Check spelling or submit a request for adding the actor in the system.");
                            break;
                        case "5":
                            int actorCounter = 1, productionCounter = 1;
                            System.out.println("\nFavorite actors: ");
                            for (LikableItems item : currentUser.getLikedItems()) {
                                if (item.getClass() == Actor.class) {
                                    System.out.println(actorCounter + ") " + item.giveName());
                                    actorCounter += 1;
                                }
                            }
                            System.out.println("\nFavorite productions: ");
                            for (LikableItems item : currentUser.getLikedItems()) {
                                if (item.getClass() == Movie.class || item.getClass() == Series.class) {
                                    System.out.println(productionCounter + ") " + item.giveName());
                                    productionCounter += 1;
                                }
                            }
                            break;
                        case "6":
                            System.out.println("Give the name of your new favorite production or actor");
                            input = scanner.nextLine();
                            found = false;
                            for (Production auxProd : IMDB.getInstance().getProductionsList()) {
                                if (auxProd.getTitle().toLowerCase().equals(input.toLowerCase())) {
                                    currentUser.addLikedItem(auxProd);
                                    found = true;
                                    System.out.println("Production added to your favorite list");
                                }
                            }
                            for (Actor auxActor : IMDB.getInstance().getActorsList()) {
                                if (auxActor.getName().toLowerCase().equals(input.toLowerCase())) {
                                    currentUser.addLikedItem(auxActor);
                                    found = true;
                                    System.out.println("Actor added to your favorite list");
                                }
                            }

                            if (found == false) {
                                System.out.println("No actor or production with this name found in the system");
                                System.out.println("Check your spelling or make a request for the actor or production to be added to the system");
                            }
                            break;
                        case "7":
                            System.out.println("Give the name of the favorite production or actor you want to remove");
                            input = scanner.nextLine();
                            found = false;
                            LikableItems itemToRemove = null;
                            for (LikableItems auxLikableItem : currentUser.getLikedItems()) {
                                if (auxLikableItem.giveName().toLowerCase().equals(input.toLowerCase())) {
                                    itemToRemove = auxLikableItem;
                                    found = true;
                                    System.out.println("Item removed from liked list");
                                }
                            }
                            if (found == false) {
                                System.out.println("No item with this name found. Check the spelling.");
                            } else currentUser.removeLikedItem(itemToRemove);
                            break;
                        case "8":
                            Request auxReqest = new Request();
                            System.out.println("Type of request:\n1)Delete account\n2)Actor Issue\n3)Movie Issue\n4)Other");
                            input = scanner.nextLine();
                            if (input.equals("1")) {
                                auxReqest.setTo("ADMIN");
                                auxReqest.setCreatedDate(LocalDateTime.now());
                                auxReqest.setUsername(currentUser.getUsername());
                                auxReqest.setRequestType(RequestType.DELETE_ACCOUNT);
                                auxReqest.setProblemName(null);
                                System.out.println("Describe the problem:");
                                input = scanner.nextLine();
                                auxReqest.setDesctiption(input);
                            }
                            if (input.equals("2")) {
                                auxReqest.setRequestType(RequestType.ACTOR_ISSUE);
                                auxReqest.setCreatedDate(LocalDateTime.now());
                                auxReqest.setUsername(currentUser.getUsername());
                                System.out.println("Who are you looking for?");
                                input = scanner.nextLine();
                                found = false;
                                for (User auxUser : getUsersList()) {
                                    if (auxUser.getUserType() == AccountType.Contributor || auxUser.getUserType() == AccountType.Admin) {
                                        for (LikableItems auxItem : ((Staff) auxUser).getListUserAdded()) {
                                            if (auxItem.giveName().toLowerCase().equals(input.toLowerCase())) {
                                                auxReqest.setTo(auxUser.getUsername());
                                                auxReqest.setProblemName(auxItem.giveName());
                                                found = true;
                                            }
                                        }
                                    }
                                }
                                if (found == false) {
                                    System.out.println("No actor with this name found. Check your spelling.");
                                    break;
                                }
                                System.out.println("What is the problem?");
                                input = scanner.nextLine();
                                auxReqest.setDesctiption(input);
                            }
                            if (input.equals("3")) {
                                auxReqest.setRequestType(RequestType.MOVIE_ISSUE);
                                auxReqest.setCreatedDate(LocalDateTime.now());
                                auxReqest.setUsername(currentUser.getUsername());
                                System.out.println("What production are you looking for?");
                                input = scanner.nextLine();
                                found = false;
                                for (User auxUser : getUsersList()) {
                                    if (auxUser.getUserType() == AccountType.Contributor || auxUser.getUserType() == AccountType.Admin) {
                                        for (LikableItems auxItem : ((Staff) auxUser).getListUserAdded()) {
                                            if (auxItem.giveName().toLowerCase().equals(input.toLowerCase())) {
                                                auxReqest.setTo(auxUser.getUsername());
                                                auxReqest.setProblemName(auxItem.giveName());
                                                found = true;
                                            }
                                        }
                                    }
                                }
                                if (found == false) {
                                    System.out.println("No production with this name found. Check your spelling.");
                                    break;
                                }
                                System.out.println("What is the problem?");
                                input = scanner.nextLine();
                                auxReqest.setDesctiption(input);
                            }
                            if (input.equals("4")) {
                                auxReqest.setRequestType(RequestType.OTHERS);
                                auxReqest.setCreatedDate(LocalDateTime.now());
                                auxReqest.setUsername(currentUser.getUsername());
                                auxReqest.setTo("ADMIN");
                                System.out.println("Describe your problem:");
                                input = scanner.nextLine();
                                auxReqest.setDesctiption(input);
                            }
                            if (auxReqest.getRequestType() != null) {
                                auxReqest.attach(currentUser);
                                if (auxReqest.getRequestType() == RequestType.OTHERS || auxReqest.getRequestType() == RequestType.DELETE_ACCOUNT) {
                                    for (User auxUser : IMDB.getInstance().getUsersList()) {
                                        if (auxUser.getUserType() == AccountType.Admin) auxReqest.attach(auxUser);
                                    }
                                    auxReqest.notifyObservers("You have a request that you can solve from " + currentUser.getUsername(), "receiver");
                                } else {
                                    for (User auxUser : IMDB.getInstance().getUsersList()) {
                                        if (auxUser.getUsername() == auxReqest.getTo()) auxReqest.attach(auxUser);
                                    }
                                    for (Observer auxObserver : auxReqest.getObservers()) {
                                        if (((User) auxObserver).getUserType() != AccountType.Regular) {
                                            for (LikableItems auxItem : ((Staff) auxObserver).getListUserAdded()) {
                                                if (auxItem.giveName().toLowerCase().equals(auxReqest.getProblemName().toLowerCase()))
                                                    ((Staff) auxObserver).addNotification("You have a request that you can solve from " + currentUser.getUsername());
                                            }
                                        }
                                    }
                                }
                                ((Contributor) currentUser).createRequest(auxReqest);
                                System.out.println("Request added to the system");
                            } else System.out.println("Not a valid input. Try again.");
                            break;
                        case "9":
                            Request reqToBeDeleted = null;
                            System.out.println("What kind of request do you want to delete?");
                            System.out.println("1)Delete account\n2)Actor issue\n3)Production Issue\n4)Other");
                            input = scanner.nextLine();
                            if (input.equals("1")) {
                                for (Request req : Admin.RequestHolder.getRequestsList()) {
                                    if (req.getUsername().equals(currentUser.getUsername()) && req.getRequestType() == RequestType.DELETE_ACCOUNT) {
                                        reqToBeDeleted = req;
                                    }
                                }
                                if (reqToBeDeleted == null) {
                                    System.out.println("You have no request like that.");
                                    break;
                                }
                            }
                            if (input.equals("2")) {
                                counter = 0;
                                System.out.println("What request for an actor do you want to delete?");
                                for (User auxUser : IMDB.getInstance().getUsersList()) {
                                    if (auxUser.getUserType() != AccountType.Regular) {
                                        for (Request auxRequest : ((Staff) auxUser).getListRequestForUser()) {
                                            if (auxRequest.getUsername().equals(currentUser.getUsername()) && auxRequest.getRequestType() == RequestType.ACTOR_ISSUE) {
                                                counter += 1;
                                                System.out.println(counter + ") " + auxRequest.getProblemName());
                                            }
                                        }
                                    }
                                }
                                if (counter == 0) {
                                    System.out.println("You have no actor requests");
                                } else {
                                    counter = 0;
                                    System.out.print("Your choice: ");
                                    input = scanner.nextLine();
                                    search = Integer.parseInt(input);
                                    for (User auxUser : IMDB.getInstance().getUsersList()) {
                                        if (auxUser.getUserType() != AccountType.Regular) {
                                            for (Request auxRequest : ((Staff) auxUser).getListRequestForUser()) {
                                                if (auxRequest.getUsername().equals(currentUser.getUsername()) && auxRequest.getRequestType() == RequestType.ACTOR_ISSUE) {
                                                    counter += 1;
                                                    if (counter == search) reqToBeDeleted = auxRequest;
                                                }
                                            }
                                        }
                                    }

                                }
                            }
                            if (input.equals("3")) {
                                counter = 0;
                                System.out.println("What request for a production do you want to delete?");
                                for (User auxUser : IMDB.getInstance().getUsersList()) {
                                    if (auxUser.getUserType() != AccountType.Regular) {
                                        for (Request auxRequest : ((Staff) auxUser).getListRequestForUser()) {
                                            if (auxRequest.getUsername().equals(currentUser.getUsername()) && auxRequest.getRequestType() == RequestType.MOVIE_ISSUE) {
                                                counter += 1;
                                                System.out.println(counter + ") " + auxRequest.getProblemName());
                                            }
                                        }
                                    }
                                }
                                if (counter == 0) {
                                    System.out.println("You have no production requests");
                                    break;
                                } else {
                                    counter = 0;
                                    System.out.print("Your choice: ");
                                    input = scanner.nextLine();
                                    search = Integer.parseInt(input);
                                    for (User auxUser : IMDB.getInstance().getUsersList()) {
                                        if (auxUser.getUserType() != AccountType.Regular) {
                                            for (Request auxRequest : ((Staff) auxUser).getListRequestForUser()) {
                                                if (auxRequest.getUsername().equals(currentUser.getUsername()) && auxRequest.getRequestType() == RequestType.MOVIE_ISSUE) {
                                                    counter += 1;
                                                    if (counter == search) reqToBeDeleted = auxRequest;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            if (input.equals("4")) {
                                counter = 0;
                                System.out.println("What request do you want to delete?");
                                for (Request auxRequest : Admin.RequestHolder.getRequestsList()) {
                                    if (auxRequest.getRequestType() == RequestType.OTHERS && auxRequest.getUsername().equals(currentUser.getUsername())) {
                                        counter += 1;
                                        System.out.println(counter + ") " + auxRequest.getDesctiption());
                                    }
                                }
                                if (counter == 0) {
                                    System.out.println("You have no request");
                                    break;
                                } else {
                                    counter = 0;
                                    System.out.println("What request do you want to delete?");
                                    input = scanner.nextLine();
                                    search = Integer.parseInt(input);
                                    for (Request auxRequest : Admin.RequestHolder.getRequestsList()) {
                                        if (auxRequest.getRequestType() == RequestType.OTHERS && auxRequest.getUsername().equals(currentUser.getUsername())) {
                                            counter += 1;
                                            if (counter == search) reqToBeDeleted = auxRequest;
                                        }
                                    }
                                }
                            }
                            if (reqToBeDeleted != null) {
                                reqToBeDeleted.detach(currentUser);
                                ((Contributor) currentUser).removeRequest(reqToBeDeleted);
                                System.out.println("Request removed successfully");
                            } else System.out.println("Not a valid input. Try again");
                            break;
                        case "10":
                            System.out.println("What kind of production do you want to add?\n1)Movie\n2)Series");
                            input = scanner.nextLine();
                            if (input.equals("1")) {
                                Movie auxMovie = new Movie();
                                System.out.print("Title: ");
                                input = scanner.nextLine();
                                auxMovie.setTitle(input);
                                System.out.print("Directors: ");
                                input = scanner.nextLine();
                                String[] directors = input.split(", ");
                                for (String auxString : directors) {
                                    auxMovie.addDirector(auxString);
                                }
                                System.out.print("Actors: ");
                                input = scanner.nextLine();
                                String[] actors = input.split(", ");
                                for (String auxString : actors) {
                                    auxMovie.addActor(auxString);
                                }
                                System.out.print("Genres: ");
                                input = scanner.nextLine();
                                String[] genres = input.split(", ");
                                for (String auxString : genres) {
                                    auxMovie.addGenre(FindInEnums.findGenre(auxString));
                                }
                                System.out.print("Plot: ");
                                input = scanner.nextLine();
                                auxMovie.setDescription(input);
                                System.out.print("Duration(in minutes):");
                                input = scanner.nextLine();
                                auxMovie.setDuration(Integer.parseInt(input));
                                System.out.print("Release year: ");
                                input = scanner.nextLine();
                                auxMovie.setReleaseYear(Integer.parseInt(input));

                                auxMovie.attach(currentUser);

                                IMDB.getInstance().getProductionsList().add(auxMovie);
                                ((Staff) currentUser).addListUserAded(auxMovie);

                                currentUser.changeExperience(new ExperienceFromAddingToSystem());
                            }
                            if (input.equals("2")) {
                                Series auxSeries = new Series();
                                System.out.print("Title: ");
                                input = scanner.nextLine();
                                auxSeries.setTitle(input);
                                System.out.print("Directors: ");
                                input = scanner.nextLine();
                                String[] directors = input.split(", ");
                                for (String auxString : directors) {
                                    auxSeries.addDirector(auxString);
                                }
                                System.out.print("Actors: ");
                                input = scanner.nextLine();
                                String[] actors = input.split(", ");
                                for (String auxString : actors) {
                                    auxSeries.addActor(auxString);
                                }
                                System.out.print("Genres: ");
                                input = scanner.nextLine();
                                String[] genres = input.split(", ");
                                for (String auxString : genres) {
                                    auxSeries.addGenre(FindInEnums.findGenre(auxString));
                                }
                                System.out.print("Plot: ");
                                input = scanner.nextLine();
                                auxSeries.setDescription(input);
                                System.out.println("Number of seasons: ");
                                input = scanner.nextLine();
                                auxSeries.setNumOfSeasons(Integer.parseInt(input));
                                for (int i = 1; i <= auxSeries.getNumOfSeasons(); i++) {
                                    ArrayList<Episode> auxEpisodeList = new ArrayList<Episode>();
                                    System.out.println("How many episodes does the Season " + i + " contain?");
                                    input = scanner.nextLine();
                                    System.out.println("Give the name and duration of each episode of the season");
                                    for (int j = 0; j < Integer.parseInt(input); j++) {
                                        System.out.print("Episode name: ");
                                        input2 = scanner.nextLine();
                                        System.out.print("Duration: ");
                                        input3 = scanner.nextLine();
                                        auxEpisodeList.add(new Episode(input2, Integer.parseInt(input3)));
                                    }
                                    auxSeries.addSeason("Season " + i, auxEpisodeList);
                                }

                                auxSeries.attach(currentUser);

                                IMDB.getInstance().getProductionsList().add(auxSeries);
                                ((Staff) currentUser).addListUserAded(auxSeries);

                                currentUser.changeExperience(new ExperienceFromAddingToSystem());
                            }
                            break;
                        case "11":
                            counter = 0;
                            Production toBeDeleted = null;
                            System.out.println("What production do you want to remove from the system?");
                            for (LikableItems auxItem : ((Staff) currentUser).getListUserAdded()) {
                                if (auxItem instanceof Production) {
                                    counter += 1;
                                    System.out.println(counter + ") " + auxItem.giveName());
                                }
                            }
                            if (counter == 0) {
                                System.out.println("You have no productions added by you.");
                                break;
                            }
                            System.out.print("Pick: ");
                            input = scanner.nextLine();
                            search = Integer.parseInt(input);
                            counter = 0;
                            for (LikableItems auxItem : ((Staff) currentUser).getListUserAdded()) {
                                if (auxItem instanceof Production) {
                                    counter += 1;
                                    if (counter == search) toBeDeleted = (Production) auxItem;
                                }
                            }
                            if (toBeDeleted == null) {
                                System.out.println("The production you are looking for was not added by you or does not exist");
                            } else {
                                System.out.println("Production removed successfully");
                                ((Staff) currentUser).removeListAddedUser(toBeDeleted.getTitle());
                                IMDB.getInstance().getProductionsList().remove(toBeDeleted);
                            }
                            break;
                        case "12":
                            Actor auxActor = new Actor();
                            System.out.print("Name: ");
                            input = scanner.nextLine();
                            auxActor.setName(input);
                            System.out.println("Biography: ");
                            input = scanner.nextLine();
                            auxActor.setBiography(input);
                            System.out.println("In how many productions did he play?");
                            input = scanner.nextLine();
                            for (int i = 0; i < Integer.parseInt(input); i++) {
                                System.out.print("Type(Movie, Series, Sitcom, etc.): ");
                                input2 = scanner.nextLine();
                                System.out.print("Name of production: ");
                                input3 = scanner.nextLine();
                                auxActor.addPerformances(input2, input3);
                            }
                            ((Staff) currentUser).addActorSystem(auxActor);
                            ((Staff) currentUser).addListUserAded(auxActor);
                            System.out.println("Actor added successfully");
                            currentUser.changeExperience(new ExperienceFromAddingToSystem());
                            break;
                        case "13":
                            counter = 0;
                            Actor actorToBeDeleted = null;
                            System.out.println("What actor do you want to remove from the system?");
                            for (LikableItems auxItem : ((Staff) currentUser).getListUserAdded()) {
                                if (auxItem instanceof Actor) {
                                    counter += 1;
                                    System.out.println(counter + ") " + auxItem.giveName());
                                }
                            }
                            if (counter == 0) {
                                System.out.println("You have no actors added by you.");
                                break;
                            }
                            System.out.print("Pick: ");
                            input = scanner.nextLine();
                            search = Integer.parseInt(input);
                            counter = 0;
                            for (LikableItems auxItem : ((Staff) currentUser).getListUserAdded()) {
                                if (auxItem instanceof Actor) {
                                    counter += 1;
                                    if (counter == search) actorToBeDeleted = (Actor) auxItem;
                                }
                            }
                            if (actorToBeDeleted == null) {
                                System.out.println("The actor you are looking for was not added by you or does not exist");
                            } else {
                                System.out.println("Actor removed successfully");
                                ((Staff) currentUser).removeListAddedUser(actorToBeDeleted.getName());
                                IMDB.getInstance().getActorsList().remove(actorToBeDeleted);
                            }
                            break;
                        case "14":
                            for (Request auxRequest : ((Staff) currentUser).getListRequestForUser()) {
                                System.out.println(auxRequest + "\n");
                            }
                            break;
                        case "15":
                            Request reqToBeRemoved = null;
                            counter = 0;
                            System.out.println("What request do you want to solve?");
                            for (Request auxRequest : ((Staff) currentUser).getListRequestForUser()) {
                                counter += 1;
                                System.out.println(counter + ") " + auxRequest.getProblemName() + "(" + auxRequest.getDesctiption() + ")");
                            }
                            if (counter == 0) {
                                System.out.println("You have no requests");

                            } else {
                                input = scanner.nextLine();
                                search = Integer.parseInt(input);
                                counter = 0;
                                for (Request auxRequest : ((Staff) currentUser).getListRequestForUser()) {
                                    counter += 1;
                                    if (counter == search) {
                                        ((Staff) currentUser).solveRequest(auxRequest);
                                        reqToBeRemoved = auxRequest;
                                    }
                                }
                                if (reqToBeRemoved != null) {
                                    ((Staff) currentUser).removeRequest(reqToBeRemoved);
                                }
                            }
                            break;
                        case "16":
                            Production prodToModify = null;
                            System.out.println("What production do you want to modify?(current name)");
                            input = scanner.nextLine();
                            for (LikableItems auxItem : ((Staff) currentUser).getListUserAdded()) {
                                if (auxItem.giveName().toLowerCase().equals(input.toLowerCase()))
                                    prodToModify = (Production) auxItem;
                            }
                            if (prodToModify == null) {
                                System.out.println("Production is not added by you or does not exist in the system");
                            } else {
                                IMDB.getInstance().getProductionsList().remove(prodToModify);
                                ((Staff) currentUser).updateProduction(prodToModify);
                                IMDB.getInstance().getProductionsList().add(prodToModify);
                            }
                            break;
                        case "17":
                            Actor actorToModify = null;
                            System.out.println("What actor do you want to modify?(current name)");
                            input = scanner.nextLine();
                            for (LikableItems auxItem : ((Staff) currentUser).getListUserAdded()) {
                                if (auxItem.giveName().toLowerCase().equals(input.toLowerCase())) {
                                    actorToModify = (Actor) auxItem;
                                }
                            }
                            if (actorToModify == null) {
                                System.out.println("No actor with this name found.");
                            } else {
                                IMDB.getInstance().getActorsList().remove(actorToModify);
                                ((Staff) currentUser).updateActor(actorToModify);
                                IMDB.getInstance().getActorsList().add(actorToModify);
                            }
                            break;
                        case "18":
                            if (currentUser.getNotifications().size() == 0)
                                System.out.println("You have no notifications");
                            else {
                                for (String auxNotif : currentUser.getNotifications()) {
                                    System.out.println(auxNotif);
                                }
                            }
                            break;
                        case "19":
                            currentUser.getNotifications().clear();
                            System.out.println("Notifications cleared.");
                            break;
                        case "20":
                            System.out.println("Logging out...");
                            currentUser = null;
                            break;
                        case "21":
                            System.out.println("App closing. Bye!!!");
                            running = false;
                            break;
                        default:
                            System.out.println("That is not a valid command. Try again.");
                            throw new InvalidCommandException("Invalid command");
                    }
                    input = scanner.nextLine();
                }
                if ((currentUser != null) && (currentUser.getUserType() == AccountType.Admin)) {
                    System.out.println("Username: " + currentUser.getUsername());
                    System.out.println("Experience: " + currentUser.getExperience());
                    System.out.println("Choose action: ");
                    System.out.println("    1)  View all productions details");
                    System.out.println("    2)  Search for a production");
                    System.out.println("    3)  View all actors details");
                    System.out.println("    4)  Search for an actor");
                    System.out.println("    5)  Show favorite list");
                    System.out.println("    6)  Add something to the favorites list");
                    System.out.println("    7)  Remove something from the favorites list");
                    System.out.println("    8)  Add a production to the system");
                    System.out.println("    9)  Remove a production from the system");
                    System.out.println("    10) Add an actor to the system");
                    System.out.println("    11) Remove an actor from the system");
                    System.out.println("    12) View your requests");
                    System.out.println("    13) Solve requests");
                    System.out.println("    14) Update production");
                    System.out.println("    15) Update actors");
                    System.out.println("    16) Add an user to the system");
                    System.out.println("    17) Remove a user from the system");
                    System.out.println("    18) Display notifications");
                    System.out.println("    19) Clear notifications");
                    System.out.println("    20) Logout");
                    System.out.println("    21) Exit");
                    System.out.print("Your choice: ");
                    input = scanner.nextLine();
                    switch (input) {
                        case "1":
                            /*for (Production auxProd : IMDB.getInstance().getProductionsList()) {
                                System.out.println(auxProd + "\n\n");
                            }
                            break;*/
                            System.out.println("Do you want to display productions in alphabetical order?\n1) Yes\n2) No");
                            input = scanner.nextLine();
                            if (input.equals("1")) {
                                Collections.sort(IMDB.getInstance().getProductionsList());
                            }
                            System.out.println("Are you looking for specific genres?");
                            input = scanner.nextLine();
                            String genresToSearch[] = input.split(", ");

                            System.out.print("Minimum number of reviews for productions: ");
                            input2 = scanner.nextLine();
                            search = Integer.parseInt(input2);
                            for (Production auxProd : IMDB.getInstance().getProductionsList()) {
                                show = false;
                                if (input == "") show = true;
                                for (String auxString : genresToSearch) {
                                    if (auxProd.getGenres().contains(FindInEnums.findGenre(input))) show = true;
                                }
                                if (auxProd.getRatings().size() < search) show = false;
                                if (show) System.out.println(auxProd);
                            }
                            break;
                        case "2":
                            System.out.println("What are you looking for?");
                            input = scanner.nextLine();
                            found = false;
                            for (Production auxProd : IMDB.getInstance().getProductionsList()) {
                                if (auxProd.getTitle().toLowerCase().equals(input.toLowerCase())) {
                                    System.out.println(auxProd);
                                    found = true;
                                }
                            }
                            if (found == false)
                                System.out.println("No production found. Check spelling or submit a request for adding the actor in the system.");
                            break;
                        case "3":
                            System.out.println("What order do you want?\n1) Random\n2) Alphabetical");
                            input = scanner.nextLine();
                            if (input.equals("1")) {
                                for (Actor auxActor : IMDB.getInstance().getActorsList()) {
                                    System.out.println(auxActor);
                                }
                            } else {
                                Collections.sort(IMDB.getInstance().getActorsList());
                                for (Actor auxActor : IMDB.getInstance().getActorsList()) {
                                    System.out.println(auxActor);
                                }
                            }
                            break;
                        case "4":
                            System.out.println("Who are you looking for?");
                            input = scanner.nextLine();
                            found = false;
                            for (Actor auxActor : IMDB.getInstance().getActorsList()) {
                                if (auxActor.getName().toLowerCase().equals(input.toLowerCase())) {
                                    System.out.println(auxActor);
                                    found = true;
                                }
                            }
                            if (found == false)
                                System.out.println("No actor found. Check spelling or submit a request for adding the actor in the system.");
                            break;
                        case "5":
                            int actorCounter = 1, productionCounter = 1;
                            System.out.println("\nFavorite actors: ");
                            for (LikableItems item : currentUser.getLikedItems()) {
                                if (item.getClass() == Actor.class) {
                                    System.out.println(actorCounter + ") " + item.giveName());
                                    actorCounter += 1;
                                }
                            }
                            System.out.println("\nFavorite productions: ");
                            for (LikableItems item : currentUser.getLikedItems()) {
                                if (item.getClass() == Movie.class || item.getClass() == Series.class) {
                                    System.out.println(productionCounter + ") " + item.giveName());
                                    productionCounter += 1;
                                }
                            }
                            break;
                        case "6":
                            System.out.println("Give the name of your new favorite production or actor");
                            input = scanner.nextLine();
                            found = false;
                            for (Production auxProd : IMDB.getInstance().getProductionsList()) {
                                if (auxProd.getTitle().toLowerCase().equals(input.toLowerCase())) {
                                    currentUser.addLikedItem(auxProd);
                                    found = true;
                                    System.out.println("Production added to your favorite list");
                                }
                            }
                            for (Actor auxActor : IMDB.getInstance().getActorsList()) {
                                if (auxActor.getName().toLowerCase().equals(input.toLowerCase())) {
                                    currentUser.addLikedItem(auxActor);
                                    found = true;
                                    System.out.println("Actor added to your favorite list");
                                }
                            }

                            if (found == false) {
                                System.out.println("No actor or production with this name foud in the system");
                                System.out.println("Check your spelling or make a request for the actor or production to be added to the system");
                            }
                            break;
                        case "7":
                            System.out.println("Give the name of the favorite production or actor you want to remove");
                            input = scanner.nextLine();
                            found = false;
                            LikableItems itemToRemove = null;
                            for (LikableItems auxLikableItem : currentUser.getLikedItems()) {
                                if (auxLikableItem.giveName().toLowerCase().equals(input.toLowerCase())) {
                                    itemToRemove = auxLikableItem;
                                    found = true;
                                    System.out.println("Item removed from liked list");
                                }
                            }
                            if (found == false) {
                                System.out.println("No item with this name found. Check the spelling.");
                            } else currentUser.removeLikedItem(itemToRemove);
                            break;
                        case "8":
                            System.out.println("What kind of production do you want to add?\n1)Movie\n2)Series");
                            input = scanner.nextLine();
                            if (input.equals("1")) {
                                Movie auxMovie = new Movie();
                                System.out.print("Title: ");
                                input = scanner.nextLine();
                                auxMovie.setTitle(input);
                                System.out.print("Directors: ");
                                input = scanner.nextLine();
                                String[] directors = input.split(", ");
                                for (String auxString : directors) {
                                    auxMovie.addDirector(auxString);
                                }
                                System.out.print("Actors: ");
                                input = scanner.nextLine();
                                String[] actors = input.split(", ");
                                for (String auxString : actors) {
                                    auxMovie.addActor(auxString);
                                }
                                System.out.print("Genres: ");
                                input = scanner.nextLine();
                                String[] genres = input.split(", ");
                                for (String auxString : genres) {
                                    auxMovie.addGenre(FindInEnums.findGenre(auxString));
                                }
                                System.out.print("Plot: ");
                                input = scanner.nextLine();
                                auxMovie.setDescription(input);
                                System.out.print("Duration(in minutes):");
                                input = scanner.nextLine();
                                auxMovie.setDuration(Integer.parseInt(input));
                                System.out.print("Release year: ");
                                input = scanner.nextLine();
                                auxMovie.setReleaseYear(Integer.parseInt(input));

                                auxMovie.attach(currentUser);

                                IMDB.getInstance().getProductionsList().add(auxMovie);
                                ((Staff) currentUser).addListUserAded(auxMovie);

                                currentUser.changeExperience(new ExperienceFromAddingToSystem());
                            }
                            if (input.equals("2")) {
                                Series auxSeries = new Series();
                                System.out.print("Title: ");
                                input = scanner.nextLine();
                                auxSeries.setTitle(input);
                                System.out.print("Directors: ");
                                input = scanner.nextLine();
                                String[] directors = input.split(", ");
                                for (String auxString : directors) {
                                    auxSeries.addDirector(auxString);
                                }
                                System.out.print("Actors: ");
                                input = scanner.nextLine();
                                String[] actors = input.split(", ");
                                for (String auxString : actors) {
                                    auxSeries.addActor(auxString);
                                }
                                System.out.print("Genres: ");
                                input = scanner.nextLine();
                                String[] genres = input.split(", ");
                                for (String auxString : genres) {
                                    auxSeries.addGenre(FindInEnums.findGenre(auxString));
                                }
                                System.out.print("Plot: ");
                                input = scanner.nextLine();
                                auxSeries.setDescription(input);
                                System.out.println("Number of seasons: ");
                                input = scanner.nextLine();
                                auxSeries.setNumOfSeasons(Integer.parseInt(input));
                                for (int i = 1; i <= auxSeries.getNumOfSeasons(); i++) {
                                    ArrayList<Episode> auxEpisodeList = new ArrayList<Episode>();
                                    System.out.println("How many episodes does the Season " + i + " contain?");
                                    input = scanner.nextLine();
                                    System.out.println("Give the name and duration of each episode of the season");
                                    for (int j = 0; j < Integer.parseInt(input); j++) {
                                        System.out.print("Episode name: ");
                                        input2 = scanner.nextLine();
                                        System.out.print("Duration: ");
                                        input3 = scanner.nextLine();
                                        auxEpisodeList.add(new Episode(input2, Integer.parseInt(input3)));
                                    }
                                    auxSeries.addSeason("Season " + i, auxEpisodeList);
                                }

                                auxSeries.attach(currentUser);

                                IMDB.getInstance().getProductionsList().add(auxSeries);
                                ((Staff) currentUser).addListUserAded(auxSeries);

                                currentUser.changeExperience(new ExperienceFromAddingToSystem());
                            }
                            break;
                        case "9":
                            counter = 0;
                            Production toBeDeleted = null;
                            System.out.println("What production do you want to remove from the system?");
                            for (LikableItems auxItem : ((Staff) currentUser).getListUserAdded()) {
                                if (auxItem instanceof Production) {
                                    counter += 1;
                                    System.out.println(counter + ") " + auxItem.giveName());
                                }
                            }
                            if (counter == 0) {
                                System.out.println("You have no productions added by you.");
                                break;
                            }
                            System.out.print("Pick: ");
                            input = scanner.nextLine();
                            search = Integer.parseInt(input);
                            counter = 0;
                            for (LikableItems auxItem : ((Staff) currentUser).getListUserAdded()) {
                                if (auxItem instanceof Production) {
                                    counter += 1;
                                    if (counter == search) toBeDeleted = (Production) auxItem;
                                }
                            }
                            if (toBeDeleted == null) {
                                System.out.println("The production you are looking for was not added by you or does not exist");
                            } else {
                                System.out.println("Production removed successfully");
                                ((Staff) currentUser).removeListAddedUser(toBeDeleted.getTitle());
                                IMDB.getInstance().getProductionsList().remove(toBeDeleted);
                            }
                            break;
                        case "10":
                            Actor auxActor = new Actor();
                            System.out.print("Name: ");
                            input = scanner.nextLine();
                            auxActor.setName(input);
                            System.out.println("Biography: ");
                            input = scanner.nextLine();
                            auxActor.setBiography(input);
                            System.out.println("In how many productions did he play?");
                            input = scanner.nextLine();
                            for (int i = 0; i < Integer.parseInt(input); i++) {
                                System.out.print("Type(Movie, Series, Sitcom, etc.): ");
                                input2 = scanner.nextLine();
                                System.out.print("Name of production: ");
                                input3 = scanner.nextLine();
                                auxActor.addPerformances(input2, input3);
                            }
                            ((Staff) currentUser).addActorSystem(auxActor);
                            ((Staff) currentUser).addListUserAded(auxActor);
                            System.out.println("Actor added successfully");
                            currentUser.changeExperience(new ExperienceFromAddingToSystem());
                            break;
                        case "11":
                            counter = 0;
                            Actor actorToBeDeleted = null;
                            System.out.println("What actor do you want to remove from the system?");
                            for (LikableItems auxItem : ((Staff) currentUser).getListUserAdded()) {
                                if (auxItem instanceof Actor) {
                                    counter += 1;
                                    System.out.println(counter + ") " + auxItem.giveName());
                                }
                            }
                            if (counter == 0) {
                                System.out.println("You have no actors added by you.");
                                break;
                            }
                            System.out.print("Pick: ");
                            input = scanner.nextLine();
                            search = Integer.parseInt(input);
                            counter = 0;
                            for (LikableItems auxItem : ((Staff) currentUser).getListUserAdded()) {
                                if (auxItem instanceof Actor) {
                                    counter += 1;
                                    if (counter == search) actorToBeDeleted = (Actor) auxItem;
                                }
                            }
                            if (actorToBeDeleted == null) {
                                System.out.println("The actor you are looking for was not added by you or does not exist");
                            } else {
                                System.out.println("Actor removed successfully");
                                ((Staff) currentUser).removeListAddedUser(actorToBeDeleted.getName());
                                IMDB.getInstance().getActorsList().remove(actorToBeDeleted);
                            }
                            break;
                        case "12":
                            for (Request auxRequest : ((Staff) currentUser).getListRequestForUser()) {
                                System.out.println(auxRequest + "\n");
                            }
                            for (Request auxRequest : Admin.RequestHolder.getRequestsList()) {
                                System.out.println(auxRequest + "\n");
                            }
                            break;
                        case "13":
                            Request reqToBeRemoved = null;
                            counter = 0;
                            System.out.println("What request do you want to solve?");
                            for (Request auxRequest : ((Staff) currentUser).getListRequestForUser()) {
                                counter += 1;
                                System.out.println(counter + ") " + auxRequest.getProblemName() + "(" + auxRequest.getDesctiption() + ")");
                            }
                            for (Request auxRequest : Admin.RequestHolder.getRequestsList()) {
                                counter += 1;
                                System.out.println(counter + ") " + "Admin problem" + "(" + auxRequest.getDesctiption() + ")");
                            }
                            if (counter == 0) {
                                System.out.println("You have no requests");
                            } else {
                                input = scanner.nextLine();
                                search = Integer.parseInt(input);
                                counter = 0;
                                for (Request auxRequest : ((Staff) currentUser).getListRequestForUser()) {
                                    counter += 1;
                                    if (counter == search) {
                                        ((Staff) currentUser).solveRequest(auxRequest);
                                        reqToBeRemoved = auxRequest;
                                    }
                                }
                                for (Request auxRequest : Admin.RequestHolder.getRequestsList()) {

                                    counter += 1;
                                    if (counter == search) {
                                        ((Staff) currentUser).solveRequest(auxRequest);
                                        reqToBeRemoved = auxRequest;
                                    }
                                }
                                if (reqToBeRemoved != null) {
                                    ((Staff) currentUser).removeRequest(reqToBeRemoved);
                                }
                            }
                            break;
                        case "14":
                            Production prodToModify = null;
                            System.out.println("What production do you want to modify?(current name)");
                            input = scanner.nextLine();
                            for (LikableItems auxItem : ((Staff) currentUser).getListUserAdded()) {
                                if (auxItem.giveName().toLowerCase().equals(input.toLowerCase()))
                                    prodToModify = (Production) auxItem;
                            }
                            if (prodToModify == null) {
                                System.out.println("Production is not added by you or does not exist in the system");
                            } else {
                                IMDB.getInstance().getProductionsList().remove(prodToModify);
                                ((Staff) currentUser).updateProduction(prodToModify);
                                IMDB.getInstance().getProductionsList().add(prodToModify);
                            }
                            break;
                        case "15":
                            Actor actorToModify = null;
                            System.out.println("What actor do you want to modify?(current name)");
                            input = scanner.nextLine();
                            for (LikableItems auxItem : ((Staff) currentUser).getListUserAdded()) {
                                if (auxItem.giveName().toLowerCase().equals(input.toLowerCase())) {
                                    actorToModify = (Actor) auxItem;
                                }
                            }
                            if (actorToModify == null) {
                                System.out.println("No actor with this name found.");
                            } else {
                                IMDB.getInstance().getActorsList().remove(actorToModify);
                                ((Staff) currentUser).updateActor(actorToModify);
                                IMDB.getInstance().getActorsList().add(actorToModify);
                            }
                            break;
                        case "16":
                            System.out.println("What is the username of the new account?");
                            input = scanner.nextLine();
                            if (input == "") throw new InformationIncompleteException("Username can not be null");
                            System.out.println("What type of account do you want to create?");
                            input2 = scanner.nextLine();
                            User auxUser = UserFactory.createUser(input, input2);
                            auxUser.setExperience(0);
                            auxUser.setUserType(FindInEnums.findAccountType(input2));

                            System.out.println("What email do you want to use for the account?");
                            input = scanner.nextLine();
                            if (input == "") throw new InformationIncompleteException("Email can not be null");
                            System.out.println("What password do you want to use for the account?(leave blank for random password)");
                            input2 = scanner.nextLine();
                            if (input2 == "") {
                                input2 = PasswordGenerator.generateRandomPassword();
                                System.out.println("The password for the account will be: " + input2);
                            }
                            System.out.println("What is the name of the person that will use the account");
                            input3 = scanner.nextLine();
                            auxUser.setInformation(new User.Information.InformationBuilder(input3, input, input2).build());

                            System.out.println("What is the gender of the accunt user?");
                            input = scanner.nextLine();
                            auxUser.getInformation().setGender(input);

                            System.out.println("What is the age of the person that will use the account");
                            input = scanner.nextLine();
                            auxUser.getInformation().setAge(Integer.parseInt(input));

                            System.out.println("From which country is the person tat will use the accout from?(Use the 2 letter format)");
                            input = scanner.nextLine();
                            auxUser.getInformation().setCountry(input);

                            System.out.println("What is the birthdate of the person that will use the account?(Use the format yyyy-mm-dd)");
                            input = scanner.nextLine();
                            auxUser.getInformation().setBirthDate(LocalDateTime.parse(input + "T00:00:00", formatter));

                            IMDB.getInstance().getUsersList().add(auxUser);
                            break;
                        case "17":
                            int index;
                            System.out.println("What account do you want to delete");
                            for (int i = 0; i < IMDB.getInstance().getUsersList().size(); i++) {
                                System.out.println(i + ") " + IMDB.getInstance().getUsersList().get(i).getUsername());
                            }
                            input = scanner.nextLine();
                            index = Integer.parseInt(input);
                            if (index >= IMDB.getInstance().getUsersList().size()) {
                                System.out.println("Index not in range");
                                break;
                            }
                            if (IMDB.getInstance().getUsersList().get(index).getUsername().equals(currentUser.getUsername())) {
                                System.out.println("Cannot delete own account");
                                break;
                            }
                            IMDB.getInstance().getUsersList().remove(index);
                            System.out.println("User removed successfully");
                            break;
                        case "18":
                            if (currentUser.getNotifications().size() == 0)
                                System.out.println("You have no notifications");
                            else {
                                for (String auxNotif : currentUser.getNotifications()) {
                                    System.out.println(auxNotif);
                                }
                            }
                            break;
                        case "19":
                            currentUser.getNotifications().clear();
                            System.out.println("Notifications cleared.");
                            break;
                        case "20":
                            System.out.println("Logging out...");
                            currentUser = null;
                            break;
                        case "21":
                            System.out.println("App closing. Bye!!!");
                            running = false;
                            break;
                        default:
                            System.out.println("That is not a valid command. Try again.");
                            throw new InvalidCommandException("Invalid command");
                    }
                    input = scanner.nextLine();
                }
            }

        }
        scanner.close();
    }

    public void displayAllRequestsInSystem() {
        for (Request auxReq : Admin.RequestHolder.getRequestsList())
            System.out.println(auxReq);
        for (User auxUser : IMDB.getInstance().getUsersList()) {
            if (auxUser.getUserType() == AccountType.Contributor || auxUser.getUserType() == AccountType.Admin) {
                for (Request auxReques : ((Staff) auxUser).getListRequestForUser()) {
                    System.out.println(auxReques + "\n In object of user: " + auxUser.getUsername());
                }
            }
        }
    }

    public void attachUsersToTheirProduction() {
        for (User auxUser : IMDB.getInstance().getUsersList()) {
            if (auxUser.getUserType() != AccountType.Regular) {
                for (LikableItems auxItem : ((Staff) auxUser).getListUserAdded()) {
                    if (auxItem instanceof Production) ((Production) auxItem).attach(auxUser);
                }
            }
        }
    }

    public static void main(String[] args) throws
            InterruptedException, InformationIncompleteException, InvalidCommandException {

        Scanner scanner = new Scanner(System.in);
        String input;
        IMDB.getInstance().readDataFromJson();
        IMDB.getInstance().sortRequestsFronJson();
        IMDB.getInstance().attachUsersToTheirProduction();

        System.out.println("In what mode would you like to run the application?\n1) CLI\n2) GUI");
        input = scanner.nextLine();
        if (input.equals("1")) {
            IMDB.getInstance().runInCLI();
        }
        else
            throw new InvalidCommandException("No GUI for the moment");
    }
}
