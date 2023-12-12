package com.three.green_defense_force.viewmodels

import com.three.green_defense_force.models.Store
import com.three.green_defense_force.models.StoreItem
import com.three.green_defense_force.models.StoreUser

class StoreViewModel {
    private var storeUser: StoreUser? = null
    private val cachedItems: MutableMap<String, List<StoreItem>> = mutableMapOf()

    /** 서버에서 사용자 정보 받아오는 함수 */
    fun fetchUserData(userId: String): StoreUser {
        if (storeUser == null) {
            storeUser = StoreUser(
                userId,
                100,
                mapOf(
                    "hair" to "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/hair_default.png?raw=true",
                    "top" to "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/top_default.png?raw=true",
                    "pants" to "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/pants_default.png?raw=true",
                    "shoes" to "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/shoes_default.png?raw=true",
                    "acc" to "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/acc_glasses_default.png?raw=true"
                )
            )
        }
        return storeUser!!
    }

    /** 이미 받아온 아이템이 있다면, 캐시에서 반환하는 함수 */
    fun fetchItems(userId: String, categoryId: String): List<StoreItem> {
        return if (cachedItems.containsKey(categoryId)) {
            cachedItems[categoryId]!!
        } else {
            val items = when (categoryId) {
                "hair" -> fetchHairData(userId, categoryId).storeItems
                "top" -> fetchTopData(userId, categoryId).storeItems
                "pants" -> fetchPantsData(userId, categoryId).storeItems
                "shoes" -> fetchShoesData(userId, categoryId).storeItems
                "weapon" -> fetchWeaponData(userId, categoryId).storeItems
                "acc" -> fetchAccData(userId, categoryId).storeItems
                else -> emptyList()
            }
            cachedItems[categoryId] = items
            items
        }
    }

    /** 서버에서 아이템 정보 받아오는 함수 */
    private fun fetchHairData(userId: String, categoryId: String): Store {
        val storeItems = listOf(
            StoreItem(
                "1001",
                "짧은 머리 (Red)",
                "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/hair_short_red.png?raw=true",
                40,
                false
            ),
            StoreItem(
                "1002",
                "짧은 머리 (Dark Brown)",
                "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/hair_short_darkbrown.png?raw=true",
                40,
                false
            ),
            StoreItem(
                "1003",
                "짧은 머리 (Brown)",
                "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/hair_short_brown.png?raw=true",
                40,
                false
            ),
            StoreItem(
                "1004",
                "짧은 머리 (Blonde)",
                "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/hair_short_blonde.png?raw=true",
                40,
                false
            ),
            StoreItem(
                "1005",
                "단발 머리 (Black)",
                "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/hair_danbal_black.png?raw=true",
                40,
                false
            ),
            StoreItem(
                "1006",
                "단발 머리 (Red)",
                "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/hair_danbal_red.png?raw=true",
                50,
                false
            ),
            StoreItem(
                "1007",
                "단발 머리 (Dark Brown)",
                "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/hair_danbal_darkbrown.png?raw=true",
                50,
                false
            ),
            StoreItem(
                "1008",
                "단발 머리 (Brown)",
                "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/hair_danbal_brown.png?raw=true",
                50,
                false
            ),
            StoreItem(
                "1009",
                "단발 머리 (Blonde)",
                "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/hair_danbal_blonde.png?raw=true",
                50,
                false
            ),
        )

        return Store(
            userId,
            categoryId,
            storeItems
        )
    }

    private fun fetchTopData(userId: String, categoryId: String): Store {
        val storeItems = listOf(
            StoreItem(
                "2001",
                "나시 (Red)",
                "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/top_sleeveless_red.png?raw=true",
                25,
                false
            ),
            StoreItem(
                "2002",
                "나시 (Orange)",
                "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/top_sleeveless_orange.png?raw=true",
                25,
                false
            ),
            StoreItem(
                "2003",
                "나시 (Yellow)",
                "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/top_sleeveless_yellow.png?raw=true",
                25,
                false
            ),
            StoreItem(
                "2004",
                "나시 (Green)",
                "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/top_sleeveless_green.png?raw=true",
                25,
                false
            ),
            StoreItem(
                "2005",
                "나시 (Blue)",
                "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/top_sleeveless_blue.png?raw=true",
                25,
                false
            ),
            StoreItem(
                "2006",
                "나시 (Sky Blue)",
                "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/top_sleeveless_skyblue.png?raw=true",
                25,
                false
            ),
            StoreItem(
                "2007",
                "나시 (Dark Blue)",
                "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/top_sleeveless_darkblue.png?raw=true",
                25,
                false
            ),
            StoreItem(
                "2008",
                "나시 (Navy)",
                "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/top_sleeveless_navy.png?raw=true",
                25,
                false
            ),
            StoreItem(
                "2009",
                "나시 (Black)",
                "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/top_sleeveless_black.png?raw=true",
                25,
                false
            ),
            StoreItem(
                "2010",
                "반팔 (White)",
                "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/top_short_white.png?raw=true",

                30,
                false
            ),
            StoreItem(
                "2011",
                "반팔 (Navy)",
                "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/top_short_navy.png?raw=true",
                30,
                false
            ),
            StoreItem(
                "2012",
                "반팔 (Black)",
                "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/top_short_black.png?raw=true",
                30,
                false
            ),
        )

        return Store(
            userId,
            categoryId,
            storeItems
        )
    }

    private fun fetchPantsData(userId: String, categoryId: String): Store {
        val storeItems = listOf(
            StoreItem(
                "3001",
                "바지 (Red)",
                "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/pants_middle_red.png?raw=true",
                15,
                false
            ),
            StoreItem(
                "3002",
                "바지 (Orange)",
                "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/pants_middle_orange.png?raw=true",
                15,
                false
            ),
            StoreItem(
                "3003",
                "바지 (Yellow)",
                "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/pants_middle_yellow.png?raw=true",
                15,
                false
            ),
            StoreItem(
                "3004",
                "바지 (Green)",
                "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/pants_middle_green.png?raw=true",
                15,
                false
            ),
            StoreItem(
                "3005",
                "바지 (Blue)",
                "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/pants_middle_blue.png?raw=true",
                20,
                false
            ),
            StoreItem(
                "3006",
                "바지 (Sky Blue)",
                "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/pants_middle_skyblue.png?raw=true",
                20,
                false
            ),
            StoreItem(
                "3007",
                "바지 (Dark Blue)",
                "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/pants_middle_darkblue.png?raw=true",
                25,
                false
            ),
            StoreItem(
                "3008",
                "바지 (Navy)",
                "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/pants_middle_navy.png?raw=true",
                25,
                false
            ),
            StoreItem(
                "3009",
                "바지 (Black)",
                "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/pants_middle_black.png?raw=true",
                25,
                false
            ),
        )

        return Store(
            userId,
            categoryId,
            storeItems
        )
    }

    private fun fetchShoesData(userId: String, categoryId: String): Store {
        val storeItems = listOf(
            StoreItem(
                "4001",
                "기본 신발 (Gray)",
                "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/shoes_basic_gray.png?raw=true",
                15,
                false
            ),
            StoreItem(
                "4001",
                "기본 신발 (Brown)",
                "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/shoes_basic_brown.png?raw=true",
                15,
                false
            ),
        )

        return Store(
            userId,
            categoryId,
            storeItems
        )
    }

    private fun fetchWeaponData(userId: String, categoryId: String): Store {
        return Store(
            userId,
            categoryId
        )
    }

    private fun fetchAccData(userId: String, categoryId: String): Store {
        return Store(
            userId,
            categoryId
        )
    }
}