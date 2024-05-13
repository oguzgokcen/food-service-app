package com.getir.patika.chatapp.data.model

data class OrderItem(
    val id: Int,
    val name: String,
    val place: String,
    val price: Double,
    val quantity: Int
)

val FakeOrderItems = listOf(
    OrderItem(1, "Pizza", "Domino's", 25.0, 1),
    OrderItem(2, "Burger", "McDonald's", 10.0, 2),
    OrderItem(3, "Fries", "KFC", 5.0, 3),
    OrderItem(4, "Coke", "Pepsi", 2.0, 4),
    OrderItem(5, "Ice Cream", "Baskin Robbins", 3.0, 5),
    OrderItem(6, "Donut", "Dunkin'", 1.0, 6),
    OrderItem(7, "Coffee", "Starbucks", 4.0, 7),
    OrderItem(8, "Tea", "Lipton", 1.0, 8),
    OrderItem(9, "Milkshake", "Shake Shack", 5.0, 9),
    OrderItem(10, "Pasta", "Olive Garden", 7.0, 10)
)
