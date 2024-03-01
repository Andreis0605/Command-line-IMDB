package org.example;

import javax.crypto.SealedObject;
import java.io.File;
import java.util.*;

public abstract class Staff extends User implements StaffInterface,RequestsManager {


    private List<Request> listRequestForUser;

    private SortedSet<LikableItems> listUserAdded;

    //constructor
    public Staff() {
        super();
        listRequestForUser = new ArrayList<Request>();
        listUserAdded = new TreeSet<LikableItems>();
    }

    public Staff(String username) {
        super(username);
        listRequestForUser = new ArrayList<Request>();
        listUserAdded = new TreeSet<LikableItems>();
    }

    //getters

    public SortedSet<LikableItems> getListUserAdded() {
        return listUserAdded;
    }

    public List<Request> getListRequestForUser() {
        return listRequestForUser;
    }

    //setters
    public void setListRequestForUser(List<Request> listRequestForUser) {
        this.listRequestForUser = listRequestForUser;
    }

    public void setListUserAdded(SortedSet<LikableItems> listUserAdded) {
        this.listUserAdded = listUserAdded;
    }

    //adders

    public void addListRequestForUser(Request r) {
        listRequestForUser.add(r);
    }

    public void addListUserAded(LikableItems item) {
        listUserAdded.add(item);
    }

    //removers

    public void removeListRequestForUser(Request r) {
        this.getListRequestForUser().remove(r);


    }

    public void removeListAddedUser(String name) {
        listUserAdded.removeIf(item -> item.giveName().toLowerCase().equals(name.toLowerCase()));
    }

    //functions for interface
    @Override
    public void addProductionSystem(Production p) {
        if (IMDB.getInstance().getProductionsList().contains(p) == false)
            IMDB.getInstance().getProductionsList().add(p);
    }

    @Override
    public void addActorSystem(Actor a) {
        if (IMDB.getInstance().getActorsList().contains(a) == false)
            IMDB.getInstance().getActorsList().add(a);
    }

    @Override
    public void removeProductionSystem(String name) {

        int i;

        for (i = 0; i < IMDB.getInstance().getProductionsList().size(); i++) {
            if (IMDB.getInstance().getProductionsList().get(i).getTitle().equals(name)) break;
        }

        if (i < IMDB.getInstance().getProductionsList().size()) IMDB.getInstance().getProductionsList().remove(i);
    }

    @Override
    public void removeActorSystem(String name) {
        int i;
        for (i = 0; i < IMDB.getInstance().getActorsList().size(); i++) {
            if (IMDB.getInstance().getActorsList().get(i).getName().equals(name)) break;
        }

        if (i < IMDB.getInstance().getActorsList().size()) IMDB.getInstance().getActorsList().remove(i);
    }

