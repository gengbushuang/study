package com.correlation.search.movie.bean;

import java.util.List;

/**
 * @author gengbushuang
 * @date 2022/2/24 14:26
 */
public class VideoBean {
    private String bestTitle;
    private String first_game_ad_industry_name;
    private String first_label;
    private String second_label;
    private String third_label;

    private String keyword;

    private String download_url;

    private List<String> video_type;
    private List<String> title;


    public String getBestTitle() {
        return bestTitle;
    }

    public void setBestTitle(String bestTitle) {
        this.bestTitle = bestTitle;
    }

    public String getFirst_game_ad_industry_name() {
        return first_game_ad_industry_name;
    }

    public void setFirst_game_ad_industry_name(String first_game_ad_industry_name) {
        this.first_game_ad_industry_name = first_game_ad_industry_name;
    }

    public String getFirst_label() {
        return first_label;
    }

    public void setFirst_label(String first_label) {
        this.first_label = first_label;
    }

    public String getSecond_label() {
        return second_label;
    }

    public void setSecond_label(String second_label) {
        this.second_label = second_label;
    }

    public String getThird_label() {
        return third_label;
    }

    public void setThird_label(String third_label) {
        this.third_label = third_label;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getDownload_url() {
        return download_url;
    }

    public void setDownload_url(String download_url) {
        this.download_url = download_url;
    }

    public List<String> getVideo_type() {
        return video_type;
    }

    public void setVideo_type(List<String> video_type) {
        this.video_type = video_type;
    }

    public List<String> getTitle() {
        return title;
    }

    public void setTitle(List<String> title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "VideoBean{" +
                "bestTitle='" + bestTitle + '\'' +
                ", first_game_ad_industry_name='" + first_game_ad_industry_name + '\'' +
                ", first_label='" + first_label + '\'' +
                ", second_label='" + second_label + '\'' +
                ", third_label='" + third_label + '\'' +
                ", keyword='" + keyword + '\'' +
                ", download_url='" + download_url + '\'' +
                ", video_type=" + video_type +
                ", title=" + title +
                '}';
    }
}
