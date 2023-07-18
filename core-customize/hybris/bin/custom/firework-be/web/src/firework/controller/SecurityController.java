package firework.controller;

import firework.model.hmac.InputGetHmacSecret;
import firework.service.SecurityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Security Controller")
@RestController
@RequestMapping("/security")
@RequiredArgsConstructor
public class SecurityController {

    private final SecurityService securityService;

    @PostMapping("/hmacSecret")
    @Operation(operationId = "getHmacSecret", summary = "Claims HMAC Secret")
    @ApiResponse(content = @Content(mediaType = APPLICATION_JSON_VALUE))
    public ResponseEntity<String> getHmacSecret(@RequestBody InputGetHmacSecret inputGetHmacSecret) {
        return ResponseEntity.ok(securityService.getHmacSecret(inputGetHmacSecret.businessStoreId()));
    }

}