    @Override
    public void updateProduction(Production p) {
        String input, input2, input3, input4;
        Scanner scanner = new Scanner(System.in);
        if (p.getClass() == Movie.class) {
            System.out.println("What do you want to change?");
            System.out.println("    1) Title");
            System.out.println("    2) Director");
            System.out.println("    3) Actors");
            System.out.println("    4) Genres");
            System.out.println("    5) Plot");
            System.out.println("    6) Duration");
            System.out.println("    7) Release year");
            input = scanner.nextLine();
            switch (input) {
                case "1":
                    System.out.println("Write the new title");
                    input = scanner.nextLine();
                    p.setTitle(input);
                    System.out.println("Title modified successfully");
                    break;
                case "2":
                    System.out.println("What do you want to do?\n1)Add director\n2)Remove director\n3)Change spelling of a director's name");
                    input = scanner.nextLine();
                    if (input.equals("1")) {
                        System.out.print("Name: ");
                        input = scanner.nextLine();
                        p.addDirector(input);
                    }
                    if (input.equals("2")) {
                        System.out.print("Name: ");
                        input = scanner.nextLine();
                        p.removeDirector(input);
                    }
                    if (input.equals("3")) {
                        System.out.print("Old name: ");
                        input = scanner.nextLine();
                        p.removeDirector(input);
                        System.out.print("New name: ");
                        input = scanner.nextLine();
                        p.addDirector(input);
                    }
                    break;
                case "3":
                    System.out.println("What do you want to do?\n1)Add actor\n2)Remove actor\n3)Change spelling of an actor's name");
                    input = scanner.nextLine();
                    if (input.equals("1")) {
                        System.out.print("Name: ");
                        input = scanner.nextLine();
                        p.addActor(input);
                    }
                    if (input.equals("2")) {
                        System.out.print("Name: ");
                        input = scanner.nextLine();
                        p.removeActor(input);
                    }
                    if (input.equals("3")) {
                        System.out.print("Old name: ");
                        input = scanner.nextLine();
                        p.removeActor(input);
                        System.out.print("New name: ");
                        input = scanner.nextLine();
                        p.addActor(input);
                    }
                    break;
                case "4":
                    System.out.println("What do you want to do?\n1)Add genre\n2)Remove genre");
                    input = scanner.nextLine();
                    if (input.equals("1")) {
                        System.out.println("Genre: ");
                        input = scanner.nextLine();
                        p.addGenre(FindInEnums.findGenre(input));
                    }
                    if (input.equals("2")) {
                        System.out.println("Genre: ");
                        input = scanner.nextLine();
                        p.removeGenre(FindInEnums.findGenre(input));
                    }
                    break;
                case "5":
                    System.out.println("Write the new plot");
                    input = scanner.nextLine();
                    p.setDescription(input);
                    break;
                case "6":
                    System.out.println("What is the new duration?");
                    input = scanner.nextLine();
                    ((Movie) p).setDuration(Integer.parseInt(input));
                    break;
                case "7":
                    System.out.println("What is the new release year?");
                    input = scanner.nextLine();
                    ((Movie) p).setReleaseYear(Integer.parseInt(input));
                    break;
                default:
                    System.out.println("Invalid command");
                    break;
            }
        } else {
            System.out.println("What do you want to change?");
            System.out.println("    1) Title");
            System.out.println("    2) Director");
            System.out.println("    3) Actors");
            System.out.println("    4) Genres");
            System.out.println("    5) Plot");
            System.out.println("    6) Release year");
            System.out.println("    7) Seasons");
            System.out.println("    8) Episode");
            input = scanner.nextLine();
            switch (input) {
                case "1":
                    System.out.println("Write the new title");
                    input = scanner.nextLine();
                    p.setTitle(input);
                    System.out.println("Title modified successfully");
                    break;
                case "2":
                    System.out.println("What do you want to do?\n1)Add director\n2)Remove director\n3)Change spelling of a director's name");
                    input = scanner.nextLine();
                    if (input.equals("1")) {
                        System.out.print("Name: ");
                        input = scanner.nextLine();
                        p.addDirector(input);
                    }
                    if (input.equals("2")) {
                        System.out.print("Name: ");
                        input = scanner.nextLine();
                        p.removeDirector(input);
                    }
                    if (input.equals("3")) {
                        System.out.print("Old name: ");
                        input = scanner.nextLine();
                        p.removeDirector(input);
                        System.out.print("New name: ");
                        input = scanner.nextLine();
                        p.addDirector(input);
                    }
                    break;
                case "3":
                    System.out.println("What do you want to do?\n1)Add actor\n2)Remove actor\n3)Change spelling of an actor's name");
                    input = scanner.nextLine();
                    if (input.equals("1")) {
                        System.out.print("Name: ");
                        input = scanner.nextLine();
                        p.addActor(input);
                    }
                    if (input.equals("2")) {
                        System.out.print("Name: ");
                        input = scanner.nextLine();
                        p.removeActor(input);
                    }
                    if (input.equals("3")) {
                        System.out.print("Old name: ");
                        input = scanner.nextLine();
                        p.removeActor(input);
                        System.out.print("New name: ");
                        input = scanner.nextLine();
                        p.addActor(input);
                    }
                    break;
                case "4":
                    System.out.println("What do you want to do?\n1)Add genre\n2)Remove genre");
                    input = scanner.nextLine();
                    if (input.equals("1")) {
                        System.out.println("Genre: ");
                        input = scanner.nextLine();
                        p.addGenre(FindInEnums.findGenre(input));
                    }
                    if (input.equals("2")) {
                        System.out.println("Genre: ");
                        input = scanner.nextLine();
                        p.removeGenre(FindInEnums.findGenre(input));
                    }
                    break;
                case "5":
                    System.out.println("Write the new plot");
                    input = scanner.nextLine();
                    p.setDescription(input);
                    break;
                case "6":
                    System.out.println("What is the new release year?");
                    input = scanner.nextLine();
                    ((Series) p).setReleaseYear(Integer.parseInt(input));
                    break;
                case "7":
                    System.out.println("What do you want to do?\n1)Add season\n2)Remove season");
                    input = scanner.nextLine();
                    if (input.equals("1")) {
                        ArrayList<Episode> auxEpisodeList = new ArrayList<Episode>();
                        System.out.println("How many episodes does the season have?");
                        input = scanner.nextLine();
                        for (int i = 0; i < Integer.parseInt(input); i++) {
                            System.out.print("Episode name: ");
                            input2 = scanner.nextLine();
                            System.out.println("Episode duration: ");
                            input3 = scanner.nextLine();
                            auxEpisodeList.add(new Episode(input2, Integer.parseInt(input3)));
                        }
                        ((Series) p).addSeason("Season " + (((Series) p).getNumOfSeasons() + 1));
                        ((Series) p).setNumOfSeasons(((Series) p).getNumOfSeasons() + 1);
                    }
                    if (input.equals("2")) {
                        System.out.println("What season do you want to remove?");
                        input = scanner.nextLine();
                        ((Series) p).getSeasons().remove("Season " + input);
                    }
                    break;
                case "8":
                    System.out.println("What do you want to do?\n1)Add episode\n2)Remove episode\n3)Modify episode");
                    input = scanner.nextLine();
                    if (input.equals("1")) {
                        System.out.println("To which season do you want to add an episode?");
                        input = scanner.nextLine();
                        if (Integer.parseInt(input) <= ((Series) p).getNumOfSeasons()) {
                            System.out.println("Episode name: ");
                            input2 = scanner.nextLine();
                            System.out.println("Episode duration: ");
                            input3 = scanner.nextLine();
                            ((Series) p).getSeasons().get("Season " + input).add(new Episode(input2, Integer.parseInt(input3)));
                            System.out.println("Episode added successfully");
                        } else {
                            System.out.println("There is no such season");
                        }
                    }
                    if (input.equals("2")) {
                        Episode episodeToRemove = null;
                        System.out.println("From which season is the episode you want to delete?");
                        input = scanner.nextLine();
                        System.out.println("What is the name of the episode you want to delete?");
                        input2 = scanner.nextLine();
                        for (Episode auxEpisode : ((Series) p).getSeasons().get("Season " + Integer.parseInt(input))) {
                            if (auxEpisode.getName().toLowerCase().equals(input2.toLowerCase())) {
                                episodeToRemove = auxEpisode;
                            }
                        }
                        if (episodeToRemove != null) {
                            ((Series) p).getSeasons().get("Season " + Integer.parseInt(input)).remove(episodeToRemove);
                            System.out.println("Episode removed successfully");
                        } else {
                            System.out.println("No such episode found");
                        }
                    }
                    if (input.equals("3")) {
                        Episode episodeToModify = null;
                        System.out.println("From which season is the episode you want to modify?");
                        input = scanner.nextLine();
                        System.out.println("What is the current name of the episode you want to modify?");
                        input2 = scanner.nextLine();
                        System.out.println("What do you want to modify?(Name or Duration)");
                        input3 = scanner.nextLine();
                        System.out.println("Give the new name or duration");
                        input4 = scanner.nextLine();
                        for (Episode auxEpisode : ((Series) p).getSeasons().get("Season " + Integer.parseInt(input))) {
                            if (auxEpisode.getName().toLowerCase().equals(input2.toLowerCase())) {
                                episodeToModify = auxEpisode;
                            }
                        }
                        if (episodeToModify != null) {
                            if (input3.toLowerCase().equals("name")) {
                                episodeToModify.setName(input4);
                            } else if (input3.toLowerCase().equals("Duration")) {
                                episodeToModify.setDuration(Integer.parseInt(input4));
                            }
                            System.out.println("Episode modified successfully");
                        } else {
                            System.out.println("No such episode found");
                        }

                    }
                    break;
                default:
                    System.out.println("Invalid command");
                    break;
            }

        }
        //scanner.close();

    }

