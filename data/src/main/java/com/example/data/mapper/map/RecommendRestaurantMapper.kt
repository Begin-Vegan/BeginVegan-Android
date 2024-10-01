package com.example.data.mapper.map

import com.example.data.model.map.RecommendRestaurantInformation
import com.example.domain.mapper.Mapper
import com.example.domain.model.map.RecommendRestaurant

class RecommendRestaurantMapper :
    Mapper<List<RecommendRestaurantInformation>, List<RecommendRestaurant>> {
    override fun mapFromEntity(type: List<RecommendRestaurantInformation>): List<RecommendRestaurant> {
        return type.map {
            RecommendRestaurant(
                id = it.restaurantId,
                name = it.name,
                bookmark = it.bookmark,
                latitude = it.latitude,
                longitude = it.longitude,
                thumbnail = it.thumbnail
            )
        }
    }
}