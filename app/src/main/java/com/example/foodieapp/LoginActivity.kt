import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.foodieapp.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val user = User(email = email.text.toString(), password = password.text.toString())

            RetrofitClient.instance.login(user)
                .enqueue(object : Callback<ResponseMsg> {
                    override fun onResponse(call: Call<ResponseMsg>, response: Response<ResponseMsg>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@LoginActivity, "Login Successful!", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this@LoginActivity, "Invalid Login", Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onFailure(call: Call<ResponseMsg>, t: Throwable) {
                        Toast.makeText(this@LoginActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }
}