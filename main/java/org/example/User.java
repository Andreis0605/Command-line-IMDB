package org.example;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public abstract class User implements Observer{
    public static class Information {
        private Credentials credentials;

        private String name;

        private String country;

        private int age;

        private String gender;

        private LocalDateTime birthDate;

        private Information(InformationBuilder builder) {
            this.credentials = builder.credentials;
            this.name = builder.name;
            this.country = builder.country;
            this.age = builder.age;
            this.gender = builder.gender;
            this.birthDate = builder.birthDate;
        }

        //getters
        public Credentials getCredentials() {
            return credentials;
        }

        public String getName() {
            return name;
        }

        public String getCountry() {
            return country;
        }

        public int getAge() {
            return age;
        }

        public String getGender() {
            return gender;
        }

        public LocalDateTime getBirthDate() {
            return birthDate;
        }

        //setters


        public void setCredentials(Credentials credentials) {
            this.credentials = credentials;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public void setBirthDate(LocalDateTime birthDate) {
            this.birthDate = birthDate;
        }

        public static class InformationBuilder {
            private Credentials credentials;

            private String name;

            private String country;

            private int age;

            private String gender;

            private LocalDateTime birthDate;

            public InformationBuilder(String name, String email, String password) throws InformationIncompleteException {

                if(email == "" || password == "") {
                    throw new InformationIncompleteException("Credentials can not be null");
                }

                this.name = name;
                this.credentials = new Credentials(email, password);
            }

            public InformationBuilder country(String country) {
                this.country = country;
                return this;
            }

            public InformationBuilder age(int age) {
                this.age = age;
                return this;
            }

            public InformationBuilder gender(String gender) {
                this.gender = gender;
                return this;
            }

            public InformationBuilder birthDate(LocalDateTime birthDate) {
                this.birthDate = birthDate;
                return this;
            }

            public Information build() {
                return new Information(this);
            }
        }
    }

    private Information information;

    private AccountType userType;

    private String username;

    private int experience;

    private List<String> notifications;

    private SortedSet<LikableItems> likedItems;

    public User() {
        this.information = null;
        this.userType = null;
        this.username = null;
        this.experience = -1;
        this.notifications = new ArrayList<String>();
        this.likedItems = new TreeSet<LikableItems>();
    }

    public User(String username) {
        this.information = null;
        this.userType = null;
        this.username = username;
        this.experience = -1;
        this.notifications = new ArrayList<String>();
        this.likedItems = new TreeSet<LikableItems>();
    }

    public User(Information info, AccountType userType, String username, int experience, List<String> notifications, SortedSet<LikableItems> likedItems) {
        this.information = info;
        this.userType = userType;
        this.username = username;
        this.experience = experience;
        this.notifications = new ArrayList<String>();
        this.notifications = notifications;
        this.likedItems = new TreeSet<LikableItems>();
        this.likedItems = likedItems;

    }

    //getters
    public Information getInformation() {
        return information;
    }

    public AccountType getUserType() {
        return userType;
    }

    public String getUsername() {
        return username;
    }

    public int getExperience() {
        return experience;
    }

    public List<String> getNotifications() {
        return notifications;
    }

    public SortedSet<LikableItems> getLikedItems() {
        return likedItems;
    }

    //setters

    public void setInformation(Information information) {
        this.information = information;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public void setLikedItems(SortedSet<LikableItems> likedItems) {
        this.likedItems = likedItems;
    }

    public void setNotifications(List<String> notifications) {
        this.notifications = notifications;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUserType(AccountType userType) {
        this.userType = userType;
    }

    //adders and removers
    public void addLikedItem(LikableItems item) {
        this.likedItems.add(item);
    }

    public void removeLikedItem(LikableItems item) {
        this.likedItems.remove(item);
    }

    public void addNotification(String notification) {
        this.notifications.add(notification);
    }

    public void removeNotification(String notification) {
        this.notifications.remove(notification);
    }

    public void removeNotification(int poz) {
        this.notifications.remove(poz);
    }

    public void changeExperience(ExperienceStrategy exp) {
        this.experience += exp.calculateExperience();
    }

    public void logOut() {
        ;
    }

    public String toString() {
        String aux = "";

        aux += "Username: " + getUsername() + "\n";
        aux += "Experience: " + getExperience() + "\n";
        aux += "Information: \n\n" + "Email: " + getInformation().getCredentials().getEmail() + "\n";
        aux += "Password: " + getInformation().getCredentials().getPassword() + "\n";
        aux += "Name: " + getInformation().getName() + "\n";
        aux += "Country: " + getInformation().getCountry() + "\n";
        aux += "Age: " + getInformation().getAge() + "\n";
        aux += "Gender: " + getInformation().getGender() + "\n";
        aux += "Birthdate: " + getInformation().getBirthDate() + "\n\n";
        aux += "User type: " + getUserType() + "\n\n";
        aux += "Notifications: \n";
        for(int i=0;i<notifications.size();i++) {
            aux+= notifications.get(i) + "\n";
        }
        aux+= "Liked Items: " + "\n\n";
        for(LikableItems obj : likedItems)
        {
            aux += obj.giveName() + "\n";
        }
        return aux;
    }

    public void update(String notification)
    {
        this.getNotifications().add(notification);
    }

}
