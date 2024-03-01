package org.example;

import java.util.ArrayList;
import java.util.List;

public class Admin extends Staff {

    public static class RequestHolder {

        private static List<Request> requestsList = new ArrayList<Request>();

        public static void addRequest(Request r)
        {
            requestsList.add(r);
        }

        public static void renoveReqest(Request r)
        {
            requestsList.remove(r);
        }

        public static List<Request> getRequestsList() {
            return requestsList;
        }

        public static void setRequestsList(List<Request> requestsList) {
            RequestHolder.requestsList = requestsList;
        }
    }
    public Admin(String userName) {
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
