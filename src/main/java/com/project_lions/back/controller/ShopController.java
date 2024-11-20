package com.project_lions.back.controller;

import com.project_lions.back.domain.Shop;
import com.project_lions.back.domain.dto.*;
import com.project_lions.back.service.ShopService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/shops")
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;

    @PostMapping(value="", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> create(
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestPart(value = "shop") @Valid ShopCreateDTO shopCreateDTO) {

        return shopService.createShop(shopCreateDTO, image);
    }


    @DeleteMapping("/delete")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> delete(@RequestParam Long shopId) {
        return shopService.deleteShop(shopId);
    }

    @PutMapping(value="/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> update(
            @RequestPart(value = "originshop") String originShopName,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestPart(value = "shop") @Valid ShopUpdateDTO shopUpdateDTO) {
        return shopService.updateShop(shopUpdateDTO, originShopName, image);
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
                        .latitude(shop.getLocation().getX())
                        .longitude(shop.getLocation().getY())
                        .openAt(shop.getOpenAt())
                        .closeAt(shop.getCloseAt())
                        .build())
                .toList();
        return ResponseEntity.ok(shopLikeResponseDtos);
    }

    //소품샵 상세보기
    @GetMapping("/find")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> findByShopName(@RequestParam String shopName) {
        Shop findShop = shopService.findShopByShopName(shopName);
        ShopResponseDTO.ShopDetailsResponseDTO returnShop = ShopResponseDTO.ShopDetailsResponseDTO.builder()
                .id(findShop.getId())
                .image(findShop.getImage())
                .shopName(findShop.getShopName())
                .ownerPhone(findShop.getOwnerPhone())
                .latitude(findShop.getLocation().getX())
                .longitude(findShop.getLocation().getY())
                .openAt(findShop.getOpenAt())
                .closeAt(findShop.getCloseAt())
                .likeShop(findShop.getLikeShop())
                .shopTags(findShop.getShopTags())
                .build();

        return ResponseEntity.ok(returnShop);
    }

    @GetMapping("/find/tag")
    public ResponseEntity<List<ShopResponseDTO.ShopFindResponseDTO>> findByTag(@RequestParam List<String> tag,
                                                                               @RequestParam double latitude,
                                                                               @RequestParam double longitude) {
        Set<Shop> shops = shopService.findShopWithTagAndRadius(tag, latitude, longitude);
        List<Shop> shopList = new ArrayList<>(shops);
        return getShopResponseDTO(shopList);
    }

    @GetMapping("/find/near")
    public ResponseEntity<List<ShopResponseDTO.ShopFindResponseDTO>> findByLocation(@RequestParam double latitude,
                                                                                    @RequestParam double longitude) {
        List<Shop> shops = shopService.findShopWithMyLocation(latitude, longitude);
        return getShopResponseDTO(shops);
    }

    private ResponseEntity<List<ShopResponseDTO.ShopFindResponseDTO>> getShopResponseDTO(List<Shop> shops) {
        List<ShopResponseDTO.ShopFindResponseDTO> shopResponseDTO = shops.stream().map(shop -> ShopResponseDTO.ShopFindResponseDTO.builder()
                        .shopName(shop.getShopName())
                        .ownerPhone(shop.getOwnerPhone())
                        .latitude(shop.getLocation().getY()) // 위도
                        .longitude(shop.getLocation().getX()) // 경도
                        .shopTags(shop.getShopTags())
                        .image(shop.getImage())
                        .build())
                .collect(Collectors.toList());
        return ResponseEntity.ok(shopResponseDTO);
    }

}
