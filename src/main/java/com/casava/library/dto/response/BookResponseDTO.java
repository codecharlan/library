package com.casava.library.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookResponseDTO {
    private UUID id;
    private String title;
    private String author;
    private int publishedYear;
    private String isbn;
    private int copiesAvailable;
}
