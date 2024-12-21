package com.example.greenfuture.controller;

import com.example.greenfuture.model.Region;
import com.example.greenfuture.model.Users;
import com.example.greenfuture.repository.UsersRepository;
import com.example.greenfuture.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/regions")
public class RegionController {

    private final RegionService regionService;
    @Autowired
    private UsersRepository usersRepository;

    public RegionController(RegionService regionService) {
        this.regionService = regionService;
    }

    @GetMapping
    public List<Region> getAllRegions() {
        return regionService.getAllRegions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Region> getRegionById(@PathVariable Integer id) {
        return regionService.getRegionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Region createRegion(@RequestBody Map<String, Object> requestData) {
        // Extract fields from the request payload
        String regionName = (String) requestData.get("name");
        List<Map<String, String>> membersData = (List<Map<String, String>>) requestData.get("members");

        // Create a set to avoid duplicate members
        Set<Users> membersSet = new HashSet<>();

        // Add team members from the request
        if (membersData != null) {
            for (Map<String, String> memberData : membersData) {
                Integer memberId = Integer.valueOf(memberData.get("id"));
                Users member = usersRepository.findById(memberId)
                        .orElseThrow(() -> new RuntimeException("Region member not found for ID: " + memberId));
                membersSet.add(member); // Add to the set (automatically prevents duplicates)
            }
        }

        // Create and populate the region object
        Region region = new Region();
        region.setName(regionName);
        region.setMembers(new ArrayList<>(membersSet)); // Convert set back to list

        // Save and return the region
        return regionService.saveRegion(region);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Region> updateRegion(@PathVariable Integer id, @RequestBody Region updatedRegion) {
        return regionService.getRegionById(id)
                .map(region -> {
                    region.setName(updatedRegion.getName());
                    region.setMembers(updatedRegion.getMembers());
                    return ResponseEntity.ok(regionService.saveRegion(region));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRegion(@PathVariable Integer id) {
        if (regionService.getRegionById(id).isPresent()) {
            regionService.deleteRegion(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
