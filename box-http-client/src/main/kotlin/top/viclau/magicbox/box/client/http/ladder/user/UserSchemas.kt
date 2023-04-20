package top.viclau.magicbox.box.client.http.ladder.user

data class LoginRequest(val username: String, val password: String) {
    data class ResponseData(val token: String)
}
