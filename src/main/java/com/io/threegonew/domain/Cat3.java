package com.io.threegonew.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "t_cat3")
public class Cat3 {
    @Id
    @Column(name = "cat3")
    private String cat3;
    @Column(name = "cat3_name")
    private String cat3Name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cat2")
    private Cat2 cat2;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cat1")
    private Cat1 cat1;

    public void updateEntityFromApiResponse(Cat3 newCat3){
        if(this.equals(newCat3)){
            this.cat3 = newCat3.cat3;
            this.cat3Name = newCat3.cat3Name;
            this.cat2 = newCat3.cat2;
            this.cat1 = newCat3.cat1;
        }
    }
}
