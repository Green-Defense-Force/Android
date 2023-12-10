package com.three.green_defense_force.viewmodels

import com.three.green_defense_force.models.Store
import com.three.green_defense_force.models.StoreItem

class StoreViewModel {
    private val cachedItems: MutableMap<String, List<StoreItem>> = mutableMapOf()

    /** 이미 받아온 아이템이 있다면 캐시에서 반환하는 함수 */
    fun fetchItems(userId: String, categoryId: String): List<StoreItem> {
        return if (cachedItems.containsKey(categoryId)) {
            cachedItems[categoryId]!!
        } else {
            val items = when (categoryId) {
                "hair" -> fetchHairData(userId, categoryId).storeItems
                "top" -> fetchTopData(userId, categoryId).storeItems
                "pants" -> fetchPantsData(userId, categoryId).storeItems
                else -> emptyList()
            }
            cachedItems[categoryId] = items
            items
        }
    }

    /** 서버에서 아이템 정보 받아오는 함수 : Hair */
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
                "",
                40,
                false
            ),
            StoreItem(
                "1003",
                "짧은 머리 (Brown)",
                "",
                40,
                false
            ),
            StoreItem(
                "1004",
                "짧은 머리 (Blonde)",
                "",
                40,
                false
            ),
            StoreItem(
                "1005",
                "단발 머리 (Black)",
                "",
                40,
                false
            ),
            StoreItem(
                "1006",
                "단발 머리 (Red)",
                "",
                50,
                false
            ),
            StoreItem(
                "1007",
                "단발 머리 (Dark Brown)",
                "",
                50,
                false
            ),
            StoreItem(
                "1008",
                "단발 머리 (Brown)",
                "",
                50,
                false
            ),
            StoreItem(
                "1009",
                "단발 머리 (Blonde)",
                "",
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

    /** 서버에서 아이템 정보 받아오는 함수 : Top */
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
                "",
                25,
                false
            ),
            StoreItem(
                "2003",
                "나시 (Yellow)",
                "",
                25,
                false
            ),
            StoreItem(
                "2004",
                "나시 (Green)",
                "",
                25,
                false
            ),
            StoreItem(
                "2005",
                "나시 (Blue)",
                "",
                25,
                false
            ),
            StoreItem(
                "2006",
                "나시 (Sky Blue)",
                "",
                25,
                false
            ),
            StoreItem(
                "2007",
                "나시 (Dark Blue)",
                "",
                25,
                false
            ),
            StoreItem(
                "2008",
                "나시 (Navy)",
                "",
                25,
                false
            ),
            StoreItem(
                "2009",
                "나시 (Black)",
                "",
                25,
                false
            ),
            StoreItem(
                "2010",
                "반팔 (White)",
                "",
                30,
                false
            ),
            StoreItem(
                "2011",
                "반팔 (Navy)",
                "",
                30,
                false
            ),
            StoreItem(
                "2012",
                "반팔 (Black)",
                "",
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

    /** 서버에서 아이템 정보 받아오는 함수 : Pants */
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
                "",
                15,
                false
            ),
            StoreItem(
                "3003",
                "바지 (Yellow)",
                "",
                15,
                false
            ),
            StoreItem(
                "3004",
                "바지 (Green)",
                "",
                15,
                false
            ),
            StoreItem(
                "3005",
                "바지 (Blue)",
                "",
                20,
                false
            ),
            StoreItem(
                "3006",
                "바지 (Sky Blue)",
                "",
                20,
                false
            ),
            StoreItem(
                "3007",
                "바지 (Dark Blue)",
                "",
                25,
                false
            ),
            StoreItem(
                "3008",
                "바지 (Navy)",
                "",
                25,
                false
            ),
            StoreItem(
                "3009",
                "바지 (Black)",
                "",
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

    /** 서버에서 아이템 정보 받아오는 함수 : Shoes */


    /** 서버에서 아이템 정보 받아오는 함수 : Weapon */


    /** 서버에서 아이템 정보 받아오는 함수 : Acc */


}