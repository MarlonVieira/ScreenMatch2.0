package br.com.alura.screenmatch;

import br.com.alura.screenmatch.model.SeriesData;
import br.com.alura.screenmatch.service.ConsumptionAPI;
import br.com.alura.screenmatch.service.ConvertData;
import br.com.alura.screenmatch.service.IConvertData;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		var consumptionAPI = new ConsumptionAPI();
		var json = consumptionAPI.getData("https://www.omdbapi.com/?t=gilmore+girls&apikey=6585022c");
		System.out.println(json);

		//json = consumptionAPI.getData("https://coffee.alexflipnote.dev/random.json");
		//System.out.println(json);

		ConvertData convertData = new ConvertData();
		SeriesData data = convertData.getData(json, SeriesData.class);
		System.out.println(data);
	}
}
