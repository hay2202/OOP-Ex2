import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Map.Entry;


public class GraphJsonDeserializer implements JsonDeserializer<directed_weighted_graph> {

    @Override
    public directed_weighted_graph deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        directed_weighted_graph graph = new DWGraph_DS();

        //loading mapNodes
        JsonObject nodeJsonObject = jsonObject.get("mapNodes").getAsJsonObject();
        for (Entry<String ,JsonElement> set : nodeJsonObject.entrySet()){
            JsonElement jsonValueElement = set.getValue();           //the value of the hashmap as json element
            int key = jsonValueElement.getAsJsonObject().get("key").getAsInt();
            int tag = jsonValueElement.getAsJsonObject().get("tag").getAsInt();
            double weight = jsonValueElement.getAsJsonObject().get("weight").getAsDouble();
//            String info = jsonValueElement.getAsJsonObject().get("info").getAsString();
//            geo_location location = jsonValueElement.getAsJsonObject().get("location");
            node_data n = new Node(key,tag,weight);
            graph.addNode(n);
        }

        //loading mapEdges
        JsonObject edgeJsonObject = jsonObject.get("mapEdges").getAsJsonObject();
        for (Entry<String ,JsonElement> set : edgeJsonObject.entrySet()){
            String hashKey = set.getKey();      //the key of the hashmap
            JsonObject neiJsonObject = edgeJsonObject.get(hashKey).getAsJsonObject();
            for (Entry<String ,JsonElement> set2 : neiJsonObject.entrySet()){
                JsonElement jsonValueElement = set2.getValue();        //the value of the inner hashmap as json element
                int src = jsonValueElement.getAsJsonObject().get("src").getAsInt();
                int dest = jsonValueElement.getAsJsonObject().get("dest").getAsInt();
                double weight = jsonValueElement.getAsJsonObject().get("weight").getAsDouble();
                graph.connect(src,dest,weight);
            }
        }
        return graph;
    }
}
