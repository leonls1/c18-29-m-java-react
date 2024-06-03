package com.desarrollo.adopcion.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MessageRequest {
    
    @NotBlank
    private Long idSender;

    @NotBlank
    private String content;
    //podria aca ser que me envia el match directamente?
    @NotBlank
    private Long id_match;
}
