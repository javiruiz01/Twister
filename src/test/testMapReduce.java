package test;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MapReduceCommand;
import com.mongodb.MapReduceOutput;

public class testMapReduce {

    public static void main(String[] args) {

        String text = "A ver que tal si hago asi";
        String regex = "/\\w+/g";
        System.out.println("REGEX: " + regex);
        if (text.matches(regex))
            System.out.println("It functions");
        String map =
                "function() {" +
                        "  var text = \"this.text\";" +
                        "  var id = this.id;" +
                        "  var words = text.match(/\\w+/g);" +
                        "  var tf = {};" +
                        "  for (var i = 0; i < words.length; i++) {" +
                        "    if (tf[words[i]] == null) {" +
                        "      tf[words[i]] = 1;" +
                        "    } else {" +
                        "      tf[words[i]] += 1;" +
                        "    }" +
                        "  }" +
                        "  for (w in tf) {" +
                        "    var ret = {};" +
                        "    ret[id] = tf[w];" +
                        "    emit(w, ret);" +
                        "  }" +
                        "}";

        String reduce =
                "function(k, v) {" +
                        "  var df = v.lenth;" +
                        "  var ret = [];" +
                        "  for (value in v) {" +
                        "    for (k in v[value]) {" +
                        "      ret[k] = v[value][k];" +
                        "    }" +
                        "  }" +
                        "  return ret.toString();" +
                        "}";

        String finalize =
                "function(k, v) {" +
                        "  var df = v.length;" +
                        "  for (d in v) {" +
                        "    v[d] = v[d] * Math.log(N/df);" +
                        "  }" +
                        "  return v;" +
                        "}";
        DBCollection collection = BD.Database.getMongoCollection("messages");

        MapReduceCommand cmd = new MapReduceCommand(collection, map, reduce, "out", MapReduceCommand.OutputType.REPLACE, null);
        cmd.setFinalize(finalize);
        BasicDBObject obj = new BasicDBObject();
        obj.put("N", collection.count());
        cmd.setScope(obj);

        MapReduceOutput out = collection.mapReduce(cmd);

        for (DBObject o : out.results()) {
            System.out.println(o.toString() + "BATMAN!");
        }
    }

}
