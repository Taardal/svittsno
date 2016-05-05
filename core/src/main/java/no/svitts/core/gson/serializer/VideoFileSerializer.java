package no.svitts.core.gson.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import no.svitts.core.file.VideoFile;

import java.lang.reflect.Type;

public class VideoFileSerializer extends CoreSerializer implements JsonSerializer<VideoFile> {

    @Override
    public JsonElement serialize(VideoFile videoFile, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("id", getJsonPrimitive(videoFile.getId()));
        jsonObject.add("name", getJsonPrimitive(videoFile.getName()));
        jsonObject.add("path", getJsonPrimitive(videoFile.getPath()));
        jsonObject.add("size", getJsonPrimitive(videoFile.getSize()));
        return jsonObject;
    }

}
