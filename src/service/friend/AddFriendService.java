package service.friend;

import org.json.JSONObject;

/**
 * Created by jruiz on 2/2/17.
 */
public class AddFriendService {

    public static JSONObject addFriend(String key, String to) {

        if ((key == null) || (to == null))
            return serviceTools.ErrorTools.serviceRefused("Wrong arguments", 0);

        String from = serviceTools.UserTools.getUserFromKey(key);
        System.out.println("KEY = " + from);

        // On prend les deux IDs pour pouvoir faire des querys a la table FRIEND
        int from_id = serviceTools.UserTools.getIdUser(from);

        if (!serviceTools.UserTools.shouldBeConnected(from_id)) {
            serviceTools.UserTools.deleteConnection(key);
            return serviceTools.ErrorTools.serviceRefused("User should not be connected", 100);
        }

        // On doit voir si l'utilisateur est connecté ou pas
        if (!serviceTools.UserTools.userConnect(from_id))
            return serviceTools.ErrorTools.serviceRefused("User " + from + " is not connected", 1);

        // On voit si la personne de qui on veut etre ami existe
        if (!serviceTools.UserTools.userExists(to))
            return serviceTools.ErrorTools.serviceRefused("User " + to + " is not in our databases", 2);

        int to_id = serviceTools.UserTools.getIdUser(to);
        if ((from_id < 0) || (to_id < 0))
            return serviceTools.ErrorTools.serviceRefused("Something went wrong with the database", 3);

        // On voit si il est deja ami
        if (serviceTools.FriendTools.isFriend(from_id, to_id))
            return serviceTools.ErrorTools.serviceRefused("User " + from + " is already friends with " + to, 4);

        // On ajoute l'amitie
        if (!serviceTools.FriendTools.insertFriend(from_id, to_id))
            return serviceTools.ErrorTools.serviceRefused("Something went wrong while adding your new friend", 5);

        return serviceTools.ErrorTools.serviceRefused("A wild error appeared", 100);
//        return serviceTools.ErrorTools.serviceAcceptedEmpty();
    }
}
