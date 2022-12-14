package top.viclau.magicbox.java.examples.junit.chapter3

interface Controller {
    fun processRequest(req: Request): Response
    fun addHandler(req: Request, handler: RequestHandler)
}

class DefaultController : Controller {

    private val requestHandlers: MutableMap<String, RequestHandler> = mutableMapOf()

    override fun processRequest(req: Request): Response = try {
        getHandler(req).process(req)
    } catch (e: Exception) {
        ErrorResponse(req, e)
    }

    override fun addHandler(req: Request, handler: RequestHandler) {
        if (requestHandlers.containsKey(req.name)) {
            throw RuntimeException("A request handler has already been registered for request name [${req.name}]")
        }
        requestHandlers[req.name] = handler
    }

    fun getHandler(req: Request): RequestHandler =
        requestHandlers[req.name] ?: throw RuntimeException("Cannot find handler for request name [${req.name}]")

}