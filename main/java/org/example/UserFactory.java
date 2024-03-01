package org.example;

public class UserFactory {

    public static User createUser(String userName,String type) {
        if(type.equals("Regular")) return new Regular(userName);
        if(type.equals("Admin")) return new Admin(userName);
        if(type.equals("Contributor")) return new Contributor(userName);
        return null;
    }
}
