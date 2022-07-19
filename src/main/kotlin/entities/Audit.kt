package entities

data class Audit(
    val toName: String,
    val fromName: String,
    var amount: Float
)
