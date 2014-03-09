package be.kdg.groepa.dtos;

import be.kdg.groepa.model.PlaceTime;
import be.kdg.groepa.model.Route;
import be.kdg.groepa.model.WeekdayRoute;
import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by delltvgateway on 3/6/14.
 */
public class GetRouteDTO {
    private String beginDate;
    private String endDate;
    private UserDTO chauffeur;
    private CarDTO car;
    private Map<Integer, List<PassageDTO>> passages;

    public GetRouteDTO() {
        this.passages = new HashMap<>();
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(LocalDate beginDate) {
        this.beginDate = beginDate.toString();
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate.toString();
    }

    public UserDTO getChauffeur() {
        return chauffeur;
    }

    public void setChauffeur(UserDTO chauffeur) {
        this.chauffeur = chauffeur;
    }

    public CarDTO getCar() {
        return car;
    }

    public void setCar(CarDTO car) {
        this.car = car;
    }

    public Map<Integer, List<PassageDTO>> getPassages() {
        return passages;
    }

    public void setPassages(Map<Integer, List<PassageDTO>> passages) {
        this.passages = passages;
    }

    public void addPassageList(int dayNum, List<PassageDTO> passages) {
        this.passages.put(dayNum, passages);
    }

    public static GetRouteDTO createFromModel(Route route,List<WeekdayRoute> wdrs) {
        GetRouteDTO dto = new GetRouteDTO();
        dto.setBeginDate(route.getBeginDate().toLocalDate());
        dto.setEndDate(route.getEndDate().toLocalDate());
        dto.setChauffeur(UserDTO.createBasic(route.getChauffeur()));
        dto.setCar(new CarDTO(route.getCar()));

        if (route.isRepeating()) {
            for (WeekdayRoute wdr : wdrs) {
                List<PassageDTO> passageDTOs = new ArrayList<>();
                int seqnr=1;
                for (PlaceTime pt : wdr.getPlaceTimes()) {
                    PassageDTO passageDTO = new PassageDTO(pt.getPlacetimeId(), seqnr, pt.getPlace().getName(), pt.getTime(), pt.getPlace().getLat(), pt.getPlace().getLon());
                    passageDTOs.add(passageDTO);
                    seqnr++;
                }
                dto.addPassageList(wdr.getDay(), passageDTOs);
            }
        } else {
            List<PassageDTO> passageDTOs = new ArrayList<>();
            int seqnr=1;
            for (PlaceTime pt : route.getPlaceTimes()) {
                PassageDTO passageDTO = new PassageDTO(pt.getPlacetimeId(), seqnr, pt.getPlace().getName(), pt.getTime(), pt.getPlace().getLat(), pt.getPlace().getLon());
                passageDTOs.add(passageDTO);
                seqnr++;
            }
            dto.addPassageList(route.getBeginDate().getDayOfWeek().getValue()-1, passageDTOs);
        }

        return dto;
    }
}
