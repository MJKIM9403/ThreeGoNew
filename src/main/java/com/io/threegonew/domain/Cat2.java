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
@Table(name = "t_cat2")
public class Cat2 {
    @Id
    @Column(name = "cat2")
    private String cat2;
    @Column(name = "cat2_name")
    private String cat2Name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cat1")
    private Cat1 cat1;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "cat2"
    )
    private List<Cat3> cat3List = new ArrayList<>();

    public void updateEntityFromApiResponse(Cat2 newCat2){
        if(this.equals(newCat2)){
            this.cat2 = newCat2.cat2;
            this.cat2Name = newCat2.cat2Name;
            this.cat1 = newCat2.cat1;
            this.cat3List = newCat2.cat3List;
        }
    }
}
