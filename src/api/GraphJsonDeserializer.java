package api;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Map.Entry;


public class GraphJsonDeserializer implements JsonDeserializer<directed_weighted_graph> {

    @Override
    public directed_weighted_graph deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        directed_weighted_graph graph = new DWGraph_DS();

        if (jsonObject.get("Nodes") instanceof JsonObject) {        //true if its like our save way
            //loading mapNodes
            JsonObject nodeJsonObject = jsonObject.get("Nodes").getAsJsonObject();
            for (Entry<String, JsonElement> set : nodeJsonObject.entrySet()) {
                JsonElement jsonValueElement = set.getValue();           //the value of the hashmap as json element
                int key = jsonValueElement.getAsJsonObject().get("id").getAsInt();
                int tag = jsonValueElement.getAsJsonObject().get("tag").getAsInt();
                double weight = jsonValueElement.getAsJsonObject().get("weight").getAsDouble();
                node_data n = new Node(key, tag, weight);
                graph.addNode(n);
            }

            //loading mapEdges
            JsonObject edgeJsonObject = jsonObject.get("Edges").getAsJsonObject();
            for (Entry<String, JsonElement> set : edgeJsonObject.entrySet()) {
                String hashKey = set.getKey();      //the key of the hashmap
                JsonObject neiJsonObject = edgeJsonObject.get(hashKey).getAsJsonObject();
                for (Entry<String, JsonElement> set2 : neiJsonObject.entrySet()) {
                    JsonElement jsonValueElement = set2.getValue();        //the value of the inner hashmap as json element
                    int src = jsonValueElement.getAsJsonObject().get("src").getAsInt();
                    int dest = jsonValueElement.getAsJsonObject().get("dest").getAsInt();
                    double weight = jsonValueElement.getAsJsonObject().get("w").getAsDouble();
                    graph.connect(src, dest, weight);
                }
            }
        } else {
            //loading mapNodes
            JsonArray nodeJsonObject = jsonObject.get("Nodes").getAsJsonArray();
            for (int i = 0; i<nodeJsonObject.size();i++){
                JsonElement jsonValueElement = nodeJsonObject.get(i);
                int key = jsonValueElement.getAsJsonObject().get("id").getAsInt();
                node_data n = new Node(key);
//                String[]location = jsonValueElement.getAsJsonObject().get("pos").getAsString().split(",");
//                double x = Double.parseDouble(location[0]);
//                double y = Double.parseDouble(location[1]);
//                double z = Double.parseDouble(location[2]);
//                geo_location pos = new GeoLocation(x,y,z);
//                n.setLocation(pos);
                graph.addNode(n);
            }

            //loading Edges
            JsonArray edgeJsonArray = jsonObject.get("Edges").getAsJsonArray();
            for (int i = 0; i<edgeJsonArray.size();i++){
                JsonElement jsonValueElement = edgeJsonArray.get(i);
                int src = jsonValueElement.getAsJsonObject().get("src").getAsInt();
                double weight = jsonValueElement.getAsJsonObject().get("w").getAsDouble();
                int dest = jsonValueElement.getAsJsonObject().get("dest").getAsInt();
                graph.connect(src, dest, weight);
            }
        }
        return graph;
    }
}
