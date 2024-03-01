package org.example;

public class Contributor extends Staff implements RequestsManager{
    public Contributor(String userName) {
        super(userName);
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
