package top.viclau.magicbox.java.examples.junit.chapter3

interface Request {
    val name: String
}

interface RequestHandler {
    fun process(req: Request): Response
}
