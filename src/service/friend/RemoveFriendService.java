package service.friend;

import org.json.JSONObject;

/**
 * Created by jruiz on 2/2/17.
 */
public class RemoveFriendService {

    public static JSONObject removeFriend(String key, String to) {

        if ((key == null) || (to == null))
            return serviceTools.ErrorTools.serviceRefused("Wrong arguments", 0);

        String from = serviceTools.UserTools.getUserFromKey(key);

        int from_id = serviceTools.UserTools.getIdUser(from);

        // On doit voir si l'utilisateur est connecté ou pas
        if (!serviceTools.UserTools.userConnect(from_id))
            return serviceTools.ErrorTools.serviceRefused("Utilisateur " + from + " n'est pas connecté", 1);


        // On voit si la personne de qui on veut etre ami existe
        if (!serviceTools.UserTools.userExists(to))
            return serviceTools.ErrorTools.serviceRefused("User " + to + " inconnu", 2);

        int to_id = serviceTools.UserTools.getIdUser(to);
        if ((from_id < 0) || (to_id < 0))
            return serviceTools.ErrorTools.serviceRefused("Erreur dans la BD", 3);

        // On voit s'il ne sont pas amis
        if (!serviceTools.FriendTools.isFriend(from_id, to_id))
            return serviceTools.ErrorTools.serviceRefused("User " + from + " is not friends with " + to, 4);

        if (!serviceTools.FriendTools.removeFriend(from_id, to_id))
            return serviceTools.ErrorTools.serviceRefused("Erreur dans l'elimination de l'ami", 5);

        return serviceTools.ErrorTools.serviceAcceptedEmpty();
    }
}
