package top.viclau.magicbox.box.stats.integration.ladder.user

data class LoginRequest(val username: String, val password: String) {
    data class ResponseData(val token: String)
}
