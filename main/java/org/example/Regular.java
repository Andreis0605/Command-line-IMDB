package org.example;

import javax.naming.directory.AttributeInUseException;

public class Regular extends User implements RequestsManager {


    public Regular(String userName) {
        super(userName);
    }

    public void addRating(Production p, Rating r) {
        p.addRating(r);
    }

    public void addRating(Production p, int grade, String description) {
        p.addRating(new Rating(super.getUsername(), grade, description));
    }

    @Override
    public void createRequest(Request r) {
        if (r.getRequestType() == RequestType.DELETE_ACCOUNT || r.getRequestType() == RequestType.OTHERS)
            Admin.RequestHolder.addRequest(r);
        else {
            for (User auxUser : IMDB.getInstance().getUsersList()) {
                if ((auxUser.getUserType() == AccountType.Contributor) || (auxUser.getUserType() == AccountType.Admin)) {
                    if (auxUser.getUsername().equals(r.getTo())) ((Staff) auxUser).addListRequestForUser(r);
                }
            }
        }
    }

    @Override
    public void removeRequest(Request r) {
        if (r.getRequestType() == RequestType.DELETE_ACCOUNT || r.getRequestType() == RequestType.OTHERS)
            Admin.RequestHolder.renoveReqest(r);
        else {
            for (User auxUser : IMDB.getInstance().getUsersList()) {
                if (auxUser.getUserType() == AccountType.Contributor || auxUser.getUserType() == AccountType.Admin) {
                    if (auxUser.getUsername().equals(r.getTo())) {
                        ((Staff) auxUser).removeListRequestForUser(r);
                    }
                }
            }
        }
    }
}
