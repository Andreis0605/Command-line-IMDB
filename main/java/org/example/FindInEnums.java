package org.example;

public class FindInEnums {
    public static Genre findGenre(String gen)
    {
        for(Genre genre:Genre.values())
        {
            if(genre.name().equals(gen)) return genre;
        }
        return null;
    }

    public static RequestType findReqest(String req)
    {
        for(RequestType reqType : RequestType.values())
        {
            if(reqType.name().equals(req)) return reqType;
        }
        return null;
    }

    public static AccountType findAccountType(String acc)
    {
        for(AccountType accType : AccountType.values())
        {
            if(accType.name().equals(acc)) return accType;
        }
        return null;
    }


}
