package com.codigo.codigo.reniec;

import com.codigo.codigo.dto.ReniecResponse;
import feign.Headers;
import feign.Param;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "reniec-client", url = "https://api.decolecta.com/v1/reniec/dni")
public interface ReniecClient {

    // metodo que hace la consulta
    @GetMapping()
    ReniecResponse buscarPersona(
            @RequestParam("numero") String dni,
            @RequestHeader("Authorization") String token);
}
