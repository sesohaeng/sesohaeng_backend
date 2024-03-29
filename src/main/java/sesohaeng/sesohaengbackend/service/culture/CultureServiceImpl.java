package sesohaeng.sesohaengbackend.service.culture;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sesohaeng.sesohaengbackend.domain.area.Area;
import sesohaeng.sesohaengbackend.domain.area.AreaRepository;
import sesohaeng.sesohaengbackend.domain.culture.Culture;
import sesohaeng.sesohaengbackend.domain.culture.CultureRepository;
import sesohaeng.sesohaengbackend.domain.place.Place;
import sesohaeng.sesohaengbackend.domain.place.PlaceRepository;
import sesohaeng.sesohaengbackend.dto.response.culture.CultureResponseAreaDto;
import sesohaeng.sesohaengbackend.dto.response.culture.CultureResponseDto;
import sesohaeng.sesohaengbackend.exception.NoDataException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CultureServiceImpl implements CultureService{
    private final CultureRepository cultureRepository;

    private final AreaRepository areaRepository;
    private final PlaceRepository placeRepository;

    @Transactional
    public List<CultureResponseAreaDto> getCulturesByArea(Long areaId){
        Area area = areaRepository.findById(areaId).orElseThrow(()->new NoDataException("특구가 존재하지 않습니다."));
        List<Place> places = placeRepository.findAllByArea(area);
        List<CultureResponseAreaDto> responseDtos = new ArrayList<>();

        for(Place place : places){
            List<Culture> cultures = cultureRepository.findAllByPlace(place);
            if(cultures.isEmpty()){
                continue;
            }
            for (Culture culture: cultures) {
                responseDtos.add(new CultureResponseAreaDto(
                        place.getArea().getAreaName(),
                        culture.getId(),
                        culture.getPlace().getId(),
                        culture.getPlace().getLatitude(),
                        culture.getPlace().getLongitude(),
                        culture.getClassification(),
                        culture.getBorough(),
                        culture.getCultureName(),
                        culture.getCultureDatetime(),
                        culture.getTargetUser(),
                        culture.getFee(),
                        culture.getCast(),
                        culture.getCulture_url(),
                        culture.getCultureImage(),
                        culture.getApplicationDate(),
                        culture.getStartDatetime(),
                        culture.getEndDatetime()
                ));
            }
        }
        return responseDtos;
    }
}
