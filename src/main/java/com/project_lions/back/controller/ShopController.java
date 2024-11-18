package com.project_lions.back.controller;

import com.project_lions.back.domain.Shop;
import com.project_lions.back.domain.dto.*;
import com.project_lions.back.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/shops")
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> create(@RequestBody ShopCreateDTO shopCreateDTO) {
        return shopService.createShop(shopCreateDTO);
    }


    @DeleteMapping("/delete")  //해야할 것: 삭제 메소드500번 뜨는것 + shopId 어떻게 하면 좋을지
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> delete(@RequestParam Long shopId) {
        return shopService.deleteShop(shopId);
    }

    @PutMapping("/update")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> update(@RequestParam Long shopId, @RequestBody ShopUpdateDTO shopUpdateDTO) {
        return shopService.updateShop(shopUpdateDTO, shopId);
    }


    @PutMapping("/like")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> likeShop(@RequestParam String shopName) {
        return shopService.likeShop(shopName);
    }

    @GetMapping("/like")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> findLikeShop() {
        List<Shop> shops= shopService.findLikeShop();
        if (shops.isEmpty()) {
            return ResponseEntity.ok("찜한 소품샵이 존재하지 않습니다.");
        }

        List<ShopResponseDTO.ShopLikeResponseDto> shopLikeResponseDtos = shops.stream().map(shop -> ShopResponseDTO.ShopLikeResponseDto.builder()
                        .shopName(shop.getShopName())
                        .likeShop(shop.getLikeShop())
                        .latitude(shop.getLocation().getX())
                        .longitude(shop.getLocation().getY())
                        .openAt(shop.getOpenAt())
                        .closeAt(shop.getCloseAt())
                        .build())
                .toList();
        System.out.println("shopLikeResponseDtos = " + shopLikeResponseDtos.toString());
        return ResponseEntity.ok(shopLikeResponseDtos);
    }

    @GetMapping("/find")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> findByShopName(@RequestParam String shopName) {
        Shop findShop = shopService.findShopByShopName(shopName);
        ShopResponseDTO.ShopFindResponseDTO returnShop = ShopResponseDTO.ShopFindResponseDTO.builder()
                .id(findShop.getId())
                .shopName(findShop.getShopName())
                .build();
        return ResponseEntity.ok(returnShop);
    }

/*
    @GetMapping("/find/all")
    public ResponseEntity<List<ShopResponseDTO>> findAll() {
        List<Shop> shops = shopService.findShopAll();
        return getShopResponseDTO(shops);

    }

*/

    @GetMapping("/find/tag")
    public ResponseEntity<List<ShopResponseDTO.ShopMainResponseDTO>> findByTag(@RequestParam List<String> tag,
                                                           @RequestParam double latitude,
                                                           @RequestParam double longitude) {
        Set<Shop> shops = shopService.findShopWithTagAndRadius(tag, latitude, longitude);
        List<Shop> shopList = new ArrayList<>(shops);
        return getShopResponseDTO(shopList);
    }

    @GetMapping("/find/near")
    public ResponseEntity<List<ShopResponseDTO.ShopMainResponseDTO>> findByLocation(@RequestParam double latitude,
                                                                @RequestParam double longitude) {
        List<Shop> shops = shopService.findShopWithMyLocation(latitude, longitude);
        return getShopResponseDTO(shops);
    }

    private ResponseEntity<List<ShopResponseDTO.ShopMainResponseDTO>> getShopResponseDTO(List<Shop> shops) {
        List<ShopResponseDTO.ShopMainResponseDTO> shopResponseDTO = shops.stream().map(shop -> ShopResponseDTO.ShopMainResponseDTO.builder()
                        .id(shop.getId())
                        .shopName(shop.getShopName())
                        .ownerPhone(shop.getOwnerPhone())
                        .ownerName(shop.getOwnerName())
                        .latitude(shop.getLocation().getY()) // 위도
                        .longitude(shop.getLocation().getX()) // 경도
                        .shopTags(shop.getShopTags())
                        .build())
                .collect(Collectors.toList());


        return ResponseEntity.ok(shopResponseDTO);
    }


}