    @Override
    public void updateActor(Actor a) {
        String input, input2, input3;
        Scanner scanner = new Scanner(System.in);
        System.out.println("What do you want to modify?");
        System.out.println("    1) Name");
        System.out.println("    2) Biography");
        System.out.println("    3) Performances");
        input = scanner.nextLine();
        switch (input) {
            case "1" -> {
                System.out.println("What is the new name of the actor?");
                input = scanner.nextLine();
                a.setName(input);
            }
            case "2" -> {
                System.out.println("What is the new biography");
                input = scanner.nextLine();
                a.setBiography(input);
            }
            case "3" -> {
                System.out.println("What do you want to do?\n1) Add performance\n2) Remove performance\n3) Modify performance");
                input = scanner.nextLine();
                switch (input) {
                    case "1":
                        System.out.println("What is the name of the new production?");
                        input = scanner.nextLine();
                        System.out.println("What is the type of the new production?");
                        input2 = scanner.nextLine();
                        a.addPerformances(input, input2);
                        break;
                    case "2":
                        System.out.println("What is the name of the production you want to remove");
                        input = scanner.nextLine();
                        a.removePerformances(input);
                        break;
                    case "3":
                        System.out.println("What is the name of the performance you want to modify?");
                        input = scanner.nextLine();
                        System.out.println("What do you want to modify?(Name or Type)");
                        input2 = scanner.nextLine();
                        System.out.println("Give the new value");
                        input3 = scanner.nextLine();
                        if(input2.toLowerCase().equals("name"))
                        {
                            String auxString = a.getPerformances().get(input);
                            a.getPerformances().remove(input);
                            a.getPerformances().put(input3,auxString);
                        }
                        else if (input2.toLowerCase().equals("type"))
                        {
                            a.getPerformances().remove(input);
                            a.getPerformances().put(input,input3);
                        }
                        break;
                    default:
                        break;
                }
            }
            default -> {
                System.out.println("Invalid option.");
            }
        }
        //scanner.close();
    }

