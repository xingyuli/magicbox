package top.viclau.magicbox.java.examples.junit.chapter3

interface Response {
    val name: String
}

class ErrorResponse(val originalRequest: Request, val originalException: Exception) : Response {

    override val name: String
        get() = "Error"

}
