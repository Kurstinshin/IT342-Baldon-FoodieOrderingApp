import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.example.foodieapp.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val name = findViewById<EditText>(R.id.name)
        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        btnRegister.setOnClickListener {

            val user = User(
                name = name.text.toString(),
                email = email.text.toString(),
                password = password.text.toString()
            )

            RetrofitClient.instance.register(user)
                .enqueue(object : Callback<ResponseMsg> {

                    override fun onResponse(call: Call<ResponseMsg>, response: Response<ResponseMsg>) {
                        Toast.makeText(this@RegisterActivity, "Registered Successfully!", Toast.LENGTH_SHORT).show()
                        // Redirect to login
                        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                        finish()
                    }

                    override fun onFailure(call: Call<ResponseMsg>, t: Throwable) {
                        Toast.makeText(this@RegisterActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }
}