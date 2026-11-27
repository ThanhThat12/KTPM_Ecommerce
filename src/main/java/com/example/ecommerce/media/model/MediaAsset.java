package com.example.ecommerce.media.model;

import com.example.ecommerce.common.model.BaseDocument;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "media_assets")
public class MediaAsset extends BaseDocument {

    private String originalFilename;

    @Indexed(unique = true)
    private String storedFilename;

    private String contentType;

    private long sizeBytes;

    private String url;

    private String uploadedByUserId;
}