    @Override
    public void solveRequest(Request r) {
        Scanner scanner = new Scanner(System.in);
        String input;
        System.out.println("What do you want to do with this request?\n1)Solve\n2)Reject");
        input = scanner.nextLine();
        if(input.equals("1"))
        {
            System.out.println("Request solved");
            r.notifyObservers("Your request for " + r.getProblemName() + " has been solved.","emitter");
            for(User auxUser : IMDB.getInstance().getUsersList())
            {
                if(auxUser.getUsername().equals(r.getUsername()) && (r.getRequestType() == RequestType.MOVIE_ISSUE || r.getRequestType() == RequestType.ACTOR_ISSUE)) {
                    auxUser.changeExperience(new ExperienceFromRequests());
                    if(auxUser.getUserType() == AccountType.Contributor) ((Staff)auxUser).getNotifications().add("Your request for " + r.getProblemName() + " has been solved.");
                }
            }
        }
        else
        {
            System.out.println("Request rejected");
            r.notifyObservers("Your request for " + r.getProblemName() + " has been rejected.","emitter");
            for(User auxUser : IMDB.getInstance().getUsersList())
            {
                if(auxUser.getUsername().equals(r.getUsername()) && (r.getRequestType() == RequestType.MOVIE_ISSUE || r.getRequestType() == RequestType.ACTOR_ISSUE)) {
                    if(auxUser.getUserType() == AccountType.Contributor) ((Staff)auxUser).getNotifications().add("Your request for " + r.getProblemName() + " has been rejected.");
                }
            }
        }

    }

    @Override
    public String toString() {
        String aux = super.toString() + "\n";
        aux += "Contributions: " + "\n\n";
        for (LikableItems auxObj : listUserAdded) {
            aux += auxObj.giveName() + "\n";
        }

        return aux;
    }
}
