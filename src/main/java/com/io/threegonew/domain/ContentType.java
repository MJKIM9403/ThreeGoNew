package com.io.threegonew.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Table(name = "t_c_type")
public class ContentType {
    @Id
    @Column(name = "contenttypeid")
    private String contentTypeId;
    @Column(name = "ctype_name")
    private String contentTypeName;

    public void updateEntityFromApiResponse(ContentType newContentType){
        if(this.equals(newContentType)){
            this.contentTypeId = newContentType.contentTypeId;
            this.contentTypeName = newContentType.contentTypeName;
        }
    }
}
