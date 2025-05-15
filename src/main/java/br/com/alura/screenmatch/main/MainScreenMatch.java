package br.com.alura.screenmatch.main;

import br.com.alura.screenmatch.model.EpisodesData;
import br.com.alura.screenmatch.model.SeasonData;
import br.com.alura.screenmatch.model.SeriesData;
import br.com.alura.screenmatch.service.ConsumptionAPI;
import br.com.alura.screenmatch.service.ConvertData;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainScreenMatch {
    private Scanner scan = new Scanner(System.in);
    private ConsumptionAPI consumptionAPI = new ConsumptionAPI();
    private ConvertData convertData = new ConvertData();
    private List<SeasonData> seasons = new ArrayList<>();
    private final String URL = "https://www.omdbapi.com/?t=";
    private final String SEASON = "&season=";
    private final String API_KEY = "&apikey=6585022c";

    public void showMenu() {
        System.out.printf("Enter the name of the serie: ");
        var serieName = scan.nextLine();
        var json = consumptionAPI.getData(URL + serieName.replace(" ", "+") + API_KEY);
        SeriesData seriesData = convertData.getData(json, SeriesData.class);

        for (int i = 1; i <= seriesData.totalSeasons(); i++) {
            json = consumptionAPI.getData(URL + serieName.replace(" ", "+") + SEASON + i + API_KEY);
            SeasonData seasonData = convertData.getData(json, SeasonData.class);
            seasons.add(seasonData);
        }
        seasons.forEach(System.out::println);

//        for (int i = 0; i < seriesData.totalSeasons(); i++) {
//            List<EpisodesData> episodesData = seasons.get(i).episodes();
//            for (int j = 0; j < episodesData.size(); j++) {
//                System.out.println(episodesData.get(j).title());
//            }
//        }
        seasons.forEach(s -> s.episodes().forEach(e -> System.out.println(e.title())));
    }
}