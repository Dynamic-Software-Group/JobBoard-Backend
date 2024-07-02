package dev.dynamic.jobboard.endpoints;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/business")
public class BusinessController {

    @PatchMapping(value = "/update")
    public ResponseEntity<?> updateBusiness() {
        return ResponseEntity.ok().build();
    }
}
