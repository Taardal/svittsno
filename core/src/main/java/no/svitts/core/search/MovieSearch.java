package no.svitts.core.search;

public class MovieSearch extends Search {

    private String query;
    private MovieSearchType movieSearchType;

    public MovieSearch(String query, MovieSearchType movieSearchType) {
        this.query = query;
        this.movieSearchType = movieSearchType;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public MovieSearchType getMovieSearchType() {
        return movieSearchType;
    }

    public void setMovieSearchType(MovieSearchType movieSearchType) {
        this.movieSearchType = movieSearchType;
    }

}
