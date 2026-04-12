import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

data class User(val name: String? = null, val email: String, val password: String)
data class ResponseMsg(val message: String)

interface ApiService {

    @POST("/api/auth/register")
    fun register(@Body user: User): Call<ResponseMsg>

    @POST("/api/auth/login")
    fun login(@Body user: User): Call<ResponseMsg>
}