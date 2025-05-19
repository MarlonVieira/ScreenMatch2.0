package br.com.alura.screenmatch.main;

import br.com.alura.screenmatch.model.Episode;
import br.com.alura.screenmatch.model.EpisodesData;
import br.com.alura.screenmatch.model.SeasonData;
import br.com.alura.screenmatch.model.SeriesData;
import br.com.alura.screenmatch.service.ConsumptionAPI;
import br.com.alura.screenmatch.service.ConvertData;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class MainScreenMatch {
    private Scanner scan = new Scanner(System.in);
    private ConsumptionAPI consumptionAPI = new ConsumptionAPI();
    private ConvertData convertData = new ConvertData();
    private List<SeasonData> seasons = new ArrayList<>();

    public void showMenu() {
        final String URL = "https://www.omdbapi.com/?t=";
        final String SEASON = "&season=";
        final String API_KEY = "&apikey=6585022c";

        System.out.print("Enter the name of the serie: ");
        var serieName = scan.nextLine();
        var json = consumptionAPI.getData(URL + serieName.replace(" ", "+") + API_KEY);
        SeriesData seriesData = convertData.getData(json, SeriesData.class);

        for (int i = 1; i <= seriesData.totalSeasons(); i++) {
            json = consumptionAPI.getData(URL + serieName.replace(" ", "+") + SEASON + i + API_KEY);
            SeasonData seasonData = convertData.getData(json, SeasonData.class);
            seasons.add(seasonData);
        }
        seasons.forEach(System.out::println);

        for (int i = 0; i < seriesData.totalSeasons(); i++) {
            List<EpisodesData> episodesData = seasons.get(i).episodes();
            for (int j = 0; j < episodesData.size(); j++) {
                System.out.println(episodesData.get(j).title());
            }
        }
        seasons.forEach(s -> s.episodes().forEach(e -> System.out.println(e.title())));

//        List<String> names = Arrays.asList("Jacque", "Iasmin", "Paulo", "Rodrigo", "Nico");
//
//        names.stream()
//                .sorted()
//                .limit(3)
//                .filter(n -> n.startsWith("N"))
//                .map(n -> n.toUpperCase())
//                .forEach(System.out::println);

        List<EpisodesData> episodesData = seasons.stream()
                                                    .flatMap(t -> t.episodes().stream())
                                                    .collect(Collectors.toList());

        System.out.println("\nTop five episodes: ");
        episodesData.stream()
                .filter(e -> !e.rating().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(EpisodesData::rating).reversed())
                .limit(5)
                .forEach(System.out::println);

        List<Episode> episodes = seasons.stream()
                                            .flatMap(s -> s.episodes().stream()
                                                    .map(d -> new Episode(s.season(), d))
                                            ).collect(Collectors.toList());

        episodes.forEach(System.out::println);

        System.out.print("From which year do you want to see the episodes: ");
        var year = scan.nextInt();
        scan.nextLine();

        LocalDate searchDate= LocalDate.of(year, 1, 1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        episodes.stream()
                .filter(e -> e.getReleaseDate() != null && e.getReleaseDate().isAfter(searchDate))
                .forEach(e -> System.out.println("Season:" + e.getSeason() +
                                                         " Episode: " + e.getEpisode() +
                                                         " Rating: " + e.getRating() +
                                                         " Release Date: " + e.getReleaseDate().format(formatter)
                ));
    }
}