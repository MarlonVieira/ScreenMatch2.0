package br.com.alura.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SeriesData(String title,
                         Integer totalSeasons,
                         @JsonAlias("imdbRating") String Rating,
                         String year) {
}