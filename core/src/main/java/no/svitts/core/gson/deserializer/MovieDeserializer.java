package no.svitts.core.gson.deserializer;

import com.google.gson.*;
import no.svitts.core.date.KeyDate;
import no.svitts.core.file.ImageFile;
import no.svitts.core.file.ImageType;
import no.svitts.core.file.VideoFile;
import no.svitts.core.movie.Genre;
import no.svitts.core.movie.Movie;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieDeserializer extends CoreDeserializer implements JsonDeserializer<Movie> {

    @Override
    public Movie deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String id = getId(jsonObject);
        String name = jsonObject.get("name").getAsString();
        String imdbId = jsonObject.get("imdbId").getAsString();
        String tagline = jsonObject.get("tagline").getAsString();
        String overview = jsonObject.get("overview").getAsString();
        int runtime = jsonObject.get("runtime").getAsInt();
        KeyDate releaseDate = getKeyDate(jsonObject.get("releaseDate").getAsString());
        List<Genre> genres = getGenres(jsonObject.get("genres").getAsJsonArray());
        VideoFile videoFile = getVideoFile(jsonObject.get("videoFile").getAsJsonObject());
        Map<ImageType, ImageFile> imageFiles = getImageFiles(jsonObject.get("imageFiles").getAsJsonObject());
        return new Movie(id, name, imdbId, tagline, overview, runtime, releaseDate, genres, videoFile, imageFiles);
    }

    private KeyDate getKeyDate(String releaseDate) {
        return releaseDate != null && !releaseDate.equals("null") ? new KeyDate(releaseDate) : null;
    }

    private List<Genre> getGenres(JsonArray jsonArray) {
        List<Genre> genres = new ArrayList<>();
        for (JsonElement jsonElement : jsonArray) {
            String genreString = jsonElement.getAsString().toUpperCase().replaceAll("-", "_");
            genres.add(Genre.valueOf(genreString));
        }
        return genres;
    }

    private VideoFile getVideoFile(JsonObject jsonObject) {
        if (jsonObject != null && !jsonObject.isJsonNull()) {
            return new VideoFile(jsonObject.get("id").getAsString(), jsonObject.get("path").getAsString());
        } else {
            throw new RuntimeException("Could not deserialize video file");
        }
    }

    private Map<ImageType, ImageFile> getImageFiles(JsonObject jsonObject) {
        Map<ImageType, ImageFile> imageFiles = new HashMap<>();
        if (jsonObject != null && !jsonObject.isJsonNull()) {
            ImageFile poster = getImageFile(jsonObject.get("POSTER").getAsJsonObject());
            ImageFile backdrop = getImageFile(jsonObject.get("BACKDROP").getAsJsonObject());
            imageFiles.put(poster.getImageType(), poster);
            imageFiles.put(backdrop.getImageType(), backdrop);
        }
        return imageFiles;
    }

    private ImageFile getImageFile(JsonObject jsonObject) {
        if (jsonObject != null && !jsonObject.isJsonNull()) {
            String id = jsonObject.get("id").getAsString();
            String path = jsonObject.get("path").getAsString();
            ImageType imageType = ImageType.valueOf(jsonObject.get("type").getAsString().toUpperCase());
            return new ImageFile(id, path, imageType);
        } else {
            throw new RuntimeException("Could not deserialize image file");
        }
    }

}
