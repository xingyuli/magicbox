package top.viclau.magicbox.box.stats.integration.ladder

data class LadderResponse<T>(val code: String, val message: String, val data: T?) {
    val isSuccess: Boolean get() = code == "0000"
}
