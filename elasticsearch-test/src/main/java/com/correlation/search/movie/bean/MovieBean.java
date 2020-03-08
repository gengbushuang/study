package com.correlation.search.movie.bean;

import java.util.List;
import java.util.Map;

public class MovieBean {

    private int id;

    private String poster_path;

    private List<Map<String,String>> production_countries;

    private String revenue;

    private String overview;

    private boolean video;

    private List<MovieGenresBean> genres;

    private String title;

    private String tagline;

    private int vote_count;

    private String homepage;

    private MovieBelongsToCollectionBean belongs_to_collection;

    private String original_language;

    private String status;

    private List<Map<String,String>> spoken_languages;

    private String imdb_id;

    private boolean adult;

    private String backdrop_path;

    private List<MovieProductionCompaniesBean> production_companies;

    private String release_date;

    private double popularity;

    private String original_title;

    private long budget;

    private List<MovieCastBean> cast;

    private List<MovieDirectorsBean> directors;

    private float vote_average;

    private int runtime;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public List<Map<String, String>> getProduction_countries() {
        return production_countries;
    }

    public void setProduction_countries(List<Map<String, String>> production_countries) {
        this.production_countries = production_countries;
    }

    public String getRevenue() {
        return revenue;
    }

    public void setRevenue(String revenue) {
        this.revenue = revenue;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public List<MovieGenresBean> getGenres() {
        return genres;
    }

    public void setGenres(List<MovieGenresBean> genres) {
        this.genres = genres;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public int getVote_count() {
        return vote_count;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public MovieBelongsToCollectionBean getBelongs_to_collection() {
        return belongs_to_collection;
    }

    public void setBelongs_to_collection(MovieBelongsToCollectionBean belongs_to_collection) {
        this.belongs_to_collection = belongs_to_collection;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Map<String, String>> getSpoken_languages() {
        return spoken_languages;
    }

    public void setSpoken_languages(List<Map<String, String>> spoken_languages) {
        this.spoken_languages = spoken_languages;
    }

    public String getImdb_id() {
        return imdb_id;
    }

    public void setImdb_id(String imdb_id) {
        this.imdb_id = imdb_id;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public List<MovieProductionCompaniesBean> getProduction_companies() {
        return production_companies;
    }

    public void setProduction_companies(List<MovieProductionCompaniesBean> production_companies) {
        this.production_companies = production_companies;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public long getBudget() {
        return budget;
    }

    public void setBudget(long budget) {
        this.budget = budget;
    }

    public List<MovieCastBean> getCast() {
        return cast;
    }

    public void setCast(List<MovieCastBean> cast) {
        this.cast = cast;
    }

    public List<MovieDirectorsBean> getDirectors() {
        return directors;
    }

    public void setDirectors(List<MovieDirectorsBean> directors) {
        this.directors = directors;
    }

    public float getVote_average() {
        return vote_average;
    }

    public void setVote_average(float vote_average) {
        this.vote_average = vote_average;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }
}
