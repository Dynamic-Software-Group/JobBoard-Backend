package dev.dynamic.jobboard.endpoints;

import dev.dynamic.jobboard.endpoints.requests.businesses.CreateBusinessRequest;
import dev.dynamic.jobboard.endpoints.requests.businesses.UpdateBusinessRequest;
import dev.dynamic.jobboard.model.Business;
import dev.dynamic.jobboard.model.User;
import dev.dynamic.jobboard.repositories.BusinessRepository;
import dev.dynamic.jobboard.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/business")
public class BusinessController {

    private final BusinessRepository businessRepository;
    private final UserRepository userRepository;

    public BusinessController(BusinessRepository businessRepository, UserRepository userRepository) {
        this.businessRepository = businessRepository;
        this.userRepository = userRepository;
    }

    @PatchMapping(value = "/update")
    public ResponseEntity<?> updateBusiness(@RequestBody UpdateBusinessRequest request, @RequestParam Long businessId) {
        Optional<Business> businessOptional = businessRepository.findById(businessId);

        if (businessOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Business business = businessOptional.get();

        if (request.getAvatar() != null) {
            business.setAvatar(request.getAvatar());
        }

        if (request.getName() != null) {
            business.setName(request.getName());
        }

        if (request.getDescription() != null) {
            business.setDescription(request.getDescription());
        }

        if (request.getWebsite() != null) {
            business.setWebsite(request.getWebsite());
        }

        if (request.getBusinessEmail() != null) {
            business.setBusinessEmail(request.getBusinessEmail());
        }

        if (request.getWebsite() != null) {
            business.setWebsite(request.getWebsite());
        }

        if (request.getSocials() != null && !request.getSocials().isEmpty()) {
            business.setSocials(request.getSocials());
        }

        businessRepository.save(business);

        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/create")
    public ResponseEntity<?> createBusiness(@RequestBody CreateBusinessRequest request) {
        String email = EndpointUtils.getCurrentUserEmail();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        Business business = new Business();

        business.setOwner(user);
        business.setAvatar(request.getAvatar());
        business.setName(request.getName());
        business.setDescription(request.getDescription());
        business.setWebsite(request.getWebsite());
        business.setBusinessEmail(request.getBusinessEmail());

        user.getBusinesses().add(business);

        businessRepository.save(business);
        userRepository.save(user);

        Long businessId = business.getId();

        return ResponseEntity.ok(businessId);
    }
}
