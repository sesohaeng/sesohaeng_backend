package sesohaeng.sesohaengbackend.domain.culture;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
import sesohaeng.sesohaengbackend.domain.place.Place;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "CULTURE")
public class Culture {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String classification;

    @Column
    private String borough;

    @Column
    private String cultureName;

    @Column
    private String cultureDatetime;

    @Column
    private String targetUser;

    @Column
    private String fee;

    @Column
    private String cast;

    @Column
    private String culture_url;

    @Column
    @Nullable
    private String cultureImage;

    @Column
    private Date applicationDate;

    @Column
    private LocalDateTime startDatetime;

    @Column
    private LocalDateTime endDatetime;

    @ManyToOne
    @JoinColumn(name = "place_id")
    private Place place;

    private Culture(String cultureName, Place place){
        this.cultureName = cultureName;
        this.place = place;
    }
    private Culture(Long cultureId,String cultureName, Place place){
        this.id = cultureId;
        this.cultureName = cultureName;
        this.place = place;
    }
    public static Culture newTestInstance(String cultureName, Place place){
        return new Culture(cultureName,place);
    }
    public static Culture newTestInstance(Long cultureId,String cultureName, Place place){
        return new Culture(cultureId,cultureName,place);
    }
}
