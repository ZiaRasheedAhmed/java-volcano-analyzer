import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class VolcanoAnalyzer {
    private List<Volcano> volcanos;

    public void loadVolcanoes(Optional<String> pathOpt) throws IOException, URISyntaxException {
        try{
            String path = pathOpt.orElse("volcano.json");
            URL url = this.getClass().getClassLoader().getResource(path);
            String jsonString = new String(Files.readAllBytes(Paths.get(url.toURI())));
            ObjectMapper objectMapper = new ObjectMapper();
            TypeFactory typeFactory = objectMapper.getTypeFactory();
            volcanos = objectMapper.readValue(jsonString, typeFactory.constructCollectionType(List.class, Volcano.class));
        } catch(Exception e){
            throw(e);
        }
    }

    public Integer numbVolcanoes(){
        return volcanos.size();
    }

    //add methods here to meet the requirements in README.md

//1. Return the volcanoes that erupted in the 1980s.
    public List<Volcano> eruptedInEighties(){
        return volcanos.stream().filter(i -> i.getYear() >= 1980 && i.getYear()<1990).collect(Collectors.toList());
    } 

//2. Return an array of the names of volcanoes that had an eruption with a Volcanic Explosivity Index (VEI) of 6 or higher.
    public String[] highVEI() {
        return volcanos.stream().filter(i -> i.getVEI() >= 6).map(Volcano::getName).toArray(String[]::new);
    }
//3. Return the eruption with the highest number of recorded deaths.
    public Volcano mostDeadly (){
        return volcanos.stream().max(Comparator.comparing(a -> Integer.parseInt(a.getDEATHS().isEmpty()?"0":a.getDEATHS()))).get();
    }
//4. Return the percentage of eruptions that caused tsunamis.
    public double causedTsunami(){
        return volcanos.stream().filter(i -> i.getTsu().equals("tsu")).collect(Collectors.toList()).size() *100 / volcanos.size();
    }
//5. Return the most common type of volcano in the set.
    public String mostCommonType(){
        List<String> commonType = volcanos.stream().map(i->i.getType()).distinct().collect(Collectors.toList());
        List<Integer> countList = new ArrayList<>();
        commonType.forEach(element -> {
         countList.add( volcanos.stream().filter(i -> i.getType().equals(element)).collect(Collectors.toList()).size());
        });
        return commonType.get(countList.indexOf(Collections.max(countList)));
    }
//6. Return the number of eruptions when supplied a country as an argument.
    public int eruptionsByCountry(String country){
        return volcanos.stream().filter(i->i.getCountry().equals(country)).collect(Collectors.toList()).size();
    }
//7. Return the average elevation of all eruptions.
    public double averageElevation(){
        double summ= volcanos.stream().map(Volcano::getElevation).mapToInt(i -> i).sum();
        double d= summ/volcanos.size();
        return d;
    }
//8. Return an array of types of volcanoes.
    public String[] volcanoTypes(){
        String[] types = volcanos.stream().map(i->i.getType()).distinct().toArray(String[]::new);
        return types;
    }
// 9. Return the percentage of eruptions that occurred in the Northern
    // Hemisphere.
    public double percentNorth() {
        double sum = volcanos.stream().filter(i -> i.getLatitude() >= 0 && i.getLatitude() <= 90)
                .collect(Collectors.toList()).size();
        double d = sum / volcanos.size();
        double per = d * 100;
        return per;
    }
// 10. Return the names of eruptions that occurred after 1800, that did NOT
    // cause a tsunami, happened in the Southern Hemisphere, and had a VEI of 5.
    public String[] manyFilters() {
        String[] names = volcanos.stream().filter(i -> i.getYear() > 1800 && i.getTsu().equals("")
                && i.getLatitude() <= 0 && i.getLatitude() >= -90 && i.getVEI() == 5).map(j -> j.getName())
                .toArray(String[]::new);
        return names;
    }
}
