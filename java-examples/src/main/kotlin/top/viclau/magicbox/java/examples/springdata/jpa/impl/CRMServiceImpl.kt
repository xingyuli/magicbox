package top.viclau.magicbox.java.examples.springdata.jpa.impl

import org.slf4j.LoggerFactory
import top.viclau.magicbox.java.examples.springdata.jpa.*


class CRMServiceImpl : CRMService {

    private val logger = LoggerFactory.getLogger(CRMServiceImpl::class.java)
    lateinit var employeeRepository: EmployeeRepository

    override fun addEmployee(context: QueryContext) {
        // use aspect before to accomplish this
        logger.debug("addEmployee request received: {} ", context.request)

        val req: QueryRequest = context.request
        val name: String? = req.param("name")
        val age: Int? = req.param("age")

        val e = Employee()
        req.scope?.let {
            e.setScope(it, req.scopeValue!!)
        }
        e.name = name
        e.age = age

        val resp = QueryResponseImpl()
        try {
            employeeRepository.save(e)
        } catch (ex: Exception) {
            logger.error("failed to save employee: {}", req, ex)
            resp.error(ex)
        }
        (context as QueryContextImpl).response = resp
    }

}