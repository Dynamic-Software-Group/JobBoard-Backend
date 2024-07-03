package dev.dynamic.jobboard.endpoints;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/recommendations")
public class RecommendationController {

    @GetMapping("/get")
    public ResponseEntity<?> getRecommendations() {
        return ResponseEntity.ok().build();
    }
}
