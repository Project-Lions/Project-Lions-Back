package com.project_lions.back.service;

import com.project_lions.back.domain.Member;
import com.project_lions.back.domain.Shop;
import com.project_lions.back.domain.Tag;
import com.project_lions.back.domain.dto.ShopCreateDTO;
import com.project_lions.back.domain.dto.ShopUpdateDTO;
import com.project_lions.back.domain.enums.Like;
import com.project_lions.back.repository.MemberRepository;
import com.project_lions.back.repository.ShopRepository;
import com.project_lions.back.repository.TagRepository;
import com.project_lions.back.util.security.SecurityUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShopService {

    private final ShopRepository shopRepository;
    private final TagRepository tagRepository;
    private final MemberRepository memberRepository;

    public ResponseEntity<?> createShop(ShopCreateDTO shopDTO) {

        Member member = findMember();
        if (shopRepository.existsByShopName(shopDTO.getShopName())) {
            throw new IllegalArgumentException("이미 존재하는 소품샵입니다.");
        }

        Set<String> tags=shopDTO.getShopTags();
        Set<Tag> tagList= checkTagExist(tags);
        if(tagList == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("존재하지 않는 소품샵 태그입니다.");
        }

        Point shopLocation = createPoint(shopDTO.getLatitude(), shopDTO.getLongitude());
        Shop shop = Shop.builder()
                .shopName(shopDTO.getShopName())
                .ownerPhone(member.getPhone())
                .ownerName(member.getPhone())
                .openAt(shopDTO.getOpenAt())
                .closeAt(shopDTO.getCloseAt())
                .location(shopLocation)
                .shopTags(tagList)
                .owner(member)
                .likeShop(Like.UNLIKE)
                .build();
        shopRepository.save(shop);

        return ResponseEntity.ok(shopDTO);
    }

    public ResponseEntity<?> updateShop(ShopUpdateDTO shopDTO, Long shopId) {

        Member member = findMember();
        Optional<Shop> checkShop=shopRepository.findById(shopId);

        if(checkShop.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if(checkShop.get().getOwner().equals(member)){
            Set<String> tags=shopDTO.getShopTags();
            Set<Tag> tagList= checkTagExist(tags);
            if(tagList == null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("존재하지 않는 소품샵 태그입니다.");
            }
            Point shopLocation = createPoint(shopDTO.getLatitude(), shopDTO.getLongitude());

            Optional<Shop> updateShop=shopRepository.findById(shopId);

            updateShop.get().setShopName(shopDTO.getShopName());
            updateShop.get().setOwnerPhone(shopDTO.getOwnerPhone());
            updateShop.get().setOwnerName(shopDTO.getOwnerName());
            updateShop.get().setLocation(shopLocation);
            updateShop.get().setShopTags(tagList);
            updateShop.get().setOpenAt(shopDTO.getOpenAt());
            updateShop.get().setCloseAt(shopDTO.getCloseAt());
            shopRepository.save(updateShop.get());
            return ResponseEntity.ok(shopDTO);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @Transactional
    public ResponseEntity<?> deleteShop(Long shopId) {

        Member member = findMember();
        Optional<Shop> deleteShop=shopRepository.findById(shopId);
        if(deleteShop.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("삭제할 소품샵이 존재하지 않습니다.");
        }
        shopRepository.delete(deleteShop.get());
        return ResponseEntity.ok().body("소품샵 삭제\n삭제된 소품샵 이름: "+deleteShop.get().getShopName());

    }

    //소품샵 찜하기
    public ResponseEntity<?> likeShop(String shopName){
        Member member = findMember();

        Optional<Shop> shop = shopRepository.findByShopName(shopName);
        if(shop.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("해당하는 소품샵이 존재하지 않습니다.");
        }
        member.getLikeshops().add(shop.get());
        shop.get().getMemberLikeShop().add(member);
        shop.get().setLikeShop(Like.LIKE);
        memberRepository.save(member);
        shopRepository.save(shop.get());

        return ResponseEntity.ok().build();
    }

    //찜한 소품샵
    public List<Shop> findLikeShop(){
        Member member = findMember();
        return shopRepository.findLikeShops(member.getId())
                .orElse(Collections.emptyList());
    }

    public Shop findShopByShopName(String shopName) {
        Member member = findMember();
        return shopRepository.findByShopName(shopName)
                .orElseThrow(()-> new IllegalArgumentException("해당 소품샵이 존재하지 않습니다."));
    }

    //소품샵 태그, 반경으로 검색하기
    public Set<Shop> findShopWithTagAndRadius(List<String> tag, double latitude, double longitude){
        List<Shop> shopsWithMyLocation = findShopWithMyLocation(latitude, longitude);
        Set<String> tagList = new HashSet<>(tag);
        Set<Shop> returnShops = new HashSet<>();

        for(Shop shop: shopsWithMyLocation){
            int tagCount=0;
            for(Tag tagName :shop.getShopTags()){
                if(tagList.contains(tagName.getTagName())){
                    tagCount++;
                    if(tagCount== tag.size())
                        returnShops.add(shop);
                }
            }
        }
        return returnShops;
    }

    //소품샵 반경으로 검색하기
    public List<Shop> findShopWithMyLocation(double latitude, double longitude) {
        Point location = createPoint(latitude, longitude);
        //반경 5km로 설정
        return shopRepository.findShopsWithinRadius(location, 5000);
    }

    public Point createPoint(double latitude, double longitude) {
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        return geometryFactory.createPoint(new Coordinate(longitude, latitude));
    }

    public Set<Tag> checkTagExist(Set<String> tagList){
        Set<Optional<Tag>> tagOptional = new HashSet<>();
        for (String tag : tagList) {
            Optional<Tag> existingTag = tagRepository.findByTagName(tag);
            if(existingTag.isEmpty()) {
                return null; //존재하지 않는 소품샵 태그
            }
            tagOptional.add(existingTag);
        }
        Set<Tag> tags = tagOptional.stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
        return tags;
    }

    public Member findMember() {
        String memberEmail= SecurityUtil.getLoginUsername();
        return memberRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new IllegalArgumentException("로그인이 필요합니다."));
    }

}
