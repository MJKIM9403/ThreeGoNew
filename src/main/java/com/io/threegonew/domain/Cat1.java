package com.io.threegonew.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "t_cat1")
public class Cat1 {
    @Id
    @Column(name = "cat1")
    private String cat1;
    @Column(name = "cat1_name")
    private String cat1Name;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "cat1"
    )
    private List<Cat2> cat2List = new ArrayList<>();

    public void updateEntityFromApiResponse(Cat1 newCat1){
        if(this.equals(newCat1)){
            this.cat1 = newCat1.cat1;
            this.cat1Name = newCat1.cat1Name;
            this.cat2List = newCat1.cat2List;
        }
    }
}
