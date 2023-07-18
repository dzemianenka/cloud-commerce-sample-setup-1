package firework.controller;

import firework.model.businessstore.create.CreateBusinessStore;
import firework.model.businessstore.create.InputCreateBusinessStore;
import firework.model.businessstore.get.BusinessStore;
import firework.model.businessstore.update.InputUpdateBusinessStore;
import firework.model.businessstore.update.UpdateBusinessStore;
import firework.service.BusinessStoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Business Stores Controller")
@RestController
@RequestMapping("/business-stores")
@RequiredArgsConstructor
public class BusinessStoreController {

    private final BusinessStoreService businessStoreService;

    @GetMapping
    @Operation(operationId = "getBusinessStores", summary = "Get Business Stores for business")
    @ApiResponse(content = @Content(mediaType = APPLICATION_JSON_VALUE))
    public ResponseEntity<List<BusinessStore>> getBusinessStores() {
        return ResponseEntity.ok(businessStoreService.getBusinessStores());
    }

    @PostMapping
    @Operation(operationId = "createBusinessStore", summary = "Create Business Store for business")
    @ApiResponse(content = @Content(mediaType = APPLICATION_JSON_VALUE))
    public ResponseEntity<CreateBusinessStore> createBusinessStore(@RequestBody InputCreateBusinessStore input) {
        return ResponseEntity.ok(businessStoreService.createBusinessStore(input));
    }

    @PutMapping
    @Operation(operationId = "updateBusinessStore", summary = "Update Business Store for business")
    @ApiResponse(content = @Content(mediaType = APPLICATION_JSON_VALUE))
    public ResponseEntity<UpdateBusinessStore> updateBusinessStore(@RequestBody InputUpdateBusinessStore input) {
        return ResponseEntity.ok(businessStoreService.updateBusinessStore(input));
    }

    //Testing endpoint TODO for the removal
    @PostMapping("/sync")
    @Operation(operationId = "syncBusinessStores", summary = "sync Business Stores for business")
    @ApiResponse(content = @Content(mediaType = APPLICATION_JSON_VALUE))
    public ResponseEntity<String> syncBusinessStores() {
        businessStoreService.syncBusinessStores();
        return ResponseEntity.ok("SUCCESS");
    }
}
